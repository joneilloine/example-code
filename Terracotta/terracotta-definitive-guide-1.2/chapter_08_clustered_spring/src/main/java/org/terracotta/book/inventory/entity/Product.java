package org.terracotta.book.inventory.entity;

public class Product {
  private final String sku;
  private final String name;
  private double price;
  private int inventoryLevel;

  public Product(final String sku, final String name, final double price, final int inventoryLevel) {
    this.sku = sku;
    this.name = name;
    this.price = price;
    this.inventoryLevel = inventoryLevel;
  }

  public String getSku() {
    return sku;
  }

  public String getName() {
    return name;
  }

  public synchronized double getPrice() {
    return price;
  }

  public synchronized int getInventoryLevel() {
    return inventoryLevel;
  }

  public int addInventory(int count) {
    return modInventory(count);
  }

  public int subtractInventory(int count) {
    return modInventory(count * -1);
  }

  private synchronized int modInventory(int count) {
    inventoryLevel += count;
    return inventoryLevel;
  }

  public synchronized void setPrice(double newPrice) {
    price = newPrice;
  }

}
