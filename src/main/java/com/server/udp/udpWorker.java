package com.server.udp;

import java.net.DatagramPacket;
import java.util.Hashtable;

import com.server.exampleServer;
import com.server.communication.packet.abstractPacket;
import com.server.communication.packet.abstractPacket.MESSAGECONTENT;
import com.server.utils.logger;

public class udpWorker extends Thread {

	private DatagramPacket packet = null;

	public udpWorker(DatagramPacket packet) {
		this.packet = packet;
	}

	@Override
	public void run() {
		try {
			if (this.packet != null) {
				abstractPacket abstractPacket = new abstractPacket(this.packet) {
				};
				Hashtable<MESSAGECONTENT, DatagramPacket> data = new Hashtable<>();
				if (abstractPacket.getContent() != null) {
					data.put(abstractPacket.getContent(), packet);
					exampleServer.server.container.putLast(data);
					synchronized (exampleServer.server.consumer) {
						exampleServer.server.consumer.notify();
					}
				} else {
					logger.ErrorLogger(udpWorker.class, "Packet content is null.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
