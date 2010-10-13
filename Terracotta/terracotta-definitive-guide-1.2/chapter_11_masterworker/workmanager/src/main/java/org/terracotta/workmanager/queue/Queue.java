/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.queue;

import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * Represents a one-way communication channel. Abstract notion of putting
 * objects and receiving them (take or poll)
 * 
 * @param <T>
 */
public interface Queue<T> {
	public static interface Factory<T> {
		public Queue<T> create();
	}

	T poll(long timeout, TimeUnit unit) throws InterruptedException;

	T put(T item) throws InterruptedException;

	T take() throws InterruptedException;

	int size();
	
	void clear();
                
        Iterator<T> iterator(); 
}
