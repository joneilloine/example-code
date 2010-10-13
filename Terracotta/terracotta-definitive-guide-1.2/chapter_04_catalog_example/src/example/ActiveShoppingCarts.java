package example;
import java.util.LinkedList;
import java.util.List;

public class ActiveShoppingCarts {

	private final List<ShoppingCart> activeShoppingCarts = new LinkedList<ShoppingCart>();
	
	public void addShoppingCart(ShoppingCart cart) {
		synchronized (activeShoppingCarts) {
			this.activeShoppingCarts.add(cart);
		}
	}
	
	public List<ShoppingCart> getActiveShoppingCarts() {
		synchronized (this.activeShoppingCarts) {
			List<ShoppingCart> carts = new LinkedList<ShoppingCart>(this.activeShoppingCarts);
			return carts;
		}
	}
}
