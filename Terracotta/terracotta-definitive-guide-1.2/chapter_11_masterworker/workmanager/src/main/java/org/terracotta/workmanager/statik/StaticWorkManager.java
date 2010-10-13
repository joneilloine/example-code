/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.statik;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;

/**
 * A routing aware WorkerManager that uses an implementation of the Router
 * interface to do the route work to different work queues.
 */
public class StaticWorkManager<ID> implements WorkManager {

	private final Router<ID> m_router;

	private final Set<RoutableWorkItem<ID>> m_completedWork;

	private int m_nrOfScheduledWork = 0;

	public StaticWorkManager(final Router<ID> router) {
		m_router = router;
		m_completedWork = router.getAllCompletedWork();
	}

	public WorkItem schedule(final Work work, final WorkListener listener) {
		System.out.println("scheduled work #: " + m_nrOfScheduledWork++);
		return m_router.route(work, listener);
	}

	public Set<RoutableWorkItem<ID>> getCompletedWork() {
		return m_completedWork;
	}

	public WorkItem schedule(final Work work) {
		return schedule(work, null);
	}

	public boolean waitForAll(Collection workItems, long timeout) throws InterruptedException {
		final int nrOfPendingWorkItems = workItems.size();
		int nrOfCompletedWorkItems = 0;
		while (true) {
			WorkItem workItem = waitForAny(timeout);
			if (workItem == null) {
				return false;
			}
			nrOfCompletedWorkItems++;
			if (nrOfPendingWorkItems == nrOfCompletedWorkItems) {
				break;
			}
		}
		return true;
	}

	public Collection waitForAny(final Collection workItems, final long timeout) throws InterruptedException {
		final Collection<WorkItem> completedWorkItems = new ArrayList<WorkItem>();

		// TODO: cheating now by only wrapping a single completed item in a
		// collection
		WorkItem workItem = waitForAny(timeout);
		if (workItem == null) {
			return Collections.EMPTY_LIST;
		}
		completedWorkItems.add(workItem);
		return completedWorkItems;
	}

	public WorkItem waitForAny(long timeout) throws InterruptedException {
		synchronized (m_completedWork) {
			while (true) {
				for (WorkItem workItem : m_completedWork) {
					if (workItem.getStatus() == WorkEvent.WORK_COMPLETED || 
							workItem.getStatus() == WorkEvent.WORK_REJECTED) {
						m_completedWork.remove(workItem);
						return workItem;
					}
				}
				if (timeout == IMMEDIATE) {
					return null;
				}
				if (timeout == INDEFINITE) {
                                        int size = m_completedWork.size();
					while (m_completedWork.size() == size) {
                                        	try {
							m_completedWork.wait();
						} catch (InterruptedException ie) {
							ie.printStackTrace();
							Thread.currentThread().interrupt();
							throw ie;
						}
					}
				}
			}
		}
	}
}
