/*
 * Copyright (c) 2005 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.naive;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import org.terracotta.workmanager.AbstractWorkManager;
import org.terracotta.workmanager.DefaultWorkItem;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

/**
 * Implementation of the WorkManager abstraction that is scheduling all work to
 * the same work single queue.
 */
public class SimpleWorkManager implements WorkManager {

	private final SimpleQueue m_queue;

	public SimpleWorkManager(final SimpleQueue queue) {
		m_queue = queue;
	}

	public WorkItem schedule(final Work work) {
		return schedule(work, null);
	}

	public WorkItem schedule(final Work work, final WorkListener listener) {
		DefaultWorkItem workItem = new DefaultWorkItem(work, null);
		try {
			m_queue.put(workItem);
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	public boolean waitForAll(final Collection workItems, final long timeout) throws InterruptedException {
		long start = System.currentTimeMillis();
		do {
			synchronized (this) {
				boolean isAllCompleted = true;
				for (Iterator<WorkItem> it = workItems.iterator(); it.hasNext() && isAllCompleted;) {
					int status = it.next().getStatus();
					isAllCompleted = status == WorkEvent.WORK_COMPLETED || status == WorkEvent.WORK_REJECTED;
				}
				if (isAllCompleted) {
					return true;
				}
				if (timeout == IMMEDIATE) {
					return false;
				}
				if (timeout == INDEFINITE) {
					continue;
				}
			}
		} while ((System.currentTimeMillis() - start) < timeout);
		return false;
	}

	public Collection waitForAny(final Collection workItems, final long timeout) throws InterruptedException {
		long start = System.currentTimeMillis();
		do {
			synchronized (this) {
				Collection<WorkItem> completed = new ArrayList<WorkItem>();
				for (Iterator<WorkItem> it = workItems.iterator(); it.hasNext();) {
					WorkItem workItem = it.next();
					if (workItem.getStatus() == WorkEvent.WORK_COMPLETED || workItem.getStatus() == WorkEvent.WORK_REJECTED) {
						completed.add(workItem);
					}
				}
				if (!completed.isEmpty()) {
					return completed;
				}
			}
			if (timeout == IMMEDIATE) {
				return Collections.EMPTY_LIST;
			}
			if (timeout == INDEFINITE) {
				continue;
			}
		} while ((System.currentTimeMillis() - start) < timeout);
		return Collections.EMPTY_LIST;
	}
}
