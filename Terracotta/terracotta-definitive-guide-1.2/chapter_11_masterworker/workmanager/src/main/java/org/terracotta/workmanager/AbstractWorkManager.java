/*
 * Copyright (c) 2006-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

/**
 * Abstract base class for implementations of a WorkManager. Provides generic
 * implementations for the waitForAll and waitForAny methods.
 */
public abstract class AbstractWorkManager implements WorkManager {

	public abstract WorkItem schedule(Work work);

	public abstract WorkItem schedule(Work work, WorkListener listener);

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
