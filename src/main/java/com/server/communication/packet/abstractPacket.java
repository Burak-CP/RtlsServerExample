package com.server.communication.packet;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.zip.CRC32;

import com.server.communication.packet.data.abstractPacketData;
import com.server.utils.logger;

public abstract class abstractPacket {

	protected DatagramPacket dpPacket = null;
	protected InetAddress address = null;
	protected Integer port = -1;
	protected PACKET packet = null;
	protected final char beginChar = '#';
	protected final char endChar = '#';
	protected boolean created = false;
	protected Integer CHECKSUMBYTEADDRESS = -1;
	protected Integer ENDCHARBYTEADDRESS = -1;

	public enum MESSAGESOURCES {
		d_SERVER, //
		s_UWBMCU, //
		s_WIFI, //
		NONE;

		public static MESSAGESOURCES fromInteger(int x) {
			switch (x) {
			case 0:
				return d_SERVER;
			case 1:
				return s_UWBMCU;
			case 2:
				return s_WIFI;
			case 3:
				return NONE;
			}
			return null;
		}
	}

	public enum MESSAGECONTENT {
		NONE(0), //
		LOCATION(1), //
		INFORMATION(2), //
		REGISTRATION(3), //
		FIRMWARE(4), //
		DEBUG(5), //
		ERROR(6), //
		WARNING(7);

		private final int value;

		private MESSAGECONTENT(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public static MESSAGECONTENT fromInteger(int x) {
			switch (x) {
			case 0:
				return LOCATION;
			case 1:
				return INFORMATION;
			case 2:
				return REGISTRATION;
			case 3:
				return FIRMWARE;
			case 4:
				return DEBUG;
			case 5:
				return ERROR;
			case 6:
				return WARNING;
			case 7:
				return NONE;
			}
			return null;
		}
	}

	public enum BYTEADDRESSBASE {
		BEGINCHAR(0), //
		SOURCE(1), //
		DESTINATION(5), //
		CONTENT(9), //
		DATALENGTH(13), //
		DATA(17); //

		private final int value;

		private BYTEADDRESSBASE(int value) {
			this.value = value;
		}

		public int getValue() {
			return value;
		}

		public String[] getNames() {
			return Arrays.toString(BYTEADDRESSBASE.class.getEnumConstants()).replaceAll("^.|.$", "").split(", ");
		}
	}

	protected class PACKET { // (73 byte) + (2 byte)
		MESSAGESOURCES source; // Source device name as SOURCES (4 byte)
		MESSAGESOURCES destination; // destination device name as SOURCES (4 byte)
		MESSAGECONTENT content; // (4 byte)
		int data_length; // (4 byte)
		abstractPacketData data; // (x byte)
		long checksum; // (4 byte)
	}

	public abstractPacket(DatagramPacket packet) {
		this.dpPacket = packet;
		this.packet = new PACKET();
		parseBaseInfos();
	}

	protected void parseBaseInfos() {
		if (dpPacket != null) {
			this.address = this.dpPacket.getAddress();
			this.port = this.dpPacket.getPort();
			byte[] bytepacketData = this.dpPacket.getData();

			packet.data_length = getIntValueFromPacket(bytepacketData, BYTEADDRESSBASE.DATALENGTH.getValue());
			this.CHECKSUMBYTEADDRESS = BYTEADDRESSBASE.DATA.getValue() + packet.data_length;
			this.ENDCHARBYTEADDRESS = this.CHECKSUMBYTEADDRESS + 4;
			if (checkBeginChar(bytepacketData) && checkEndChar(bytepacketData)) {

				this.packet.checksum = getUlongValueFromPacket(bytepacketData, this.CHECKSUMBYTEADDRESS);
				if (checksumConfirmation(bytepacketData, packet.checksum)) {

					int ssource = getIntValueFromPacket(bytepacketData, BYTEADDRESSBASE.SOURCE.getValue());
					packet.source = MESSAGESOURCES.fromInteger(ssource);

					int sdestination = getIntValueFromPacket(bytepacketData, BYTEADDRESSBASE.DESTINATION.getValue());
					packet.destination = MESSAGESOURCES.fromInteger(sdestination);

					int scontent = getIntValueFromPacket(bytepacketData, BYTEADDRESSBASE.CONTENT.getValue());
					packet.content = MESSAGECONTENT.fromInteger(scontent);
					this.created = true;
				}
			}
		}
	}

