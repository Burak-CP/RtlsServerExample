package com.server.tcp;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import com.server.utils.logger;

public class tcpServer {
	private Socket socket = null;
	private ServerSocket server = null;
	private DataInputStream in = null;
	private Integer port = -1;
	private final String endOfSteam = "#";

	public tcpServer(int tcpPort) {
		try {
			this.port = tcpPort;
			server = new ServerSocket(port);
			logger.DebugLogger(tcpServer.class, "Server started");
		} catch (IOException e) {
			logger.ErrorLogger(tcpServer.class, e);
		}
	}

	private void connect() {
		try {
			// reads message from client until "Over" is sent
			String line = "";
			while (!line.equals("Over")) {
				try {
					line = in.readUTF();
					logger.DebugLogger(tcpServer.class, line);

				} catch (IOException i) {
					logger.ErrorLogger(tcpServer.class, i);
				}
			}
			// close connection
			in.close();
		} catch (IOException i) {
			logger.ErrorLogger(tcpServer.class, i);
		}
	}

	private String receive() {
		String line = "";
		try {
			socket = server.accept();
			logger.DebugLogger(tcpServer.class, "Client accepted");
			// takes input from the client socket
			in = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			// reads message from client until "Over" is sent
			while (!line.equals(endOfSteam)) {
				try {
					line = in.readUTF();
					logger.DebugLogger(tcpServer.class, line);
				} catch (IOException i) {
					logger.ErrorLogger(tcpServer.class, i);
				}
			}
			// close connection
			in.close();
		} catch (Exception e) {
			logger.ErrorLogger(tcpServer.class, e);
		}
		return line;
	}
}
