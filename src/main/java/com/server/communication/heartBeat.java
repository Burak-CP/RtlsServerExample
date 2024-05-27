package com.server.communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;

import com.server.communication.packet.heartbeatPacket;
import com.server.communication.packet.registrationPacket;
import com.server.db.system.dbLocationProcess;
import com.server.utils.logger;

public class heartBeat extends Thread {

	private boolean isRunning = true;
	private DatagramSocket socket;
	private final Integer WAITTIME = 5000;
	private final Integer heartbeatPort = 27900;
	private final Integer socketTimeout = 1500;

	private Hashtable<String, Boolean> isconnected;

	public heartBeat() {
		isconnected = new Hashtable<String, Boolean>();
		try {
			this.setName("HeartBeat");
			this.setPriority(MIN_PRIORITY);
			this.socket = new DatagramSocket(heartbeatPort);
			this.socket.setSoTimeout(socketTimeout);
			logger.DebugLogger(heartBeat.class, "Heartbeat started on port: " + this.heartbeatPort);
		} catch (SocketException e) {
			logger.ErrorLogger(heartBeat.class, e);
		}
	}

	public DatagramPacket receive() throws SocketException, IOException {
		DatagramPacket packet = null;

		byte[] buf = new byte[registrationPacket.size];
		packet = new DatagramPacket(buf, buf.length);
		socket.receive(packet);

		return packet;
	}

	public void send(String message, String ipAddress, int port) throws UnknownHostException, IOException {
		byte[] bytePacket = message.getBytes();
		InetAddress address = InetAddress.getByName(ipAddress);
		DatagramPacket packet = new DatagramPacket(bytePacket, bytePacket.length, address, port);
		socket.send(packet);
	}

	private void createConnectedList(ArrayList<registrationPacket> regPackets, dbLocationProcess dbconn) {
		for (registrationPacket regpacket : regPackets) {
			String clientIp = regpacket.getIpAddress().getHostAddress();
			if (!isconnected.containsKey(clientIp)) {
				isconnected.put(clientIp, dbconn.isDeviceOnline(clientIp));
			}
		}
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				try {
					dbLocationProcess dbconn = new dbLocationProcess();
					ArrayList<registrationPacket> regPackets = dbconn.getRegistrations();
					createConnectedList(regPackets, dbconn);
					for (registrationPacket regpacket : regPackets) {
						heartbeatPacket heartbeatPacket = new heartbeatPacket(null);
						heartbeatPacket.create(regpacket);

						String message = heartbeatPacket.getMessage();
						String clientIp = heartbeatPacket.getIpAddress().getHostAddress();

						int count = 0;
						String receivedMessage = "";
						boolean match = false;
						do {
							try {
								count++;
								send(message, clientIp, heartbeatPort);

								DatagramPacket packet = receive();
								receivedMessage = new String(packet.getData(), StandardCharsets.UTF_8);
								match = receivedMessage.trim().toLowerCase().equalsIgnoreCase(message);
							} catch (SocketTimeoutException stex) {
//								logger.ErrorLogger(bbaHeartBeat.class, stex);
							} catch (Exception e) {
								logger.ErrorLogger(heartBeat.class, e);
								break;
							}
						} while (!match && count < 2);
						if ((!match && isconnected.get(clientIp)) || (match && !isconnected.get(clientIp))) {
							dbconn.setDeviceOnline(heartbeatPacket, match);
							isconnected.put(clientIp, match);
							if (match) {
								logger.DebugLogger(heartBeat.class, clientIp + "	connected.");
							} else {
								logger.DebugLogger(heartBeat.class, clientIp + "	disconnected.");
							}
						}
						if (match) {
							dbconn.setDeviceOnlineTime(heartbeatPacket);
						}
					}
					dbconn.closeConnection();
					Thread.sleep(WAITTIME);
				} catch (Exception e) {
					logger.ErrorLogger(heartBeat.class, e);
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(heartBeat.class, e);
		}
	}
}