	protected Float getFloatValueFromPacket(byte[] packet, int byteAddress) {
		int value = -1;
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + 4);
		float f = ByteBuffer.wrap(slice).order(ByteOrder.LITTLE_ENDIAN).getFloat();
		return f;
	}

	protected Integer getIntValueFromPacket(byte[] packet, int byteAddress) {
		Integer value = -1;
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + 4);
		value = byteArrayToInteger(slice);
		return value;
	}

	protected boolean checksumConfirmation(byte[] data, long checksum) {
		try {
			byte[] slice = Arrays.copyOfRange(data, BYTEADDRESSBASE.SOURCE.getValue(), this.CHECKSUMBYTEADDRESS);
			CRC32 crc32 = new CRC32();
			crc32.update(slice, 0, slice.length);
			long crc = crc32.getValue();
			if (crc == checksum) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected int byteArrayToInteger(byte[] bytes) {
		return ByteBuffer.wrap(bytes).getInt();
	}

	protected long getUlongValueFromPacket(byte[] packet, int byteAddress) {
		long value = -1;
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + 4);
		value = byteArrayToInteger(slice) & 0xFFFFFFFFL;
		return value;
	}

	protected boolean checkBeginChar(byte[] packet) {
		try {
			char beginchar = (char) packet[BYTEADDRESSBASE.BEGINCHAR.getValue()];
			if (beginchar == this.beginChar) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	protected boolean checkEndChar(byte[] packet) {
		try {
			char endchar = (char) packet[this.ENDCHARBYTEADDRESS];
			if (endchar == this.endChar) {
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean isPrepared() {
		return created;
	}

	public String getMacFromPacket(byte[] packet, int byteAddress) {
		String value = "";
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + 17);
		value = byteArrayToString(slice);
		return value;
	}

	public String getIdFromPacket(byte[] packet, int byteAddress) {
		String value = "";
		byte[] slice = Arrays.copyOfRange(packet, byteAddress, byteAddress + 16);
		value = byteArrayToString(slice);
		return value;
	}

	protected String byteArrayToString(byte[] bytes) {
		String returnValue = null;
		if (bytes.length > 10000) {
			return returnValue;
		}
		try {
			returnValue = new String(bytes, StandardCharsets.UTF_8);
		} catch (Exception e) {
			logger.ErrorLogger(abstractPacket.class, e);
		}
		return returnValue;
	}

	public Integer getDataLength() {
		Integer returnValue = -1;
		if (created) {
			returnValue = packet.data_length;
		}
		return returnValue;
	}

	public PACKET getPacket() {
		return this.packet;
	}

	public MESSAGECONTENT getContent() {
		return this.packet.content;
	}

	public InetAddress getIpAddress() {
		return this.address;
	}

	public void setIpAddress(InetAddress address) {
		this.address = address;
	}

	public MESSAGESOURCES getSource() {
		MESSAGESOURCES returnValue = MESSAGESOURCES.NONE;
		if (created) {
			returnValue = packet.source;
		}
		return returnValue;
	}

	public MESSAGESOURCES getDestination() {
		MESSAGESOURCES returnValue = MESSAGESOURCES.NONE;
		if (created) {
			returnValue = packet.destination;
		}
		return returnValue;
	}

	public abstractPacketData getData() {
		abstractPacketData returnValue = null;
		try {
			returnValue = packet.data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	private byte[] getByteRangeFromPacket(byte[] data, int beginByte, int endByte) {
		return Arrays.copyOfRange(data, beginByte, endByte);
	}
}
