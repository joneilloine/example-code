package org.terracotta.book.inventory.cli;

public interface IO {
	
	public void print(Object o);
	public void println(Object o);
	public void flush();
	
	public String readLine();

}