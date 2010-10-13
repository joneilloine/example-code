/*
 * Copyright (c) 2006-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.dynamic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.terracotta.util.jmx.ClusterEvents;
import org.terracotta.workmanager.pipe.Pipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.worker.Worker;
import org.terracotta.workmanager.worker.WorkerTask;

import commonj.work.WorkException;

/**
 * Worker that has an is aware of routing. Each instance of the
 * RoutingAwareWorker class gets a routing ID from the Terracotta server and
 * gets a unique work queue mapped to this routing ID.
 */
public class DynamicWorker implements Worker, ClusterEvents.Listener {

	private Pipe<RoutableWorkItem<String>, String> m_pipe;

	private final ExecutorService m_threadPool = Executors.newCachedThreadPool();

	private PipeManager<String> m_pipeManager;

	private volatile boolean m_isRunning = true;

	private String m_routingID;

	public static final int WORKER_TIMEOUT_IN_SECONDS = 60;

	public DynamicWorker(final PipeManager<String> pipeManager) {
		m_pipeManager = pipeManager;
        ClusterEvents.registerListener(this);
	}

	public void start() throws WorkException {
		while (m_isRunning) {
			waitForRegistration();
			
			final RoutableWorkItem<String> workItem;
			try {

				workItem = m_pipe.getPendingWorkQueue().take();

				// workItem =
				// m_pipe.getOutboundChannel().poll(WORKER_TIMEOUT_IN_SECONDS,
				// TimeUnit.SECONDS);
				// if (workItem == null) {
				// System.out.println("Worker has waited " +
				// String.valueOf(WORKER_TIMEOUT_IN_SECONDS)
				// + " seconds for new work. Stopping now.");
				// m_isRunning = false;
				// }
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new WorkException(e);
			}

			if (workItem != null) {
				m_threadPool.execute(new WorkerTask<RoutableWorkItem<String>>(workItem, m_pipe.getCompletedWorkQueue()));

			}
		}
	}

	public void stop() {
		m_isRunning = false;
		m_threadPool.shutdown();
		m_pipeManager.removePipeFor(m_routingID);
	}

	public synchronized void thisNodeId(final Object nodeId) {
		System.out.println("worker node id: " + nodeId);
		m_routingID = (String) nodeId;
		m_pipe = m_pipeManager.getOrCreatePipeFor(m_routingID);
		notifyAll(); // worker is registered  - notify the worker
	}

	private synchronized void waitForRegistration() {
		try {
			if (m_pipe == null) {
				System.out.println("waiting for registration");
				wait();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

    public void initialClusterMembers(Object[] arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void nodeConnected(Object arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void nodeDisconnected(Object arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void thisNodeConnected(Object arg0)
    {
        // TODO Auto-generated method stub
        
    }

    public void thisNodeDisconnected(Object arg0)
    {
        // TODO Auto-generated method stub
        
    }
}
