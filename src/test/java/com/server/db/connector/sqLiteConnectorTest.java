package com.server.db.connector;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.server.communication.packet.abstractPacket.MESSAGECONTENT;
import com.server.db.log.logTable;

class sqLiteConnectorTest {

	private sqLiteConnector connector;
	private static final String TEST_DB_PATH = "test.db";
	private static final String TEST_TABLE_NAME = "test_table";

	@BeforeEach
	public void setUp() {
		connector = new sqLiteConnector();
		connector.execute(TEST_DB_PATH,
				"CREATE TABLE IF NOT EXISTS " + TEST_TABLE_NAME + " (id INTEGER PRIMARY KEY, name TEXT)");
	}

	@AfterEach
	public void tearDown() {
		connector.execute(TEST_DB_PATH, "DROP TABLE IF EXISTS " + TEST_TABLE_NAME);
		File dbFile = new File(TEST_DB_PATH);
		if (dbFile.exists()) {
			dbFile.delete();
		}
	}

	@Test
	public void testConnect() {
		assertTrue(connector.connect(TEST_DB_PATH));
		connector.disconnect();
	}

	@Test
	public void testGetTableSize() {
		connector.execute(TEST_DB_PATH, "INSERT INTO " + TEST_TABLE_NAME + " (name) VALUES ('test')");
		int size = connector.getTableSize(TEST_DB_PATH, TEST_TABLE_NAME);
		assertEquals(1, size);
	}

	@Test
	public void testIsTableExist() {
		assertTrue(connector.isTableExist(TEST_DB_PATH, TEST_TABLE_NAME));
		assertFalse(connector.isTableExist(TEST_DB_PATH, "nonexistent_table"));
	}

	@Test
	public void testExecute() {
		assertTrue(connector.execute(TEST_DB_PATH, "INSERT INTO " + TEST_TABLE_NAME + " (name) VALUES ('test')"));
		int size = connector.getTableSize(TEST_DB_PATH, TEST_TABLE_NAME);
		assertEquals(1, size);
	}

	@Test
	public void testExecuteUpdate() {
		logTable log = new logTable(MESSAGECONTENT.INFORMATION, "TestClass", "Test Message", "127.0.0.1",
				System.currentTimeMillis());

		String createLogTableSQL = "CREATE TABLE IF NOT EXISTS log_table (logType INTEGER, clazz TEXT, message TEXT, ip TEXT, time LONG)";
		connector.execute(TEST_DB_PATH, createLogTableSQL);

		String insertLogSQL = "INSERT INTO log_table (logType, clazz, message, ip, time) VALUES (?, ?, ?, ?, ?)";
		int result = connector.executeUpdate(TEST_DB_PATH, insertLogSQL, log);
		assertEquals(1, result);

		int size = connector.getTableSize(TEST_DB_PATH, "log_table");
		assertEquals(1, size);
	}
}
