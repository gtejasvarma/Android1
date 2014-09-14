package com.avance.SmsScheduler.utils;

import java.util.Calendar;

public class DateTimeUtils {
	
	
	public static int getYear(){
		return Calendar.getInstance().get(Calendar.YEAR);
	}
	
	public static int getMonth(){
		return Calendar.getInstance().get(Calendar.MONTH);
	}
	
	public static int getDay(){
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static int getHour(){
		return Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
	}
	
	public static int getMinute(){
		return Calendar.getInstance().get(Calendar.MINUTE);
	}
	
}
