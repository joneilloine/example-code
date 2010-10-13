/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.naive;

import org.terracotta.workmanager.worker.Worker;

public class SimpleWorkerRunner {

	public static void main(String[] args) throws Exception {
		System.out.println("-- starting worker...");
		System.out.println("-- kill it with ^C when finished");

		Worker worker = new SimpleWorker(new SimpleQueue());
		worker.start();
	}
}
