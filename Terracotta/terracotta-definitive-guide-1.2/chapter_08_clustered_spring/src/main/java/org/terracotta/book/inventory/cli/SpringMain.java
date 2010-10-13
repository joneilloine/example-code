package org.terracotta.book.inventory.cli;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringMain {
  public static void main(String[] args) {
    BeanFactory factory = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml" });
    CLI cli = (CLI) factory.getBean("cli");
    cli.run();
  }
}
