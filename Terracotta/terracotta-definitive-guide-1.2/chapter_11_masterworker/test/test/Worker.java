package test;

import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.dynamic.DynamicWorker;

import commonj.work.WorkException;

public class Worker {

	public static void main(String[] args) {
		System.out.println("-- starting worker...");
		System.out.println("-- kill it with ^C when finished");
		String routingID = null;
		if (args.length != 0) {
			routingID = args[0];
		}

		PipeManager<String> pipeManager = new PipeManager<String>(Master.PIPE_FACTORY, PipeManager.Initiator.WORKER);
		DynamicWorker worker = new DynamicWorker(pipeManager);
		try {
			worker.start();
		} catch (WorkException e) {
			e.printStackTrace();
		}
	}

}
