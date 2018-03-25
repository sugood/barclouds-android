package com.barclouds.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.domain.User;
import com.barclouds.views.interfaces.Netcallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AmedUserNameActivity extends BaseActivity implements
        OnClickListener {
	private ImageView imageview_back;
	private TextView textview_back;
	private EditText edittext_username;
	private Button button_confirm;
	private String oldname;
	private AppContext appContext;
	private User user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	
	void init() {
		setContentView(R.layout.amedusername_activity);
		imageview_back = (ImageView) findViewById(R.id.imageview_aboutmeituanback);
		textview_back = (TextView) findViewById(R.id.textview_meituan);
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		button_confirm = (Button) findViewById(R.id.button_confirm);

		imageview_back.setOnClickListener(this);
		textview_back.setOnClickListener(this);
		button_confirm.setOnClickListener(this);

		appContext = (AppContext) getApplicationContext();
		user = appContext.getUser();
		if (user != null) {
			edittext_username.setText(user.getUsername());
		}
		oldname = user.getUsername();

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_aboutmeituanback:
		case R.id.textview_meituan:
			finish();
			break;
		case R.id.button_confirm:
			NetRequestConstant nrc = new NetRequestConstant();
			// post请求
			nrc.setType(HttpRequestType.POST);
			final String newname = edittext_username.getText().toString();

			NetRequestConstant.requestUrl = NetUrlConstant.CHANGEUSENAMEURL;
			NetRequestConstant.context = this;
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("newname", newname);
			map.put("oldname", oldname);
			NetRequestConstant.map = map;

			getServer(new Netcallback() {

				public void preccess(Object res, boolean flag) {
					if (res != null) {
						try {
							JSONObject object = new JSONObject((String) res);
							String success = object.optString("success");
							if (success.equals("1")) {
								Toast.makeText(AmedUserNameActivity.this,
                                        "修改成功", Toast.LENGTH_SHORT).show();
								user.setUsername(newname);
								appContext.setUser(user);
								Intent data = new Intent();
								data.putExtra("newname", newname);
								setResult(19, data);
							    finish();
							} else {
								Toast.makeText(AmedUserNameActivity.this,
                                        "出现不知名的错误", Toast.LENGTH_SHORT).show();
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
}
