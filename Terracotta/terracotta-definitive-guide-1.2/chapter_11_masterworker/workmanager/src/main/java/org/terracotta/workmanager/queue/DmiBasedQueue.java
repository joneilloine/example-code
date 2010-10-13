/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.queue;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class DmiBasedQueue<T> implements Queue<T> {
	public static class Factory<T> implements Queue.Factory<T> {
		public Queue<T> create() {
			return new DmiBasedQueue<T>();
		}
	}

	transient Object m_local = null;

	transient BlockingQueue<T> m_queue;

	public T put(T item) throws InterruptedException {
		if (item == null) {
			throw new AssertionError("Invalid argument passed");
		}
		if (m_local == null) {
			m_local = new Object();
		}

		store(item);
		return item;
	}

	protected void store(T item) throws InterruptedException {
		// put was called
		if (m_local != null) {
			return;
		}

		if (item == null) {
			throw new AssertionError("item is null after DMI!");
		}
		// put wasn't called
		synchronized (this) {
			if (m_queue == null) {
				m_queue = new LinkedBlockingQueue<T>();
				notify();
			}
		}
		m_queue.put(item);
	}

	public T take() throws InterruptedException {
		synchronized (this) {
			while (m_queue == null) {
				wait();
			}
		}

		return m_queue.take();
	}

	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		synchronized (this) {
			while (m_queue == null) {
				// TODO timeout
				wait();
			}
		}
		return m_queue.poll(timeout, unit);
	}

	public int size() {
		return m_queue.size();
	}

	public void clear() {
		m_queue.clear();
	}
        
        public Iterator<T> iterator() {
          return m_queue.iterator();
        }
}
