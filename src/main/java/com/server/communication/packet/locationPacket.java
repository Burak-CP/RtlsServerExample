package com.server.communication.packet;

import java.net.DatagramPacket;

import com.server.communication.packet.data.locationPacketData;
import com.server.communication.packet.data.abstractPacketData.DATABYTEADDRESSBASE;
import com.server.communication.packet.data.locationPacketData.DATABYTEADDRESS;

public class locationPacket extends abstractPacket {

	public static final int size = 76;

	public locationPacket(DatagramPacket packet) {
		super(packet);
		parse();
	}

	private void parse() {
		try {
			locationPacketData data = new locationPacketData();
			byte[] bytepacketData = dpPacket.getData();
			data.setAnchorId(getIntValueFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORID.getValue()));
			data.setAnchorChipId(getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORCHIPID.getValue()));
			data.setAnchorBluetoothId(getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORBLUETOOTHID.getValue()));
			data.setAnchorMacAddress(getMacFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORMACADDRESS.getValue()));
			data.setTagId(getIntValueFromPacket(bytepacketData, DATABYTEADDRESS.TAGID.getValue()));
			data.setTagChipId(getIdFromPacket(bytepacketData, DATABYTEADDRESS.TAGCHIPID.getValue()));
			data.setTagBluetoothId(getIdFromPacket(bytepacketData, DATABYTEADDRESS.TAGBLUETOOTHID.getValue()));
			data.setDistance(getFloatValueFromPacket(bytepacketData, DATABYTEADDRESS.DISTANCE.getValue()));

			this.packet.data = data;
			created = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public locationPacketData getData() {
		locationPacketData returnValue = null;
		if (created) {
			returnValue = (locationPacketData) packet.data;
		}
		return returnValue;
	}
}
