package com.barclouds.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends BaseActivity implements OnClickListener {

	private TextView textview_register;
	private ImageView imageview_back, imageview_meituan;
	private EditText edittext_username, edittext_password;
	private Button login;
    private AppContext appContext;
    private User user;
    long exitTime=0;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	void init() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.login_user);
		textview_register = (TextView) findViewById(R.id.textView_register);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		imageview_meituan = (ImageView) findViewById(R.id.imageview_meituan);
		login = (Button) findViewById(R.id.button_login);
		edittext_username = (EditText) findViewById(R.id.login_userName);
		edittext_password = (EditText) findViewById(R.id.login_userPassword);

		textview_register.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		imageview_meituan.setOnClickListener(this);
		login.setOnClickListener(this);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getPreferencesUser();

        if(user!=null){
            edittext_username.setText(user.getUsername());
        }
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.textView_register:
			startActivityForResult(new Intent(this, RegisterActivity.class), 9);
			break;
		case R.id.imageview_back:
		case R.id.imageview_meituan:
			finish();
			break;
		case R.id.button_login:
            if(NetUtil.isCheckNet(this)==false) {
                Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
                break;
            }
			NetRequestConstant nrc = new NetRequestConstant();
			// post请求
			nrc.setType(HttpRequestType.POST);
			final String username = edittext_username.getText().toString();
			final String password = edittext_password.getText().toString();
			NetRequestConstant.requestUrl = NetUrlConstant.LOGINURL;
			NetRequestConstant.context = this;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("user_Name", username);
			map.put("user_Password", password);
			NetRequestConstant.map = map;

			getServer(new Netcallback() {

				public void preccess(Object res, boolean flag) {
					if (res != null) {
						try {
							JSONObject object = new JSONObject((String) res);
							String success = object.optString("success");
							if (success.equals("1")) {
								Intent data = new Intent();
								data.putExtra("username", username);
								user.setUsername(username);
								user.setPassword(password);
								appContext.setUser(user);
								Toast.makeText(LoginActivity.this, "登陆成功！",
                                        Toast.LENGTH_LONG).show();

                                getInfoIndexForNet();
//								setResult(12, data);
//								finish();
							} else {
								Toast.makeText(LoginActivity.this, "账号或密码不正确",
                                        Toast.LENGTH_LONG).show();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

				}
			}, nrc);

			break;
		default:
			break;
		}

	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(data!=null){
            String username = data.getStringExtra("username");
            String password = data.getStringExtra("password");
            edittext_username.setText(username);
            edittext_password.setText(password);
        }
	}
    public void getInfoIndexForNet() {
        Log.d("BarcloudsDebug:", "getInfoIndexForNet");
        NetRequestConstant nrc = new NetRequestConstant();
        // post请求
        nrc.setType(HttpRequestType.POST);
        NetRequestConstant.requestUrl = NetUrlConstant.INFOINDEXPAGE;
        NetRequestConstant.context = this;
        Map<String, Object> map = new HashMap<String, Object>();

        Log.d("BarcloudsDebug:","uid="+user.getUsername());
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

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        LoginActivity.this.startActivity(intent);
                        LoginActivity.this.finish();
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }, nrc);
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
