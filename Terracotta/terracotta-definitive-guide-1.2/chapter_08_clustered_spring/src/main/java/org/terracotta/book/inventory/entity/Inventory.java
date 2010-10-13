package org.terracotta.book.inventory.entity;

import java.util.Set;
import java.util.TreeSet;

import org.terracotta.book.inventory.display.ProductsDisplay;
import org.terracotta.book.inventory.util.ProductByNameComparator;


public class Inventory {

	private final Set<Product> products;

	public Inventory(final ProductByNameComparator comparator) {
		this.products = new TreeSet<Product>(comparator);
	}

	public void addProduct(final Product product) {
		synchronized (products) {
			this.products.add(product);
		}
	}

	public void display(final ProductsDisplay inventoryDisplay) {
		inventoryDisplay.displayInventory(this);
		synchronized (products) {
			inventoryDisplay.displayProducts(products);
		}
	}

	public Product getProductBySKU(String sku) {
		// XXX: Obviously, this isn't an efficient way to find the products by sku.
		synchronized (products) {
			for (Product product : products) {
				if (sku.equals(product.getSku())) {
					return product;
				}
			}
		}
		// didn't find the product
		return null;
	}
}
