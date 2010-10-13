package org.terracotta.workmanager;

import commonj.work.WorkException;
import commonj.work.WorkItem;

public interface MutableWorkItem extends WorkItem {
	
	void setStatus(int status, WorkException exception);
	
	void fireListener();

}
