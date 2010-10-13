/*
 * Copyright (c) 2006-2007 Terracotta, Inc. All rights reserved.
 */
package org.terracotta.workmanager.pipe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.terracotta.workmanager.pipe.PipeManager;
import org.terracotta.workmanager.routing.RoutableWorkItem;

/**
 * A pipe manager that manages the different pipes.
 */
public class PipeManager<ID> {

	public static enum Initiator {
		MASTER, WORKER
	};

	private Initiator m_type;

	private Pipe.Factory<RoutableWorkItem<ID>, ID> m_pipeFactory;

	private final Map<ID, Pipe<RoutableWorkItem<ID>, ID>> m_pipes = new ConcurrentHashMap<ID, Pipe<RoutableWorkItem<ID>, ID>>();

	private transient Set<RoutableWorkItem<ID>> m_allCompletedWork = new HashSet<RoutableWorkItem<ID>>();
	
	private final List<ID> m_routingIDs = new ArrayList<ID>();
	
	private volatile boolean m_isRunning = true;

	public PipeManager(final Pipe.Factory<RoutableWorkItem<ID>, ID> factory, Initiator type) {
		System.out.println("created PipeManager for Initiator: " + type);
		m_pipeFactory = factory;
		m_type = type;
	}

	public Pipe<RoutableWorkItem<ID>, ID> getOrCreatePipeFor(final ID routingID) {
		if (!m_isRunning) throw new IllegalStateException("pipe manager has been shut down");
		if (routingID == null) throw new IllegalArgumentException("routing ID is null");

		synchronized (m_pipes) {
			Pipe<RoutableWorkItem<ID>, ID> pipe;
			if (m_pipes.containsKey(routingID)) {
				pipe = m_pipes.get(routingID);
			} else {
				m_routingIDs.add(routingID);
				pipe = m_pipeFactory.create(routingID);
				m_pipes.put(routingID, pipe);
				System.out.println("registering a new work queue for routing ID: " + routingID);
			}
			switch (m_type) {
			case MASTER:
				pipe.startQueueListener(m_allCompletedWork);
				break;
			case WORKER:
				break;
			}
			return pipe;
		}
	}

	public void removePipeFor(final ID routingID) {
		if (!m_isRunning) throw new IllegalStateException("pipe manager has been shut down");
		if (routingID == null) throw new IllegalArgumentException("routing ID is null");
		synchronized (m_pipes) {
			if (!m_pipes.containsKey(routingID)) {
				return;
			}
			m_pipes.get(routingID).clear();
			m_pipes.remove(routingID);
			System.out.println("unregistering the work queue for routing ID: " + routingID);
		}
	}

	public void put(final RoutableWorkItem<ID> workItem, final ID routingID) throws InterruptedException {
		if (!m_isRunning) throw new IllegalStateException("pipe manager has been shut down");
		Pipe<RoutableWorkItem<ID>, ID> pipe = getOrCreatePipeFor(routingID);
		pipe.getPendingWorkQueue().put(workItem); // blocks if queue is full
	}

	public Map<ID, Pipe<RoutableWorkItem<ID>, ID>> getPipes() {
		if (!m_isRunning) throw new IllegalStateException("pipe manager has been shut down");
		return m_pipes;
	}

	public Set<RoutableWorkItem<ID>> getAllCompletedWork() {
		return m_allCompletedWork;
	}
	
	public List<ID> getRoutingIDs() {
		return Collections.unmodifiableList(m_routingIDs);
	}
	
	public void reset() {
		m_allCompletedWork.clear();
		synchronized (m_pipes) {
			m_isRunning = false;
			for (Pipe<RoutableWorkItem<ID>, ID> pipe : m_pipes.values()) {
				pipe.clear();
			}
		}
	}
}
