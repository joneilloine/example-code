package example;

import java.util.concurrent.CyclicBarrier;

public class Roots {
	private final CyclicBarrier barrier;
	private final Catalog catalog;
	private final ActiveShoppingCarts activeShoppingCarts;

	public Roots(CyclicBarrier barrier, Catalog catalog, ActiveShoppingCarts activeShoppingCarts) {
		this.barrier = barrier;
		this.catalog = catalog;
		this.activeShoppingCarts = activeShoppingCarts;
	}

	public ActiveShoppingCarts getActiveShoppingCarts() {
		return activeShoppingCarts;
	}

	public CyclicBarrier getBarrier() {
		return barrier;
	}

	public Catalog getCatalog() {
		return catalog;
	}
}
