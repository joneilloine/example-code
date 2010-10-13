/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.routing;

import org.terracotta.workmanager.DefaultWorkEvent;
import org.terracotta.workmanager.MutableWorkItem;
import org.terracotta.workmanager.routing.Routable;
import org.terracotta.workmanager.routing.RoutableWorkItem;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;

/**
 * The work item, holds the work to be executed, status of the progress and the
 * future result.
 * <p>
 * This class is an implementation of the <code>OptimizableWorkItem</code>
 * interface which provides optional cancellation of the tracking of work status
 * that is defined by the CommonJ spec, which means that the worker will *not*
 * track work status. This this is the preferred WorkItem to use and will enable
 * much better performance and scalability while sacrificing control and
 * spec-compliance.
 */
public class RoutableWorkItem<ID> implements Routable<ID>, MutableWorkItem {

	public static int CANCEL_WORK_STATUS_TRACKING = 1 << WorkEvent.WORK_STARTED;

	protected final int m_flags;

	protected final Work m_work;

	protected transient final WorkListener m_workListener;

	protected int m_status;

	protected boolean m_dirty;

	protected WorkException m_exception;

	protected ID m_routingID;

	public RoutableWorkItem(final int flags, final Work work, final WorkListener workListener, final ID routingID) {
		m_flags = flags;
		m_work = work;
		m_status = -1;
		m_dirty = false;
		m_workListener = workListener;
		m_routingID = routingID;
	}

	public RoutableWorkItem(final Work work, final WorkListener workListener, final ID routingID) {
		m_flags = 0;
		m_work = work;
		m_status = -1;
		m_dirty = false;
		m_workListener = workListener;
		m_routingID = routingID;
	}

	public Work getResult() {
		return m_work;
	}

	public int getFlags() {
		return m_flags;
	}

	public synchronized int getStatus() {
		return m_status;
	}

	public synchronized void setStatus(final int status, final WorkException exception) {
		m_status = status;
		m_exception = exception;
		m_dirty = true;
	}

	public synchronized ID getRoutingID() {
		return m_routingID;
	}

	public synchronized void setRoutingID(final ID routingID) {
		m_routingID = routingID;
	}

	public WorkListener getWorkListener() {
		return m_workListener;
	}

	public void fireListener() {
		if (m_workListener == null) {
			return;
		}

		int status;
		WorkException exception;
		synchronized (this) {
			if (!m_dirty) {
				return;
			}
			status = getStatus();
			exception = m_exception;
			m_dirty = false;
		}

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

	public synchronized String toString() {
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

	public static class Factory<ID> {
		private final int flags;

		public Factory() {
			this(0);
		}

		public Factory(final int flags) {
			this.flags = flags;
		}

		public RoutableWorkItem<ID> create(Work work, WorkListener listener, ID routingID) {
			return new RoutableWorkItem<ID>(flags, work, listener, routingID);
		}
	}
}
