package example;
public class ShoppingCartFactory {
	
	private final ActiveShoppingCarts activeShoppingCarts;
	
	public ShoppingCartFactory(final ActiveShoppingCarts activeCarts) {
		this.activeShoppingCarts = activeCarts;
	}
	
	public ShoppingCart newShoppingCart() {
		ShoppingCart shoppingCart = new ShoppingCartImpl();
		activeShoppingCarts.addShoppingCart(shoppingCart);
		return shoppingCart;
	}
	
}
