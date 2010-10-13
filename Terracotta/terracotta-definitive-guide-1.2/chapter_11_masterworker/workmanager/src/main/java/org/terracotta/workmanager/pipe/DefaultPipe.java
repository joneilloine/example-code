/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.pipe;

import java.util.Set;

import org.terracotta.workmanager.MutableWorkItem;
import org.terracotta.workmanager.queue.DefaultQueueListener;
import org.terracotta.workmanager.queue.Queue;
import org.terracotta.workmanager.queue.QueueListener;

public class DefaultPipe<T extends MutableWorkItem, ID> implements Pipe<T, ID> {
	private final Queue<T> m_pendingWorkQueue;

	private final Queue<T> m_completedWorkQueue;

	private transient QueueListener<T, ID> m_queueListener;

	private ID m_routingID;
	
	public static class Factory<T extends MutableWorkItem, ID> implements Pipe.Factory<T, ID> {
		private final Queue.Factory<T> m_queueFactory;

		public Factory(final Queue.Factory<T> queueFactory) {
			m_queueFactory = queueFactory;
		}

		public Pipe<T, ID> create(ID routingID) {
			return new DefaultPipe<T, ID>(routingID, m_queueFactory.create(), m_queueFactory.create());
		}
	}

	/**
	 * Private constructor - use DefaultPipe.Factory.
	 * 
	 * @param pendingWorkQueue
	 * @param completedWorkQueue
	 * @param allCompletedWork
	 */
	private DefaultPipe(final ID routingID, final Queue<T> pendingWorkQueue, final Queue<T> completedWorkQueue) {
		m_routingID = routingID;
		m_pendingWorkQueue = pendingWorkQueue;
		m_completedWorkQueue = completedWorkQueue;
	}

	public Queue<T> getPendingWorkQueue() {
		return m_pendingWorkQueue;
	}

	public Queue<T> getCompletedWorkQueue() {
		return m_completedWorkQueue;
	}

	public ID getRoutingID() {
		return m_routingID;
	}
	
	public QueueListener<T, ID> getQueueListener() {
		return m_queueListener;
	}

	public void startQueueListener(final Set<T> allCompletedWork) {
		if (m_queueListener != null) return;
		m_queueListener = new DefaultQueueListener<T, ID>(m_routingID, m_completedWorkQueue, allCompletedWork);
		m_queueListener.start();
	}

	public void clear() {
		m_pendingWorkQueue.clear();
		m_completedWorkQueue.clear();
		if (m_queueListener != null) {
			m_queueListener.stop();			
		}
	  try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
