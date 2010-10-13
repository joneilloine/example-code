package org.terracotta.book.coordination.queues;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class CoordinationWithQueues {

	private static final String[] MOODS = { "sleepy", "grouchy", "happy", "worried", "sad", "like partying" };
	private static final String[] COLOR_NAMES = { "Red", "Orange", "Yellow", "Green", "Blue", "Indigo", "Violet" };
	private static List<NamedQueue> QUEUES;
	private static final AtomicInteger sharedCounter = new AtomicInteger();

	private final String myColor;
	private final NamedQueue myQueue;
	private final Random random;

	public CoordinationWithQueues(final String color, final NamedQueue queue) {
		myColor = color;
		myQueue = queue;
		random = new Random();
	}

	private void run() {
		while (true) {
			try {
				String mood = MOODS[random.nextInt(MOODS.length)];
				myQueue.add(myColor + " says, \"I'm feeling " + mood + ".\"");
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// A real application would do something with this exception
				e.printStackTrace();
			}
		}
	}

	private static final class NamedQueue {
		private final String myName;
		private final LinkedBlockingQueue<Object> myQueue;

		public NamedQueue(final String name, final LinkedBlockingQueue<Object> queue) {
			this.myName = name;
			this.myQueue = queue;
		}

		public String getName() {
			return myName;
		}

		public void add(Object o) {
			myQueue.add(o);
		}

		public Object take() throws InterruptedException {
			return myQueue.take();
		}
	}

	private static final class QueueReader implements Runnable {
		private final NamedQueue myQueue;

		public QueueReader(NamedQueue queue) {
			this.myQueue = queue;
		}

		public void run() {
			while (true) {
				try {
					System.out.println(new Date() + ": message from the " + myQueue.getName() + " queue: " + myQueue.take());
				} catch (InterruptedException e) {
					// A real application would do something interesting with this
					// exception.
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(final String[] args) {
		final int counterValue = sharedCounter.getAndIncrement();
		if (counterValue == 0) {
			// I'm the first JVM to join the cluster, so I'll initialized the queues
			// and start the queue reader threads...
			QUEUES = new LinkedList<NamedQueue>();
			synchronized (QUEUES) {
				if (QUEUES.size() == 0) {
					for (int i = 0; i < COLOR_NAMES.length; i++) {
						QUEUES.add(new NamedQueue(COLOR_NAMES[i], new LinkedBlockingQueue<Object>()));
					}
				}

				for (Iterator<NamedQueue> i = QUEUES.iterator(); i.hasNext();) {
					new Thread(new QueueReader(i.next())).start();
				}
			}
		}
		if (counterValue > 0 ) {
		    // I'm not the queue reader JVM, so create and run the example... 
		    String color = (counterValue % COLOR_NAMES.length) + " " + (counterValue / COLOR_NAMES.length);
		    System.out.println("I'm a " + color + " JVM.");
		    NamedQueue queue;
		    synchronized (QUEUES) {
			queue = QUEUES.get(counterValue % COLOR_NAMES.length);
		    }
		    (new CoordinationWithQueues(color, queue)).run();
		}
	}

}
