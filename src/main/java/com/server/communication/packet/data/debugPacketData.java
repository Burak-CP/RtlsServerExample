package com.server.communication.packet.data;

import java.util.Arrays;

public class debugPacketData extends abstractPacketData {

	private Integer messageLength = null;
	private String message = null;

	private static final int messageLengthLength = 4; // (4 byte)

	public enum DATABYTEADDRESS {
		MESSAGELENGTH(DATABYTEADDRESSBASE.NEXTADDRESS.getValue()), //
		MESSAGE(MESSAGELENGTH.getValue() + messageLengthLength);

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

	public debugPacketData() {
		super();
	}

	public String getMessage() {
		return this.message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getMessageLength() {
		return this.messageLength;
	}

	public void setMessageLength(Integer messageLength) {
		this.messageLength = messageLength;
	}

	public debugPacketData getData() {
		return this;
	}
}
