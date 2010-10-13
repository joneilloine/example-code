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
 * Routes all work to the same single queue.
 */
public class SingleQueueRouter<ID> implements Router<ID> {
	private final ID m_singleQueueID;

	private final PipeManager<ID> m_pipeManager;

	private final RoutableWorkItem.Factory<ID> m_workItemFactory = new RoutableWorkItem.Factory<ID>();

	public SingleQueueRouter(final PipeManager<ID> pipeManager, final ID singleQueueID) {
		m_singleQueueID = singleQueueID;
		m_pipeManager = pipeManager;
	}

	public RoutableWorkItem<ID> route(final Work work) {
		return route(work, null);
	}

	public RoutableWorkItem<ID> route(final Work work, final WorkListener listener) {
		RoutableWorkItem<ID> workItem = m_workItemFactory.create(work, listener, m_singleQueueID);
		try {
			m_pipeManager.getOrCreatePipeFor(m_singleQueueID).getPendingWorkQueue().put(workItem);
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	public RoutableWorkItem<ID> route(final RoutableWorkItem<ID> workItem) {
		try {
			m_pipeManager.getOrCreatePipeFor(m_singleQueueID).getPendingWorkQueue().put(workItem);
		} catch (InterruptedException e) {
			workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
			Thread.currentThread().interrupt();
		}
		return workItem;
	}

	public Pipe<RoutableWorkItem<ID>, ID> getPipeFor(ID routingID) {
          return m_pipeManager.getOrCreatePipeFor(routingID);
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
}