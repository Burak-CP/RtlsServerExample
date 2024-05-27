package com.server.location;

public class locationCalculator {

	public tagInfo calc(String anchor1ChipId, double anchor1TagLength, String anchor2ChipId, double anchor2TagLength,
			String anchor3ChipId, double anchor3TagLength, String anchor4ChipId, double anchor4TagLength, long time) {
		// Calculate location here
		float x = 0;
		float y = 0;
		float z = 0;
		return new tagInfo(time, 1, "", "", x, y, z);

	}
}
