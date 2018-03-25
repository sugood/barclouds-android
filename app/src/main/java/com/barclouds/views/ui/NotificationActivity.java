package com.barclouds.views.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

import com.barclouds.R;

public class NotificationActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_activity);
	}
	

}
