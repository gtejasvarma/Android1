package com.avance.SmsScheduler.model;

import com.avance.SmsScheduler.model.InputDataModel.InputDataModelBuilder;
import com.avance.SmsScheduler.ui.activity.SlideAnimation;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class CheckBoxListener implements View.OnClickListener{

	CheckBox checkBox;
	LinearLayout layout;
	Context context;
	InputDataModelBuilder inputDataModel;
	
	public CheckBoxListener(CheckBox checkBox,LinearLayout layout,Context context,InputDataModelBuilder inputDataModel){
		this.checkBox = checkBox;
		this.layout = layout;
		this.context = context;
		this.inputDataModel = inputDataModel;
	}
	
	@Override
	public void onClick(View v) {
		toggle_contents(v);
	}
	
	private void toggle_contents(View v) {
		if (!checkBox.isChecked()) {
			layout.setVisibility(View.GONE);
			SlideAnimation.slide_up(context, layout);
			inputDataModel.setRepeat(false);
		}
		else {
			layout.setVisibility(View.VISIBLE);
			SlideAnimation.slide_down(context, layout);
			inputDataModel.setRepeat(true);
		}

	}

}
