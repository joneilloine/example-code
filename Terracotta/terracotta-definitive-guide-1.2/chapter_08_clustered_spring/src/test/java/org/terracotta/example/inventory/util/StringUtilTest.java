package org.terracotta.example.inventory.util;

import org.terracotta.book.inventory.util.StringUtil;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
	public void testBasics() {
		StringUtil util = new StringUtil();
		assertEquals("   ", util.pad(3, null));

		String original = "original";
		assertEquals("ori", util.pad(3, original));
		
		assertEquals(original, util.pad(original.length(), original));
		assertEquals(original + "   ", util.pad(original.length() + 3, original));
		
		assertEquals("", util.pad(0, original));
		assertEquals("", util.pad(-1, original));
	}
}
