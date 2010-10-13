/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager;

import commonj.work.Work;

import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;

/**
 * The work item, holds the work to be executed, status of the progress and the
 * future result.
 */
public class DefaultWorkItem implements WorkItem {

	protected volatile int m_status;

	protected final Work m_work;

	protected final WorkListener m_workListener;

	public DefaultWorkItem(final Work work, final WorkListener workListener) {
		m_work = work;
		m_status = WorkEvent.WORK_ACCEPTED;
		m_workListener = workListener;
	}

	public Work getResult() {
		return m_work;
	}

	public synchronized void setStatus(final int status, final WorkException exception) {
		m_status = status;
		if (m_workListener != null) {
			switch (status) {
			case WorkEvent.WORK_ACCEPTED:
				m_workListener.workAccepted(new DefaultWorkEvent(WorkEvent.WORK_ACCEPTED, this, exception));
				break;
			case WorkEvent.WORK_REJECTED:
				m_workListener.workRejected(new DefaultWorkEvent(WorkEvent.WORK_REJECTED, this, exception));
				break;
			case WorkEvent.WORK_STARTED:
				m_workListener.workStarted(new DefaultWorkEvent(WorkEvent.WORK_STARTED, this, exception));
				break;
			case WorkEvent.WORK_COMPLETED:
				m_workListener.workCompleted(new DefaultWorkEvent(WorkEvent.WORK_COMPLETED, this, exception));
				break;
			}
		}
	}

	public synchronized int getStatus() {
		return m_status;
	}

	public WorkException newWorkException(final Throwable e) {
		e.printStackTrace();
		WorkException we = new WorkException(e.getMessage());
		setStatus(WorkEvent.WORK_REJECTED, we);
		Thread.currentThread().interrupt();
		return we;
	}

	public int compareTo(Object compareTo) {
		// check if Work is implementing Comparable
		Work work = ((WorkItem) compareTo).getResult();
		if (m_work instanceof Comparable) {
			Comparable<Comparable<?>> comparableWork1 = (Comparable<Comparable<?>>) m_work;
			if (work instanceof Comparable) {
				Comparable<?> comparableWork2 = (Comparable<?>) work;
				return comparableWork1.compareTo(comparableWork2);
			}
		}
		return 0; // ordering is not specified
	}

	public String toString() {
		String status;
		switch (m_status) {
		case WorkEvent.WORK_ACCEPTED:
			status = "WORK_ACCEPTED";
			break;
		case WorkEvent.WORK_COMPLETED:
			status = "WORK_COMPLETED";
			break;
		case WorkEvent.WORK_REJECTED:
			status = "WORK_REJECTED";
			break;
		case WorkEvent.WORK_STARTED:
			status = "WORK_STARTED";
			break;
		default:
			throw new IllegalStateException("illegal (unknown) status " + m_status);
		}
		return m_work.toString() + ":" + status;
	}
}
