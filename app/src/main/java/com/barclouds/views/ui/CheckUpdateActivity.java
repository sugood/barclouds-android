package com.barclouds.views.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.barclouds.R;

public class CheckUpdateActivity extends BaseActivity implements OnClickListener {
	private Button button_cancelupdate, button_confirmupdate;

	protected void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(1);
		super.onCreate(savedInstanceState);
	}

	void init() {
		setContentView(R.layout.checkupdateactivity);
		button_cancelupdate = (Button) findViewById(R.id.button_cancelupdate);
		button_confirmupdate = (Button) findViewById(R.id.button_confirmupdate);
		
		button_cancelupdate.setOnClickListener(this);
		button_confirmupdate.setOnClickListener(this);
		
		
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_cancelupdate:
			finish();
			break;
		case R.id.button_confirmupdate:
			Toast.makeText(this, "已更新至最新版本", Toast.LENGTH_SHORT).show();
			finish();
			break;
		default:
			break;
		}
		
	}

}
