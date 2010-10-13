package org.terracotta.workmanager.spider;

public class PageNotFoundException extends RuntimeException {

	private static final long	serialVersionUID	= -3678135197004403049L;

	public PageNotFoundException(String msg) {
		super(msg);
	}
}
