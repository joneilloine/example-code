package org.terracotta.book.inventory.display;

import java.util.Set;

import org.terracotta.book.inventory.cli.IO;
import org.terracotta.book.inventory.entity.Inventory;
import org.terracotta.book.inventory.entity.Product;


public class ProductsDisplay {

	protected final ProductDisplay productDisplay;
	protected final IO io;

	public ProductsDisplay(final IO io, final ProductDisplay productDisplay) {
		this.io = io;
		this.productDisplay = productDisplay;
	}

	public void displayInventory(Inventory inventory) {
		io.println("Inventory");
	}

	public void displayProducts(final Set<Product>products) {
		productDisplay.displayHeader();
		synchronized (products) {
			for (Product product : products) {
				productDisplay.display(product);
			}
		}
	}


}
