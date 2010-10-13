package org.terracotta.example.inventory.cli;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.terracotta.book.inventory.cli.CLIHelper;

import junit.framework.TestCase;

public class CLIHelperTest extends TestCase {
	public void testBasics() throws IOException, InterruptedException {
		TestIO io = new TestIO();


		final CLIHelper helper = new CLIHelper(io);

		int choice = helper.askMenu(null);
		assertEquals(-1, choice);

		choice = helper.askMenu(new String[] {});
		assertEquals(-1, choice);

		final LinkedBlockingQueue<Integer> answers = new LinkedBlockingQueue<Integer>();
		Asker asker = new Asker(helper, answers);

		asker.askMenu(new String[] { "a menu with only one option" });
	}

	private static final class Asker {
		private final LinkedBlockingQueue<Integer> answers;
		private final CLIHelper helper;

		public Asker(final CLIHelper helper, final LinkedBlockingQueue<Integer> answers) {
			this.helper = helper;
			this.answers = answers;
		}

		public void askMenu(String[] options) {
			spawn(new Runnable() {
				public void run() {
					System.out.println("asking...");
					int choice = helper.askMenu(new String[] { "an option" });
					System.err.println("ANSWER: " + choice);
					try {
						answers.put(new Integer(choice));
					} catch (InterruptedException e) {
						throw new RuntimeException(e);
					}
				}
			});
		}

		private void spawn(Runnable r) {
			Thread t = new Thread(r);
			t.start();
		}
	}

}
