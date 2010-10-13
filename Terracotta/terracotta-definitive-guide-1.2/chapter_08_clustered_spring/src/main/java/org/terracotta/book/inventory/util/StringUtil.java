package org.terracotta.book.inventory.util;

public class StringUtil {
	
	public String pad(int maxCharacters, String value) {
		return padRight(maxCharacters, value);
	}
	
	public String padLeft(int maxCharacters, String value) {
		return pad(false, maxCharacters, value);
	}
	
	public String padRight(int maxCharacters, String value) {
		return pad(true, maxCharacters, value);
	}
	
	private String pad(boolean right, int maxCharacters, String value) {
		if (value == null) {
			value = "";
		}
		if (maxCharacters < 0) return "";
		if (value.length() < maxCharacters) {
			StringBuffer rv = new StringBuffer(value);
			while (rv.length() < maxCharacters) {
				if (right) {
					rv.append(" ");
				} else {
					rv.insert(0, " ");
				}
			}
			return rv.toString();
		} else {
			return value.substring(0, maxCharacters);
		}
	}
}
