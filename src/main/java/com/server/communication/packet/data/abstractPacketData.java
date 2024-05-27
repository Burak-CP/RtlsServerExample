package com.server.communication.packet.data;

import java.util.Arrays;

public abstract class abstractPacketData {

	protected Integer anchor_id = null; // (4 byte)
	protected String anchor_chip_id = null; // (16 byte)
	protected String anchor_btooth_id = null; // (16 byte)
	protected String anchor_mac_adr = null; // (17 byte)

	private static final int packetStartAddress = 17;
	private static final int anchorIdLength = 4;
	private static final int anchorChipLength = 17;
	private static final int anchorBChipLength = 17;
	private static final int anchorMacLength = 18;

	private static final int anchorChipAddress = packetStartAddress + anchorIdLength;
	private static final int anchorBChipAddress = anchorChipAddress + anchorChipLength;
	private static final int anchorMacAddress = anchorBChipAddress + anchorBChipLength;

	protected static final int nextAddress = packetStartAddress + anchorIdLength + anchorChipLength + anchorBChipLength + anchorMacLength;

	public enum DATABYTEADDRESSBASE {
		ANCHORID(packetStartAddress), //
		ANCHORCHIPID(anchorChipAddress), //
		ANCHORBLUETOOTHID(anchorBChipAddress), //
		ANCHORMACADDRESS(anchorMacAddress), // 55
		NEXTADDRESS(nextAddress);

		private final int value;

		private DATABYTEADDRESSBASE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String[] getNames() {
			return Arrays.toString(DATABYTEADDRESSBASE.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
		}
	}

	public abstractPacketData() {

	}

	public Integer getAnchorId() {
		return anchor_id;
	}

	public String getAnchorChipId() {
		return anchor_chip_id;
	}

	public String getAnchorBluetoothId() {
		return anchor_btooth_id;
	}

	public String getAnchorMacAddress() {
		return anchor_mac_adr;
	}

	public void setAnchorId(int id) {
		anchor_id = id;
	}

	public void setAnchorChipId(String id) {
		anchor_chip_id = id;
	}

	public void setAnchorBluetoothId(String id) {
		anchor_btooth_id = id;
	}

	public void setAnchorMacAddress(String mac) {
		anchor_mac_adr = mac;
	}
}
