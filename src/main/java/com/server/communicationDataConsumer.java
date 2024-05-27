package com.server;

import java.net.DatagramPacket;
import java.util.Hashtable;
import java.util.Map;

import com.server.communication.communicationHandler;
import com.server.communication.packet.abstractPacket.MESSAGECONTENT;

public class communicationDataConsumer extends Thread {

	private boolean isRunning = true;
	private long waitTime = 10;

	public communicationDataConsumer() {
		this.setName("CONSUMER");
	}

	public Hashtable<MESSAGECONTENT, DatagramPacket> removeFirst() {
		return exampleServer.server.container.removeFirst();
	}

	@Override
	public void run() {
		try {
			Hashtable<MESSAGECONTENT, DatagramPacket> data = null;
			while (isRunning) {
				try {
					data = removeFirst();
					while (data == null) {
						synchronized (this) {
							this.wait(waitTime);
						}
						data = removeFirst();
					}
					Map.Entry<MESSAGECONTENT, DatagramPacket> entry = data.entrySet().iterator().next();
					MESSAGECONTENT key = entry.getKey();
					DatagramPacket value = entry.getValue();
					communicationHandler handler = new communicationHandler(key, value);
					handler.handleRequest();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
