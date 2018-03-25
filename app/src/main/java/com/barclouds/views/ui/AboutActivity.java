package com.barclouds.views.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

import com.barclouds.R;

public class AboutActivity extends BaseActivity implements OnClickListener {
	private ImageView imageview_back,imageview_back2;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}
	
	@Override
	void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.about_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_back2 = (ImageView) findViewById(R.id.imageview_back2);
		
		imageview_back.setOnClickListener(this);
		imageview_back2.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.imageview_back2:
			finish();
			break;

		default:
			break;
		}
		
	}
	
}
