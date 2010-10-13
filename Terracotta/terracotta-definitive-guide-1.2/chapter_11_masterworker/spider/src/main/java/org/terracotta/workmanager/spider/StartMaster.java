/*
 * Copyright (c) 2005-2006 Terracotta, Inc. All rights reserved.
 * 
 * Source code governed by BSD License; see included LICENSE file. 
 */
package org.terracotta.workmanager.spider;

import java.util.StringTokenizer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;

public class StartMaster {

	private static final String	SPIDER_MASTER_NAME					= "master";
	private static final String	ROUTING_IDS_LIST_SEPARATOR	= ":";

	public static void main(String[] args) throws Exception {

		Options options = new Options();
		options.addOption("u", "url", true, "the start URL");
		options.addOption("d", "depth", true, "the parse depth");
		options.addOption("l", "external-links", false, "follow external links (optional, defaults to false)");
		options.addOption("i", "routing-ids", true, "list with the routing IDs for the work queues, separated with '"
				+ ROUTING_IDS_LIST_SEPARATOR + "'");
		options.addOption("f", "fail-over-routing-id", true, "the routing ID for the fail-over queue (optional)");

		CommandLineParser parser = new PosixParser();
		CommandLine cmd = parser.parse(options, args);

		HelpFormatter formatter = new HelpFormatter();

		String startURL;
		int maxDepth;
		boolean followExternalLinks = false; // Boolean.parseBoolean(args[2]);
		String[] routingIDs = null;
		String failOverRoutingID = null;
		if (cmd.hasOption("u")) {
			startURL = cmd.getOptionValue("u");
		} else {
			formatter.printHelp(SPIDER_MASTER_NAME, options);
			return;
		}
		if (cmd.hasOption("d")) {
			try {
				maxDepth = Integer.parseInt(cmd.getOptionValue("d"));
			} catch (NumberFormatException nfe) {
				formatter.printHelp(SPIDER_MASTER_NAME, options);
				return;
			}
		} else {
			formatter.printHelp(SPIDER_MASTER_NAME, options);
			return;
		}
		if (cmd.hasOption("i")) {
			String tokens = cmd.getOptionValue("i");
			StringTokenizer tokenizer = new StringTokenizer(tokens, ROUTING_IDS_LIST_SEPARATOR);
			routingIDs = new String[tokenizer.countTokens()];
			for (int i = 0; i < routingIDs.length; i++) {
				routingIDs[i] = ((String) tokenizer.nextElement()).trim();
			}
		} else {
			formatter.printHelp(SPIDER_MASTER_NAME, options);
			return;
		}
		if (cmd.hasOption("f")) {
			failOverRoutingID = cmd.getOptionValue("f");
		} // else {
		// formatter.printHelp(SPIDER_MASTER_NAME, options);
		// return;
		// }

		if (cmd.hasOption("l")) {
			followExternalLinks = Boolean.parseBoolean(cmd.getOptionValue("l"));
		}

		StringBuilder builder = new StringBuilder();
		builder.append("Starting Spider: ");
		builder.append("\n  start url \t\t\t= ");
		builder.append(startURL);
		builder.append("\n  max parse depth \t\t= ");
		builder.append(maxDepth);
		builder.append("\n  follow external links \t= ");
		builder.append(followExternalLinks);
		builder.append("\n  list with routing IDs \t= [ ");
		for (int i = 0; i < routingIDs.length; i++) {
			builder.append(routingIDs[i]);
			builder.append(' ');
		}
		builder.append("]");
		if (failOverRoutingID != null) {
			builder.append("\n  routing ID for fail-over node = ");
			builder.append(failOverRoutingID);
		}
		System.out.println(builder.toString());

		new SpiderMaster(startURL, maxDepth, followExternalLinks, routingIDs, failOverRoutingID).run();
	}
}