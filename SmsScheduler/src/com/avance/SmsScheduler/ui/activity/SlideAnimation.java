package com.avance.SmsScheduler.ui.activity;


import com.avance.SmsScheduler.R;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;


public class SlideAnimation {

	public static void slide_down(Context ctx, View view) {
		Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);
		if (animation != null) {
			animation.reset();
			if (view != null) {
				view.clearAnimation();
				view.startAnimation(animation);
			}
		}
	}

	public static void slide_up(Context ctx, View view) {
		Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);
		if (animation != null) {
			animation.reset();
			if (view != null) {
				view.clearAnimation();
				view.startAnimation(animation);
			}
		}
	}
}
