package com.avance.SmsScheduler;

import android.app.Activity;
import android.content.Intent;

public class MainActivity extends Activity{
	protected void onCreate(android.os.Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		Intent i = new Intent(MainActivity.this,DisplaySchedules.class);
		startActivity(i);
	
	};
	
	
}
