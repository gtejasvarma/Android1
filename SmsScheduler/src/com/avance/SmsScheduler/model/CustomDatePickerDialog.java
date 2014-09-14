package com.avance.SmsScheduler.model;

import com.avance.SmsScheduler.model.InputDataModel.InputDataModelBuilder;
import com.avance.SmsScheduler.utils.DateTimeUtils;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.Button;
import android.widget.DatePicker;

public class CustomDatePickerDialog extends DialogFragment implements
		DatePickerDialog.OnDateSetListener {

	private InputDataModelBuilder builder;
	private Button button;

	public CustomDatePickerDialog(InputDataModelBuilder builder, Button button) {
		this.builder = builder;
		this.button = button;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		CustomDatePickerDialog dialogListener = new CustomDatePickerDialog(
				builder, button);
		int year = DateTimeUtils.getYear();
		int month = DateTimeUtils.getMonth();
		int day = DateTimeUtils.getDay();
		return new DatePickerDialog(getActivity(), dialogListener, year, month,
				day);
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		DateModel model = new DateModel(year, monthOfYear, dayOfMonth);
		builder.setDateModel(model);
		button.setText((monthOfYear + 1) + "-" + dayOfMonth + "-" + year);
	}

}
