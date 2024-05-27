package com.server;

import com.server.communication.heartBeat;
import com.server.location.locationProcess;
import com.server.udp.udpServer;
import com.server.utils.lockManager;
import com.server.utils.logger;

public class exampleServer {

	public static exampleServer server = null;
	private boolean isRunning = true;
//	private bbaFtpServer ftpServer = null;
	private udpServer udpServer = null;
//	private bbaDbRunner dbrunner = null;
//	private bbaPostgresConnector pdbcon = null;
	public lockManager locks = null;
	public communicationDataContainer container = null;
	public communicationDataConsumer consumer = null;
	public heartBeat heartbeat = null;
	public locationProcess locationProcess;

	public static void main(String[] args) {
		try {
			server = new exampleServer();
			server.locks = new lockManager();
			server.container = new communicationDataContainer();
//		server.ftpServer = new bbaFtpServer();
			server.udpServer = new udpServer(29700);
//		server.dbrunner = new bbaDbRunner();
			server.consumer = new communicationDataConsumer();
			server.heartbeat = new heartBeat();
			server.locationProcess = new locationProcess();
//		server.ftpServer.start();
			server.udpServer.start();
//		server.dbrunner.start();
			server.consumer.start();
			server.heartbeat.start();
			server.locationProcess.start();

//		server.pdbcon = new bbaPostgresConnector();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				@Override
				public void run() {
					try {
						logger.DebugLogger(exampleServer.class, "exampleServer.main(...).new Thread() {...}.run()");
//					server.pdbcon.closeConnection();
						server.udpServer.stopThis();
//					server.ftpServer.stopThread();
//					server.dbrunner.stopThis();
					} catch (Exception e) {
						logger.ErrorLogger(exampleServer.class, e);
					}
				}
			});

			while (server.isRunning) {
				try {

				} catch (Exception e) {
					logger.ErrorLogger(exampleServer.class, e);
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(exampleServer.class, e);
		}
	}

	public void stopMain() {
		server.isRunning = false;
	}
}
