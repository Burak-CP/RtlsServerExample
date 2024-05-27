package com.server.db.connector;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.server.db.log.logTable;
import com.server.utils.fileWriter;
import com.server.utils.logger;

public class sqLiteConnector {

	private Connection conn;
	private Object lock;

	public static String jdbcPrefix = "Jdbc:sqlite:";
	public static String dbFile = "comrecord";
	public static String dbFolder = "records";
	public static String recordTable = "records";

	public sqLiteConnector() {
		try {
			Class.forName("org.sqlite.JDBC");
			lock = new Object();
		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
	}

	protected boolean connect(String path) {
		boolean isConnected = false;
		synchronized (lock) {
			try {
				conn = DriverManager.getConnection(jdbcPrefix + path);
				isConnected = true;
			} catch (Exception e) {
				if (fileWriter.ensureFolder(path.substring(0, path.lastIndexOf('/')))) {
					try {
						conn = DriverManager.getConnection(jdbcPrefix + path);
					} catch (SQLException e1) {
						logger.ErrorLogger(sqLiteConnector.class, e1);
					}
				}
			}
		}
		return isConnected;
	}

	protected void disconnect() {
		try {
			conn.close();
		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
	}

	public Integer getTableSize(String path, String tableName) {
		int count = 0;

		String sql = "SELECT count(*) FROM " + tableName;

		Statement stmt = null;
		ResultSet resultset = null;
		try {
			if (connect(path)) {
				stmt = conn.createStatement();
				resultset = stmt.executeQuery(sql);

				resultset.next();
				count = resultset.getInt(1);

				resultset.close();
				stmt.close();
				disconnect();
			}

		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
		return count;
	}

	public boolean isTableExist(String path, String tableName) {
		boolean isEmpty = false;
		ResultSet resultset = null;
		try {
			if (connect(path)) {
				DatabaseMetaData dmd = conn.getMetaData();
				resultset = dmd.getTables(null, null, tableName, null);
				resultset.next();
				if (resultset.getRow() > 0) {
					resultset.close();
					dmd = null;
					isEmpty = true;
				}
				disconnect();
			}
		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
		return isEmpty;
	}

	public synchronized boolean execute(String path, String sql) {
		boolean result = false;
		try {
			synchronized (lock) {
				if (connect(path)) {
					Statement stmt = conn.createStatement();
					int numColsAffected = stmt.executeUpdate(sql);
					if (numColsAffected > 0) {
						result = true;
					}
					stmt.close();
					disconnect();
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
		return result;
	}

	public synchronized int executeUpdate(String path, String sql, logTable logTable) {
		int result = -1;
		try {
			synchronized (lock) {
				if (connect(path)) {
					PreparedStatement pstmt = conn.prepareStatement(sql);
					pstmt.setInt(1, logTable.getLogType().getValue());
					pstmt.setString(2, logTable.getClazz());
					pstmt.setString(3, logTable.getMessage());
					pstmt.setString(4, logTable.getIp());
					pstmt.setLong(5, logTable.getTime());
					result = pstmt.executeUpdate();
					pstmt.close();
					disconnect();
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(sqLiteConnector.class, e);
		}
		return result;
	}
}
