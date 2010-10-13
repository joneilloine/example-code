package org.terracotta.workmanager.dynamic;

import java.util.Map;
import java.util.Set;

import org.terracotta.workmanager.pipe.Pipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkListener;

/**
 * Load balancing router, routes work to the shortest queue.
 */
public class LoadBalancingRouter<ID> implements Router<ID> {

	private final PipeManager<ID> m_pipeManager;

	private final RoutableWorkItem.Factory<ID> m_workItemFactory = new RoutableWorkItem.Factory<ID>();

	public LoadBalancingRouter(final PipeManager<ID> pipeManager) {
		m_pipeManager = pipeManager;
	}

	public RoutableWorkItem<ID> route(final Work work) {
		return route(work, null);
	}

	public RoutableWorkItem<ID> route(final Work work, final WorkListener listener) {
		waitForAtLeastOnePipe();
		LoadBalancingRouter.PipeLength<ID> shortestQueue = getShortestWorkQueue();
		RoutableWorkItem<ID> workItem = m_workItemFactory.create(work, listener, shortestQueue.routingID);
		try {
			shortestQueue.pipe.getPendingWorkQueue().put(workItem);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		isInterrupted(workItem);
		return workItem;
	}

	public RoutableWorkItem<ID> route(final RoutableWorkItem<ID> workItem) {
		waitForAtLeastOnePipe();
		LoadBalancingRouter.PipeLength<ID> shortestQueue = getShortestWorkQueue();
		synchronized (workItem) {
			workItem.setRoutingID(shortestQueue.routingID);
		}
		try {
			shortestQueue.pipe.getPendingWorkQueue().put(workItem);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		isInterrupted(workItem);
		return workItem;
	}

  public Pipe<RoutableWorkItem<ID>, ID> getPipeFor(ID routingID) {
    return m_pipeManager.getOrCreatePipeFor(routingID);
  }
	private void isInterrupted(RoutableWorkItem<ID> workItem) {
		if (Thread.interrupted()) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException("thread interrupted"));
		}
	}

	private synchronized void waitForAtLeastOnePipe() {
		try {
			while (m_pipeManager.getPipes().isEmpty()) {
				System.out.println("waiting for at least one worker to join");
				wait();
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	private LoadBalancingRouter.PipeLength<ID> getShortestWorkQueue() {
		LoadBalancingRouter.PipeLength<ID> shortestQueue = new LoadBalancingRouter.PipeLength<ID>();

		int queueLengthForShortestQueue = Integer.MAX_VALUE;
		ID routingIDForShortestQueue = null;
		Map<ID, Pipe<RoutableWorkItem<ID>, ID>> allPipes = m_pipeManager.getPipes();
		for (Map.Entry<ID, Pipe<RoutableWorkItem<ID>, ID>> pipes : allPipes.entrySet()) {
			ID routingID = pipes.getKey();
			Pipe<RoutableWorkItem<ID>, ID> pipe = pipes.getValue();
			int queueSize = pipe.getPendingWorkQueue().size();
			if (queueSize <= queueLengthForShortestQueue) {
				queueLengthForShortestQueue = queueSize;
				routingIDForShortestQueue = routingID;
			}
		}
		shortestQueue.pipe = m_pipeManager.getOrCreatePipeFor(routingIDForShortestQueue);
		shortestQueue.routingID = routingIDForShortestQueue;
		return shortestQueue;
	}

	public void reset() {
		m_pipeManager.reset();
	}

	public Set<RoutableWorkItem<ID>> getAllCompletedWork() {
		return m_pipeManager.getAllCompletedWork();
	}

	public synchronized void register(ID routingID) {
		m_pipeManager.getOrCreatePipeFor(routingID);
		notifyAll();
	}

	public void unregister(ID routingID) {
		m_pipeManager.removePipeFor(routingID);
	}

	private static class PipeLength<ID> {
		public ID routingID;

		public Pipe<RoutableWorkItem<ID>, ID> pipe;

		public int pipeLength = 1000;
	}
}
