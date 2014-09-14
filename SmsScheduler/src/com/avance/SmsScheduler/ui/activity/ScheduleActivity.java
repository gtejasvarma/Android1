package com.avance.SmsScheduler.ui.activity;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.avance.SmsScheduler.DisplaySchedules;
import com.avance.SmsScheduler.domain.ScheduleManager;
import com.avance.SmsScheduler.model.CheckBoxListener;
import com.avance.SmsScheduler.model.CustomDatePickerDialog;
import com.avance.SmsScheduler.model.CustomTimePickerDialog;
import com.avance.SmsScheduler.model.InputDataModel;
import com.avance.SmsScheduler.model.InputDialog;
import com.avance.SmsScheduler.model.ScheduleDO;
import com.avance.SmsScheduler.model.InputDataModel.InputDataModelBuilder;
import com.avance.SmsScheduler.utils.DateTimeUtils;
import com.avance.SmsScheduler.utils.MapperUtils;
import com.avance.SmsScheduler.utils.ValidationUtils;
import com.avance.SmsScheduler.R;

public class ScheduleActivity extends Activity {

	Object checkedItem;
	InputDataModelBuilder inputBuilder = new InputDataModelBuilder();
	Map<Integer, String> values = new HashMap<Integer, String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getActionBar().setHomeButtonEnabled(true);
		drawElements();

	}

	private void drawElements() {

		// getResources
		Resources res = getResources();
		Drawable smallBtn = res.getDrawable(R.drawable.gradient_box);
		Drawable largeBtn = res.getDrawable(R.drawable.contacts);

		Drawable titleColor = res.getDrawable(R.drawable.button);
		getActionBar().setBackgroundDrawable(titleColor);

		// set button properties
		setElementProperties(R.id.contactBtn, largeBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.dateBtn, smallBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.timeBtn, smallBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.intervalBtn, smallBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.noOfTimesBtn, smallBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.smsBtn, largeBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.saveBtn, smallBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.cancelBtn, smallBtn, LinearLayout.VISIBLE);

		// check box
		CheckBox chkBox = getCheckBox(R.id.chkIntervals);
		chkBox.setChecked(false);
		chkBox.setOnClickListener(new CheckBoxListener(chkBox,
				getLinearLayout(R.id.expandLayout), getApplicationContext(),
				inputBuilder));

		// hide repeat buttons by default
		((LinearLayout) findViewById(R.id.expandLayout))
				.setVisibility(View.GONE);

		// to fetch the call/sms option selected by user
		Intent selectedOption = getIntent();
		int option = selectedOption.getIntExtra("option", 1);
		inputBuilder.setEventType(option);
		if (option == 1) {
			// hide sms buttons for call option
			getActionBar().setTitle("Schedule Call");
			chkBox.setText("Repeat (call more than once)");
			((LinearLayout) findViewById(R.id.smsLayout))
					.setVisibility(View.GONE);
		} else {
			getActionBar().setTitle("Schedule SMS");
			chkBox.setText("Repeat (msg more than once)");
			((LinearLayout) findViewById(R.id.smsLayout))
					.setVisibility(View.VISIBLE);
		}

	}

	private void setElementProperties(final int buttonId, Drawable drawable,
			int visibility) {
		Button button = getButton(buttonId);
		// button.setBackground(drawable);
		button.setVisibility(visibility);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// showDialog(buttonId);
				openDialog(buttonId);

			}
		});
	}

	private Button getButton(int buttonId) {
		Button button = (Button) findViewById(buttonId);
		return button;
	}

	private CheckBox getCheckBox(int checkboxId) {
		CheckBox chkBox = (CheckBox) findViewById(checkboxId);
		return chkBox;
	}

	private LinearLayout getLinearLayout(int linearLayoutId) {
		LinearLayout layout = (LinearLayout) findViewById(linearLayoutId);
		return layout;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem menuItem) {
		switch (menuItem.getItemId()) {
		case android.R.id.home:
			startActivityAfterCleanup(DisplaySchedules.class);
			return true;
		}
		return (super.onOptionsItemSelected(menuItem));
	}

	private void startActivityAfterCleanup(Class<?> cls) {
		Intent intent = new Intent(getApplicationContext(), cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	// @Override
	// protected void onPrepareDialog(int id, Dialog dialog) {
	// // TODO Auto-generated method stub
	// switch (id) {
	// case R.id.dateBtn:
	// CustomDatePickerDialog dialogListener = new CustomDatePickerDialog(
	// inputBuilder, getButton(id));
	// int year = DateTimeUtils.getYear();
	// int month = DateTimeUtils.getMonth();
	// int day = DateTimeUtils.getDay();
	// dialog = (Dialog) new DatePickerDialog(this, dialogListener, year,
	// month, day);
	// case R.id.timeBtn:
	// CustomTimePickerDialog timePickerDialogListener = new
	// CustomTimePickerDialog(
	// inputBuilder, getButton(id));
	// int hour = DateTimeUtils.getHour();
	// int minute = DateTimeUtils.getMinute();
	// dialog = (Dialog) new TimePickerDialog(this,
	// timePickerDialogListener, hour, minute, false);
	// }
	// super.onPrepareDialog(id, dialog);
	//


	private Dialog openDialog(int id) {
		InputDialog inputDialog = new InputDialog();
		Dialog dialog;
		switch (id) {
		case R.id.contactBtn:
			getContactDetails();
			break;
		case R.id.dateBtn:
			DialogFragment newFragment = new CustomDatePickerDialog(
					inputBuilder, getButton(id));
			newFragment.show(getFragmentManager(), "datePicker");
			break;
		case R.id.timeBtn:
			DialogFragment newTimeFragment = new CustomTimePickerDialog(
					inputBuilder, getButton(id));
			newTimeFragment.show(getFragmentManager(), "timepicker");
			break;
		case R.id.intervalBtn:
			dialog = inputDialog.getIntervals(ScheduleActivity.this,
					inputBuilder, getButton(id));
			dialog.show();
			break;
		case R.id.noOfTimesBtn:
			dialog = inputDialog.getNoOftimes(ScheduleActivity.this,
					inputBuilder, getButton(id));
			dialog.show();
			break;
		case R.id.smsBtn:
			dialog = inputDialog.getSmsText(ScheduleActivity.this,
					inputBuilder, getButton(id));
			dialog.show();
			break;
		case R.id.saveBtn:
			InputDataModel inputData = inputBuilder.build();

			boolean isValid = ValidationUtils.isValid(inputData);
			if (!isValid) {
				Toast.makeText(getApplicationContext(),
						ValidationUtils.getValidationError(inputData),
						Toast.LENGTH_SHORT).show();
				break;
			}
			ScheduleDO scheduleDO = MapperUtils.getScheduleDO(inputData);

			ScheduleManager.createSchedule(getApplicationContext(), scheduleDO);
			Toast.makeText(getApplicationContext(), "Tap on schedule to view/delete.",
					Toast.LENGTH_LONG).show();

			Intent i1 = new Intent(ScheduleActivity.this,
					DisplaySchedules.class);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i1);

			break;
		case R.id.cancelBtn:
			Intent i = new Intent(ScheduleActivity.this, DisplaySchedules.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			break;
		}
		return null;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		InputDialog inputDialog = new InputDialog();
		switch (id) {
		case R.id.contactBtn:
			getContactDetails();
			break;
		case R.id.dateBtn:
			CustomDatePickerDialog dialogListener = new CustomDatePickerDialog(
					inputBuilder, getButton(id));
			int year = DateTimeUtils.getYear();
			int month = DateTimeUtils.getMonth();
			int day = DateTimeUtils.getDay();
			return new DatePickerDialog(ScheduleActivity.this, dialogListener,
					year, month, day);
		case R.id.timeBtn:
			CustomTimePickerDialog timePickerDialogListener = new CustomTimePickerDialog(
					inputBuilder, getButton(id));
			int hour = DateTimeUtils.getHour();
			int minute = DateTimeUtils.getMinute();
			return new TimePickerDialog(ScheduleActivity.this,
					timePickerDialogListener, hour, minute, false);

		case R.id.intervalBtn:
			return inputDialog.getIntervals(ScheduleActivity.this,
					inputBuilder, getButton(id));

		case R.id.noOfTimesBtn:
			return inputDialog.getNoOftimes(ScheduleActivity.this,
					inputBuilder, getButton(id));
		case R.id.smsBtn:
			return inputDialog.getSmsText(ScheduleActivity.this, inputBuilder,
					getButton(id));
		case R.id.saveBtn:
			InputDataModel inputData = inputBuilder.build();

			boolean isValid = ValidationUtils.isValid(inputData);
			if (!isValid) {
				Toast.makeText(getApplicationContext(),
						ValidationUtils.getValidationError(inputData),
						Toast.LENGTH_SHORT).show();
				break;
			}
			ScheduleDO scheduleDO = MapperUtils.getScheduleDO(inputData);

			ScheduleManager.createSchedule(getApplicationContext(), scheduleDO);
			Toast.makeText(getApplicationContext(), "Tap to view/delete on Schedule.",
					Toast.LENGTH_LONG).show();

			Intent i1 = new Intent(ScheduleActivity.this,
					DisplaySchedules.class);
			i1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i1);

			break;
		case R.id.cancelBtn:
			Intent i = new Intent(ScheduleActivity.this, DisplaySchedules.class);
			i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i);
			break;
		}
		return null;
	}

	private void getContactDetails() {
		Intent pickContactIntent = new Intent(Intent.ACTION_PICK,
				Uri.parse("content://contacts"));
		pickContactIntent.setType(Phone.CONTENT_TYPE);
		startActivityForResult(pickContactIntent, R.id.contactBtn);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == R.id.contactBtn && resultCode == RESULT_OK) {
			String contactId = getContactId(data);
			inputBuilder.setContactId(contactId);
			String contactName = MapperUtils.getContactName(
					getApplicationContext(), contactId);
			getButton(R.id.contactBtn).setText(contactName);
		}
	}

	private String getContactId(Intent data) {
		Uri contactData = data.getData();
		ContentResolver cr = getContentResolver();
		Cursor c = cr.query(contactData, null, null, null, null);
		if (c.moveToFirst()) {
			long id = Long.parseLong(c.getString(c
					.getColumnIndexOrThrow(ContactsContract.Contacts._ID)));
			c.close();
			return String.valueOf(id);
		}
		c.close();
		return null;
	}

}
