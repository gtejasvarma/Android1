package com.avance.SmsScheduler.model;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TimePicker;

import com.avance.SmsScheduler.model.InputDataModel.InputDataModelBuilder;
import com.avance.SmsScheduler.utils.DateTimeUtils;

public class CustomTimePickerDialog extends DialogFragment implements
		TimePickerDialog.OnTimeSetListener {

	InputDataModelBuilder builder;
	Button button;

	public CustomTimePickerDialog(InputDataModelBuilder builder, Button button) {
		this.builder = builder;
		this.button = button;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CustomTimePickerDialog timePickerDialogListener = new CustomTimePickerDialog(
				builder, button);
		int hour = DateTimeUtils.getHour();
		int minute = DateTimeUtils.getMinute();
		return new TimePickerDialog(getActivity(),
				timePickerDialogListener, hour, minute, false);
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		TimeModel timeModel = new TimeModel(hourOfDay, minute);
		builder.setTime(timeModel);
		button.setText(hourOfDay + ":" + formatMinutes(minute));
	}

	private String formatMinutes(int minute) {
		String displayMinute = String.valueOf(minute);
		if (minute < 10) {
			displayMinute = "0" + displayMinute;
		}
		return displayMinute;
	}
}
