package org.terracotta.workmanager.spider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import au.id.jericho.lib.html.Source;

public class SimplePageCache implements PageCache {

	private final Map<String, Source>	cache	= new ConcurrentHashMap<String, Source>();

	public Source getPage(String url) {
		return cache.get(url);
	}

	public boolean hasPage(String url) {
		return cache.containsKey(url);
	}

	public Source setPage(String url, Source page) {
		cache.put(url, page);
		return page;
	}

}