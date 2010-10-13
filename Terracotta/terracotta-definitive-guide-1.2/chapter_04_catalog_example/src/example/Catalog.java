package example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Catalog {

	private final Map<String, Product> catalog;

	public Catalog() {
		this.catalog = new HashMap<String, Product>();
	}

	public Product getProductBySKU(String sku) {
		synchronized (this.catalog) {
			Product product = this.catalog.get(sku);
			if (product == null) {
				product = new NullProduct();
			}
			return product;
		}
	}

	public Iterator<Product> getProducts() {
		synchronized (this.catalog) {
			return new ArrayList<Product>(this.catalog.values()).iterator();
		}
	}

	public int getProductCount() {
		synchronized (this.catalog) {
			return this.catalog.size();
		}
	}

	public void putProduct(Product product) {
		synchronized (this.catalog) {
			this.catalog.put(product.getSKU(), product);
		}
	}

}
