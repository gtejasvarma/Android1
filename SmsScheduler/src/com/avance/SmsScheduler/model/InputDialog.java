package com.avance.SmsScheduler.model;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TimePicker;

import com.avance.SmsScheduler.R;
import com.avance.SmsScheduler.model.InputDataModel.InputDataModelBuilder;
import com.avance.SmsScheduler.utils.DateTimeUtils;
import com.avance.SmsScheduler.utils.EventTypeConstants;

public class InputDialog extends DialogFragment{

	

	private String[] getIntevals() {
		return new String[] { "Every Minute", "Every 30 minutes", "Every hour",
				"Every day", "Every weekday", "Every week", "Every month",
				"Custom Time" };
	}
	
	private String[] getIntevalBtnTxt() {
		return new String[] { "1 Minute", "30 minutes", "hourly",
				"daily", "weekday", "weekly", "monthly",
				"Custom Interval" };
	}
	
	

	private long getInterval(int value) {
		long duration = 0;
		switch (value) {
		case 1:
			duration = 60 * 1000;
			break;
		case 2:
			duration = AlarmManager.INTERVAL_HALF_HOUR;
			break;
		case 3:
			duration = AlarmManager.INTERVAL_HOUR;
			break;
		case 4:
			duration = AlarmManager.INTERVAL_DAY;
			break;
		case 5:
			int day = Calendar.DAY_OF_WEEK;
			if (day > 1) {
				duration = AlarmManager.INTERVAL_DAY;
			} else {
				duration = AlarmManager.INTERVAL_DAY + 2;
			}
			break;
		case 6:
			duration = AlarmManager.INTERVAL_DAY * 7;
			break;
		case 7:
			duration = AlarmManager.INTERVAL_DAY * 30;
			break;
	
		}
		return duration;

	}



	private String getOccurenceValue(int value) {
		return getIntevals()[value - 1];
	}
	

	private void customTime(Context context,final InputDataModelBuilder builder,int hour,int minute,final int value,final Button button) {

	    TimePickerDialog mTimePicker;
	    
	    mTimePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
			
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				// TODO Auto-generated method stub
			
				long hrs = hourOfDay * 60 * 60 * 1000;
				long mins = minute* 60 * 1000;
				long milliseconds = hrs + mins;
				
				builder.setInterval(milliseconds);
				String buttonTxt = hourOfDay+":";
				if(minute<10){
					buttonTxt = buttonTxt+"0"+minute;
				}else{
					buttonTxt = buttonTxt+minute;
				}
				button.setText(buttonTxt);
			}
		}, hour, minute, true);
	    
        mTimePicker.setTitle("Custom Interval");
	    mTimePicker.show();
	}
	

	public Dialog getIntervals(final Context context,
			final InputDataModelBuilder builder, final Button button) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("Choose Interval");
		dialog.setContentView(R.layout.numberpicker);
		Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		final NumberPicker np = (NumberPicker) dialog
				.findViewById(R.id.numberOfTimes);
		np.setMaxValue(8);
		np.setMinValue(1);
		np.setDisplayedValues(getIntevals());
		np.setWrapSelectorWheel(false);
		btnSet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				int value = np.getValue();
				String interval = getOccurenceValue(value);
				if (interval == "Custom Time") {
					int hour = DateTimeUtils.getHour();
					int minute = DateTimeUtils.getMinute();
					customTime(context,builder,hour,minute, value,button);
				 
				}
				
				button.setText(getIntevalBtnTxt()[value-1]);
				if(value != 8) {builder.setInterval(getInterval(value));}
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public Dialog getNoOftimes(Context context,
			final InputDataModelBuilder builder, final Button button) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("No of times");
		dialog.setContentView(R.layout.numberpicker);
		Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		final NumberPicker np = (NumberPicker) dialog
				.findViewById(R.id.numberOfTimes);
		np.setMaxValue(100);
		np.setMinValue(1);
		np.setWrapSelectorWheel(false);
		btnSet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String value = String.valueOf(np.getValue());
				button.setText(value + "time(s)");
				builder.setNoOfTimes(Long.parseLong(value));
				dialog.dismiss();
			}
		});
		return dialog;
	}

	public Dialog getSmsText(Context context,
			final InputDataModelBuilder builder, final Button button) {
		final Dialog dialog = new Dialog(context);
		dialog.setTitle("Enter Message");
		dialog.setContentView(R.layout.smslayout);
		final Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		btnSet.setEnabled(false);
		final EditText smsText = (EditText) dialog.findViewById(R.id.smsText);

		// To enable button only when text is entered.
		smsText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (smsText.length() > 0) {
					btnSet.setEnabled(true);
				} else {
					btnSet.setEnabled(false);
				}

			}
		});

		btnSet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String value = smsText.getText().toString();
				if (value == null || value.isEmpty()) {
					dialog.dismiss();
				} else {
					String displayText = value;
					builder.setSmsTxt(value);
					builder.setEventType(EventTypeConstants.EVENT_MSG);
					if (displayText.length() > 15) {
						displayText = value.substring(0, 15) + "..";
					}
					button.setText(displayText);
					dialog.dismiss();
				}

			}
		});
		return dialog;
	}
}
