package org.terracotta.util.jmx;

/**
 * Default implementation of ClusterEvents.Listener provides stub
 * implementations for all methods.
 * 
 */
public class DefaultClusterEventsListener implements ClusterEvents.Listener
{

    public void initialClusterMembers(Object[] nodeId) { }
    public void nodeConnected(Object nodeId) { }
    public void nodeDisconnected(Object nodeId) { }
    public void thisNodeConnected(Object nodeId) { }
    public void thisNodeDisconnected(Object nodeId) { }
    public void thisNodeId(Object nodeId) { }
}
