package org.terracotta.book.inventory.entity;

import java.util.Set;
import java.util.TreeSet;

import org.terracotta.book.inventory.display.DepartmentDisplay;
import org.terracotta.book.inventory.util.DepartmentByNameComparator;

public class Departments {
  private final Set<Department> departments;
  private final DepartmentFactory departmentFactory;
  

  public Departments(final DepartmentByNameComparator comparator, final DepartmentFactory departmentFactory) {
    this.departmentFactory = departmentFactory;
    departments = new TreeSet<Department>(comparator);
  }

  public Department newDepartment(final String name) {
    Department department = departmentFactory.newDepartment(name);
    synchronized (departments) {
      departments.add(department);
    }
    return department;
  }

  public void displayDepartments(final DepartmentDisplay departmentDisplay) {
    synchronized (departments) {
      for (Department department : departments) {
        department.display(departmentDisplay);
      }
    }
  }

  public int getDepartmentCount() {
    synchronized (departments) {
      return departments.size();
    }
  }
}
