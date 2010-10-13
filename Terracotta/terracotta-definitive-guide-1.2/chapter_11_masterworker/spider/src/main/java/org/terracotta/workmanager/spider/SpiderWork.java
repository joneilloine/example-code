/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.spider;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import commonj.work.Work;

import au.id.jericho.lib.html.Source;
import au.id.jericho.lib.html.StartTag;

/**
 * The implementation of the work to do on each worker node.
 * <p>
 * Each <code>SpiderPageWork</code> processes an input page and returns a list
 * of {@link PageLink} objects that contain the depth of the page (relative to
 * the visited graph, i.e. one more than the current page) and a url, one
 * <code>PageLink</code> per page reference.
 */
public class SpiderWork implements Work {

	private final String					m_url;
	private final int							m_depth;
	private final boolean					m_followExternalLinks;
	private final List<PageLink>	m_links	= new ArrayList<PageLink>();

	public SpiderWork(final String url, final int depth, final boolean followExternalLinks) {
		m_url = url;
		m_depth = depth;
		m_followExternalLinks = followExternalLinks;
	}

	public List<PageLink> getLinks() {
		return m_links;
	}

	public void run() {
		try {
			URL resource = new URL(m_url);
			Source source = retrievePage(resource);

			List<StartTag> tags = source.findAllStartTags();
			System.out.println(m_url + " has " + tags.size() + " tags to process.");

			for (StartTag tag : tags) {
				if (tag.getName().equals("a")) {
					addLink(m_links, resource, tag.getAttributeValue("href"));
					continue;
				}
				if (tag.getName().equals("frame")) {
					addLink(m_links, resource, tag.getAttributeValue("src"));
					continue;
				}
			}
		} catch (IOException e) {
			throw new PageNotFoundException("Skipping page; couldn't parse URL: " + m_url);
		}

		System.out.println("Returning " + m_links.size() + " links.");
	}

	/**
	 * helper method, try to use the page cache to retrieve the page, otherwise
	 * get it and parse it
	 */
	private static Source retrievePage(URL resource) throws IOException {
		Source source = null; // cache.getPage(resource.toString());
		if (source != null) {
			System.out.println("Processing " + resource);
		} else {
			System.out.println("Retrieving " + resource);
			// get the url into the parse structure
			source = new Source(resource);
			source.fullSequentialParse();

			source = SpiderMaster.cachePage(resource.toString(), source);
		}
		return source;
	}

	/**
	 * helper method to add a link from a page to the return list - null op if the
	 * link is null, 0 length, or begins with a "#" (anchor link within the same
	 * page)
	 * 
	 * @param links
	 *          the list of links to add the new link to
	 * @param m_depth
	 *          the current page depth
	 * @param url
	 *          the base url
	 * @param relativeLink
	 *          the relative link
	 */
	private synchronized void addLink(List<PageLink> links, URL url, String relativeLink) {
		// don't follow anchor links
		if (relativeLink == null || relativeLink.length() == 0 || relativeLink.contains("#")) {
			return;
		}

		// relative links will not have a ':' in them, otherwise, they are external
		if (relativeLink.indexOf(":") < 0) {
			try {
				relativeLink = new URL(url, relativeLink).toString();
			} catch (Exception e) {
				// one url failure should not stop the parsing process
				e.printStackTrace();
				return;
			}
		} else {
			// leave if we don't support external links
			if (!m_followExternalLinks) {
				return;
			}
		}
		links.add(new PageLink(m_depth + 1, relativeLink));
	}

	public boolean isDaemon() {
		// TODO Auto-generated method stub
		return false;
	}

	public void release() {
		// TODO Auto-generated method stub
	}
}