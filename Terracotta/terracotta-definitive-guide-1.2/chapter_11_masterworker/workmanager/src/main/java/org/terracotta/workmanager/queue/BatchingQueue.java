/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.queue;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class BatchingQueue<T> implements Queue<T> {
	private final long FLUSH_DELAY = 10000;

	private final int m_batchSize;
	private final Queue<T[]> m_queue;

	private final Object m_batchPutLock = new Object();
	private final Object m_batchGetLock = new Object();

	// private final ReentrantLock m_batchPutLock = new ReentrantLock();
	// private final ReentrantLock m_batchGetLock = new ReentrantLock();

	private transient List<T> m_batchPut = null;
	private transient T[] m_batchGet = null;

	private transient volatile int m_index = 0;
	private transient volatile long m_last = 0;
	private transient Timer m_timer = null;

	public static class Factory<T> implements Queue.Factory<T> {
		private final int m_batchSize;
		private final Queue.Factory<T[]> m_factory;

		public Factory(final int batchSize, final Queue.Factory<T[]> factory) {
			m_batchSize = batchSize;
			m_factory = factory;
		}

		public Queue<T> create() {
			return new BatchingQueue<T>(m_batchSize, m_factory.create());
		}
	}

	private class MyTimerTask extends TimerTask {
		@Override
		public void run() {
			System.out.println("--------timer task---------");
			List<T> ready = null;

			long current = System.currentTimeMillis();
			synchronized (m_batchPutLock) {
				// try {
				// m_batchPutLock.lock();
				if (current - m_last < FLUSH_DELAY || m_batchPut == null) {
					return;
				}
				ready = m_batchPut;
				m_batchPut = null;
				// }
				// } finally {
				// m_batchPutLock.lock();
				// }
			}
			try {
				System.out.println("flushing buffer");
				m_queue.put((T[]) ready.toArray());
			} catch (Exception ie) {
				// TODO
				ie.printStackTrace();
			}
		}
	}

	/**
	 * Private constructor - use BatchingQueue.Factory.
	 * 
	 * @param batchSize
	 * @param queue
	 */
	private BatchingQueue(final int batchSize, final Queue<T[]> queue) {
		m_batchSize = batchSize;
		m_queue = queue;
	}

	public T put(T item) throws InterruptedException {
		List<T> ready = null;
		long current = System.currentTimeMillis();

		synchronized (m_batchPutLock) {
			// try {
			// m_batchPutLock.lock();
			m_last = current;

			if (m_timer == null) {
				m_timer = new Timer(true);
				m_timer.schedule(new MyTimerTask(), 0, 100);
			}

			if (m_batchPut == null) {
				m_batchPut = new ArrayList<T>(m_batchSize);
			}

			m_batchPut.add(item);
			if (m_batchPut.size() >= m_batchSize) {
				ready = m_batchPut;
				m_batchPut = null;
			}
			// } finally {
			// m_batchPutLock.lock();
			// }
		}

		if (ready != null) {
			System.out.println("pushing buffer");
			m_queue.put((T[]) ready.toArray());
		}

		return item;
	}

	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		synchronized (m_batchGetLock) {
			// try {
			// m_batchGetLock.lock();
			batchPoll(timeout, unit);
			return m_batchGet[m_index++];
			// }
			// } finally {
			// m_batchGetLock.lock();
			// }
		}
	}

	public T take() throws InterruptedException {
		synchronized (m_batchGetLock) {
			// try {
			// m_batchGetLock.lock();
			if (m_batchGet == null || m_index >= m_batchGet.length) {
				m_batchGet = m_queue.take();
				m_index = 0;
			}
			final T workItem = m_batchGet[m_index];
			m_index++;
			return workItem;
			// } finally {
			// m_batchGetLock.lock();
			// }
		}
	}

	public int size() {
		return m_queue.size();
	}

	public void clear() {
		m_queue.clear();
	}

	private void batchPoll(long timeout, TimeUnit unit) throws InterruptedException {
		if (m_batchGet == null || m_index >= m_batchGet.length) {
			m_batchGet = m_queue.poll(timeout, unit);
			m_index = 0;
		}
	}
        
        public Iterator<T> iterator() {
          throw new UnsupportedOperationException("not implemented yet");// return m_queue.iterator();
        }
}
