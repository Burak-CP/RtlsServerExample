package com.server;

import java.net.DatagramPacket;
import java.util.Hashtable;
import java.util.LinkedList;

import com.server.communication.packet.abstractPacket.MESSAGECONTENT;

public class communicationDataContainer {

	private LinkedList<Hashtable<MESSAGECONTENT, DatagramPacket>> container = null;
	private final String LOCKNAME = this.getClass().getSimpleName();

	public communicationDataContainer() {
		container = new LinkedList<>();
		exampleServer.server.locks.add(LOCKNAME);
	}

	private void lock() {
		try {
			exampleServer.server.locks.lock(LOCKNAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void unLock() {
		try {
			exampleServer.server.locks.unLock(LOCKNAME);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Integer size() {
		Integer size = -1;
		lock();
		try {
			size = container.size();
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return size;
	}

	public void put(Hashtable<MESSAGECONTENT, DatagramPacket> arg) {
		lock();
		try {
			container.add(arg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
	}

	public void putFirst(Hashtable<MESSAGECONTENT, DatagramPacket> arg) {
		lock();
		try {
			container.addFirst(arg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
	}

	public void putLast(Hashtable<MESSAGECONTENT, DatagramPacket> arg) {
		lock();
		try {
			container.addLast(arg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> get(int index) {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.get(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> getFirst() {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.getFirst();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> getLast() {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.getLast();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> remove(int index) {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.remove(index);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> removeFirst() {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.removeFirst();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> removeLast() {
		lock();
		Hashtable<MESSAGECONTENT, DatagramPacket> packet = null;
		try {
			if (container.size() > 0) {
				packet = container.removeLast();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		unLock();
		return packet;
	}

	public boolean isEmpty() {
		return container.isEmpty();
	}
}
