package com.avance.SmsScheduler.model;

import java.io.Serializable;

/**
 * 
 * @author tgadiraju
 * represents each event in active schedule event list 
 * also passed to broadcast receiver to schedule call and sms
 */
public class ActiveSchedule implements Serializable{

	
	private static final long serialVersionUID = -971435249616901863L;

	private long scheduleId;

	private String contactId;

	private long eventType;

	private long time;

	private String text;
	
	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public long getEventType() {
		return eventType;
	}

	public void setEventType(long eventType) {
		this.eventType = eventType;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}


}
