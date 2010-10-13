/*
 * Copyright (c) 2005-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.pipe;

import java.util.Set;

import org.terracotta.workmanager.queue.Queue;
import org.terracotta.workmanager.queue.QueueListener;

/**
 * A pipe is a two way queue for work management. 
 * <p>
 * It is wrapping one queue for pending work and one for completed work as well as 
 * two listeners that are receiving call backs whenever an item has been added to 
 * one of its corresponding queues.
 * 
 * @param <T>
 */
public interface Pipe<T, ID> {
	
	Queue<T> getPendingWorkQueue();

	Queue<T> getCompletedWorkQueue();
	
	QueueListener<T, ID> getQueueListener();
	
	void startQueueListener(Set<T> allCompletedWork);
	
	void clear();

	public static interface Factory<T, ID> { 		
		Pipe<T, ID> create(ID routingID);
	}
}
