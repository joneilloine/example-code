package example;

import java.util.*;

public class Cart {
    private Collection<Item> items = new TreeSet<Item>();

    public Collection<Item> getItems() {
        return Collections.unmodifiableCollection(items);
    }

    public void addItem(Item item) {
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item i = it.next();
            if (i.equals(item)) {
                if (item.getAmount() <= 0) {
                    it.remove();
                } else {
                    i.setAmount(item.getAmount());
                }
                return;
            }
        }
        if (item.getAmount() > 0) {
            items.add(item);
        }
    }

    public void removeItem(String name) {
        addItem(new Item(name, 0));
    }
}

