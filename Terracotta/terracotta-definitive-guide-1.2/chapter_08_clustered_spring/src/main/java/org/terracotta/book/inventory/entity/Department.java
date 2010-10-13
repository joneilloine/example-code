package org.terracotta.book.inventory.entity;

import java.util.Set;
import java.util.TreeSet;

import org.terracotta.book.inventory.display.DepartmentDisplay;
import org.terracotta.book.inventory.util.ProductByNameComparator;

public class Department {

  private final String name;
  private final Set<Product> productsByName;
  private final Inventory inventory;

  public Department(final ProductByNameComparator comparator, final Inventory inventory, final String name) {
    this.inventory = inventory;
    this.name = name;
    productsByName = new TreeSet<Product>(comparator);
  }

  public String getName() {
    return this.name;
  }

  public void addProduct(Product product) {
    synchronized (productsByName) {
      productsByName.add(product);
    }
    inventory.addProduct(product);
  }

  public void display(final DepartmentDisplay departmentDisplay) {
    departmentDisplay.displayDepartment(this);
    synchronized (productsByName) {
      departmentDisplay.displayProducts(productsByName);
    }
  }
}
