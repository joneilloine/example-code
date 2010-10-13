package org.terracotta.workmanager.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ListBasedQueue<T> implements Queue<T> {
	private final List<T> m_list;

	private final int m_maxSize;

	public static class Factory<T> implements Queue.Factory<T> {
		private final int m_size;

		public Factory() {
			this(1000);
		}

		public Factory(final int size) {
			super();
			m_size = size;
		}

		public Queue<T> create() {
			return new ListBasedQueue<T>(new ArrayList<T>(), m_size);
		}
	}

	public ListBasedQueue() {
		this(new ArrayList<T>(), 1000);
	}

	public ListBasedQueue(final List<T> list, int maxSize) {
		m_list = list;
		m_maxSize = maxSize;
	}

	public T put(T item) throws InterruptedException {
		synchronized (m_list) {
			while (m_list.size() >= m_maxSize) {
				m_list.wait();
			}

			m_list.add(item);
			m_list.notify();
		}

		return item;
	}

	public T take() throws InterruptedException {
		synchronized (m_list) {
			while (m_list.isEmpty()) {
				m_list.wait();
			}

			return m_list.remove(0);
		}
	}

	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		long started = System.currentTimeMillis();
		synchronized (m_list) {
			while (m_list.isEmpty()) {
				m_list.wait(timeout - System.currentTimeMillis() + started);
				if (System.currentTimeMillis() - started >= timeout) {
					return null;
				}
			}

			return m_list.remove(0);
		}
	}

	public int size() {
		return m_list.size();
	}

	public void clear() {
		m_list.clear();
	}

        
        public Iterator<T> iterator() {
          return m_list.iterator();
        }
}
