/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.spider;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.terracotta.workmanager.pipe.DefaultPipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.pipe.DefaultPipe.Factory;
import org.terracotta.workmanager.queue.BlockingQueueBasedQueue;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;
import org.terracotta.workmanager.statik.LoadBalancingRouter;
import org.terracotta.workmanager.statik.SingleQueueRouter;
import org.terracotta.workmanager.statik.StaticWorkManager;

import au.id.jericho.lib.html.Source;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

public class SpiderMaster implements Runnable {

	public final static int QUEUE_SIZE = 100;
	public final static int BATCH_SIZE = 100;

	// static so I can reuse the same factory in the RetryWorkListener
	public static final Factory<RoutableWorkItem<String>, String> PIPE_FACTORY = 
		new DefaultPipe.Factory<RoutableWorkItem<String>, String>(
			new BlockingQueueBasedQueue.Factory<RoutableWorkItem<String>>(QUEUE_SIZE));
//	public static final Factory<RoutableWorkItem<String>, String> PIPE_FACTORY = 
//    new DefaultPipe.Factory<RoutableWorkItem<String>, String>(
//			new BatchingQueue.Factory(BATCH_SIZE, new BlockingQueueBasedQueue.Factory<RoutableWorkItem<String>>(QUEUE_SIZE)),
//			new HashSet<RoutableWorkItem<String>>());

	private final static PageCache s_pageCache = new SimplePageCache();

	// the work queue manager
	private final PipeManager<String> m_pipeManager;

	// the router
	private final Router<String> m_router;

	// the work manager
	private final StaticWorkManager<String> m_workManager;

	// the work listener
	private final WorkListener m_workListener;

	private final Set<String> m_visitedPages = new HashSet<String>();
	private final String m_startURL;
	private final int m_maxDepth;
	private final boolean m_followExternalLinks;
	private int m_nrOfCompletedWork = 0;

	public SpiderMaster(final String startURL, final int maxDepth, final boolean followExternalLinks,
			final String[] routingIDs, final String routingIdForFailOverNode) {

		m_startURL = startURL;
		m_maxDepth = maxDepth;
		m_followExternalLinks = followExternalLinks;

		m_pipeManager = new PipeManager<String>(PIPE_FACTORY, PipeManager.Initiator.MASTER);

		if (routingIDs.length == 1) {
			// -- SINGLE WORK QUEUE - NO ROUTING - NO RETRY
			m_router = new SingleQueueRouter<String>(m_pipeManager, routingIDs[0]);
			m_workListener = null;
		} else {
			// -- MULTIPLE WORK QUEUE - WITH ROUTING AND RETRY
			m_router = new LoadBalancingRouter<String>(m_pipeManager, routingIDs);
			m_workListener = null; // RetryWorkListener(routingIdForFailOverNode);
		}
		m_workManager = new StaticWorkManager<String>(m_router);
		
	}

	public static Source cachePage(final String link, final Source page) {
		return s_pageCache.setPage(link, page);
	}

	public void run() {
		WorkItem firstWorkItem;
		try {
			// schedule first work
			// IN: start URL
			// OUT: first pending work item
			firstWorkItem = scheduleWork(m_startURL);
		} catch (WorkException e) {
			System.err.println("WorkException: " + e.getMessage());
			return;
		}

		// keep track of all pending work
		final Set<WorkItem> pendingWork = new HashSet<WorkItem>();
		pendingWork.add(firstWorkItem);

		// loop while there still is pending work to wait for
		while (!pendingWork.isEmpty()) {
			
			// wait for any work that is completed
			Collection completedWork;
			try {
				completedWork = m_workManager.waitForAny(pendingWork, WorkManager.INDEFINITE);
			} catch (InterruptedException e) {
				throw new RuntimeException(e); // bail out
			}

			// loop over all completed work
			for (Object o : completedWork) {
				WorkItem workItem = (WorkItem) o;

				// check work status (completed or rejected)
				switch (workItem.getStatus()) {
				case WorkEvent.WORK_COMPLETED:
					List<PageLink> linksToProcess = null;
					// if completed - grab the result
					SpiderWork work = ((SpiderWork) workItem.getResult());

					// grab the new links
					linksToProcess = work.getLinks();
					m_nrOfCompletedWork++;

					// remove work from the pending list
					pendingWork.remove(workItem);

					// process all the new links
					processLinks(linksToProcess, pendingWork);
					break;

				case WorkEvent.WORK_REJECTED:
					// work rejected - just remove the work
					pendingWork.remove(workItem);
					break;

				default:
					// status is either WORK_ACCEPTED or WORK_STARTED - should never
					// happen
					throw new IllegalStateException("WorkItem is in unexpected state: " + workItem);
				}
			}
		}
		System.out.println("Completed successfully - processed " + m_nrOfCompletedWork + " pages.");
	}

	private void processLinks(final List<PageLink> linksToProcess, final Set<WorkItem> pendingWork) {
		System.out.println("Processing " + linksToProcess.size() + " link results.");
		for (PageLink link : linksToProcess) {

			// loop over all new links
			if (followLink(link)) {
				try {
					// schedule work for each link that is found
					WorkItem newWorkItem = scheduleWork(link.getUrl(), link.getDepth());

					// add the new work item to pending work list
					pendingWork.add(newWorkItem);
				} catch (WorkException e) {
					System.err.println("WorkException: " + e.getMessage());
					continue; // TODO should retry; workItem.getResult().run()
				}
			}
		}
	}

	private boolean followLink(PageLink link) {
		if (m_visitedPages.contains(link.getUrl())) {
			return false;
		} else if (link.getDepth() >= m_maxDepth) {
			return false;
		} else if (link.getUrl().endsWith(".pdf") || link.getUrl().endsWith(".gz") || link.getUrl().endsWith(".tgz")
				|| link.getUrl().endsWith(".zip") || link.getUrl().endsWith(".doc") || link.getUrl().endsWith(".ppt")) {
			// don't follow binary content links
			return false;
		} else if (!link.getUrl().startsWith("http") && !link.getUrl().startsWith("file")) {
			// we're only going to support http and file for now
			return false;
		} else {
			return true;
		}
	}

	private WorkItem scheduleWork(final String url, final int depth) throws WorkException {
		System.out.println("Submitting URL: " + url);
		m_visitedPages.add(url);

		// create new work for the link
		Work work = new SpiderWork(url, depth, m_followExternalLinks);

		// schedule the work in the work manager
		return m_workManager.schedule(work, m_workListener);
	}

	private WorkItem scheduleWork(String url) throws WorkException {
		return scheduleWork(url, 1);
	}
}
