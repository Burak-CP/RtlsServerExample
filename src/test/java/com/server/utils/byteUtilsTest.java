package com.server.utils;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

class byteUtilsTest {

	@Test
	public void testIntToByteArray() {
		int number = 123456789;
		byte[] expected = ByteBuffer.allocate(4).putInt(number).array();
		byte[] actual = byteUtils.intToByteArray(number);
		assertArrayEquals(expected, actual);
	}

	@Test
	public void testGetByteValueOpc() {
		assertEquals(255, byteUtils.getByteValueOpc(-1));
		assertEquals(128, byteUtils.getByteValueOpc(-128));
		assertEquals(0, byteUtils.getByteValueOpc(0));
		assertEquals(127, byteUtils.getByteValueOpc(127));
	}

	@Test
	public void testGetWordValueOpc() {
		assertEquals(65535, byteUtils.getWordValueOpc(-1));
		assertEquals(32768, byteUtils.getWordValueOpc(-32768));
		assertEquals(0, byteUtils.getWordValueOpc(0));
		assertEquals(32767, byteUtils.getWordValueOpc(32767));
	}

	@Test
	public void testGetByteAt() {
		byte[] buffer = { 0, 0, 0, 1 };
		assertEquals(1, byteUtils.getByteAt(buffer, 3));
	}

	@Test
	public void testSetByteAt() {
		byte[] buffer = { 0, 0, 0, 0 };
		byte[] expected = { 0, 0, 0, 1 };
		assertArrayEquals(expected, byteUtils.setByteAt(buffer, 3, 1));
	}

	@Test
	public void testGetStringLengthForS7() {
		byte[] buffer = { 0, 5 };
		assertEquals(5, byteUtils.getStringLengthForS7(buffer));
	}

	@Test
	public void testGetString() {
		byte[] buffer = "Hello".getBytes(StandardCharsets.UTF_8);
		assertEquals("Hello", byteUtils.getString(buffer));
	}

	@Test
	public void testSetString() {
		byte[] buffer = new byte[10];
		byteUtils.setString(buffer, "Hello", 10);
		assertEquals(10, buffer[0]);
		assertEquals(5, buffer[1]);
		assertEquals("Hello", new String(buffer, 2, 5, StandardCharsets.UTF_8));
	}
}
