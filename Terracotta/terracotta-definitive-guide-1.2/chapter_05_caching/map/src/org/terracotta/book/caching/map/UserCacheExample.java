package org.terracotta.book.caching.map;

import java.util.HashMap;
import java.util.Map;

public class UserCacheExample {

  private Map<Integer, String> userCache = new HashMap<Integer, String>();

  public String findUser(Integer id) {
    String user = cacheGet(id);
    if (user == null) {
      user = fetch(id);
      cachePut(id, user);
      System.out.println("\t- MISS");
    } else {
      System.out.println("\t- HIT");
    }
    return user;
  }

  private String cacheGet(Integer id) {
    synchronized (userCache) {
      return userCache.get(id);
    }
  }

  private void cachePut(Integer id, String user) {
    synchronized (userCache) {
      userCache.put(id, user);
    }
  }

  private String fetch(Integer id) {
    switch (id.intValue()) {
      case 0: return "Ari Zilka";
      case 1: return "Alex Miller";
      case 2: return "Geert Bevin";
      case 3: return "Taylor Gautier";
      case 4: return "Jonas Boner";
      default:
        throw new RuntimeException("Unknown id: " + id.intValue());
    }
  }

  public static void main(String arg[]) throws InterruptedException {
    UserCacheExample userCache = new UserCacheExample();
    for (int i = 0; i < 10; i++) {
      helpFind(i % 5, userCache);
    }
  }

  public static void helpFind(int id, UserCacheExample userCache) {
    System.out.print("Find " + id);
    userCache.findUser(id);
  }
}
