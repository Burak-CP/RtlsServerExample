package com.server.communication.packet.data;

import java.util.Arrays;

public class registrationPacketData extends abstractPacketData {

	String anchorWifiVersion; // (4 byte)
	String anchorUWBMCUVersion; // (4 byte)

	public enum DATABYTEADDRESS {
		VERSIONLENGTH(nextAddress);

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

	public registrationPacketData() {
		super();
	}

	public void setAnchorWifiVersion(String version) {
		this.anchorWifiVersion = version;
	}

	public void setAnchorUWBMCUVersion(String version) {
		this.anchorUWBMCUVersion = version;
	}

	public String getAnchorWifiVersion() {
		return anchorWifiVersion;
	}

	public String getAnchorUWBMCUVersion() {
		return anchorUWBMCUVersion;
	}
}
