package org.terracotta.book.caching.tccache;

public class User {

  private String first;
  private String last;
  
  public User(String first, String last) {
    this.first = first;
    this.last = last;
  }
  
  public String getFirst() {
    return this.first;
  }
  
  public String getLast() {
    return this.last;
  }
  
  public String toString() {
    return first + " " + last;
  }

}

