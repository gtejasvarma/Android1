package com.avance.SmsScheduler.utils;

public class EventTypeConstants {

	public static final int EVENT_CALL = 1;
	public static final int EVENT_MSG = 2;

		public static String getEventName(long value)
		{
			if (EVENT_CALL == value)
			{
				return "Call";
			}
			else {
				return "Message";
			}
			
			
		
		}

}
