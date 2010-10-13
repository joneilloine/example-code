/**
 * 
 * All content copyright (c) 2003-2008 Terracotta, Inc.,
 * except as may otherwise be noted in a separate copyright notice.
 * All rights reserved.
 *
 */
package org.terracotta.util.jmx;

import java.util.List;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

/**
 * Utility class for registering Cluster Events Listeners.
 * <p>
 * Usage:
 * </p>
 * <pre>
 *   ClusterEvents.registerListener(myListener);
 * </pre>
 *
 */
public class ClusterEvents 
{   
    private static final String DELEGATE_NAME = "JMImplementation:type=MBeanServerDelegate";
    private static final String CLUSTER_BEAN_NAME = "org.terracotta:type=Terracotta Cluster,name=Terracotta Cluster Bean";

    public static final String TYPE_CONNECTED = "com.tc.cluster.event.nodeConnected";
    public static final String TYPE_DISCONNECTED = "com.tc.cluster.event.nodeDisconnected";
    public static final String TYPE_THIS_NODE_CONNECTED = "com.tc.cluster.event.thisNodeConnected";
    public static final String TYPE_THIS_NODE_DISCONNECTED = "com.tc.cluster.event.thisNodeDisconnected";
   
    private ClusterEvents() { } 
    
    /**
     * Listener class that receives Cluster Events.
     * 
     */
    public interface Listener 
    {        
        /**
         * When connected, this will be called to tell the node about it's nodeid 
         * node and the cluster.
         * 
         * @param nodeId the nodeId for this connected node
         * @param nodeIds the nodeIds of the nodes in the cluster
         */
        public void thisNodeId(Object nodeId);

        /**
         * When connected, this will be called to tell the node about it's nodeid 
         * node and the cluster.
         * 
         * @param nodeIds the nodeIds of the nodes in the cluster
         */
        public void initialClusterMembers(Object[] nodeId);
        
        /**
         * Called when this node (re)connects to the cluster.
         * 
         * @param nodeId the nodeId (of this node)
         */
        public void thisNodeConnected(Object nodeId);
        
        /**
         * Called when this node disconnects from the cluster.
         * 
         * @param nodeId the nodeId (of this node)
         */
        public void thisNodeDisconnected(Object nodeId);
        
        /**
         * Called when a node connects to the cluster.
         * 
         * @param nodeId the nodeId of the node that disconnected
         */
        public void nodeConnected(Object nodeId);
        
        /**
         * Called when a node disconnects from the cluster.
         * 
         * @param nodeId the nodeId of the node that disconnected
         */
        public void nodeDisconnected(Object nodeId);
    }
    
    /**
     * Provides a default implementation of Listener that implements
     * empty methods for easy extension.
     *
     */
    public static class ListenerAdapter implements Listener
    {
        public void thisNodeId(Object nodeId) { }
        public void initialClusterMembers(Object[] nodeIds) { }        
        public void thisNodeConnected(Object nodeId) { }
        public void thisNodeDisconnected(Object nodeId) { }
        public void nodeConnected(Object nodeId) { }
        public void nodeDisconnected(Object nodeId) { }        
    }
    
    /**
     * Provides a default implementation of Listener that stores
     * the nodeId for later retrieval, and provides a default
     * mechanism to wait for the nodeId to be set.
     * 
     */
    public static class DefaultListener extends ListenerAdapter
    {
        private Object nodeId = null;
        
        /**
         * Returns the nodeId assigned for this listener.  This call
         * is non-blocking, and may return null.  If you want to make
         * sure the value is non-null, you should call
         * waitForRegistration() first.
         * 
         * @return the nodeId if set, null otherwise
         */       
        public final synchronized Object getMyNodeId() 
        {
            return nodeId;
        }

        /**
         * Call this to wait for the node to get the registration callback.
         */
        public synchronized void waitForRegistration() throws InterruptedException
        {
            while (nodeId == null) {
                wait();
            }
        }

        /**
         * Called when the registration succeeds.  If you override this, make sure
         * to call super.thisNodeId() or the waitForRegistration() method will never
         * return.
         */
        public synchronized void thisNodeId(Object nodeId)
        {
            this.nodeId = nodeId;
            notify();
        }
    }
    
    /**
     * Register a cluster events listener
     * 
     * @param listener the listener that will listen for events
     */
    public static void registerListener(final Listener listener)
    {
        registerNotificationListenerAdapter(new NotificationListenerAdapter(listener));
    }

    /**
     * Register a cluster events listener
     * 
     * @param listener the listener that will listen for events
     * @param handback the object that will be sent back during notifications
     */
    public static void registerListener(final Listener listener, final Object handback)
    {
        registerNotificationListenerAdapter(new NotificationListenerAdapter(listener), handback);
    }

    /**
     * Register a cluster events listener adapter
     * 
     * @param adapter the adapter that will listen for events
     */
    public static void registerNotificationListenerAdapter(final RegisteredNotificationListener adapter)
    {
        registerNotificationListenerAdapter(adapter, null);
    }
        
    /**
     * Register a cluster events listener adapter.
     * 
     * @param listener the listener that will listen for events
     * @param handback the object that will be sent back during notifications
     */
    public static void registerNotificationListenerAdapter(final RegisteredNotificationListener adapter, final Object handback)
    {
        new Thread(new Runnable() {            
            public void run() 
            {
                try {
                    ObjectName delegateName = ObjectName.getInstance(DELEGATE_NAME);
                    ObjectName clusterBeanName = new ObjectName(CLUSTER_BEAN_NAME);

                    List servers = MBeanServerFactory.findMBeanServer(null);
                    MBeanServer server = (MBeanServer) servers.get(0);

                    ClusterBeanFilter clusterBeanFilter = new ClusterBeanFilter(clusterBeanName);
                    ClusterBeanRegistrationListener clusterBeanRegistrationListener =
                        new ClusterBeanRegistrationListener(delegateName,
                                                            clusterBeanName,
                                                            clusterBeanFilter,
                                                            server);

                    clusterBeanRegistrationListener.waitForClusterBeanRegistration();
                    server.addNotificationListener(clusterBeanName, adapter, null, handback);

                    // Now that the cluster bean is registered, we can find out what our node id is
                    adapter.registered(server.getAttribute(clusterBeanName, "NodeId"),
                                       (Object[]) server.getAttribute(clusterBeanName, "NodesInCluster"));
                } catch (Exception e) {
                    throw new AssertionError(e);
                }
            }
        }).start();
    }
}