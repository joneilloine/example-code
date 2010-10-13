package org.terracotta.book.caching.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class UserEhcacheExample {

  private CacheManager manager = new CacheManager();

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
    Cache userCache = manager.getCache("userCache");
    Element element = userCache.get(id);
    if (element != null) {
      return (String) element.getObjectValue();
    } else {
      return null;
    }
  }

  private void cachePut(Integer id, String user) {
    Cache userCache = manager.getCache("userCache");
    userCache.put(new Element(id, fetch(id)));
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
    UserEhcacheExample userCache = new UserEhcacheExample();
    for (int i = 0; i < 10; i++) {
      helpFind(i % 5, userCache);
    }
  }

  public static void helpFind(int id, UserEhcacheExample userCache) {
    System.out.print("Find " + id);
    userCache.findUser(id);
  }
}
