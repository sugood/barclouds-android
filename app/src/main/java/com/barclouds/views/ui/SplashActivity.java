package com.barclouds.views.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.domain.InfoIndex;
import com.barclouds.views.domain.User;
import com.barclouds.views.interfaces.Netcallback;
import com.barclouds.views.utils.NetUtil;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @{# ViewPagerAdapter.java Create on 2015-8-4 下午11:03:39
 *
 *     class desc: 启动画面 (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 *     (2)是，则进入GuideActivity；否，则进入MainActivity (3)3s后执行(2)操作
 *
 *     <p>
 *     Copyright: Copyright(c) 2015
 *     </p>
 * @Version 1.0
 * @Author <a href="sxq19880115@163.com">sugood</a>
 *
 *
 */
public class SplashActivity extends BaseActivity {
	boolean isFirstIn = false;

	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
    private static final int GO_LOGIN = 1002;
    private static final int GO_NONET = 1003;
	// 延迟3秒
	private static final long SPLASH_DELAY_MILLIS = 3000;

	private static final String SHAREDPREFERENCES_NAME = "first_pref";

    private SharedPreferences preferencesUser;
    private AppContext appContext;

    private User user;
	/**
	 * Handler:跳转到不同界面
	 */
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case GO_HOME:
				goHome();
				break;
			case GO_GUIDE:
				goGuide();
				break;
            case GO_LOGIN:
                goLogin();
                break;
            case GO_NONET:
                goNoNet();
                break;
			}
			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	void init() {
        setContentView(R.layout.splash);
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

        //初始化AppContext，创建一个全局可以用的数据类
        appContext = (AppContext) getApplicationContext();
        preferencesUser = getPreferences(MODE_PRIVATE);
        appContext.setPreferences(preferencesUser);

        user = appContext.getPreferencesUser();

		// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
		if (!isFirstIn) {
			// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            Log.d("splash","password"+user.getPassword()+"username"+user.getUsername());
            if (user.getPassword() != null && !user.getPassword().equals("") && user.getUsername() != null&&!user.getUsername().equals("")) {
                appContext.setUser(user);
                //如果网络可用则查询用户信息,否则结束程序
                if(NetUtil.isCheckNet(this)==true) {
                    Log.d("BarcloudsDebug","有网络");
                    getInfoIndexForNet();

                }else {
                    Log.d("BarcloudsDebug", "无网络");
                    Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessageDelayed(GO_NONET, SPLASH_DELAY_MILLIS);
                }

            }else{
                mHandler.sendEmptyMessageDelayed(GO_LOGIN, SPLASH_DELAY_MILLIS);
            }

		} else {
            user.setUsername("");
            appContext.setUser(user);
			mHandler.sendEmptyMessageDelayed(GO_GUIDE, SPLASH_DELAY_MILLIS);
		}
	}

	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}
    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    private void goNoNet() {
        Log.d("BarcloudsDebug:", "goNoNet");
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        SplashActivity.this.startActivity(intent);
        SplashActivity.this.finish();
    }

    public void getInfoIndexForNet() {
        Log.d("BarcloudsDebug:", "getInfoIndexForNet");
        NetRequestConstant nrc = new NetRequestConstant();
        // post请求
        nrc.setType(HttpRequestType.POST);
        NetRequestConstant.requestUrl = NetUrlConstant.INFOINDEXPAGE;
        NetRequestConstant.context = this;
        Map<String, Object> map = new HashMap<String, Object>();

        Log.d("BarcloudsDebug:","sugood");
        map.put("uid", user.getUsername());
        NetRequestConstant.map = map;

        getServer(new Netcallback() {

            public void preccess(Object res, boolean flag) {
                if (res != null) {
                    try {
                        Gson gson = new Gson();
                        InfoIndex infoIndex = gson.fromJson((String) res, InfoIndex.class);
                        //获取了列表名称后放到全局数据中
                        appContext.setInfoIndex(infoIndex);
                        mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }else{
                    Log.d("BarcloudsDebug","连接服务器失败");
                    Toast.makeText(SplashActivity.this, "连接服务器失败...", Toast.LENGTH_SHORT).show();
                    mHandler.sendEmptyMessageDelayed(GO_NONET, SPLASH_DELAY_MILLIS);
                }
            }
        }, nrc);
    }
}
