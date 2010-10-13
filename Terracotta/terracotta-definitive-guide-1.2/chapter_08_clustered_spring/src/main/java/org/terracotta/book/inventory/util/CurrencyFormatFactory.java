package org.terracotta.book.inventory.util;

import java.text.NumberFormat;

/**
 * Encapsulates the static accessor nastiness of NumberFormat.
 * 
 * @author orion
 *
 */
public class CurrencyFormatFactory {

	private final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
	
	public NumberFormat getCurrencyFormat() {
		return currencyFormat;
	}
}
