/*
 * Copyright (c) 2006 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.dynamic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.terracotta.util.jmx.ClusterEvents;
import org.terracotta.workmanager.AbstractWorkManager;
import org.terracotta.workmanager.pipe.Pipe;
import org.terracotta.workmanager.queue.Queue;
import org.terracotta.workmanager.routing.RoutableWorkItem;
import org.terracotta.workmanager.routing.Router;

import commonj.work.Work;
import commonj.work.WorkEvent;
import commonj.work.WorkItem;
import commonj.work.WorkListener;
import commonj.work.WorkManager;
import java.util.Iterator;

/**
 * A routing aware WorkerManager that uses an implementation of the Router
 * interface to do the route work to different work queues.
 */
public class DynamicWorkManager implements WorkManager, ClusterEvents.Listener {

    private final Router<String> m_router;

    private final Set<RoutableWorkItem<String>> m_completedWork;

    private int m_nrOfScheduledWork = 0;

    private Object m_nodeId;

    public DynamicWorkManager(final Router<String> router) {
        m_router = router;
        m_completedWork = router.getAllCompletedWork();
        ClusterEvents.registerListener(this);
    }

    public WorkItem schedule(final Work work, final WorkListener listener) {
        System.out.println("scheduled work number: " + m_nrOfScheduledWork++);
        return m_router.route(work, listener);
    }

    public Set<RoutableWorkItem<String>> getCompletedWork() {
        return m_completedWork;
    }

    public WorkItem schedule(final Work work) {
        return schedule(work, null);
    }

    public boolean waitForAll(Collection workItems, long timeout) throws InterruptedException {
        final int nrOfPendingWorkItems = workItems.size();
        int nrOfCompletedWorkItems = 0;
        while (true) {
            WorkItem workItem = waitForAny(timeout);
            if (workItem == null) {
                return false;
            }
            nrOfCompletedWorkItems++;
            if (nrOfPendingWorkItems == nrOfCompletedWorkItems) {
                break;
            }
        }
        return true;
    }

    public Collection waitForAny(final Collection workItems, final long timeout) throws InterruptedException {
        final List<WorkItem> completedWorkItems = new ArrayList<WorkItem>();

        // TODO: cheating now by only wrapping a single completed item in a
        // collection
        WorkItem workItem = waitForAny(timeout);
        if (workItem == null) {
            return Collections.EMPTY_LIST;
        }
        completedWorkItems.add(workItem);
        return completedWorkItems;
    }

    public WorkItem waitForAny(long timeout) throws InterruptedException {
        synchronized (m_completedWork) {
            while (true) {
                for (WorkItem workItem : m_completedWork) {
                    if (workItem.getStatus() == WorkEvent.WORK_COMPLETED || workItem.getStatus() == WorkEvent.WORK_REJECTED) {
                        m_completedWork.remove(workItem);
                        return workItem;
                    }
                }
                if (timeout == IMMEDIATE) {
                    return null;
                }
                if (timeout == INDEFINITE) {
                                       int size = m_completedWork.size();
                                        while (m_completedWork.size() == size) {
                                                try {   
                                                        m_completedWork.wait();
                                                } catch (InterruptedException ie) {
                                                        ie.printStackTrace();
                                                        Thread.currentThread().interrupt();
                                                        throw ie;
                                                }
                                        }
                }
            }
        }
    }

    private boolean isMasterNode(Object nodeId) {
        return nodeId.equals((String)m_nodeId);
    }

    public synchronized Object getMyNodeId() throws InterruptedException {
        while (m_nodeId == null) {
            wait();
        }
        return m_nodeId;
    }

    public synchronized void thisNodeId(final Object nodeId) {
        System.out.println("work manager node id: " + nodeId);
        m_nodeId = nodeId;
        notify();
    }

    public void initialClusterMembers(Object[] nodeIds) {
        // first time around for work manager - clean up potential previous state
        m_router.reset();

        for (Object nodeId : nodeIds) {
            if (isMasterNode(nodeId)) {             
                return; // this is the master node
            }           
            System.out.println("registering worker node with ID: " + nodeId);
            m_router.register((String)nodeId);          
        }
    }

    public void nodeConnected(Object nodeId) {
        if (isMasterNode(nodeId)) {
            return; // this is the master node
        }           
        System.out.println("registering worker node with ID: " + nodeId);
        m_router.register(nodeId.toString());
    }

    public void nodeDisconnected(Object nodeId) {
        System.out.println("unregistering worker node with ID: " + nodeId );
        
        Pipe<RoutableWorkItem<String>, String> pipe = m_router.getPipeFor(nodeId.toString());
        
        // add all completed items to the completed work set
        Queue<RoutableWorkItem<String>> compWorkQueue = pipe.getCompletedWorkQueue();
        for (Iterator<RoutableWorkItem<String>> it = compWorkQueue.iterator(); it.hasNext();) {
          m_completedWork.add(it.next());
        }
     
        // copy all pending work (needed since we will have to remove the pipe before the rerouting)
        List<RoutableWorkItem<String>> pending = new ArrayList<RoutableWorkItem<String>>();
        Queue<RoutableWorkItem<String>> pendWorkQueue = pipe.getPendingWorkQueue();
        for (Iterator<RoutableWorkItem<String>> it = pendWorkQueue.iterator(); it.hasNext();) {
          pending.add(it.next());
        }
        
        // unregister the worker which also clears and removes the pipe
        m_router.unregister(nodeId.toString()); // remove pipe for disconnected worker before fail-over

        // reroute the pending work
        for (RoutableWorkItem<String> workItem : pending) {
          m_router.route(workItem);       
        }
    }

    public void thisNodeConnected(Object arg0) { }
    public void thisNodeDisconnected(Object arg0) { }
}
