package org.terracotta.util.jmx;

import javax.management.Notification;
import javax.management.NotificationListener;

import org.terracotta.util.jmx.ClusterEvents.Listener;

/**
 * Adapts a RegisteredNotificationListener into a ClusterEvents.Listener
 *
 */
public class NotificationListenerAdapter implements RegisteredNotificationListener
{
    private ClusterEvents.Listener listener;
    private NotificationListener eventHandler;
    
    public NotificationListenerAdapter(Listener listener)
    {
        super();
        this.listener = listener;
    }

    /**
     * Create a ListenerAdapter with the specified default handler.  
     * It will be called in the event that an event is delivered 
     * that is not recognized.
     * 
     * @param eventHandler
     */
    public NotificationListenerAdapter(Listener listener, NotificationListener eventHandler)
    {
        this(listener);
        this.eventHandler = eventHandler;
    }
        
    public void registered(Object nodeId, Object[] nodeIds)
    {
        listener.thisNodeId(nodeId);
        listener.initialClusterMembers(nodeIds);
    }

    /**
     * Called for cluster events.  To get the type from the Notification object,
     * use the getType() method, which returns a string that will be equal to either
     * <code>TYPE_CONNECTED</code>, <code>TYPE_DISCONNECTED</code>, <code>TYPE_THIS_NODE_CONNECTED</code>, 
     * <code>TYPE_THIS_NODE_DISCONNECTED</code>.
     * 
     * @param notification the type of event.  Either {@link TYPE_CONNECTED} or {@link TYPE_DISCONNECTED} 
     * @param handback the handback object from the JMX notification, set during the initialization of the 
     * @see javax.management.NotificationListener
     */
    public final void handleNotification(Notification notification, Object handback) 
    {
        if (notification.getType().equals(ClusterEvents.TYPE_CONNECTED)) {
            listener.nodeConnected(notification.getMessage());
        } else if (notification.getType().equals(ClusterEvents.TYPE_DISCONNECTED)) {
            listener.nodeDisconnected(notification.getMessage());
        } else if (notification.getType().equals(ClusterEvents.TYPE_THIS_NODE_DISCONNECTED)) {
            listener.thisNodeDisconnected(notification.getMessage());
        } else if (notification.getType().equals(ClusterEvents.TYPE_THIS_NODE_CONNECTED)) {
            listener.thisNodeConnected(notification.getMessage());
        } 
        
        if (eventHandler != null) { 
            eventHandler.handleNotification(notification, handback);
        }
    }
}
