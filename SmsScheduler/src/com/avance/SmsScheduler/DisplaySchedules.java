package com.avance.SmsScheduler;

import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.avance.SmsScheduler.domain.ScheduleManager;
import com.avance.SmsScheduler.model.ActiveSchedule;
import com.avance.SmsScheduler.model.ActiveSchedules;
import com.avance.SmsScheduler.ui.activity.ScheduleActivity;
import com.avance.SmsScheduler.utils.EventTypeConstants;
import com.avance.SmsScheduler.utils.MapperUtils;

public class DisplaySchedules extends PreferenceActivity {

	PreferenceCategory preferenceCategory;
	Object checkedItem;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.fragment_display_schedules);
		addPreferencesFromResource(R.xml.settings);
		onResume();
		setTitleBdgColor();

		LinearLayout mLayout = (LinearLayout) findViewById(R.id.footerLayout);
		final Button calButton = (Button) mLayout.getChildAt(0);
		setElementProperties(calButton, 0);
		final Button smsButton = (Button) mLayout.getChildAt(2);

		setElementProperties(smsButton, 2);
		final Button settingsButton = (Button) mLayout.getChildAt(4);
		setElementProperties(settingsButton, 4);
		
	}
	
	private void setElementProperties(final Button button, final int id) {
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				openDialog(id);
			}
		});
	}

	
	private void openDialog(int id) {
		switch (id) {
		case 0:
			Intent callIntent = new Intent(DisplaySchedules.this,
					ScheduleActivity.class);
			callIntent.putExtra("option", 1);
			callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(callIntent);
			break;
		case 2:
			Intent smsIntent = new Intent(DisplaySchedules.this,
					ScheduleActivity.class);
			smsIntent.putExtra("option", 2);
			smsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(smsIntent);
			break;
		case 4:
			openSettings();
			break;
		}
		
	}

	private void openContactUs() {
		final Dialog dialog = new Dialog(this);
		dialog.setTitle("Enter Feedback");
		dialog.setContentView(R.layout.smslayout);
		final Button btnSet = (Button) dialog.findViewById(R.id.btnSet);
		btnSet.setEnabled(false);
		btnSet.setText("Send");
		final EditText smsText = (EditText) dialog.findViewById(R.id.smsText);

		// To enable button only when text is entered.
		smsText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (smsText.length() > 0) {
					btnSet.setEnabled(true);
				} else {
					btnSet.setEnabled(false);
				}

			}
		});

		btnSet.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				Intent intentEmail = new Intent(Intent.ACTION_SEND);
				intentEmail.putExtra(Intent.EXTRA_EMAIL,
						new String[] { "apps.avance@gmail.com" });
				intentEmail.putExtra(Intent.EXTRA_SUBJECT, "Sub: Feedback");
				intentEmail.putExtra(Intent.EXTRA_TEXT, smsText.getText()
						.toString());
				intentEmail.setType("message/rfc822");
				intentEmail.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				startActivity(Intent.createChooser(intentEmail,
						"Choose an Email client :"));
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void openSettings() {
		final CharSequence[] items = { "Delete All Schedules", "Feedback"};
		checkedItem = "Delete All Schedules";
		AlertDialog.Builder UnitSelection = new AlertDialog.Builder(this);
		UnitSelection.setTitle("Settings");
		UnitSelection.setSingleChoiceItems(items, 0,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						ListView lw = ((AlertDialog) dialog).getListView();
						checkedItem = lw.getAdapter().getItem(
								lw.getCheckedItemPosition());

					}
				});

		UnitSelection.setNeutralButton(android.R.string.ok,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (checkedItem == "Delete All Schedules") {
							int result = ScheduleManager
									.deleteAllSchedules(getApplicationContext());
							onResume();
							if (result > 0) {
								Toast.makeText(getApplicationContext(),
										"All Schedules deleted",
										Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(getApplicationContext(),
										"No Schedules to delete",
										Toast.LENGTH_SHORT).show();
							}
						} else {
							if(checkedItem == "Feedback")
							openContactUs();
						}

						dialog.cancel();
					}
				});
		
		UnitSelection.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		AlertDialog alert = UnitSelection.create();
		alert.show();
	}

	private void setTitleBdgColor() {
		Resources res = getResources();
		Drawable titleColor = res.getDrawable(R.drawable.button);
		getActionBar().setBackgroundDrawable(titleColor);
	}

	@Override
	public void onResume() {
		super.onResume();
		preferenceCategory = (PreferenceCategory) findPreference("cat");
		preferenceCategory.removeAll();
		fillScreen(getApplicationContext());
	}

	private void fillScreen(final Context context) {

		ActiveSchedules schedule = ScheduleManager
				.getAllActiveScheduleEvents(context);
		Resources res=getResources();
		Drawable call = res.getDrawable(R.drawable.call2);
		Drawable sms = res.getDrawable(R.drawable.sms_blue);
	
		List<ActiveSchedule> list = schedule.getActiveScheduleList();
		for (ActiveSchedule activeSchedule : list) {
			final long seqId = activeSchedule.getScheduleId();
			String contactId = activeSchedule.getContactId();
			long dateTime = activeSchedule.getTime();
			final String msg = activeSchedule.getText();
			long type = activeSchedule.getEventType();
			final String contactName = MapperUtils.getContactName(context, contactId);
			final String formattedDate = MapperUtils
					.getCurrentDateTimeFromMilliSeconds(dateTime,
							"dd/MM/yyyy hh:mm");
			final String eventName = EventTypeConstants.getEventName(type);
			Preference p = new Preference(this);
			p.setTitle(contactName);
			p.setSummary("Next " + eventName + " @ " + formattedDate);
			
			if(eventName == EventTypeConstants.getEventName(1))
			{
				p.setIcon(call);
			}
			else
			{
			p.setIcon(sms);	
			}
			
			preferenceCategory.addPreference(p);

			p.setOnPreferenceClickListener(new OnPreferenceClickListener() {

				@Override
				public boolean onPreferenceClick(Preference preference) {
					Dialog dialog;

					AlertDialog.Builder b = new AlertDialog.Builder(
							DisplaySchedules.this);
					b.setTitle(eventName + " Details");
					StringBuilder builder = new StringBuilder();
					
					if(msg == null || msg.isEmpty() ) {
					builder.append("Event:").append("\t").append(eventName).append("\n").append("Contact:").append("\t").append(contactName).append("\n").append("Scheduled time:").append("\t").append(formattedDate);
					}
					else
					{
						builder.append("Event:").append("\t").append(eventName).append("\n").append("Contact:").append("\t").append(contactName).append("\n").append("Scheduled time:")
						.append("\t").append(formattedDate).append("\n").append("Message:").append("\t").append(msg);
					}
					b.setMessage(builder.toString());
					

					b.setPositiveButton("Delete",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {
									ScheduleManager.deleteSchedule(context,
											seqId);
									onResume();
								}
							});

					b.setNegativeButton("OK",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									dialog.dismiss();
								}
							});

					dialog = b.create();
					dialog.show();

					return false;
				}
			});

		}

		addDefaultMessage();

	}

	private void addHomePreferences(Preference p, String summary,
			Drawable icon, String key) {
	//	p.setKey(key);
		p.setIcon(icon);
		p.setTitle(summary);
		preferenceCategory.addPreference(p);
	}

	private void addDefaultMessage() {

		if (preferenceCategory.getPreferenceCount() == 0) {
			preferenceCategory.setTitle("No Active Schedules");
			Resources res = getResources();
			Drawable call = res.getDrawable(R.drawable.call2);
			Drawable sms = res.getDrawable(R.drawable.sms_blue);
			Drawable setting = res.getDrawable(R.drawable.settings2);
			
			IconPreferenceScreen test = (IconPreferenceScreen) findPreference("key1");
			test.setIcon(sms);
			addHomePreferences(test, "Schedule SMS", sms, "sms");
			
			IconPreferenceScreen test1 = (IconPreferenceScreen) findPreference("key2");
			
			test1.setIcon(call);
			addHomePreferences(test1, "Schedule Call", call, "call");
			
			IconPreferenceScreen test2 = (IconPreferenceScreen) findPreference("key3");
			test2.setIcon(setting);
			addHomePreferences(test2, "Settings", setting, "settings");
			
			
//			Preference smsPreference = new Preference(this);
//			addHomePreferences(smsPreference, "Schedule SMS", sms, "sms");
//			
//			smsPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//				
//				@Override
//				public boolean onPreferenceClick(Preference preference) {
//					Intent smsIntent = new Intent(DisplaySchedules.this,
//							ScheduleActivity.class);
//					smsIntent.putExtra("option", 2);
//					smsIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(smsIntent);
//					return false;
//				}
//			});
//		
//			Preference callPreference = new Preference(this);
//			addHomePreferences(callPreference, "Schedule Call", call, "call");
//			
//			callPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//				
//				@Override
//				public boolean onPreferenceClick(Preference preference) {
//					Intent callIntent = new Intent(DisplaySchedules.this,
//							ScheduleActivity.class);
//					callIntent.putExtra("option", 1);
//					callIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					startActivity(callIntent);
//					return false;
//				}
//			});
//			
//			Preference settingPreference = new Preference(this);
//			addHomePreferences(settingPreference, "Settings", setting, "settings");
//			
//			settingPreference.setOnPreferenceClickListener(new OnPreferenceClickListener() {
//				
//				@Override
//				public boolean onPreferenceClick(Preference preference) {
//					openSettings();
//					return false;
//				}
//			});

		} else {

			if (!(preferenceCategory.findPreference("call") == null))
				preferenceCategory.removePreference(preferenceCategory
						.findPreference("call"));
			if (!(preferenceCategory.findPreference("sms") == null))
				preferenceCategory.removePreference(preferenceCategory
						.findPreference("sms"));
			if (!(preferenceCategory.findPreference("settings") == null))
				preferenceCategory.removePreference(preferenceCategory
						.findPreference("settings"));
			preferenceCategory.setTitle("Active Schedules");
		}
	}
}
