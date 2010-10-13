package org.terracotta.book.inventory.display;

import org.terracotta.book.inventory.cli.IO;
import org.terracotta.book.inventory.entity.Inventory;


public class InventoryDisplay extends ProductsDisplay {
	
	public InventoryDisplay(final IO io, final ProductDisplay productDisplay) {
		super(io, productDisplay);
	}
	
	public void displayInventory(final Inventory inventory) {
		io.println("Inventory");
	}
}
