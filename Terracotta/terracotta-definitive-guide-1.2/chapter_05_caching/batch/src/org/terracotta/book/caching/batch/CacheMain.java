package org.terracotta.book.caching.batch;

import java.util.HashMap;
import java.util.Map;

public class CacheMain {
  public static void main(String arg[]) {
    int itemCount = Integer.parseInt(arg[0]);   // to bulk load
    int reps = Integer.parseInt(arg[1]);        // repetitions in test
        
    for(int batchSize=1; batchSize<=itemCount; batchSize=batchSize*10) {
      long bestElapsed = Long.MAX_VALUE;
      CacheMain main = new CacheMain(itemCount, batchSize);     
      for(int rep=0; rep<reps; rep++) {
        long start = System.currentTimeMillis();
        main.primeCache();
        long elapsed = System.currentTimeMillis() - start;
        if(elapsed < bestElapsed) { 
          bestElapsed = elapsed;
        }
      }
            
      System.out.println("Loaded " + itemCount + 
                                " items with batch size = " + batchSize + 
                                " best time = " + bestElapsed + " ms");
    }
  }
    
  private final int batchSize; 
  private final Map<String,String> data;    
  private final Cache<String,String> cache = new Cache<String,String>();  // Root
    
  public CacheMain(int itemCount, int batchSize) {
    this.batchSize = batchSize;
    data = generateData(itemCount);
  }

  private Map<String,String> generateData(int count) {
    Map<String,String> data = new HashMap<String,String>();
    for(int i=0; i<count; i++) {
      String key = "key" + i;
      String value = "value" + i;
      data.put(key, value);
    }
    return data;
  }

  public void primeCache() { 
    cache.putAllBatched(data, batchSize);
  }
}
