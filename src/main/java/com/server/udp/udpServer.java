package com.server.udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;

import com.server.communication.packet.registrationPacket;
import com.server.utils.logger;

public class udpServer extends Thread {
	private boolean isRunning = true;
	private DatagramSocket socket;
	private Integer port = -1;

	public udpServer(Integer udpPort) {
		try {
			this.setName("UDP Server");
			this.setPriority(MAX_PRIORITY);
			this.port = udpPort;
			this.socket = new DatagramSocket(udpPort);
			logger.DebugLogger(udpServer.class, "UDP Server started listening on port: " + this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}

	public DatagramPacket receive() {
		DatagramPacket packet = null;
		try {
			byte[] buf = new byte[registrationPacket.size];
			packet = new DatagramPacket(buf, buf.length);
			socket.receive(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return packet;
	}

	public void send(String message, String ipAddress, int port) {
		try {
			byte[] bytePacket = message.getBytes();
			InetAddress address = InetAddress.getByName(ipAddress);
			DatagramPacket packet = new DatagramPacket(bytePacket, bytePacket.length, address, port);
			socket.send(packet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				try {
					DatagramPacket packet = receive();
					if (packet != null) {
						udpWorker worker = new udpWorker(packet);
						worker.start();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String bytearrayToString(byte[] bytearray) {
		return new String(bytearray, StandardCharsets.UTF_8);
	}

	public void stopThis() {
		isRunning = false;
	}
}
