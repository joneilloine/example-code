/*
 * Copyright (c) 2006-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.statik;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.terracotta.workmanager.worker.Worker;
import org.terracotta.workmanager.worker.WorkerTask;
import org.terracotta.workmanager.pipe.Pipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.routing.RoutableWorkItem;

import commonj.work.WorkException;

/**
 * Worker that has an is aware of routing. Each instance of the
 * RoutingAwareWorker class has a routing ID and gets a unique work queue mapped
 * to this routing ID.
 */
public class StaticWorker<ID> implements Worker {

	protected volatile boolean m_isRunning = true;

	protected final ExecutorService m_threadPool = Executors.newCachedThreadPool();

	protected Pipe<RoutableWorkItem<ID>, ID> m_pipe;

	protected PipeManager<ID> m_pipeManager;

	protected ID m_routingID;

	public static final int WORKER_TIMEOUT_IN_SECONDS = 60;

	public StaticWorker(final PipeManager<ID> pipeManager, final ID routingID) {
		m_routingID = routingID;
		m_pipe = pipeManager.getOrCreatePipeFor(m_routingID);
		m_pipeManager = pipeManager;
		System.out.println("Starting worker with routing ID: " + routingID);
	}

	public void start() throws WorkException {
		while (m_isRunning) {
			final RoutableWorkItem<ID> workItem;
			try {
				
			  workItem = m_pipe.getPendingWorkQueue().take();
				
//				workItem = m_pipe.getOutboundChannel().poll(WORKER_TIMEOUT_IN_SECONDS, TimeUnit.SECONDS);
//				if (workItem == null) {
//					System.out.println("Worker has waited " + String.valueOf(WORKER_TIMEOUT_IN_SECONDS)
//							+ " seconds for new work. Stopping now.");
//					m_isRunning = false;
//				}
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new WorkException(e);
			}

			if (workItem != null) {
				m_threadPool.execute(new WorkerTask<RoutableWorkItem<ID>>(workItem, m_pipe.getCompletedWorkQueue()));

			} // -- if (workItem != null)
		}
	}

	public void stop() {
		m_isRunning = false;
		m_threadPool.shutdown();
		m_pipeManager.removePipeFor(m_routingID);
	}
}
