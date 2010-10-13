/**
 * 
 */
package org.terracotta.book.inventory.util;

import java.util.Comparator;

import org.terracotta.book.inventory.entity.Product;


public final class ProductByNameComparator implements Comparator<Product> {
	public int compare(Product o1, Product o2) {
		if (o1 == null || o2 == null) return 0;
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}