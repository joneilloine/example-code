package org.terracotta.book.inventory.entity;

import org.terracotta.book.inventory.util.ProductByNameComparator;

public class DepartmentFactory {
  
  private final ProductByNameComparator productByNameComparator;
  private final Inventory inventory;

  public DepartmentFactory(final ProductByNameComparator productByNameComparator, Inventory inventory) {
    this.productByNameComparator = productByNameComparator;
    this.inventory = inventory;
  }
  
  public Department newDepartment(String name) {
    return new Department(productByNameComparator, inventory, name);
  }
}
