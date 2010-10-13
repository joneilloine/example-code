/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.routing;

import java.util.Set;

import org.terracotta.workmanager.pipe.Pipe;

import commonj.work.Work;
import commonj.work.WorkListener;

/**
 * Interface that all routers should implement in order to route the work to a
 * specific routing ID (registered by a worker).
 */
public interface Router<ID> {

	/**
	 * Routes or reroutes a work item. Is responsible for putting the Work onto
	 * the "correct" queue according the the routing algorithm.
	 * 
	 * @param work
	 *          the Work to be routed
	 * 
	 * @return the routable work item that is wrapping the Work
	 * @throws {@link WorkException}
	 * @throws {@link WorkRejectedException}
	 */
	RoutableWorkItem<ID> route(RoutableWorkItem<ID> workItem);

	/**
	 * Routes the work. Is responsible for putting the Work onto the "correct"
	 * queue according the the routing algorithm.
	 * 
	 * @param work
	 *          the Work to be routed
	 * 
	 * @return the routable work item that is wrapping the Work
	 * @throws {@link WorkException}
	 * @throws {@link WorkRejectedException}
	 */
	RoutableWorkItem<ID> route(Work work);

	/**
	 * Routes the work. Is responsible for putting the Work onto the "correct"
	 * queue according the the routing algorithm.
	 * 
	 * @param work
	 *          the Work to be routed
	 * @param listener
	 *          the WorkListener to monitor the status for the Work
	 * 
	 * @return the routable work item that is wrapping the Work
	 * @throws {@link WorkException}
	 * @throws {@link WorkRejectedException}
	 */
	RoutableWorkItem<ID> route(Work work, WorkListener listener);
	
	/**
	 * Resets down the Router and its PipeManager.
	 */
	void reset();

	/**
	 * Returns a set with all the completed work items.
	 * 
	 * @return all completed work items
	 */
	Set<RoutableWorkItem<ID>> getAllCompletedWork();

	/**
	 * Registers a new routing id.
	 * 
	 * @param routingID
	 */
	void register(ID routingID);

	/**
	 * Unregisters a new routing id.
	 * 
	 * @param routingID
	 */
	void unregister(ID routingID);

	/**
	 * Returns the Pipe for a specific routing ID.
	 * 
	 * @param routingID
         * @return the pipe
	 */
        Pipe<RoutableWorkItem<ID>, ID> getPipeFor(ID routingID);
}
