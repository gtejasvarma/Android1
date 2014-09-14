package com.avance.SmsScheduler.utils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import android.app.AlarmManager;
import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.format.DateFormat;

import com.avance.SmsScheduler.model.ActiveSchedule;
import com.avance.SmsScheduler.model.DateModel;
import com.avance.SmsScheduler.model.InputDataModel;
import com.avance.SmsScheduler.model.ScheduleDO;
import com.avance.SmsScheduler.model.TimeModel;

public class MapperUtils {

	public static ActiveSchedule getActiveSchedule(ScheduleDO inputDataModel) {
		ActiveSchedule activeSchedule = new ActiveSchedule();
		
		if(inputDataModel == null ) return activeSchedule;
		
		activeSchedule.setContactId(inputDataModel.getContactId());
		activeSchedule.setScheduleId(inputDataModel.getId());
		activeSchedule
				.setEventType((inputDataModel.getMessage() == null || inputDataModel
						.getMessage().isEmpty()) ? EventTypeConstants.EVENT_CALL
						: EventTypeConstants.EVENT_MSG);
		activeSchedule.setText(inputDataModel.getMessage());
		activeSchedule.setTime(inputDataModel.getLastEventTime());
		return activeSchedule;
	}

	
	//TODO correct this later
	public long getInterval(int id) {
		if (id == 4) {
			int day = Calendar.DAY_OF_WEEK;
			if (day > 1) {
				return AlarmManager.INTERVAL_DAY;
			} else {
				return AlarmManager.INTERVAL_DAY + 2;
			}
		} else {
				getIntervals();
		}
		return id;
	}

	private void getIntervals() {
		Map<Integer, Long> interval = new HashMap<Integer, Long>();
		long value = 1000 * 60;
		interval.put(0, value);
		interval.put(1, AlarmManager.INTERVAL_HALF_HOUR);
		interval.put(2, AlarmManager.INTERVAL_HOUR);
		interval.put(3, AlarmManager.INTERVAL_DAY);
		interval.put(5, AlarmManager.INTERVAL_DAY * 7);
		interval.put(6, AlarmManager.INTERVAL_DAY * 30);
	}

	public static ScheduleDO getScheduleDO(InputDataModel dataModel) {
		Calendar calender = Calendar.getInstance();
		DateModel dateModel = dataModel.getDate();
		TimeModel timeModel = dataModel.getTime();
		
		calender.set(dateModel.getYear(), dateModel.getMonth(),
				dateModel.getDay(), timeModel.getHour(), timeModel.getMinute());
		calender.set(calender.SECOND, 0);
		
		long dateTime = calender.getTimeInMillis();
		
		long interval = dataModel.getInterval();
		TimeModel customTime = dataModel.getCustomTime();
		if (customTime != null) {
			long hours = customTime.getHour() * 60 * 60 * 1000;
			long minutes = customTime.getMinute() * 60 * 1000;
			interval = hours + minutes;
		}

		ScheduleDO scheduleDO = new ScheduleDO.InputBuilder()
				.setContactId(dataModel.getContactId()).setDateTime(dateTime)
				.setInterval(interval).setNoOfTimes(dataModel.getNoOfTimes())
				.setMessage(dataModel.getSmsTxt()).build();

		return scheduleDO;
	}

	public static String getPhoneNumber(Context context, String contactId) {
		return getValueForField(context, contactId,
				ContactsContract.CommonDataKinds.Phone.NUMBER);
	}

	public static String getContactName(Context context, String contactId) {
		return getValueForField(context, contactId,
				ContactsContract.Contacts.DISPLAY_NAME);
	}
	
	public static String getCurrentDateTimeFromMilliSeconds(long dateInMilliseconds,String dateFormat) {
	    return DateFormat.format(dateFormat, dateInMilliseconds).toString();
	}

	private static String getValueForField(Context context, String contactId,
			String fieldName) {
		String value = null;
		String[] whereArgs = new String[] { String.valueOf(contactId) };
		Cursor cursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone._ID + " = ? ",
				whereArgs, null);
		int valueIndex = cursor.getColumnIndexOrThrow(fieldName);
		if (cursor != null) {
			try {
				if (cursor.moveToNext()) {
					value = cursor.getString(valueIndex);
				}
			} finally {
				cursor.close();
			}
		}
		return value;
	}

}
