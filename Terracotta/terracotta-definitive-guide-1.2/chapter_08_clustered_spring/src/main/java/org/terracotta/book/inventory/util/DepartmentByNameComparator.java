/**
 * 
 */
package org.terracotta.book.inventory.util;

import java.util.Comparator;

import org.terracotta.book.inventory.entity.Department;


public final class DepartmentByNameComparator implements Comparator<Department> {

	public int compare(Department o1, Department o2) {
		if (o1 == null || o2 == null) return 0;
		return o1.getName().compareToIgnoreCase(o2.getName());
	}
}