package com.server.db.system;

import java.net.InetAddress;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

import com.server.exampleServer;
import com.server.communication.packet.heartbeatPacket;
import com.server.communication.packet.registrationPacket;
import com.server.db.connector.postgresConnector;
import com.server.location.anchorInfo;
import com.server.location.locationInfo;
import com.server.utils.logger;

public class dbLocationProcess {

	postgresConnector conn;

	public dbLocationProcess() {
		conn = new postgresConnector();
	}

	public boolean updateLocation(Long time, Integer anchorid, String anchorchipid, String anchorbtoothid,
			String anchormacadr, String anchoripadr, Integer tagid, String tagchipid, String tagbtoothid,
			Float distance) {
		if (time != null && anchorid != null && anchorchipid != null && anchorbtoothid != null && anchormacadr != null
				&& anchoripadr != null && tagid != null && tagchipid != null && tagbtoothid != null
				&& distance != null) {
			try {
				String SQL = "INSERT INTO location(time,anchorid,anchorchipid,anchorbtoothid,anchormacadr,anchoripadr,tagid,tagchipid,tagbtoothid,distance) " //
						+ "VALUES(?,?,?,?,?,?,?,?,?,?) " //
						+ "ON CONFLICT (anchorchipid,tagchipid) " //
						+ "DO UPDATE SET distance = EXCLUDED.distance, " //
						+ "time = EXCLUDED.time;";

				conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

				conn.setLong(time);
				conn.setInt(anchorid);
				conn.setString(anchorchipid);
				conn.setString(anchorbtoothid);
				conn.setString(anchormacadr);
				conn.setString(anchoripadr);
				conn.setInt(tagid);
				conn.setString(tagchipid);
				conn.setString(tagbtoothid);
				conn.setFloat(distance);

//			System.out.println(pstmt.toString());

				int affectedRows = conn.executeUpdate();
				if (affectedRows > 0) {
					ResultSet rs = conn.getGeneratedKeys();
					if (rs.next()) {
						Long id = rs.getLong(1);
					}
					return true;
				}
			} catch (Exception e) {
				logger.ErrorLogger(postgresConnector.class, e);
			}
		}
		return false;
	}

