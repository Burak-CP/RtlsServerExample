package com.server.location;

public class anchorInfo {

	private Integer Id;
	private String Chipid;
	private String Btoothid;
	private String Macadr;
	private String Ipadr;
	private String Wifiversion;
	private String UWBMCUversion;
	private Float Coordinate_x;
	private Float Coordinate_y;
	private Float Coordinate_z;

	public anchorInfo(int anchorid, String anchorchipid, String anchorbtoothid, String anchormacadr, String anchoripadr,
			String anchorWifiversion, String anchorUWBMUCversion, float coordinate_x, float coordinate_y,
			float coordinate_z) {
		Id = anchorid;
		Chipid = anchorchipid;
		Btoothid = anchorbtoothid;
		Macadr = anchormacadr;
		Ipadr = anchoripadr;
		Wifiversion = anchorWifiversion;
		UWBMCUversion = anchorUWBMUCversion;
		Coordinate_x = coordinate_x;
		Coordinate_y = coordinate_y;
		Coordinate_z = coordinate_z;
	}

	public Integer getId() {
		return Id;
	}

	public String getChipId() {
		return Chipid;
	}

	public String getBluetoothId() {
		return Btoothid;
	}

	public String getMacAddress() {
		return Macadr;
	}

	public String getIpAddress() {
		return Ipadr;
	}

	public String getWifiVersion() {
		return Wifiversion;
	}

	public String getUWBMCUVersion() {
		return UWBMCUversion;
	}

	public Float getXCoordinate() {
		return Coordinate_x;
	}

	public Float getYCoordinate() {
		return Coordinate_y;
	}

	public Float getZCoordinate() {
		return Coordinate_z;
	}
}
