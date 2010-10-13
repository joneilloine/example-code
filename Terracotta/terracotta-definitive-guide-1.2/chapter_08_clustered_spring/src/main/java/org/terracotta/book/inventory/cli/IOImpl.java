package org.terracotta.book.inventory.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class IOImpl implements IO {
	
	private final BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
	private final PrintWriter out = new PrintWriter(System.out);
	
	public BufferedReader getReader() {
			return in;
	}
	
	public PrintWriter getWriter() {
		return out;
	}

	public void flush() {
		out.flush();
	}

	public void print(Object o) {
		out.print(o);
	}

	public void println(Object o) {
		out.println(o);
		flush();
	}

	public String readLine() {
		try {
			return in.readLine();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
