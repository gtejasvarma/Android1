package com.avance.SmsScheduler.model;

import java.util.List;

/**
 * @author tgadiraju
 * used to display active schedule events
 */
public class ActiveSchedules {

	private List<ActiveSchedule> activeScheduleList;

	public ActiveSchedules(List<ActiveSchedule> activeScheduleList){
		this.activeScheduleList = activeScheduleList;
	}
	
	public List<ActiveSchedule> getActiveScheduleList() {
		return activeScheduleList;
	}

	public void setActiveScheduleList(List<ActiveSchedule> activeScheduleList) {
		this.activeScheduleList = activeScheduleList;
	}
	
}
