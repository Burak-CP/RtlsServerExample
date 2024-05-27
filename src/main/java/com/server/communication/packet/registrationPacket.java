package com.server.communication.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.Arrays;

import com.server.communication.packet.data.abstractPacketData.DATABYTEADDRESSBASE;
import com.server.communication.packet.data.registrationPacketData;
import com.server.communication.packet.data.registrationPacketData.DATABYTEADDRESS;
import com.server.utils.logger;

public class registrationPacket extends abstractPacket {
	public static final int size = 4096;

	public registrationPacket(DatagramPacket packet) {
		super(packet);
		if (packet != null) {
			parse();
		}
	}

	public registrationPacket createPacket(Integer anchorId, String anchorChipId, String anchorBluetoothId,
			String anchorMacAddress, InetAddress address, String anchorWifiVersion, String anchorUWBMCUVersion) {
		this.packet.data = new registrationPacketData();
		try {
			this.packet.data.setAnchorId(anchorId);
			this.packet.data.setAnchorChipId(anchorChipId);
			this.packet.data.setAnchorBluetoothId(anchorBluetoothId);
			this.packet.data.setAnchorMacAddress(anchorMacAddress);
			this.setIpAddress(address);
			((registrationPacketData) this.packet.data).setAnchorWifiVersion(anchorWifiVersion);
			((registrationPacketData) this.packet.data).setAnchorUWBMCUVersion(anchorUWBMCUVersion);
		} catch (Exception e) {
			logger.ErrorLogger(registrationPacket.class, e);
		}
		return this;
	}

	private void parse() {
		try {
			registrationPacketData data = new registrationPacketData();
			byte[] bytepacketData = dpPacket.getData();
			data.setAnchorId(getIntValueFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORID.getValue()));
			data.setAnchorChipId(getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORCHIPID.getValue()));
			data.setAnchorBluetoothId(
					getIdFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORBLUETOOTHID.getValue()));
			data.setAnchorMacAddress(getMacFromPacket(bytepacketData, DATABYTEADDRESSBASE.ANCHORMACADDRESS.getValue()));

			int len = getIntValueFromPacket(bytepacketData, DATABYTEADDRESS.VERSIONLENGTH.getValue());
			int startaddress = DATABYTEADDRESS.VERSIONLENGTH.getValue() + 4;
			data.setAnchorWifiVersion(getversionFromPacket(bytepacketData, startaddress, len - 1));

			int nextAddress = startaddress + len;
			len = getIntValueFromPacket(bytepacketData, nextAddress);
			startaddress = nextAddress + 4;
			data.setAnchorUWBMCUVersion(getversionFromPacket(bytepacketData, startaddress, len - 1));

			packet.data = data;
			created = true;
		} catch (Exception e) {
			logger.ErrorLogger(registrationPacket.class, e);
		}
	}

	@Override
	public registrationPacketData getData() {
		registrationPacketData returnValue = null;
		returnValue = (registrationPacketData) packet.data;
		return returnValue;
	}

	public String getversionFromPacket(byte[] packet, int byteAddress, int length) {
		String value = "";
		int endAddress = byteAddress + length;
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, endAddress);
		value = byteArrayToString(slice);
		return value;
	}
}
