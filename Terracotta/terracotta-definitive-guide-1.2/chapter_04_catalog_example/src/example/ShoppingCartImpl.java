package example;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;


public class ShoppingCartImpl implements ShoppingCart {

	private List<Product> products = new LinkedList<Product>();
	
	public void addProduct(final Product product) {
		synchronized (products) {
			this.products.add(product);
		}
	}

	public String toString() {
		final StringBuffer buf = new StringBuffer();
		synchronized (this.products) {
			int count = 1;
			for (Iterator<Product> i = this.products.iterator(); i.hasNext();) {
				buf.append("\titem " + count + ": " + i.next() + "\n");
			}
		}
		return buf.toString();
	}
	
}
