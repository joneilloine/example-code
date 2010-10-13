package example;

import java.text.NumberFormat;

public class ProductImpl implements Product {
	private String name;
	private String sku;
	private double price;

	public ProductImpl(String sku, String name, double price) {
		this.sku = sku;
		this.name = name;
		this.price = price;
	}
	
	public String getName() {
		return this.name;
	}

	public String getSKU() {
		return this.sku;
	}
	
	public String toString() {
		NumberFormat fmt = NumberFormat.getCurrencyInstance();
		return "Price: " + fmt.format(price) + "; Name: " + this.name;
	}

	public synchronized void increasePrice(double rate) {
		this.price += this.price * rate;
	}
}
