/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager;

import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkItem;

/**
 * Work event, is sent to the registered WorkListener when the status for the
 * work has been changed.
 */
public class DefaultWorkEvent implements WorkEvent {

	private final int m_type;

	private final WorkItem m_workItem;

	private final WorkException m_exception;

	public DefaultWorkEvent(final int type, final WorkItem item, final WorkException exception) {
		m_type = type;
		m_workItem = item;
		m_exception = exception;
	}

	public int getType() {
		return m_type;
	}

	public WorkItem getWorkItem() {
		return m_workItem;
	}

	public WorkException getException() {
		return m_exception;
	}
}