	public boolean addLocation(Long time, Integer anchorid, String anchorchipid, Integer anchorbtoothid,
			String anchormacadr, String anchoripadr, Integer tagid, String tagchipid, Integer tagbtoothid,
			Float distance) {
		try {
			String SQL = "INSERT INTO location(time,anchorid,anchorchipid,anchorbtoothid,anchormacadr,anchoripadr,tagid,tagchipid,tagbtoothid,distance) " //
					+ "VALUES(?,?,?,?,?,?,?,?,?,?);";

			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			conn.setLong(time);
			conn.setInt(anchorid);
			conn.setString(anchorchipid);
			conn.setInt(anchorbtoothid);
			conn.setString(anchormacadr);
			conn.setString(anchoripadr);
			conn.setInt(tagid);
			conn.setString(tagchipid);
			conn.setInt(tagbtoothid);
			conn.setFloat(distance);

			logger.DebugLogger(exampleServer.class, "bbaServer.main(...).new Thread() {...}.run()");
//			System.out.println(pstmt.toString());

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
				}
				return true;
			}
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return false;
	}

	public boolean addRegistration(Integer anchorid, String anchorchipid, String anchorbtoothid, String anchormacadr,
			String anchoripadr, String anchorWifiversion, String anchorUWBMCUversion) {
		try {
			String SQL = "INSERT INTO onlinedevices(anchorid,anchorchipid,anchorbtoothid,anchormacadr,anchoripadr,anchorWifiversion,anchorUWBMCUversion,isonline,lasttimeonline) " //
					+ "VALUES(?,?,?,?,?,?,?,?,?) " //
					+ "ON CONFLICT (anchorchipid) " //
					+ "DO UPDATE SET anchoripadr = EXCLUDED.anchoripadr, "//
					+ "anchorWifiversion = EXCLUDED.anchorWifiversion, "//
					+ "anchorUWBMCUversion = EXCLUDED.anchorUWBMCUversion, "//
					+ "isonline = EXCLUDED.isonline, "//
					+ "lasttimeonline = EXCLUDED.lasttimeonline;";

			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			conn.setInt(anchorid);
			conn.setString(anchorchipid);
			conn.setString(anchorbtoothid);
			conn.setString(anchormacadr);
			conn.setString(anchoripadr);
			conn.setString(anchorWifiversion);
			conn.setString(anchorUWBMCUversion);
			conn.setBoolean(true);
			conn.setTimestamp(new Timestamp(System.currentTimeMillis()));

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
				}
				return true;
			}
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return false;
	}

	public ArrayList<registrationPacket> getRegistrations() {
		ArrayList<registrationPacket> registrationPacket = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM onlinedevices;";
			ResultSet rs = conn.executeQuery(SQL);
			while (rs.next()) {
				int anchorid = rs.getInt("anchorid");
				String anchorchipid = rs.getString("anchorchipid");
				String anchorbtoothid = rs.getString("anchorbtoothid");
				String anchormacadr = rs.getString("anchormacadr");
				String anchoripadr = rs.getString("anchoripadr");
				String anchorWifiversion = rs.getString("anchorWifiversion");
				String anchorUWBMCUversion = rs.getString("anchorUWBMCUversion");
				registrationPacket tmp = new registrationPacket(null);
				tmp = tmp.createPacket(anchorid, anchorchipid, anchorbtoothid, anchormacadr,
						InetAddress.getByName(anchoripadr), anchorWifiversion, anchorUWBMCUversion);
				registrationPacket.add(tmp);
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return registrationPacket;
	}

	public ArrayList<anchorInfo> getAnchors() {
		ArrayList<anchorInfo> anchorList = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM onlinedevices;";
			ResultSet rs = conn.executeQuery(SQL);
			while (rs.next()) {
				int anchorid = rs.getInt("anchorid");
				String anchorchipid = rs.getString("anchorchipid");
				String anchorbtoothid = rs.getString("anchorbtoothid");
				String anchormacadr = rs.getString("anchormacadr");
				String anchoripadr = rs.getString("anchoripadr");
				String anchorWifiversion = rs.getString("anchorWifiversion");
				String anchorUWBMCUversion = rs.getString("anchorUWBMCUversion");
				Integer coordinate_x = rs.getInt("coordinate_x");
				Integer coordinate_y = rs.getInt("coordinate_y");
				Integer coordinate_z = rs.getInt("coordinate_z");
				anchorInfo anchorInfo = new anchorInfo(anchorid, anchorchipid, anchorbtoothid, anchormacadr,
						anchoripadr, anchorWifiversion, anchorUWBMCUversion, coordinate_x, coordinate_y, coordinate_z);
				anchorList.add(anchorInfo);
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return anchorList;
	}

	public float getScale() {
		try {
			String SQL = "SELECT * FROM bbaglobal;";
			ResultSet rs = conn.executeQuery(SQL);
			while (rs.next()) {
				float scale = rs.getFloat("scale");
				return scale;
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return 0;
	}

	public ArrayList<String> getAllTags() {
		ArrayList<String> tagList = new ArrayList<>();
		try {
			String SQL = "SELECT DISTINCT tagchipid FROM location;";
			ResultSet rs = conn.executeQuery(SQL);
			while (rs.next()) {
				String tagchipid = rs.getString("tagchipid");
				tagList.add(tagchipid);
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return tagList;
	}

	public ArrayList<locationInfo> getAllInfoByTagChipId(String tagChipId) {
		ArrayList<locationInfo> locationList = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM location WHERE tagchipid=?;";
			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			conn.setString(tagChipId);

			ResultSet rs = conn.executeQuery();
			while (rs.next()) {
				int anchorid = rs.getInt("anchorid");
				String anchorchipid = rs.getString("anchorchipid");
				String anchorbtoothid = rs.getString("anchorbtoothid");
				String anchormacadr = rs.getString("anchormacadr");
				String anchoripadr = rs.getString("anchoripadr");
				int tagid = rs.getInt("tagid");
				String tagchipid = rs.getString("tagchipid");
				String tagbtoothid = rs.getString("tagbtoothid");
				Double distance = rs.getDouble("distance");
				long time = rs.getLong("time");
				locationInfo locationInfo = new locationInfo(anchorid, anchorchipid, anchorbtoothid, anchormacadr,
						anchoripadr, tagid, tagchipid, tagbtoothid, distance, time);
				locationList.add(locationInfo);
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return locationList;
	}

	public boolean updaTagLocation(long time, String tagChipid, Integer coordinate_x, Integer coordinate_y,
			Integer coordinate_z) {
		try {
			String SQL = "INSERT INTO locations(tagchipid, coordinate_x, coordinate_y, coordinate_z, time) " //
					+ "VALUES(?,?,?,?,?) " //
					+ "ON CONFLICT (tagchipid) " //
					+ "DO UPDATE SET coordinate_x = EXCLUDED.coordinate_x, "//
					+ "coordinate_y = EXCLUDED.coordinate_y, "//
					+ "coordinate_z = EXCLUDED.coordinate_z, "//
					+ "time = EXCLUDED.time;";

			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);

			conn.setString(tagChipid);
			conn.setInt(coordinate_x);
			conn.setInt(coordinate_y);
			conn.setInt(coordinate_z);
			conn.setLong(time);

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
				}
				return true;
			}
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return false;
	}

	public void removeRegistration(heartbeatPacket packet) {
		try {
			String SQL = "DELETE FROM onlinedevices WHERE anchormacadr = ?";
			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			String macAddress = packet.getData().getAnchorMacAddress();
			conn.setString(macAddress);

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
	}

	public void setDeviceOnline(heartbeatPacket packet, boolean isOnline) {
		try {
			String SQL = "UPDATE onlinedevices SET isonline = ? WHERE anchormacadr = ?";

			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			String macAddress = packet.getData().getAnchorMacAddress();
			conn.setBoolean(isOnline);
			conn.setString(macAddress);

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
					rs.close();
				}
			}
			conn.closeStatements();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
	}

	public boolean isDeviceOnline(String ip) {
		String SQL = "SELECT * FROM onlinedevices WHERE anchoripadr = '" + ip + "'";
		boolean isonline = false;
		try {
			ResultSet rs = conn.executeQuery(SQL);
			while (rs.next()) {
				isonline = rs.getBoolean("isonline");
			}
			rs.close();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
		return isonline;
	}

	public void setDeviceOnlineTime(heartbeatPacket packet) {
		try {
			String SQL = "UPDATE onlinedevices SET lasttimeonline = ? WHERE anchormacadr = ?";

			conn.createPrepareStatement(SQL, Statement.RETURN_GENERATED_KEYS);
			conn.setTimestamp(new Timestamp(System.currentTimeMillis()));

			String macAddress = packet.getData().getAnchorMacAddress();
			conn.setString(macAddress);

			int affectedRows = conn.executeUpdate();
			if (affectedRows > 0) {
				ResultSet rs = conn.getGeneratedKeys();
				if (rs.next()) {
					Long id = rs.getLong(1);
					rs.close();
				}
//				return true;
			}
			conn.closeStatements();
		} catch (Exception e) {
			logger.ErrorLogger(postgresConnector.class, e);
		}
	}

	public void closeConnection() {
		conn.disconnect();
	}
}
