package com.server.communication;

import java.net.DatagramPacket;

import com.server.communication.packet.abstractPacket.MESSAGECONTENT;
import com.server.communication.packet.debugPacket;
import com.server.communication.packet.locationPacket;
import com.server.communication.packet.registrationPacket;
import com.server.db.system.dbLocationProcess;
import com.server.utils.logger;
import com.server.utils.simpleDate;

public class communicationHandler {

	private MESSAGECONTENT content = null;
	private DatagramPacket packet = null;

	public communicationHandler(MESSAGECONTENT content, DatagramPacket packet) {
		this.content = content;
		this.packet = packet;
	}

	public boolean handleRequest() {
		try {
			long timeLong = System.currentTimeMillis();
			String timestr = simpleDate.converttimestampToString(simpleDate.DATETIMES, timeLong);
			switch (content) {
			case LOCATION: {
				locationPacket locationPacket = new locationPacket(this.packet);
				Integer anchorId = locationPacket.getData().getAnchorId();
				String anchorChipId = locationPacket.getData().getAnchorChipId().trim();
				String anchorBluetoothId = locationPacket.getData().getAnchorBluetoothId();
				String anchorMacAddress = locationPacket.getData().getAnchorMacAddress().trim();
				String anchorIpAddress = locationPacket.getIpAddress().getHostAddress().trim();

				Integer tagId = locationPacket.getData().getTagId();
				String tagChipId = locationPacket.getData().getTagChipId().trim();
				String tagBluetoothId = locationPacket.getData().getTagBluetoothId();
				Float x = locationPacket.getData().getDistance();
				long time = System.currentTimeMillis();
				if (anchorId != null && anchorChipId != null && anchorBluetoothId != null && anchorMacAddress != null
						&& anchorIpAddress != null && tagId != null && tagChipId != null && tagBluetoothId != null
						&& x != null) {
					dbLocationProcess dbLocationProcess = new dbLocationProcess();
					if (dbLocationProcess.updateLocation(time, anchorId, anchorChipId, anchorBluetoothId,
							anchorMacAddress, anchorIpAddress, tagId, tagChipId, tagBluetoothId, x)) {
						try {
							logger.DebugLogger(communicationHandler.class, "Location information between: "
									+ anchorChipId + "<->" + tagChipId + "	Distance: " + x);

							logger.EmbeddedDbLogger(MESSAGECONTENT.LOCATION, //
									"Location information between: " + anchorChipId + "<->" + tagChipId + "	Distance: "
											+ x, //
									anchorIpAddress, //
									timeLong);
						} catch (Exception e) {
							logger.ErrorLogger(communicationHandler.class, e);
						}
					}
//					dbLocationProcess.closeConnection();
				}
			}
				break;

			case REGISTRATION: {
				registrationPacket registrationPacket = new registrationPacket(this.packet);
				Integer anchorId = registrationPacket.getData().getAnchorId();
				String anchorChipId = registrationPacket.getData().getAnchorChipId();
				String anchorBluetoothId = registrationPacket.getData().getAnchorBluetoothId();
				String anchorMacAddress = registrationPacket.getData().getAnchorMacAddress();
				String anchorIpAddress = registrationPacket.getIpAddress().getHostAddress();
				String anchorWifiVersion = registrationPacket.getData().getAnchorWifiVersion();
				String anchorUWBMUCVersion = registrationPacket.getData().getAnchorUWBMCUVersion();
				dbLocationProcess dbLocationProcess = new dbLocationProcess();
				if (dbLocationProcess.addRegistration(anchorId, anchorChipId, anchorBluetoothId, anchorMacAddress,
						anchorIpAddress, anchorWifiVersion, anchorUWBMUCVersion)) {
					logger.DebugLogger(communicationHandler.class, "Registration complete for -> " + anchorIpAddress);

					logger.EmbeddedDbLogger(MESSAGECONTENT.REGISTRATION, "[REGISTER] Registration complete.",
							anchorIpAddress, timeLong);
				}
//				dbconnector.closeConnection();
			}
				break;

			case FIRMWARE:

				break;

			case INFORMATION:
				debugPacket DebugPacket = new debugPacket(this.packet);
				Integer anchorId = DebugPacket.getData().getAnchorId();
				String anchorChipId = DebugPacket.getData().getAnchorChipId();
				String anchorBluetoothId = DebugPacket.getData().getAnchorBluetoothId();
				String anchorMacAddress = DebugPacket.getData().getAnchorMacAddress();
				String anchorIpAddress = DebugPacket.getIpAddress().toString();
				String anchorMessage = DebugPacket.getData().getMessage();
				try {
					logger.DebugLogger(communicationHandler.class,
							"INFORMATION - " + anchorIpAddress + ":	" + anchorMessage);

					logger.EmbeddedDbLogger(MESSAGECONTENT.INFORMATION, anchorMessage, anchorIpAddress, timeLong);
				} catch (Exception e) {
					logger.ErrorLogger(communicationHandler.class, e);
				}
				break;

			case DEBUG:
				DebugPacket = new debugPacket(this.packet);
				anchorId = DebugPacket.getData().getAnchorId();
				anchorChipId = DebugPacket.getData().getAnchorChipId();
				anchorBluetoothId = DebugPacket.getData().getAnchorBluetoothId();
				anchorMacAddress = DebugPacket.getData().getAnchorMacAddress();
				anchorIpAddress = DebugPacket.getIpAddress().toString();
				anchorMessage = DebugPacket.getData().getMessage();
				try {
					logger.DebugLogger(communicationHandler.class,
							"DEBUG - " + anchorIpAddress + ":	" + anchorMessage);
					logger.EmbeddedDbLogger(MESSAGECONTENT.DEBUG, anchorMessage, anchorIpAddress, timeLong);
				} catch (Exception e) {
					logger.ErrorLogger(communicationHandler.class, e);
				}

				break;

			case ERROR:
				DebugPacket = new debugPacket(this.packet);
				anchorId = DebugPacket.getData().getAnchorId();
				anchorChipId = DebugPacket.getData().getAnchorChipId();
				anchorBluetoothId = DebugPacket.getData().getAnchorBluetoothId();
				anchorMacAddress = DebugPacket.getData().getAnchorMacAddress();
				anchorIpAddress = DebugPacket.getIpAddress().toString();
				anchorMessage = DebugPacket.getData().getMessage();
				try {
					logger.DebugLogger(communicationHandler.class,
							"ERROR - " + anchorIpAddress + ":	" + anchorMessage);
					logger.EmbeddedDbLogger(MESSAGECONTENT.ERROR, anchorMessage, anchorIpAddress, timeLong);
				} catch (Exception e) {
					logger.ErrorLogger(communicationHandler.class, e);
				}
				break;

			case WARNING:
				DebugPacket = new debugPacket(this.packet);
				anchorId = DebugPacket.getData().getAnchorId();
				anchorChipId = DebugPacket.getData().getAnchorChipId();
				anchorBluetoothId = DebugPacket.getData().getAnchorBluetoothId();
				anchorMacAddress = DebugPacket.getData().getAnchorMacAddress();
				anchorIpAddress = DebugPacket.getIpAddress().toString();
				anchorMessage = DebugPacket.getData().getMessage();
				try {
					logger.DebugLogger(communicationHandler.class,
							"WARNING - " + anchorIpAddress + ":	" + anchorMessage);

					logger.EmbeddedDbLogger(MESSAGECONTENT.WARNING, anchorMessage, anchorIpAddress, timeLong);
				} catch (Exception e) {
					logger.ErrorLogger(communicationHandler.class, e);
				}
				break;

			case NONE:

				break;

			default:
				break;
			}
		} catch (Exception e) {
			logger.ErrorLogger(communicationHandler.class, e);
		}
		return false;
	}
}
