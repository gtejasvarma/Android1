package com.avance.SmsScheduler.model;

public class InputDataModel {

	private String contactId;

	/**
	 * dateTime is used to save start date and time for each schedule
	 */
	private DateModel date;
	
	private TimeModel time;
	
	private TimeModel customTime;
	
	/**
	 * interval will represent how often this schedule will be active
	 */
	private long interval;

	/**
	 * this represent how many times this schedule will be repeated excluding
	 * first schedle
	 */
	private long noOfTimes;
	
	private boolean repeat;

	private String smsTxt;
	
	private int eventType;
	

	private InputDataModelBuilder inputDataModelBuilder;
	
	public InputDataModelBuilder getInputDataModelBuilder() {
		return inputDataModelBuilder;
	}
	
	
	public int getEventType() {
		return eventType;
	}
	
	public String getSmsTxt(){
		return smsTxt;
	}
	
	public String getContactId() {
		return contactId;
	}

	public DateModel getDate() {
		return date;
	}

	public TimeModel getTime() {
		return time;
	}

	public TimeModel getCustomTime() {
		return customTime;
	}

	public long getInterval() {
		return interval;
	}

	public long getNoOfTimes() {
		return noOfTimes;
	}

	public boolean isRepeat() {
		return repeat;
	}
	
	
	public InputDataModel(InputDataModelBuilder builder){
		this.contactId = builder.contactId;
		this.date = builder.date;
		this.time = builder.time;
		this.customTime = builder.customTime;
		this.interval = builder.interval;
		this.noOfTimes = builder.noOfTimes;
		this.repeat = builder.repeat;
		this.smsTxt = builder.smsTxt;
		this.inputDataModelBuilder = builder;
		this.eventType = builder.eventType;
	}
	
	public static class InputDataModelBuilder{
	
		private String contactId;
		private DateModel date;
		private TimeModel time;
		private TimeModel customTime;
		private long interval;
		private long noOfTimes;
		private boolean repeat;
		private String smsTxt;
		private int eventType;
		
		public InputDataModelBuilder setEventType(int eventType){
			this.eventType = eventType;
			return this;
		}
		
		public InputDataModelBuilder setSmsTxt(String smsTxt){
			this.smsTxt = smsTxt;
			return this;
		}
		public InputDataModelBuilder setContactId(String contactId){
			this.contactId = contactId;
			return this;
		}
		
		public InputDataModelBuilder setDateModel(DateModel date){
			this.date = date;
			return this;
		}
		
		public InputDataModelBuilder setTime(TimeModel time){
			this.time = time;
			return this;
		}
		
		public InputDataModelBuilder setCustomTime(TimeModel customTime){
			this.customTime = customTime;
			return this;
		}
		
		public InputDataModelBuilder setInterval(long interval){
			this.interval = interval;
			return this;
		}
		
		public InputDataModelBuilder setNoOfTimes(long noOfTimes){
			this.noOfTimes = noOfTimes;
			return this;
		}
		
		public InputDataModelBuilder setRepeat(boolean repeat){
			this.repeat = repeat;
			return this;
		}
		
		public InputDataModel build(){
			return new InputDataModel(this);
		}
	}
	
}
