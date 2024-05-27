package com.server.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.junit.jupiter.api.Test;

class SimpleDateTest {

	@Test
	public void testFormat() {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		Date date = new Date(1635714000000L); // This is 01.11.2021 00:00:00 in Europe/Istanbul
		String expected = "01.11.2021 00:00:00";

		String formattedDate = simpleDate.format(pattern, date);
		assertEquals(expected, formattedDate, "The formatted date should match the expected value.");
	}

	@Test
	public void testGetCurrentTime() {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		String currentTime = simpleDate.getCurrentTime(pattern);
		assertNotNull(currentTime, "The current time should not be null.");

		// Verify that the current time matches the expected pattern
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setTimeZone(TimeZone.getDefault());
		try {
			sdf.parse(currentTime);
		} catch (Exception e) {
			throw new AssertionError("The current time format is invalid: " + currentTime);
		}
	}

	@Test
	public void testConvertTimestampToString() {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		long timestamp = 1635714000000L; // This is 01.11.2021 00:00:00 in Europe/Istanbul
		String expected = "01.11.2021 00:00:00";

		String formattedDate = simpleDate.converttimestampToString(pattern, timestamp);
		assertEquals(expected, formattedDate, "The converted timestamp should match the expected value.");
	}
}
