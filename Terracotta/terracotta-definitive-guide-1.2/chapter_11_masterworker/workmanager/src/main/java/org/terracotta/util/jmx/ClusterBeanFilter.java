/**
 * 
 * All content copyright (c) 2003-2008 Terracotta, Inc.,
 * except as may otherwise be noted in a separate copyright notice.
 * All rights reserved.
 *
 */
package org.terracotta.util.jmx;

import javax.management.MBeanServerNotification;
import javax.management.Notification;
import javax.management.NotificationFilter;
import javax.management.ObjectName;

class ClusterBeanFilter implements NotificationFilter 
{
    private static final long serialVersionUID = 1L;

    private static final String TYPE = "JMX.mbean.registered";
    private final ObjectName filterName;
    
    public ClusterBeanFilter(ObjectName filterName)
    {
        this.filterName = filterName;
    }

    public boolean isNotificationEnabled(Notification notification) 
    {
        return (notification.getType().equals(TYPE) &&
                ((MBeanServerNotification) notification).getMBeanName().equals(filterName));
    }
}
