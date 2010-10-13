package example;

public class NullProduct implements Product {

	public void decrementInventory(int count) {
		return;
	}

	public String getName() {
		return "NULL";
	}

	public String getSKU() {
		return "NULL";
	}

	public void increasePrice(double hike) {
		return;
	}

}
