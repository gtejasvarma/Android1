package com.avance.SmsScheduler.model;

import java.io.Serializable;

/**
 * 
 * @author sowmya vuddaraju This class saves all the data from user Input. Uses
 *         Builder pattern to construct input
 */
public class ScheduleDO implements Serializable{

	
	private static final long serialVersionUID = -5319407938404672493L;

	@Override
	public String toString() {
		return "ScheduleDO [id=" + id + ", contactId=" + contactId
				+ ", dateTime=" + dateTime + ", interval=" + interval
				+ ", noOfTimes=" + noOfTimes + ", lastEventTime="
				+ lastEventTime + ", message=" + message + ", inputBuilder="
				+ inputBuilder + "]";
	}

	/**
	 * represents each schedule uniquely. 
	 */
	private long id;

	/**
	 * contactId is used to get contact details like phone number and contact
	 * name
	 */
	private String contactId;

	/**
	 * dateTime is used to save start date and time for each schedule
	 */
	private long dateTime;

	/**
	 * interval will represent how often this schedule will be active
	 */
	private long interval;

	/**
	 * this represent how many times this schedule will be repeated excluding
	 * first schedle
	 */
	private long noOfTimes;

	/**
	 * last time this event has happened
	 */
	private long lastEventTime;
	
	/**
	 * represents message that will be sent
	 */
	private String message;

	/**
	 * returns builder instance
	 */
	private transient InputBuilder inputBuilder;
	
	public InputBuilder getInputBuilder(){
		return inputBuilder;
	}
	
	public String getContactId() {
		return contactId;
	}


	public long getDateTime() {
		return dateTime;
	}

	public long getInterval() {
		return interval;
	}


	public long getNoOfTimes() {
		return noOfTimes;
	}


	public String getMessage() {
		return message;
	}


	public long getId() {
		return id;
	}

	public long getLastEventTime() {
		return lastEventTime;
	}

	private ScheduleDO(InputBuilder inputBuilder){
		this.id = inputBuilder.id;
		this.contactId = inputBuilder.contactId;
		this.dateTime = inputBuilder.dateTime;
		this.interval = inputBuilder.interval;
		this.noOfTimes = inputBuilder.noOfTimes;
		this.message = inputBuilder.message;
		this.lastEventTime = inputBuilder.lastEventTime;
		this.inputBuilder = inputBuilder;
	}
	
	public static class InputBuilder {

		private String contactId;
		private long dateTime;
		private long interval;
		private long noOfTimes;
		private String message;
		private long id;
		private long lastEventTime;
		
		public InputBuilder setLastEventTime(long lastEventTime){
			this.lastEventTime = lastEventTime;
			return this;
		}
		
		public InputBuilder setId(long id){
			this.id = id;
			return this;
		}
		
		public InputBuilder setContactId(String contactId){
			this.contactId = contactId;
			return this;
		}
		
		public InputBuilder setDateTime(long dateTime){
			this.dateTime = dateTime;
			return this;
		}
		
		public InputBuilder setInterval(long interval){
			this.interval = interval;
			return this;
		}
		
		public InputBuilder setNoOfTimes(long noOfTimes){
			this.noOfTimes = noOfTimes;
			return this;
		}
		
		public InputBuilder setMessage(String message){
			this.message = message;
			return this;
		}
		
		public ScheduleDO build(){
			return new ScheduleDO(this);
		}
	}

}
