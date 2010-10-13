package org.terracotta.example.inventory.display;

import java.io.PrintWriter;

import junit.framework.TestCase;

import org.terracotta.book.inventory.cli.IOImpl;
import org.terracotta.book.inventory.display.ProductDisplay;
import org.terracotta.book.inventory.entity.Product;
import org.terracotta.book.inventory.util.CurrencyFormatFactory;
import org.terracotta.book.inventory.util.StringUtil;

public class ProductDisplayTest extends TestCase {
	public void testBasics() {
		PrintWriter out = new PrintWriter(System.out);
		String sku = "123456";
		String name = "My Product Name";
		double price = 10d;
		int inventoryLevel = 100;
		Product product = new Product(sku, name, price, inventoryLevel);
		int skuWidth = 8;
		int nameWidth = 30;
		int priceWidth = 10;
		int inventoryLevelWidth = 10;
		ProductDisplay display = new ProductDisplay(new IOImpl(), new StringUtil(), new CurrencyFormatFactory(), skuWidth,
				nameWidth, priceWidth, inventoryLevelWidth);
		display.displayHeader();
		display.display(product);
		out.flush();
	}
}
