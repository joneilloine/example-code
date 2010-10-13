package example;

import java.util.Iterator;
import java.util.concurrent.CyclicBarrier;

public class Main implements Runnable {

	private final CyclicBarrier barrier;
	private final int participants;
	private int arrival = -1;
	private Catalog catalog;
	private ShoppingCartFactory shoppingCartFactory;
	private ActiveShoppingCarts activeCarts;

	public Main(final int participants, final CyclicBarrier barrier, final Catalog catalog,
			final ActiveShoppingCarts activeCarts, final ShoppingCartFactory shoppingCartFactory) {
		this.barrier = barrier;
		this.participants = participants;
		this.catalog = catalog;
		this.activeCarts = activeCarts;
		this.shoppingCartFactory = shoppingCartFactory;
	}

	public void run() {
		try {
			display("Step 1: Waiting for everyone to arrive.  I'm expecting " + (participants - 1) + " other thread(s)...");
			this.arrival = barrier.await();
			display("We're all here!");

			String skuToPurchase;
			display();
			display("Step 2: Set Up");
			boolean firstThread = arrival == (participants - 1);

			if (firstThread) {
				display("I'm the first thread, so I'm going to populate the catalog...");
				Product razor = new ProductImpl("123", "14 blade super razor", 12);
				catalog.putProduct(razor);

				Product shavingCream = new ProductImpl("456", "Super-smooth shaving cream", 5);
				catalog.putProduct(shavingCream);

				skuToPurchase = "123";
			} else {
				skuToPurchase = "456";
			}

			// wait for all threads.
			barrier.await();

			display();
			display("Step 3: Let's do a little shopping...");
			ShoppingCart cart = shoppingCartFactory.newShoppingCart();

			Product product = catalog.getProductBySKU(skuToPurchase);
			display("I'm adding \"" + product + "\" to my cart...");
			cart.addProduct(product);
			barrier.await();

			display();
			display("Step 4: Let's look at all shopping carts in all JVMs...");
			displayShoppingCarts();

			display();
			if (firstThread) {
				display("Step 5: Let's make a 10% price increase...");
				for (Iterator<Product> i = catalog.getProducts(); i.hasNext();) {
					Product p = i.next();
					p.increasePrice(0.1d);
				}
			} else {
				display("Step 5: Let's wait for the other JVM to make a price change...");
			}
			barrier.await();

			display();
			display("Step 6: Let's look at the shopping carts with the new prices...");
			displayShoppingCarts();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

  private void displayShoppingCarts() {
		StringBuffer buf = new StringBuffer();
		for (Iterator<ShoppingCart> i = activeCarts.getActiveShoppingCarts().iterator(); i.hasNext();) {
			ShoppingCart thisCart = i.next();
			buf.append("==========================\n");
			buf.append("Shopping Cart\n");
			buf.append(thisCart);
		}
		display(buf);
	}

	private void display() {
		display("");
	}

	private void display(Object o) {
		System.err.println(o);
	}

	public static void main(String[] args) throws Exception {
		int participants = 2;
		if (args.length > 0) {
			participants = Integer.parseInt(args[0]);
		}

		Roots roots = new Roots(new CyclicBarrier(participants), new Catalog(), new ActiveShoppingCarts());

		if (args.length > 1 && "run-locally".equals(args[1])) {
			// Run 'participants' number of local threads. This is the non-clustered
			// case.
			for (int i = 0; i < participants; i++) {
				new Thread(new Main(participants, roots.getBarrier(), roots.getCatalog(), roots.getActiveShoppingCarts(),
						new ShoppingCartFactory(roots.getActiveShoppingCarts()))).start();
			}
		} else {
			// Run a single local thread. This is the clustered case. It is assumed
			// that main() will be called participants - 1 times in other JVMs
			new Main(participants, roots.getBarrier(), roots.getCatalog(), roots.getActiveShoppingCarts(),
					new ShoppingCartFactory(roots.getActiveShoppingCarts())).run();
		}

	}

}
