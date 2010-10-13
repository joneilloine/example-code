/*
 * Copyright (c) 2006-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.naive;

import commonj.work.WorkException;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.terracotta.workmanager.DefaultWorkItem;

/**
 * Implements a single work queue which wraps a linked blocking queue.
 */
public class SimpleQueue {

	// Terracotta Shared Root
	private final BlockingQueue<DefaultWorkItem> m_workQueue = new LinkedBlockingQueue<DefaultWorkItem>();

	public void put(final DefaultWorkItem workItem) throws InterruptedException {
		m_workQueue.put(workItem); // blocks if queue is full
	}

	public DefaultWorkItem peek() {
		return m_workQueue.peek(); // returns null if queue is empty
	}

	public DefaultWorkItem take() throws WorkException {
		try {
			return m_workQueue.take(); // blocks if queue is empty
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new WorkException(e);
		}
	}
}
