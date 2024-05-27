package com.server.utils;

import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.server.communication.packet.abstractPacket.MESSAGECONTENT;
import com.server.db.log.dbLogging;
import com.server.db.log.logTable;

public class logger {
	private static String strsep = "/";
	private static String strFile = simpleDate.format("yyyyMMdd", new Date());
	private static String strLogPath = "." + strsep + "log" + strsep;

	/*
	 * Logging embedded logs to db
	 */
	public static void EmbeddedDbLogger(MESSAGECONTENT logtype, String anchorMessage, String anchorIpAddress,
			long time) {
		String messageType = null;
		try {
			if (anchorMessage.contains("[") && anchorMessage.contains("]")) {
				messageType = anchorMessage.substring(anchorMessage.indexOf('[') + 1, anchorMessage.indexOf(']'));
			} else {
				ErrorLogger(logger.class,
						"EmbeddedDbLogger error. Message not contains '[' or ']'. Message: " + anchorMessage);
			}
		} catch (Exception e) {
			ErrorLogger(logger.class, e);
		}
		if (messageType != null) {
			dbLogging dbLogging = new dbLogging();
			dbLogging.insert( //
					new logTable( //
							logtype, //
							messageType, //
							anchorMessage, //
							anchorIpAddress, //
							time //
					) //
			);
			dbLogging.closeConnection();
		}

	}

	public static void DebugLogger(Class clazz, Object... message) {
		StringBuilder sb = new StringBuilder("[") //
				.append(simpleDate.getCurrentTime(simpleDate.DATETIMES)) //
				.append("] ");
		sb.append(clazz.getName()) //
				.append(": ");
		for (Object string : message) {
			if (string != null) {
				sb.append(string.toString());
			}
		}
		sb.append("\n");
		Logger logger = Logger.getLogger(clazz.getName());
		logger.log(Level.ALL, sb.toString());
		System.out.print(sb.toString());
		String path = strLogPath + strFile + ".log";
		fileWriter.appendToFile(path, sb.toString());
	}

	public static void ErrorLogger(Class clazz, Object... message) {
		DebugLogger(clazz, message);
	}

	public static void ErrorLogger(Object clazz, Exception e) {
		StringBuilder sb = new StringBuilder("[").append(simpleDate.getCurrentTime(simpleDate.DATETIMES)).append("] ");
		sb.append(clazz.getClass().getName()).append(" ");
		sb.append("Error Message: " + e.getMessage());
		sb.append("\n");
		sb.append("Error Cause: " + e.getCause());
		sb.append("\n");
		sb.append(Arrays.stream(e.getStackTrace()).skip(0).map(StackTraceElement::toString)
				.reduce((s1, s2) -> s1 + "\n" + s2).get());
		sb.append("\n");
		System.err.print(sb.toString());
		String path = strLogPath + strFile + ".log";
		fileWriter.appendToFile(path, sb.toString());
	}
}
