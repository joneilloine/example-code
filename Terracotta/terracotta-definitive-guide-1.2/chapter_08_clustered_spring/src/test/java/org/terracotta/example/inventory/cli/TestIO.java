package org.terracotta.example.inventory.cli;

import java.util.concurrent.LinkedBlockingQueue;

import org.terracotta.book.inventory.cli.IO;

public class TestIO implements IO  {

	public final LinkedBlockingQueue<String> outputQueue = new LinkedBlockingQueue<String>();
	public final LinkedBlockingQueue<String> inputQueue = new LinkedBlockingQueue<String>();
	
	private final StringBuffer buf = new StringBuffer();
	
	public void print(Object o) {
		buf.append(o);
	}

	public void println(Object o) {
		buf.append(o + "\n");
		flush();
	}

	public String readLine() {
		try {
			String rv = inputQueue.take();
			System.err.println("INPUT: " + rv);
			return rv;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public void flush() {
		outputQueue.add(buf.toString());
		buf.replace(0, buf.length(), "");
	}
}
