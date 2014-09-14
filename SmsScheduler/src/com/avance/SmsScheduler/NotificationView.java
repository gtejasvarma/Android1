package com.avance.SmsScheduler;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.avance.SmsScheduler.model.ActiveSchedule;
import com.avance.SmsScheduler.utils.EventTypeConstants;
import com.avance.SmsScheduler.utils.MapperUtils;

public class NotificationView extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification);
		setTitleBdgColor();
		getActionBar().setHomeButtonEnabled(true);
		ActiveSchedule activeSchedule = (ActiveSchedule) getIntent().getExtras().getSerializable("notification");

		populateElements(activeSchedule);

	}

	private void populateElements(ActiveSchedule activeSchedule) {

		String contactId = activeSchedule.getContactId();
		long dateTime = activeSchedule.getTime();
		String sms = activeSchedule.getText();
		String formattedDate = MapperUtils.getCurrentDateTimeFromMilliSeconds(dateTime, "dd/MM/yyyy hh:mm");
		String userName = MapperUtils.getContactName(getApplicationContext(),contactId);
		long type = activeSchedule.getEventType();
		String EventName = EventTypeConstants.getEventName(type);
		
		if(type == EventTypeConstants.EVENT_CALL)
		{
			((LinearLayout) findViewById(R.id.smsLayout))
			.setVisibility(View.GONE);
		}
		else
		{
			((LinearLayout) findViewById(R.id.smsLayout))
			.setVisibility(View.VISIBLE);
		}

		TextView eventTxt = (TextView) findViewById(R.id.eventDataTxt);
		TextView timeTxt = (TextView) findViewById(R.id.lastEventTimeDataTxt);
		TextView contactTxt = (TextView) findViewById(R.id.contactDataTxt);
		TextView smsTxt = (TextView) findViewById(R.id.smsDataTxt);

		eventTxt.setText(EventName);
		timeTxt.setText(formattedDate);
		contactTxt.setText(userName);
		smsTxt.setText(sms);

		setElementProperties(R.id.homeBtn, LinearLayout.VISIBLE);
		setElementProperties(R.id.cancelBtn, LinearLayout.VISIBLE);
	}

	private void setElementProperties(final int buttonId, int visibility) {
		Button button = getButton(buttonId);
		button.setVisibility(visibility);
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				openDialog(buttonId);

			}
		});
	}

	private Button getButton(int buttonId) {
		Button button = (Button) findViewById(buttonId);
		return button;
	}

	private void openDialog(int id) {
		switch (id) {
		case R.id.homeBtn:
			startActivityAfterCleanup(DisplaySchedules.class);
			break;
		case R.id.cancelBtn:
			finish();
			break;
		}
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

	private void setTitleBdgColor() {
		Resources res = getResources();
		Drawable titleColor = res.getDrawable(R.drawable.button);
		getActionBar().setBackgroundDrawable(titleColor);
	}

}