/**
 * 
 * All content copyright (c) 2003-2008 Terracotta, Inc.,
 * except as may otherwise be noted in a separate copyright notice.
 * All rights reserved.
 *
 */
package org.terracotta.util.jmx;

import java.util.HashSet;
import java.util.Set;

import javax.management.InstanceNotFoundException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

/**
 * This class provides a mechanism for blocking until the Terracotta Cluster
 * Bean has been registered. It encapsulates a solution to a race condition
 * where the Terracotta Cluster Bean may or may not have been registered when
 * you look for it/register for it.
 */
final class ClusterBeanRegistrationListener implements NotificationListener {
	private final Set<ObjectName> beanBag;
	private final MBeanServer server;
	private final ObjectName delegateName;
	private final ClusterBeanFilter clusterBeanFilter;
	private final ObjectName clusterBeanName;
	private boolean clusterBeanRegistered;

	public ClusterBeanRegistrationListener(ObjectName delegateName, 
                                           ObjectName clusterBeanName,
                                           ClusterBeanFilter clusterBeanFilter, 
                                           MBeanServer server) throws InstanceNotFoundException {
		this.delegateName = delegateName;
		this.clusterBeanName = clusterBeanName;
		this.clusterBeanFilter = clusterBeanFilter;
		this.server = server;
		beanBag = new HashSet<ObjectName>();
	}

	/**
	 * This method will block until the Terracotta Cluster Bean has been
	 * registered.
	 * 
	 * @throws InstanceNotFoundException
	 * @throws InterruptedException
	 * @throws ListenerNotFoundException
	 */
	public synchronized void waitForClusterBeanRegistration() 
    throws InstanceNotFoundException, InterruptedException, ListenerNotFoundException {
		if (clusterBeanRegistered) { return; }
        	
        // register for bean registration events. The handback is the
        // cluster bean name.
        server.addNotificationListener(delegateName, this, clusterBeanFilter,
                                       clusterBeanName);

        // check to see if the bean has already been registered.
        Set allObjectNames = server.queryNames(null, null);
        if (!allObjectNames.contains(clusterBeanName)) {
            // the bean hasn't been registered yet, so we'll expect our
            // handleNotification() interface to get called which will put the
            // handback into the bean bag.
            synchronized (beanBag) {
                while (beanBag.isEmpty()) {
                    beanBag.wait();
                }
            }
        }
        // We're done waiting for the Terracotta Cluster Bean to be registered.
        // We can unregister ourselves and move forward.
        server.removeNotificationListener(delegateName, this);
        clusterBeanRegistered = true;
    }

	/**
	 * This method will get called if the Terracotta cluster bean gets registered
	 * AFTER we were registered as a listener.
	 */
	public void handleNotification(Notification notification, Object handback) {
		synchronized (beanBag) {
			// the handback should be the cluster bean name.
			beanBag.add((ObjectName) handback);
			beanBag.notifyAll();
		}
	}
}