package example;

public class Item implements Comparable {
    private String name;
    private int amount;

    public Item(String name, int amount) {
        setName(name);
        setAmount(amount);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (null == name) throw new IllegalArgumentException();
        this.name = name;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    @Override
    public boolean equals(Object o) {
        return name.equalsIgnoreCase(((Item)o).name);
    }
    
    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    public int compareTo(Object o) {
        return name.compareToIgnoreCase(((Item)o).name);
    }
}

