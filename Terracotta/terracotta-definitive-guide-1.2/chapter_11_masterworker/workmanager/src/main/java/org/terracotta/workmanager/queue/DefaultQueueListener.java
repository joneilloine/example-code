package org.terracotta.workmanager.queue;

import java.util.Set;

import org.terracotta.workmanager.MutableWorkItem;

import commonj.work.WorkEvent;

public class DefaultQueueListener<T extends MutableWorkItem, ID> extends QueueListener<T, ID> {

	private Set<T> m_allCompletedWorkItems;

	public DefaultQueueListener(final ID routingID, final Queue<T> queue, final Set<T> allCompletedWorkItems) {
		super(routingID, queue);
		m_allCompletedWorkItems = allCompletedWorkItems;
	}

	@Override
	public boolean event(T workItem) throws Exception {
		
		/*
		 * If t comes back with status of ACCEPTED, it ran on the fast path, so it's
		 * real status is COMPLETED, so we set that now (not on the remote node)
		 */
		if (workItem.getStatus() == WorkEvent.WORK_ACCEPTED) {
			workItem.setStatus(WorkEvent.WORK_COMPLETED, null);
		}

		/*
		 * Now call the listener
		 */
		workItem.fireListener();

		/*
		 * If the status is not completed, don't flag the item as done
		 */
		if (workItem.getStatus() != WorkEvent.WORK_COMPLETED) {
			return true;
		}

		/*
		 * The item is completed, flag it as done
		 */
		synchronized (m_allCompletedWorkItems) {
			m_allCompletedWorkItems.add(workItem);
			m_allCompletedWorkItems.notify();
		}
		return true;
	}

	public void start() {
		System.out.println(this.getClass().getSimpleName() + " has started - serving pipe: " + getRoutingID());
		super.start();
	}
}
