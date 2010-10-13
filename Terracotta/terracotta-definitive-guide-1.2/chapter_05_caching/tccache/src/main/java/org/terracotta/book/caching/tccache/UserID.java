package org.terracotta.book.caching.tccache;

public class UserID {
  private int id;
  
  public UserID(int id) {
    this.id = id;
  }
  
  public int getId() {
    return id;
  }
  
  public int hashCode() {
    return id;
  }
  
  public boolean equals(Object other) {
    if(other == this) {
      return true;      
    } else if(! (other instanceof UserID) ) {
      return false;
    } else {
      return id == ((UserID)other).id; 
    }
  }
  
  public String toString() {
    return "User<" + this.id + ">";
  }
  
}

