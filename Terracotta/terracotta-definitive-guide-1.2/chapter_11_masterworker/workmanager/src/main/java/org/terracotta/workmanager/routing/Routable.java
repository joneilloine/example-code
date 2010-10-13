/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.routing;

/**
 * Interface that all routable work items should implement.
 */
public interface Routable<ID> {

	public void setRoutingID(ID routingID);

	public ID getRoutingID();
}
