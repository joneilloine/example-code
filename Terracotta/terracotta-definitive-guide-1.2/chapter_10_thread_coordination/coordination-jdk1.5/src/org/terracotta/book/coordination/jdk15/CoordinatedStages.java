package org.terracotta.book.coordination.jdk15;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CoordinatedStages {

	private CyclicBarrier enterBarrier;
	private CyclicBarrier exitBarrier;

	private static int MINIMUM_EXPECTED_PARTICIPANTS = 2;

	/**
	 * Create an instance, setting the number of VMs expected to participate in
	 * the demo.
	 * 
	 * @param expectedParticipants
	 *            Description of Parameter
	 */
	public CoordinatedStages(int expectedParticipants) {
		assert (expectedParticipants > 1);
		System.out.println("Expected Participants: " + expectedParticipants);
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

	public static final void main(String[] args) throws Exception {
		int expectedParticipants = MINIMUM_EXPECTED_PARTICIPANTS;
		if (args.length == 0) {
			System.out.println("Node count NOT specified. Setting count to "
					+ MINIMUM_EXPECTED_PARTICIPANTS);
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
		(new CoordinatedStages(expectedParticipants)).run();
	}

}
