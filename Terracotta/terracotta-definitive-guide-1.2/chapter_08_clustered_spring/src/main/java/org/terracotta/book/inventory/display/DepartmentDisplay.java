package org.terracotta.book.inventory.display;

import org.terracotta.book.inventory.cli.IO;
import org.terracotta.book.inventory.entity.Department;


public class DepartmentDisplay extends ProductsDisplay {

	public DepartmentDisplay(final IO io, final ProductDisplay productDisplay) {
		super(io, productDisplay);
	}

	public void displayDepartment(Department department) {
		productDisplay.displaySeparator();
		io.println("Department " + department.getName());
	}

}
