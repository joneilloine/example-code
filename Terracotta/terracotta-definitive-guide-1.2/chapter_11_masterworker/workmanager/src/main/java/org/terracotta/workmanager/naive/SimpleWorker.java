/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.naive;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.terracotta.workmanager.DefaultWorkItem;
import org.terracotta.workmanager.worker.Worker;

/**
 * Worker bean, recieves a work from the same single work queue. It grabs the
 * next pending work and executes it.
 */
public class SimpleWorker implements Worker {

	protected final ExecutorService m_threadPool = Executors.newCachedThreadPool();

	protected final SimpleQueue m_queue;

	protected volatile boolean m_isRunning = true;

	public SimpleWorker(final SimpleQueue queue) {
		m_queue = queue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.terracotta.commonj.workmanager.simple.Worker#start()
	 */
	public void start() throws WorkException {
		while (m_isRunning) {
			final DefaultWorkItem workItem = m_queue.take();
			final Work work = workItem.getResult();
			m_threadPool.execute(new Runnable() {
				public void run() {
					try {
						workItem.setStatus(WorkEvent.WORK_STARTED, null);
						work.run();
						workItem.setStatus(WorkEvent.WORK_COMPLETED, null);
					} catch (Throwable e) {
						workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
					}
				}
			});
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.terracotta.commonj.workmanager.simple.Worker#stop()
	 */
	public void stop() {
		m_threadPool.shutdown();
		m_isRunning = false;
	}
}
