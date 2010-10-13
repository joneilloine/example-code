package org.terracotta.book.caching.tccache;

import org.jboss.cache.CacheException;
import org.jboss.cache.TreeCache;

public class UserTreeCacheExample {

  private TreeCache userCache;

  public UserTreeCacheExample() throws Exception {
	  userCache = new TreeCache();
	  userCache.setCacheMode(TreeCache.LOCAL);
	  userCache.startService();
  }
  
	public User findUser(UserID id) throws CacheException {
		User user = cacheGet(id);
		if(user == null) {
			user = fetch(id);
			cachePut(id, user);
			System.out.println("\t- MISS");
		} else {
			System.out.println("\t- HIT");
		}
		return user;
	}
	
	private User cacheGet(UserID id) throws CacheException {
		return (User) userCache.get("users", id);
	}
	
	private void cachePut(UserID id, User user) throws CacheException {
	    userCache.put("users", id, fetch(id));
	}
  
  private User fetch(UserID id) {
    switch(id.getId()) {
    case 0: return new User("Ari", "Zilka");
    case 1: return new User("Alex", "Miller");
    case 2: return new User("Geert", "Bevin");
    case 3: return new User("Taylor", "Gautier");
    case 4: return new User("Jonas", "Boner");
    default: throw new RuntimeException("Unknown id: " + id.getId());
    }
  }
  
  public static void main(String arg[]) throws Exception {
    UserTreeCacheExample userCache = new UserTreeCacheExample();

    for(int i=0; i<10; i++) {
      helpFind(i%5, userCache);
    }
  }
  
  public static void helpFind(int id, UserTreeCacheExample userCache) throws CacheException {
    UserID key = new UserID(id);
    System.out.print("Find " + id);
    userCache.findUser(key);
  }
}
