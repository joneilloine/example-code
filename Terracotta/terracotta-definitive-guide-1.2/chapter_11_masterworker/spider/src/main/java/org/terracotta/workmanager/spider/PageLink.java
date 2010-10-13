/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.spider;

/**
 * Immutable class holding a page depth and url. The return type from the worker
 * nodes. Stores a page depth and the reference url (a String that should be
 * fully qualified).
 */
public class PageLink {
	private final int			depth;

	private final String	url;

	/**
	 * Creates a new <code>PageLink</code> with the specified page depth and
	 * url.
	 * 
	 * @param depth
	 * @param url
	 */
	public PageLink(int depth, String url) {
		super();
		this.depth = depth;
		this.url = url;
	}

	/**
	 * Gets the page depth.
	 * 
	 * @return the depth
	 */
	public final int getDepth() {
		return depth;
	}

	/**
	 * Gets the url.
	 * 
	 * @return the url
	 */
	public final String getUrl() {
		return url;
	}
}