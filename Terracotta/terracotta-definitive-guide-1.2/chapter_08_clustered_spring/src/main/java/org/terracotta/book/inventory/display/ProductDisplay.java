package org.terracotta.book.inventory.display;

import java.text.NumberFormat;

import org.terracotta.book.inventory.cli.IO;
import org.terracotta.book.inventory.entity.Product;
import org.terracotta.book.inventory.util.CurrencyFormatFactory;
import org.terracotta.book.inventory.util.StringUtil;

public class ProductDisplay {

	private final StringUtil util;
	private final NumberFormat currencyFormat;
	private final int skuWidth;
	private final int nameWidth;
	private final int priceWidth;
	private final int inventoryLevelWidth;
	private final IO io;
	private final String header;

	public ProductDisplay(final IO io, final StringUtil util, final CurrencyFormatFactory currencyFormatFactory,
			final int skuWidth, final int nameWidth, final int priceWidth, final int inventoryLevelWidth) {
		this.io = io;
		this.util = util;
		this.currencyFormat = currencyFormatFactory.getCurrencyFormat();
		this.skuWidth = skuWidth;
		this.nameWidth = nameWidth;
		this.priceWidth = priceWidth;
		this.inventoryLevelWidth = inventoryLevelWidth;
		this.header = util.pad(skuWidth, "SKU") + " | " + util.pad(nameWidth, "Product Name") + " | "
			+ util.padLeft(priceWidth, "Price") + " | " + util.padLeft(inventoryLevelWidth, "Inventory");
	}

	public void display(Product product) {
		io.println(util.pad(skuWidth, product.getSku()) + " | " + util.pad(nameWidth, product.getName()) + " | "
				+ util.padLeft(priceWidth, currencyFormat.format(product.getPrice())) + " | "
				+ util.padLeft(inventoryLevelWidth, ("" + product.getInventoryLevel())));
	}

	public void displayHeader() {
		io.println(header);
		displaySeparator();
	}
	
	public void displaySeparator() {
		for (int i = 0; i < header.length(); i++) {
			io.print("-");
		}
		io.println("");
		io.flush();
	}
}
