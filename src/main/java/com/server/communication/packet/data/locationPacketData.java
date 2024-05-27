package com.server.communication.packet.data;

import java.util.Arrays;

public class locationPacketData extends abstractPacketData {

	protected Integer tagId; // (4 byte)
	protected String tagChipId; // (16 byte)
	protected String tagBluetoothId; // (4 byte) -> (16 byte)
	protected Float distance; // (4 byte)

	private static final int tagIdLength = 4;
	private static final int tagChipLength = 17;
	private static final int tagBChipLength = 17;
	private static final int distanceLength = 17;

	private static final int tagChipAddress = nextAddress + tagIdLength;
	private static final int tagBChipAddress = tagChipAddress + tagChipLength;
	private static final int tagDistanceAddress = tagBChipAddress + tagBChipLength;
	private static final int nextAddress2 = tagDistanceAddress + distanceLength;

	public enum DATABYTEADDRESS {
		TAGID(nextAddress), //
		TAGCHIPID(tagChipAddress), //
		TAGBLUETOOTHID(tagBChipAddress), //
		DISTANCE(tagDistanceAddress), //
		NEXTADDRESS(nextAddress2);
//		CHECKSUM(71), //
//		ENDCHAR(75);

		private final int value;

		private DATABYTEADDRESS(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String[] getNames() {
			return Arrays.toString(DATABYTEADDRESS.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
		}
	}

	public void setTagId(int id) {
		this.tagId = id;
	}

	public void setTagChipId(String id) {
		this.tagChipId = id;
	}

	public void setTagBluetoothId(String id) {
		this.tagBluetoothId = id;
	}

	public void setDistance(Float x) {
		this.distance = x;
	}

	public Integer getTagId() {
		return this.tagId;
	}

	public String getTagChipId() {
		return this.tagChipId;
	}

	public String getTagBluetoothId() {
		return this.tagBluetoothId;
	}

	public Float getDistance() {
		return this.distance;
	}

	public locationPacketData getData() {
		return this;
	}
}
