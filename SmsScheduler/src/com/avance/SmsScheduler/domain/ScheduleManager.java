package com.avance.SmsScheduler.domain;

import java.util.ArrayList;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.avance.SmsScheduler.AlarmReceiver;
import com.avance.SmsScheduler.db.DBManager;
import com.avance.SmsScheduler.model.ActiveSchedule;
import com.avance.SmsScheduler.model.ActiveSchedules;
import com.avance.SmsScheduler.model.ScheduleDO;
import com.avance.SmsScheduler.utils.MapperUtils;

public class ScheduleManager {

	public static void createPendingIntent(Context applicationContext, ActiveSchedule activeSchedule){
	Intent intentAlarm = new Intent(applicationContext, AlarmReceiver.class);
		intentAlarm.putExtra("activeSchedule", activeSchedule);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(applicationContext, (int)activeSchedule.getScheduleId(), intentAlarm, 0);
		System.out.println("Schedule Time is:"+activeSchedule.getTime());
		AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService(Context.ALARM_SERVICE);
	
		alarmManager.set(AlarmManager.RTC_WAKEUP, activeSchedule.getTime(), pendingIntent);
	}
	
	public static ActiveSchedule createSchedule(Context context,ScheduleDO inputDataModel){
		long dateTime = inputDataModel.getDateTime();
		inputDataModel = inputDataModel.getInputBuilder().setLastEventTime(dateTime).build();
		inputDataModel = DBManager.getDBManager(context).insertInputModel(inputDataModel);
		ActiveSchedule activeSchedule = MapperUtils.getActiveSchedule(inputDataModel);
		createPendingIntent(context, activeSchedule);
		return activeSchedule;
	}
	
	public static ActiveSchedule getSchedule(Context context, long scheduleId){
		ScheduleDO scheduleDO = DBManager.getDBManager(context).selectInputDataModel(scheduleId);
		return MapperUtils.getActiveSchedule(scheduleDO);
	}
	
	public static ActiveSchedules getAllActiveScheduleEvents(Context context){
		List<ScheduleDO> inputDataModelList = DBManager.getDBManager(context).getAllActiveSchedules();
		List<ActiveSchedule> activeScheduleList = new ArrayList<ActiveSchedule>();
		for(ScheduleDO inputDataModel:inputDataModelList){
			activeScheduleList.add(MapperUtils.getActiveSchedule(inputDataModel));
		}
		return new ActiveSchedules(activeScheduleList);
	}
	
	public static ActiveSchedule scheduleNext(Context context,ActiveSchedule activeSchedule){
		ScheduleDO scheduleDO = DBManager.getDBManager(context).selectInputDataModel(activeSchedule.getScheduleId());
		return scheduleNext(context, scheduleDO);
	}
	
	public static ActiveSchedule scheduleNext(Context context,ScheduleDO inputDataModel){
		
		if(inputDataModel == null) return null;
		
		long noOfTimes = inputDataModel.getNoOfTimes()-1;
		if(noOfTimes <= 0){
			deleteSchedule(context, inputDataModel.getId());
			return null;
		}else{
			long nextEventTime = inputDataModel.getLastEventTime()+inputDataModel.getInterval();
			ScheduleDO inputDataModel3 = inputDataModel.getInputBuilder().setNoOfTimes(noOfTimes).setLastEventTime(nextEventTime).build();
			ScheduleDO inputDataModel2 = DBManager.getDBManager(context).updateInputDataModel(inputDataModel3);
			ActiveSchedule activeSchedule = MapperUtils.getActiveSchedule(inputDataModel2);
			createPendingIntent(context, activeSchedule);
			return activeSchedule;
		}
	}
	
	public static void cancelPendingEvent(Context context,long scheduleId)
	{
		Intent intentAlarm = new Intent(context, AlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, (int) scheduleId, intentAlarm, 0);
		pendingIntent.cancel();
	}

	public static void deleteSchedule(Context context,long scheduleId){
		DBManager.getDBManager(context).deleteInputDataModel(scheduleId);
		cancelPendingEvent(context,scheduleId);
	}
	
	public static int deleteAllSchedules(Context context)
	{
		ActiveSchedules schedule = ScheduleManager.getAllActiveScheduleEvents(context);
		List<ActiveSchedule> list = schedule.getActiveScheduleList();
		for (ActiveSchedule activeSchedule : list) {
			final long scheduleId = activeSchedule.getScheduleId();
			cancelPendingEvent(context, scheduleId);
		}
		int result = DBManager.getDBManager(context).deleteAllFromModel();
		
		return result;
	}
	
}
