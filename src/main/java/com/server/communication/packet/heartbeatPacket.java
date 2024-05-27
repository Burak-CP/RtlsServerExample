package com.server.communication.packet;

import java.net.DatagramPacket;

import com.server.communication.packet.data.registrationPacketData;

public class heartbeatPacket extends abstractPacket {

	public final String message = "heartbeat!";

	public heartbeatPacket(DatagramPacket packet) {
		super(packet);
	}

	public void create(registrationPacket packet) {
		this.packet.data = new registrationPacketData();
		try {
			this.packet.data.setAnchorId(packet.getData().getAnchorId());
			this.packet.data.setAnchorChipId(packet.getData().getAnchorChipId());
			this.packet.data.setAnchorBluetoothId(packet.getData().getAnchorBluetoothId());
			this.packet.data.setAnchorMacAddress(packet.getData().getAnchorMacAddress());
			this.setIpAddress(packet.getIpAddress());
			((registrationPacketData) this.packet.data).setAnchorWifiVersion(packet.getData().getAnchorWifiVersion());
			((registrationPacketData) this.packet.data)
					.setAnchorUWBMCUVersion(packet.getData().getAnchorUWBMCUVersion());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getMessage() {
		return this.message;
	}
}
