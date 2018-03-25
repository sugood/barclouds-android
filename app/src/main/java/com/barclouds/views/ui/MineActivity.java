package com.barclouds.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.domain.User;

public class MineActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_mine1;// 通知
	private LinearLayout lL_mine1;//数据总数
	private LinearLayout lL_mine2;//资料总数
    private LinearLayout lL_mine3;//设置
	private TextView textview_username;
    private TextView textViewDataSum;
    private TextView textViewInfoSum;
	private AppContext appContext;
	private User user;
    private long exitTime=0;
    private long dataSum = 0;
    private long infoSum = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		appContext = (AppContext) getApplicationContext();
		user = appContext.getUser();
        if(appContext.getDataSum()!=null)
            dataSum=appContext.getDataSum();
        if(appContext.getInfoSum()!=null)
            infoSum=appContext.getInfoSum();

        Log.d("BarcloudsDebug","MineActivity.onCreate");

		if (user != null) {
			String username = user.getUsername();
			textview_username.setText(username);
		}
        textViewDataSum.setText(""+dataSum);
        textViewInfoSum.setText(""+infoSum);
	}
    public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iV_mine1:// 通知
			Intent intent = new Intent(MineActivity.this,
					NotificationActivity.class);
			startActivity(intent);

			break;
		case R.id.lL_mine1:// 数据总数
//			Intent intent4 = new Intent(MineActivity.this,
//					RecommendedActivity.class);
//			startActivity(intent4);
			break;
		case R.id.lL_mine2://资料总数
//			if (user != null) {
//				Intent intent5 = new Intent(MineActivity.this,
//						ObligationActivity.class);
//				startActivity(intent5);
//			} else {
//				Toast.makeText(this, "亲，请先登录", Toast.LENGTH_SHORT).show();
//			}
			break;
        case R.id.lL_mine3:// 设置
                Intent intent3 = new Intent(MineActivity.this,
                        SettingActivity.class);
                startActivity(intent3);
            break;
		default:
			break;
		}

	}

	@Override
	void init() {
		setContentView(R.layout.mine_activity);
		iv_mine1 = (ImageView) findViewById(R.id.iV_mine1);
        lL_mine1 = (LinearLayout) findViewById(R.id.lL_mine1);
        lL_mine2 = (LinearLayout) findViewById(R.id.lL_mine2);
        lL_mine3 = (LinearLayout) findViewById(R.id.lL_mine3);
        textViewDataSum = (TextView) findViewById(R.id.textViewDataSum);
        textViewInfoSum = (TextView) findViewById(R.id.textViewInfoSum);
		textview_username = (TextView) findViewById(R.id.textview_username);

		iv_mine1.setOnClickListener(this);
		lL_mine2.setOnClickListener(this);
        lL_mine3.setOnClickListener(this);
	}

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis()-exitTime)>2000){
                Toast.makeText(getApplicationContext(),"再按一次退出程序",Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            }else{
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
