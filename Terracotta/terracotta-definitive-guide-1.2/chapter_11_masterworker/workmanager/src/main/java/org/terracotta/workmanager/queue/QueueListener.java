/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.queue;

import java.util.concurrent.TimeUnit;

/**
 * Listens to a channel, and calls the listener event method when something
 * arrives.
 * 
 * @param <T>
 */
public abstract class QueueListener<T, ID> implements Runnable {
	private final ID m_routingID;
	private final Queue<T> m_queue;
	private transient volatile boolean m_shouldRun = true;
	private transient Thread m_currentThread;

	public QueueListener(final ID routingID, final Queue<T> queue) {
		m_routingID = routingID;
		m_queue = queue;
	}

	public abstract boolean event(T workItem) throws Exception;

	public void start() {
		Thread t = new Thread(this);
		t.setDaemon(true);
		t.start();
	}

	public synchronized void stop() {
		m_shouldRun = false;
	}

	public ID getRoutingID() {
		return m_routingID;
	}
	
	public void run() {
		m_currentThread = Thread.currentThread();
		String reasonForShutdown = "normal shutdown using stop()";
		while (m_shouldRun) {
			synchronized (this) {
				try {
					final T workItem = m_queue.poll(100, TimeUnit.MILLISECONDS);
					if (workItem == null) continue;
					m_shouldRun = event(workItem);
				} catch (InterruptedException ie) {
					m_currentThread.interrupt();
					m_shouldRun = false;
					reasonForShutdown = ie.toString();
				} catch (Exception e) {
					m_shouldRun = false;
					reasonForShutdown = e.toString();
				}
			}
		}
		System.out.println("QueueListener has been stopped due to: " + reasonForShutdown);
	}
}
