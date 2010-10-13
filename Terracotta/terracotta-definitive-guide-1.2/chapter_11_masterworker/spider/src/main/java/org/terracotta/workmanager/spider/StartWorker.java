package org.terracotta.workmanager.spider;

import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.statik.StaticWorker;
import org.terracotta.workmanager.worker.Worker;

public class StartWorker {
	public static final String	SINGLE_QUEUE_ID	= "SINGLE_QUEUE_ID";

	public static void main(String[] args) throws Exception {
		System.out.println("-- starting worker...");
		System.out.println("-- kill it with ^C when finished");
		String routingID = null;
		if (args.length != 0) {
			routingID = args[0];
		}
		if (routingID == null) {
			routingID = SINGLE_QUEUE_ID;
		}

		PipeManager<String> pipeManager = new PipeManager<String>(SpiderMaster.PIPE_FACTORY, PipeManager.Initiator.WORKER);
		Worker worker = new StaticWorker<String>(pipeManager, routingID);
		worker.start();
	}
}
