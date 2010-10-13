package org.terracotta.workmanager.statik;

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

	public LoadBalancingRouter(final PipeManager<ID> pipeManager, final ID[] routingIDs) {
		m_pipeManager = pipeManager;
		// create all queues upfront
		for (int i = 0; i < routingIDs.length; i++) {
			m_pipeManager.getOrCreatePipeFor(routingIDs[i]);
		}
	}

	public RoutableWorkItem<ID> route(final Work work) {
		return route(work, null);
	}

	public RoutableWorkItem<ID> route(final Work work, final WorkListener listener) {
		LoadBalancingRouter.PipeInfo<ID> shortestPipeInfo = getShortestPipe();
		RoutableWorkItem<ID> workItem = m_workItemFactory.create(work, listener, shortestPipeInfo.routingID);
		return scheduleWorkItem(shortestPipeInfo, workItem);
	}

	public RoutableWorkItem<ID> route(final RoutableWorkItem<ID> workItem) {
		LoadBalancingRouter.PipeInfo<ID> shortestPipeInfo = getShortestPipe();
		synchronized (workItem) {
			workItem.setRoutingID(shortestPipeInfo.routingID);
		}
		return scheduleWorkItem(shortestPipeInfo, workItem);
	}

        public Pipe<RoutableWorkItem<ID>, ID> getPipeFor(ID routingID) {
          return m_pipeManager.getOrCreatePipeFor(routingID);
        }
        
      private RoutableWorkItem<ID> scheduleWorkItem(LoadBalancingRouter.PipeInfo<ID> shortestQueue, RoutableWorkItem<ID> workItem) {
		try {
			shortestQueue.pipe.getPendingWorkQueue().put(workItem);
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	private LoadBalancingRouter.PipeInfo<ID> getShortestPipe() {
		LoadBalancingRouter.PipeInfo<ID> shortestQueue = new LoadBalancingRouter.PipeInfo<ID>();

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
	
	public void register(ID routingID) {
		m_pipeManager.getOrCreatePipeFor(routingID);
	}

	public void unregister(ID routingID) {
		m_pipeManager.removePipeFor(routingID);
	}

	public void reset() {
	  m_pipeManager.reset();
	}

	public Set<RoutableWorkItem<ID>> getAllCompletedWork() {
		return m_pipeManager.getAllCompletedWork();
	}

	private static class PipeInfo<ID> {
		public ID routingID;

		public Pipe<RoutableWorkItem<ID>, ID> pipe;

		public int pipeLength = 1000;
	}

}