/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.worker;

import commonj.work.WorkException;

/**
 * Standard Worker interface.
 */
public interface Worker {

	public abstract void start() throws WorkException;

	public abstract void stop();
}
