package com.server.utils;

import java.nio.ByteBuffer;

public class byteUtils {

	public static byte[] intToByteArray(int number) {
		return ByteBuffer.allocate(4).putInt(number).array();
	}

	public static int getByteValueOpc(int value) {
		if (value < 0 && value >= -128) {
			return value + 256;
		} else {
			return value;
		}
	}

	public static int getWordValueOpc(int value) {
		if (value < 0 && value >= -32768) {
			return value + 65536;
		} else {
			return value;
		}
	}

	public static long getByteAt(byte buffer[], int pos) {
		long result;
		result = (0x0000);
		result <<= 8;
		result += (0x0000);
		result <<= 8;
		result += (0x0000);
		result <<= 8;
		result += buffer[pos] & 0x00FF;
		return result;
	}

	public static byte[] setByteAt(byte buffer[], int pos, int value) {
		buffer[pos] = (byte) (value & 0x00FF);
		return buffer;
	}

	public static int getStringLengthForS7(byte buffer[]) {
		return ((0x0000 << 8) + (buffer[1] & 0x00FF));
	}

	public static String getString(byte buffer[]) {
		String s;
		try {
			s = new String(buffer, "UTF-8");
		} catch (Exception e) {
			s = "";
		}
		return s;
	}

	public static void setString(byte buffer[], String value, int maxlength) {
		byte bytevals[] = new byte[maxlength];
		int strlen = (value.length() <= maxlength) ? value.length() : maxlength;
		try {
			bytevals = value.getBytes("UTF-8");
		} catch (Exception e) {
			buffer[1] = 0;
		}
		buffer[0] = (byte) maxlength;
		buffer[1] = (byte) strlen;
		System.arraycopy(bytevals, 0, buffer, 2, strlen);
	}
}
