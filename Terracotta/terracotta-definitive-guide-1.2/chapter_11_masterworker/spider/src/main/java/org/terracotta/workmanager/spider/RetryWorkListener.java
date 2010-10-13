package org.terracotta.workmanager.spider;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.terracotta.workmanager.pipe.Pipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;
import org.terracotta.workmanager.statik.StaticWorkManager;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkException;
import commonj.work.WorkListener;
import commonj.work.WorkManager;

/**
 * Work listener that retries work upon rejection.
 */
public class RetryWorkListener implements WorkListener {

	private static final int MAX_NR_OF_RETRIES = 3;

	private final String m_routingIdForFailOverNode;

	private final Map<Work, Integer> m_retryCount = new ConcurrentHashMap<Work, Integer>();

	// work manager sends schedules all work to the fail-over worker
	private final WorkManager m_workManager;;

	public RetryWorkListener(
			final String routingIdForFailOverNode, 
			final Pipe.Factory<RoutableWorkItem<String>, String> pipeFactory) {
		
		m_routingIdForFailOverNode = routingIdForFailOverNode;

		m_workManager = new StaticWorkManager<String>(new Router<String>() {
			private final PipeManager<String> m_pipeManager = new PipeManager<String>(pipeFactory, PipeManager.Initiator.MASTER);

			public RoutableWorkItem<String> route(final Work work, final WorkListener listener) {
				final RoutableWorkItem<String> workItem = new RoutableWorkItem<String>(work, RetryWorkListener.this,
						m_routingIdForFailOverNode);
				try {
					m_pipeManager.put(workItem, m_routingIdForFailOverNode);
				} catch (InterruptedException e) {
					workItem.setStatus(WorkEvent.WORK_REJECTED, new WorkException(e));
					Thread.currentThread().interrupt();
				}
				return workItem;
			}

			public RoutableWorkItem<String> route(final Work work) {
				return route(work, null);
			}

			public RoutableWorkItem<String> route(final RoutableWorkItem<String> workItem) {
				throw new UnsupportedOperationException();
			}

			public void reset() {
				throw new UnsupportedOperationException();			
			}

			public Set<RoutableWorkItem<String>> getAllCompletedWork() {
				throw new UnsupportedOperationException();			
			}

			public void removePipeFor(String routingID) {
				throw new UnsupportedOperationException();			
			}

			public void register(String routingID) {
				throw new UnsupportedOperationException();			
			}

			public void unregister(String routingID) {
				throw new UnsupportedOperationException();			
			}

			public Pipe<RoutableWorkItem<String>, String> getPipeFor(String routingID) {
				throw new UnsupportedOperationException();			
			}
		});
	}

	public void workRejected(WorkEvent event) {
		Work rejectedWork = event.getWorkItem().getResult();

		if (!m_retryCount.containsKey(rejectedWork)) {
			m_retryCount.put(rejectedWork, 1);
		}
		int nrOfRetries = m_retryCount.get(rejectedWork);
		if (nrOfRetries <= MAX_NR_OF_RETRIES) {
			System.err.println("### RETRYING WORK: " + rejectedWork + " " + nrOfRetries + " time(s)...");

			// reschedule the work
			m_workManager.schedule(rejectedWork, this);

			m_retryCount.put(rejectedWork, ++nrOfRetries);
		} else {
			m_retryCount.remove(rejectedWork); // giving up on this work
		}
	}

	public void workAccepted(WorkEvent event) {
	}

	public void workCompleted(WorkEvent event) {
	}

	public void workStarted(WorkEvent event) {
	}

}
