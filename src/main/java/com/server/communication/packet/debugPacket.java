package com.server.communication.packet;

import java.net.DatagramPacket;
import java.util.Arrays;

import com.server.communication.packet.data.debugPacketData;
import com.server.communication.packet.data.abstractPacketData.DATABYTEADDRESSBASE;
import com.server.communication.packet.data.debugPacketData.DATABYTEADDRESS;
import com.server.utils.logger;

public class debugPacket extends abstractPacket {

	public static final int size = 76;

	public debugPacket(DatagramPacket packet) {
		super(packet);
		parse();
	}

	private void parse() {
		try {
			debugPacketData data = new debugPacketData();
			byte[] bytepacketData = dpPacket.getData();
			data.setAnchorId(getIntValueFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORID.getValue()));
			data.setAnchorChipId(getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORCHIPID.getValue()));
			data.setAnchorBluetoothId(
					getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORBLUETOOTHID.getValue()));
			data.setAnchorMacAddress(getMacFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORMACADDRESS.getValue()));
			data.setMessageLength(getIntValueFromPacket(bytepacketData, DATABYTEADDRESS.MESSAGELENGTH.getValue()));
			if (data.getMessageLength() < Integer.MAX_VALUE) {
				data.setMessage(getMessageFromPacket(bytepacketData, DATABYTEADDRESS.MESSAGE.getValue(),
						data.getMessageLength()));
			} else {
				logger.ErrorLogger(debugPacket.class, "Message length too big.");
			}
			this.packet.data = data;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessageFromPacket(byte[] packet, int byteAddress, int messageLength) {
		String value = "";
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + messageLength);
		value = byteArrayToString(slice);
		return value;
	}

	@Override
	public debugPacketData getData() {
		debugPacketData returnValue = null;
		if (created) {
			returnValue = (debugPacketData) packet.data;
		}
		return returnValue;
	}
}
