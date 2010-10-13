package org.terracotta.workmanager.statik;

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
 * Round robin router. 
 * <p>
 * TODO need to handle work queue removal (in case of worker failure)
 */
public class RoundRobinRouter<ID> implements Router<ID> {

	private final PipeManager<ID> m_pipeManager;

	private final RoutableWorkItem.Factory<ID> m_workItemFactory = new RoutableWorkItem.Factory<ID>();

	private final ID[] m_routingIDs;

	private int m_index = 0;

	public RoundRobinRouter(final PipeManager<ID> pipeManager, final ID[] routingIDs) {
		m_pipeManager = pipeManager;
		m_routingIDs = routingIDs;
		// create all queues upfront
		for (int i = 0; i < routingIDs.length; i++) {
			m_pipeManager.getOrCreatePipeFor(routingIDs[i]);
		}
	}

	public RoutableWorkItem<ID> route(final Work work) {
		return route(work, null);
	}

	public RoutableWorkItem<ID> route(final Work work, final WorkListener listener) {
		RoutableWorkItem<ID> workItem = createRoutableWorkItem(work, listener);
		try {
			m_pipeManager.put(workItem, workItem.getRoutingID());
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	public RoutableWorkItem<ID> route(final RoutableWorkItem<ID> workItem) {
		try {
			m_pipeManager.put(workItem, workItem.getRoutingID());
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	public Pipe<RoutableWorkItem<ID>, ID> getPipeFor(ID routingID) {
          return m_pipeManager.getOrCreatePipeFor(routingID);
        }
        
      private synchronized RoutableWorkItem<ID> createRoutableWorkItem(final Work work, final WorkListener listener) {
		if (m_index == m_routingIDs.length) {
			m_index = 0;
		}
		return m_workItemFactory.create(work, listener, m_routingIDs[m_index++]);
	}

	public void reset() {
	  m_pipeManager.reset();
	}

	public Set<RoutableWorkItem<ID>> getAllCompletedWork() {
		return m_pipeManager.getAllCompletedWork();
	}

	public void register(ID routingID) {
		m_pipeManager.getOrCreatePipeFor(routingID);
	}

	public void unregister(ID routingID) {
		m_pipeManager.removePipeFor(routingID);
	}
}