package org.terracotta.util.jmx;

import javax.management.NotificationListener;

public interface RegisteredNotificationListener extends NotificationListener
{
    public void registered(Object nodeId, Object[] nodeIds);
}
