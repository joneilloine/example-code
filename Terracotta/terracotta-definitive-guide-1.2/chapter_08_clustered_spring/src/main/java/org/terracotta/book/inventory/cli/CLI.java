package org.terracotta.book.inventory.cli;

import org.terracotta.book.inventory.display.DepartmentDisplay;
import org.terracotta.book.inventory.display.InventoryDisplay;
import org.terracotta.book.inventory.display.ProductDisplay;
import org.terracotta.book.inventory.entity.Department;
import org.terracotta.book.inventory.entity.Product;
import org.terracotta.book.inventory.entity.Store;

public class CLI implements Runnable {
  private final Store store;
  private final String[] initMenu = { "Use default items", "Enter items by hand" };
  private final String[] viewMenu = { "Exit", "View departments", "View inventory", "Modify product" };
  private final String[] modifyProductMenu = { "Escape", "Change price", "Change inventory" };

  private final DepartmentDisplay departmentDisplay;
  private final InventoryDisplay inventoryDisplay;
  private final CLIHelper helper;
  private final ProductDisplay productDisplay;

  public CLI(final CLIHelper helper, final DepartmentDisplay departmentDisplay, final ProductDisplay productDisplay,
      final InventoryDisplay inventoryDisplay, final Store store) {
    this.helper = helper;
    this.departmentDisplay = departmentDisplay;
    this.productDisplay = productDisplay;
    this.inventoryDisplay = inventoryDisplay;
    this.store = store;
  }

  public void run() {
    helper.println("Welcome to the Terracotta inventory demo.");
    if (store.getDepartmentCount() == 0) {
      helper.println("The store is currently empty.  Let's add some departments and products...");
      int choice = helper.askMenu(initMenu);
      switch (choice) {
      case (0):
        helper.println("Adding default products");
        Department dept = store.newDepartment("Shoes");
        dept.addProduct(new Product("SAND", "Sandals", 15.96, 29));
        dept.addProduct(new Product("LOAF", "Loafers", 59.99, 45));

        dept = store.newDepartment("Sports");
        dept.addProduct(new Product("BBAL", "Basketball", 12.95, 30));
        dept.addProduct(new Product("SK8Z", "Ice Skates", 61.99, 5));
        break;
      case (1):
        addDepartments();
        break;
      }
    }
    while (true) {
      // brain-dead application loop
      helper.println("");
      helper.printSeparator();
      helper.println("Main Menu");
      helper.printSeparator();
      int choice = helper.askMenu(viewMenu);
      switch (choice) {
      case 0:
        helper.println("Goodbye.");
        return;
      case 1:
        displayDepartments();
        break;
      case 2:
        displayInventory();
        break;
      case 3:
        modifyProduct();
        break;
      }
    }
  }

  private void displayInventory() {
    helper.printSeparator();
    helper.println("Inventory Display");
    helper.printSeparator();
    store.displayInventory(inventoryDisplay);
  }

  private void displayDepartments() {
    helper.printSeparator();
    helper.println("Department Display");
    helper.printSeparator();
    store.displayDepartments(departmentDisplay);
  }

  private void addDepartments() {
    String addMore = "y";
    while (!"n".equals(addMore)) {
      addDepartment();
      addMore = helper.ask("Add more departments? [y|n]");
    }
  }

  private void addDepartment() {
    helper.println("Add department.");
    String name = helper.ask("Enter department name: ");
    Department department = store.newDepartment(name);
    String addMore = "y";
    while (!"n".equals(addMore)) {
      addProduct(department);
      addMore = helper.ask("Add more products in this department? [y|n]");
    }
  }

  private void addProduct(Department department) {
    helper.printSeparator();
    helper.println("Add product.");
    String sku = helper.ask("Enter product SKU: ");
    String name = helper.ask("Enter product name: ");
    Double price = (Double) helper.ask("Enter product price: ", Double.class);
    Integer inventoryLevel = (Integer) helper.ask("Enter product inventory level: ", Integer.class);
    Product product = new Product(sku, name, price.doubleValue(), inventoryLevel.intValue());
    department.addProduct(product);
  }

  private void modifyProduct() {
    helper.printSeparator();
    helper.println("Modify Product");
    helper.printSeparator();
    Product product = null;
    while (product == null) {
      String sku = helper.ask("Enter sku:");
      product = store.getProductBySKU(sku);
      if (product == null) {
        helper.println("No product found by that sku.");
      }
    }
    productDisplay.display(product);
    int choice = helper.askMenu(modifyProductMenu);
    switch (choice) {
    case 0:
      return;
    case 1:
      double newPrice = (Double) helper.ask("Enter new price: ", Double.class);
      product.setPrice(newPrice);
      break;
    case 2:
      int delta = (Integer) helper.ask("Increment/decrement inventory by: ", Integer.class);
      product.addInventory(delta);
      break;
    }
    helper.printSeparator();
    helper.println("Modified Product");
    helper.printSeparator();
    productDisplay.display(product);
  }

}
