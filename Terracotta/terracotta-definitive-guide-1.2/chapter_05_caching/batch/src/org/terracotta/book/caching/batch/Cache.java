package org.terracotta.book.caching.batch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cache<K, V> {

  private final Map<K, V> data = new HashMap<K, V>();
  
  // ... other methods like get and put
  
  public void putAllBatched(Map<K, V> entries, int batchSize) {
    List<Map.Entry<K, V>> entryList = new ArrayList<Map.Entry<K, V>>(
                entries.entrySet());
    int itemCount = entries.size();
    int batchStart = 0; // first index of current batch
    while (batchStart < itemCount) {
      int batchEnd = batchStart + batchSize;
      if (batchEnd > itemCount) {
         batchEnd = itemCount;
      }

      synchronized (this) {
        for (int i = batchStart; i < batchEnd; i++) {
          Map.Entry<K, V> entry = entryList.get(i);
          data.put(entry.getKey(), entry.getValue());
        }
      }

      batchStart = batchEnd;
    }
  }
}
