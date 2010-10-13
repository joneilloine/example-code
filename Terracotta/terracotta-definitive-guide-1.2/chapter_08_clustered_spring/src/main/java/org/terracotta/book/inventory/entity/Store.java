package org.terracotta.book.inventory.entity;

import org.terracotta.book.inventory.display.DepartmentDisplay;
import org.terracotta.book.inventory.display.InventoryDisplay;

public class Store {

	private final Departments departments;
	private final Inventory inventory;

	public Store(final Departments departments, final Inventory inventory) {
		this.departments = departments;
		this.inventory = inventory;
	}

	public int getDepartmentCount() {
		return this.departments.getDepartmentCount();
	}

	public Department newDepartment(final String name) {
		return departments.newDepartment(name);
	}

	public void displayInventory(InventoryDisplay inventoryDisplay) {
		inventory.display(inventoryDisplay);
	}

	public void displayDepartments(DepartmentDisplay departmentDisplay) {
		departments.displayDepartments(departmentDisplay);
	}

	public Product getProductBySKU(String sku) {
		return inventory.getProductBySKU(sku);
	}
}
