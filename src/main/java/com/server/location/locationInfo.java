package com.server.location;

public class locationInfo {
	private int AnchorId;
	private String AnchorChipid;
	private String AnchorBtoothid;
	private String AnchorMacadr;
	private String AnchorIpadr;
	private int TagId;
	private String TagChipid;
	private String TagBtoothid;
	private double Distance;
	private long Time;

	public locationInfo(int ancorid, String ancorchipid, String ancorbtoothid, String ancormacadr, String ancoripadr, Integer tagId, String tagChipid, String tagBtoothid, double distance,
			long time) {
		AnchorId = ancorid;
		AnchorChipid = ancorchipid;
		AnchorBtoothid = ancorbtoothid;
		AnchorMacadr = ancormacadr;
		AnchorIpadr = ancoripadr;
		TagId = tagId;
		TagChipid = tagChipid;
		TagBtoothid = tagBtoothid;
		Distance = distance;
		Time = time;
	}

	public Integer getId() {
		return AnchorId;
	}

	public String getChipId() {
		return AnchorChipid;
	}

	public String getBluetoothId() {
		return AnchorBtoothid;
	}

	public String getMacAddress() {
		return AnchorMacadr;
	}

	public String getIpAddress() {
		return AnchorIpadr;
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

	public Double getDistance() {
		return Distance;
	}

	public Long getTime() {
		return Time;
	}
}
