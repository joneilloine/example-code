package org.terracotta.book.coordination.jmx.jdk15;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import org.terracotta.util.jmx.ClusterEvents;
import org.terracotta.util.jmx.SimpleListener;

/**
 * This sample shows the use of Cyclic barrier to coordinate two or more JVM processes as if they were threads running in the 
 * same JVM.  It also shows how you can integrate cluster events to tell your code when nodes join and leave the cluster.
 *
 * If you pass in a number at startup that will be used as the number of participants to wait for. If no number
 * is passed in then it defaults to three.
 * 
 * By extending the abstract class org.terracotta.util.jmx.SimpleListener, instances of this class receive notification 
 * when this JVM or other JVMs join or leave the Terracotta cluster.
 */
public class CoordinatedStagesWithClusterEvents extends SimpleListener {

	private CyclicBarrier enterBarrier;
	private CyclicBarrier exitBarrier;

	private static int MINIMUM_EXPECTED_PARTICIPANTS = 3;

	/**
	 * Create an instance, setting the number of VMs expected to participate in
	 * the demo.
	 * 
	 * @param expectedParticipants
	 *          Description of Parameter
	 */
	public CoordinatedStagesWithClusterEvents(int expectedParticipants) {
		ClusterEvents.registerListener(this);
		assert (expectedParticipants > 1);
		System.out.println("Excpected Participants: " + expectedParticipants);
		this.enterBarrier = new CyclicBarrier(expectedParticipants);
		this.exitBarrier = new CyclicBarrier(expectedParticipants);
	}

	public void run() {
		try {
			// wait for all participants before performing tasks
			System.out.println("Stage 1: Arriving at enterBarrier.  I'll pause here until all "  + enterBarrier.getParties() + " threads have arrived...");			
			enterBarrier.await();
			System.out.println("Stage 2: Passed through enterBarrier because all threads arrived.  Arriving at exitBarrier...");
			exitBarrier.await();
			System.out.println("Stage 3: Passed through exitBarrier.  All done.  Goodbye.");

		} catch (BrokenBarrierException ie) {
			ie.printStackTrace();
		} catch (InterruptedException ie) {
			ie.printStackTrace();
		}
	}


	/**
	 * This method, defined in the org.terracotta.util.jmx.SimpleListener class, is called when another JVM joins the cluster
	 */
	public void nodeConnected(Object nodeID) {
		System.out.println("Another JVM has just connected to the Terracotta cluster: node id " + nodeID);
	}

	/**
	 * This method, defined in the org.terracotta.util.jmx.SimpleListener class, is called when another JVM leaves the cluster
	 */
	public void nodeDisconnected(Object nodeID) {
		System.out.println("Another JVM has just disconnected from the cluster: node id " + nodeID);
	}

	/**
	 * This method, defined in the org.terracotta.util.jmx.SimpleListener class, is called when this JVM joins the cluster
	 */
	public void thisNodeConnected(Object nodeID) {
		System.out.println("I just connected to the Terracotta cluster: node id " + nodeID);
	}

	/**
	 * This method, defined in the org.terracotta.util.jmx.SimpleListener class, is called when this JVM leaves the cluster
	 */
	public void thisNodeDisconnected(Object nodeID) {
		System.out.println("I just disconnected from the Terracotta cluster: node id " + nodeID);
	}

	public static final void main(String[] args) throws Exception {
		int expectedParticipants = MINIMUM_EXPECTED_PARTICIPANTS;
		if (args.length == 0) {
			System.out.println("Node count NOT specified. Setting count to " + MINIMUM_EXPECTED_PARTICIPANTS);
		} else {
			try {
				expectedParticipants = Integer.parseInt(args[0]);
				if (expectedParticipants < 1) {
					throw new Exception("Invalid node count");
				}
			} catch (Exception e) {
				System.out.println("Invalid node count:" + args[0]);
				System.exit(1);
			}
		}
		CoordinatedStagesWithClusterEvents demo = new CoordinatedStagesWithClusterEvents(expectedParticipants);
		demo.run();
	}
}
