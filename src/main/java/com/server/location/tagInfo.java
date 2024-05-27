package com.server.location;

public class tagInfo {

	private long Time;
	private Integer TagId;
	private String TagChipid;
	private String TagBtoothid;
	private Float Coordinate_x;
	private Float Coordinate_y;
	private Float Coordinate_z;

	public tagInfo(long time, int tagid, String tagchipid, String tagbtoothid, float coordinate_x, float coordinate_y, float coordinate_z) {
		Time = time;
		TagId = tagid;
		TagChipid = tagchipid;
		TagBtoothid = tagbtoothid;
		Coordinate_x = coordinate_x;
		Coordinate_y = coordinate_y;
		Coordinate_z = coordinate_z;
	}

	public void setChipId(String value) {
		TagChipid = value;
	}

	public void setBluetoothChipId(String value) {
		TagBtoothid = value;
	}

	public long getTime() {
		return Time;
	}

	public Integer getTagId() {
		return TagId;
	}

	public String getTagChipid() {
		return TagChipid;
	}

	public String getTagBtoothid() {
		return TagBtoothid;
	}

	public Integer getXCoordinate() {
		return (int) Math.round(Coordinate_x);
	}

	public Integer getYCoordinate() {
		return (int) Math.round(Coordinate_y);
	}

	public Integer getZCoordinate() {
		return (int) Math.round(Coordinate_z);
	}

	public Float getXCoordinateDouble() {
		return Coordinate_x;
	}

	public Float getYCoordinateDouble() {
		return Coordinate_y;
	}

	public Float getZCoordinateDouble() {
		return Coordinate_z;
	}
}
