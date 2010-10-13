package org.terracotta.workmanager.dynamic;

import java.util.List;
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

	private int m_index = 0;

	public RoundRobinRouter(final PipeManager<ID> pipeManager) {
		m_pipeManager = pipeManager;
	}

	public RoutableWorkItem<ID> route(final Work work) {
		return route(work, null);
	}

	public RoutableWorkItem<ID> route(final Work work, final WorkListener listener) {
		waitForAtLeastOnePipe();
		RoutableWorkItem<ID> workItem = createRoutableWorkItem(work, listener);
		try {
			m_pipeManager.put(workItem, workItem.getRoutingID());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		isInterrupted(workItem);
		return workItem;
	}

	public RoutableWorkItem<ID> route(final RoutableWorkItem<ID> workItem) {
		try {
			waitForAtLeastOnePipe();
			m_pipeManager.put(workItem, workItem.getRoutingID());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
		isInterrupted(workItem);
		return workItem;
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

	private RoutableWorkItem<ID> createRoutableWorkItem(Work work, WorkListener listener) {
		synchronized (m_pipeManager) {
			List<ID> routingIDs = m_pipeManager.getRoutingIDs();
			if (m_index == routingIDs.size()) {
				m_index = 0;
			}
			return m_workItemFactory.create(work, listener, routingIDs.get(m_index++));
		}
	}
}
