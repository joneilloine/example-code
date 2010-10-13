/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.worker;

import org.terracotta.workmanager.queue.Queue;
import org.terracotta.workmanager.routing.RoutableWorkItem;

import commonj.work.WorkEvent;
import commonj.work.WorkException;

/**
 * Worker tasks that should be executed by the worker - knows how to set the
 * status and execute the work.
 */
public class WorkerTask<T extends RoutableWorkItem<?>> implements Runnable {
	private final Queue<T> m_completedWork;
	private final T m_workItem;

	public WorkerTask(T workItem, Queue<T> completedWork) {
		m_workItem = workItem;
		m_completedWork = completedWork;
	}

	public void run() {
		try {
			setStatus(WorkEvent.WORK_STARTED, null);
			runWork();
			setStatus(WorkEvent.WORK_COMPLETED, null);
		} catch (Exception e) {
			setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
		} finally {

			// Now send the workItem back on the completion queue for processing by to
			// the sender.
			try {
				m_completedWork.put(m_workItem);
			} catch (InterruptedException e) {
				e.printStackTrace();
				Thread.currentThread().interrupt();
			}

		}
	}

	private void runWork() {
		m_workItem.getResult().run();
	}

	private void setStatus(int status, WorkException exception) {
		if ((m_workItem.getFlags() & RoutableWorkItem.CANCEL_WORK_STATUS_TRACKING) > 0) {

			/*
			 * Code path to optimize setting of WORK_STARTED and WORK_COMPLETED, this
			 * path never sets these statuses on the Worker VM
			 */

			// if the incoming status is WORK_STARTED, we do nothing
			if (status == WorkEvent.WORK_STARTED) {
				return;
			}

			// if the incoming status is WORK_COMPLETED, we do nothing, but
			// drop down to the m_completedWork portion.
			// otherwise, we set the status, and put the item on the queue
			if (status != WorkEvent.WORK_COMPLETED) {
				m_workItem.setStatus(status, exception);
			}
		} else {
			m_workItem.setStatus(status, exception);
		}
	}
}
