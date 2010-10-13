/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.spider;

import au.id.jericho.lib.html.Source;

/**
 * Defines the interface to an object which holds page state.
 * <p>
 * Implementations are free to store pages (or not) however they see fit.
 */
public interface PageCache {
	/**
	 * Store the page for future reference.
	 * 
	 * @param url
	 * 
	 * @param page
	 *          the page
	 * @return if the page already exists, the existing page, otherwise the page
	 *         should be stored and returned (atomic set/get)
	 */
	public Source setPage(String url, Source page);

	/**
	 * Return the page for a given (fully qualified) url.
	 * 
	 * @param url
	 *          the fully qualified url
	 * @return the cached page, null if it does not exist
	 */
	public Source getPage(String url);

	/**
	 * Return true if the given page is cached.
	 * 
	 * @param url
	 *          the fully qualified url
	 * @return true if the page exists, otherwise false
	 */
	public boolean hasPage(String url);
}