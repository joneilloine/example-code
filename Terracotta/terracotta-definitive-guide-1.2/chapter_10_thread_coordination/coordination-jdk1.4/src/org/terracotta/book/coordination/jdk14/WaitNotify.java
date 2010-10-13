package org.terracotta.book.coordination.jdk14;
/**
 * This sample shows the use of wait and notify to coordinate 2 or more processes as if they were threads.
 * It also demonstrates how one might share a clustered configuration across processes in the same way that it
 * is shared across threads.
 * 
 * In this case the passed in number of nodes (2 by default will) startup and wait for the number to join (like
 * a cyclic barrier).
 *
 * 
 */
public class WaitNotify {
	private static final int MINIMUM_EXPECTED_PARTICIPANTS = 2;
	private Configuration config;
	private int nodesJoined;

	public WaitNotify(Configuration aConfig) {
		this.config = aConfig;
		synchronized (this.config) {
			config.setMyNodeID(++nodesJoined);
			System.out.println("Nodes Joined:" + nodesJoined);
			this.config.notifyAll();
		}
	}

	public void run() throws Exception {
		synchronized (config) {

			while (nodesJoined < config.getTotalNodeCount()) {
				System.out.println("Node " + config.getMyNodeID()
						+ " is waiting for more nodes to join.");
				config.wait();
			}
			System.out.println("All " + config.getTotalNodeCount()
					+ " nodes joined! I'm node:" + config.getMyNodeID());
		}
	}

	public static void main(String[] args) throws Exception {
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

		Configuration c = new Configuration(expectedParticipants);
		new WaitNotify(c).run();
	}

	private static class Configuration {
		private int totalNodeCount;
		private transient int myNodeID;

		public Configuration(int totalNodeCount) {
			this.totalNodeCount = totalNodeCount;
		}

		public int getMyNodeID() {
			return myNodeID;
		}

		public void setMyNodeID(int myNodeID) {
			this.myNodeID = myNodeID;
		}

		public int getTotalNodeCount() {
			return totalNodeCount;
		}

	}
}
