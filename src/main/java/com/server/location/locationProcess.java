package com.server.location;

import java.util.ArrayList;

import com.server.db.system.dbLocationProcess;
import com.server.utils.logger;

public class locationProcess extends Thread {

	private boolean isRunning = false;
	private long timeInterval = 1000;
	ArrayList<String> Tags;
	locationCalculator calculator;

	public locationProcess() {
		this.setName("Location Processes");
		Tags = new ArrayList<String>();
		dbLocationProcess conn = new dbLocationProcess();
		Tags = conn.getAllTags();
		conn.closeConnection();
		calculator = new locationCalculator();
	}

	@Override
	public void run() {
		try {
			while (isRunning) {
				long proccessBegin = System.currentTimeMillis();

				for (int i = 0; i < Tags.size(); i++) {
					String tagWillCalculate = Tags.get(i);

					ArrayList<locationInfo> locationInfos;
					dbLocationProcess conn = new dbLocationProcess();
					locationInfos = conn.getAllInfoByTagChipId(tagWillCalculate);
					conn.closeConnection();
					long timenow = System.currentTimeMillis();
					for (int j = 0; j < locationInfos.size(); j++) {
						if ((locationInfos.get(j).getTime() < timenow - 2000)
								|| (locationInfos.get(j).getTime() > timenow + 2000)) {
							locationInfos.remove(j);
						}
					}
					tagInfo taginfo = null;
					if (locationInfos.size() > 3) {
						taginfo = calculator.calc(locationInfos.get(0).getChipId(), locationInfos.get(0).getDistance(), //
								locationInfos.get(1).getChipId(), locationInfos.get(1).getDistance(), //
								locationInfos.get(2).getChipId(), locationInfos.get(2).getDistance(), //
								locationInfos.get(3).getChipId(), locationInfos.get(3).getDistance(), //
								locationInfos.get(1).getTime());
					}
					if (taginfo != null) {
						logger.DebugLogger(locationProcess.class,
								"Tag(" + tagWillCalculate + ") coordinate calculated. x: "
										+ taginfo.getXCoordinateDouble() + ", y: " + taginfo.getYCoordinateDouble()
										+ ", z: " + taginfo.getZCoordinateDouble());

						taginfo.setChipId(tagWillCalculate);
						conn = new dbLocationProcess();
						conn.updaTagLocation(taginfo.getTime(), taginfo.getTagChipid(), taginfo.getXCoordinate(),
								taginfo.getYCoordinate(), taginfo.getZCoordinate());
						conn.closeConnection();
					}
				}

				try {
					long waitTime = timeInterval - (System.currentTimeMillis() - proccessBegin);
					waitTime = waitTime >= 0 ? waitTime : 0;
					if (waitTime >= 0) {
						synchronized (this) {
							Thread.sleep(waitTime);
						}
					}
				} catch (Exception e) {
					logger.ErrorLogger(locationProcess.class, e);
				}
			}
		} catch (Exception e) {
			logger.ErrorLogger(locationProcess.class, e);
		}
	}

	public void stopCalculator() {
		isRunning = false;
	}

	public void setPeriod(long time) {
		this.timeInterval = time;
	}
}
