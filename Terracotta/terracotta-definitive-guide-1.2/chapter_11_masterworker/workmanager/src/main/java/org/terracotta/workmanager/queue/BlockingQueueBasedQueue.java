/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.queue;

import java.util.Iterator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * An implementation of a Channel that uses a BlockingQueue. The default
 * constructor instantiates an unbounded LinkedBlockingQueue for the Queue.
 * 
 * @param <T>
 *          The type of message passed in the Channel
 */
public class BlockingQueueBasedQueue<T> implements Queue<T> {
	public static class Factory<T> implements Queue.Factory<T> {
		private final int m_size;

		public Factory(final int size) {
			m_size = size;
		}

		public Queue<T> create() {
			return new BlockingQueueBasedQueue<T>(m_size);
		}
	}

	private final BlockingQueue<T> m_queue;

	public BlockingQueueBasedQueue(int size) {
		this(new LinkedBlockingQueue<T>(size));
	}

	public BlockingQueueBasedQueue(final BlockingQueue<T> queue) {
	  m_queue = queue;
	}

	public T poll(long timeout, TimeUnit unit) throws InterruptedException {
		return m_queue.poll(timeout, unit);
	}

	public T put(T item) throws InterruptedException {
		m_queue.put(item);
		return item;
	}

	public T take() throws InterruptedException {
		return m_queue.take();
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

