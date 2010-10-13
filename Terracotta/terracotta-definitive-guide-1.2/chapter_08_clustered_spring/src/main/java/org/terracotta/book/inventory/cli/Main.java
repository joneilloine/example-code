package org.terracotta.book.inventory.cli;

import org.terracotta.book.inventory.display.DepartmentDisplay;
import org.terracotta.book.inventory.display.InventoryDisplay;
import org.terracotta.book.inventory.display.ProductDisplay;
import org.terracotta.book.inventory.entity.DepartmentFactory;
import org.terracotta.book.inventory.entity.Departments;
import org.terracotta.book.inventory.entity.Inventory;
import org.terracotta.book.inventory.entity.Store;
import org.terracotta.book.inventory.util.CurrencyFormatFactory;
import org.terracotta.book.inventory.util.DepartmentByNameComparator;
import org.terracotta.book.inventory.util.ProductByNameComparator;
import org.terracotta.book.inventory.util.StringUtil;

public class Main {

  public static void main(String[] args) {
    IO io = new IOImpl();
    StringUtil util = new StringUtil();
    CurrencyFormatFactory currencyFormatFactory = new CurrencyFormatFactory();
    int skuWidth = 8;
    int nameWidth = 30;
    int priceWidth = 10;
    int inventoryLevelWidth = 10;
    ProductDisplay productDisplay = new ProductDisplay(io, util, currencyFormatFactory, skuWidth, nameWidth,
        priceWidth, inventoryLevelWidth);
    DepartmentDisplay departmentDisplay = new DepartmentDisplay(io, productDisplay);
    InventoryDisplay inventoryDisplay = new InventoryDisplay(io, productDisplay);
    ProductByNameComparator productByNameComparator = new ProductByNameComparator();
    Inventory inventory = new Inventory(productByNameComparator);
    Departments departments = new Departments(new DepartmentByNameComparator(), new DepartmentFactory(
        productByNameComparator, inventory));
    Store store = new Store(departments, inventory);
    new CLI(new CLIHelper(io), departmentDisplay, productDisplay, inventoryDisplay, store).run();
  }

}
