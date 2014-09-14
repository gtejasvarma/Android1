package com.avance.SmsScheduler.utils;

import com.avance.SmsScheduler.model.DateModel;
import com.avance.SmsScheduler.model.InputDataModel;
import com.avance.SmsScheduler.model.TimeModel;

public class ValidationUtils {

	public static boolean isValid(InputDataModel inputDataModel){
		String contactId = inputDataModel.getContactId();
		DateModel date = inputDataModel.getDate();
		TimeModel time = inputDataModel.getTime();
		boolean chkBox = inputDataModel.isRepeat();
		long interval = inputDataModel.getInterval();
		long noOfTimes = inputDataModel.getNoOfTimes();
		int eventType = inputDataModel.getEventType();
		String sms = inputDataModel.getSmsTxt();
		
		boolean basicValidation = isNotNull(contactId) && isNotNull(date) && isNotNull(time);
		boolean checkBoxValidation = true;
		if(chkBox){
			checkBoxValidation = (interval != 0) && (noOfTimes != 0);
		}
		boolean smsValidation = true;
		if(eventType == EventTypeConstants.EVENT_MSG){
			smsValidation = isNotNull(sms);
		}
		return basicValidation && checkBoxValidation && smsValidation;
	}

	public static String getValidationError(InputDataModel inputDataModel){
		StringBuilder builder = new StringBuilder();
		
		if(!isNotNull(inputDataModel.getContactId())) append(builder,"Choose Contact");
		if(!isNotNull(inputDataModel.getDate())) append(builder, "Select Date");
		if(!isNotNull(inputDataModel.getTime())) append(builder,"Select Time");
		
		if(inputDataModel.isRepeat()){
			if(inputDataModel.getInterval() == 0 ) append(builder,"Intervals");
			if(inputDataModel.getNoOfTimes() == 0) append(builder, "No of times");
		}
		
		if(EventTypeConstants.EVENT_MSG == inputDataModel.getEventType()){
			if(!isNotNull(inputDataModel.getSmsTxt())) append(builder, "Enter Sms");
		}
		
		StringBuilder finalString = new StringBuilder();
		if(!builder.toString().isEmpty()){
			finalString.append("Please select ").append(builder.toString());
		}
		return finalString.toString();
	}
	
	private static void append(StringBuilder builder,String string){
		if(!builder.toString().isEmpty()){
			builder.append(" , ");
		}
		builder.append(string);
	}
	
	private static boolean isNotNull(Object string){
		return string != null;
	}
}
