package test;

import java.util.ArrayList;
import java.util.List;

import org.terracotta.workmanager.dynamic.LoadBalancingRouter;
import org.terracotta.workmanager.dynamic.RoundRobinRouter;
import org.terracotta.workmanager.dynamic.DynamicWorkManager;
import org.terracotta.workmanager.pipe.DefaultPipe;
import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.pipe.DefaultPipe.Factory;
import org.terracotta.workmanager.queue.BlockingQueueBasedQueue;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;

import commonj.work.Work;
import commonj.work.WorkItem;

public class Master {

	public static final Factory<RoutableWorkItem<String>, String> PIPE_FACTORY = 			
		new DefaultPipe.Factory<RoutableWorkItem<String>, String>(
			new BlockingQueueBasedQueue.Factory<RoutableWorkItem<String>>(1000));
	
	private final PipeManager<String> m_pipeManager;

	private final Router<String> m_router;

	private final DynamicWorkManager m_workManager;

	public Master(String[] routingIDs) {
		m_pipeManager = new PipeManager<String>(PIPE_FACTORY, PipeManager.Initiator.MASTER);
//		m_router = new RoundRobinRouter<String>(m_pipeManager);
		m_router = new LoadBalancingRouter<String>(m_pipeManager);
		m_workManager = new DynamicWorkManager(m_router);

	}
	
	public void run() {
		List<WorkItem> workList = new ArrayList<WorkItem>(); 
		for (int i = 0; i < 100; i++) {
			workList.add(m_workManager.schedule(new MyWork(i)));
		}
		
//		testWaitForAll(workList);
		testWaitForAny_ReturnSingleItem(workList);
//		testWaitForAny_ReturnCollection(workList);		
		m_pipeManager.clearPipes();
		System.out.println("All work completed successfully");
	}

	private void testWaitForAll(List<WorkItem> workList) {
		m_workManager.waitForAll(workList, Long.MAX_VALUE);
		System.out.println("Number of completed work items: " + m_workManager.getCompletedWork().size());
	}

	private void testWaitForAny_ReturnSingleItem(List<WorkItem> workList) {
		List<WorkItem> completed = new ArrayList<WorkItem>();		
		while (true) {
			WorkItem item = m_workManager.waitForAny(Long.MAX_VALUE);
			System.out.println("Completed work item: " + ((MyWork)item.getResult()).m_i);
			completed.add(item);
			if (completed.size() == workList.size()) {
				break;
			}
		}
	}

	private void testWaitForAny_ReturnCollection(List<WorkItem> workList) {
		List<WorkItem> completed = new ArrayList<WorkItem>();
		while (true) {
			completed.addAll(m_workManager.waitForAny(null, Long.MAX_VALUE));
			if (completed.size() == workList.size()) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		Master master = new Master(args);
		master.run();
	}

	public static class MyWork implements Work {
		private int m_i = 0;
		public MyWork(int i) {
			m_i = i;
		}	

		public void run() {
			System.out.println("work nr: " + m_i);
		}
		
		public boolean isDaemon() {
			return false;
		}

		public void release() {
		}
	}
}
