package com.barclouds.views.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.interfaces.Netcallback;
import com.barclouds.views.utils.NetUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends BaseActivity implements OnClickListener {
	private EditText edittext_username, edittext_password,edittext_confirmPassword,
             edittext_email,edittext_question,edittext_answer;
	private Button button_confirm;
	private ImageView imageview_back;
	private TextView textview_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	void init() {
		setContentView(R.layout.register_activity);
		edittext_username = (EditText) findViewById(R.id.edittext_username);
		edittext_password = (EditText) findViewById(R.id.edittext_password);
        edittext_confirmPassword = (EditText) findViewById(R.id.edittext_confirmPassword);
        edittext_email = (EditText) findViewById(R.id.edittext_email);
        edittext_question = (EditText) findViewById(R.id.edittext_question);
        edittext_answer = (EditText) findViewById(R.id.edittext_answer);

		button_confirm = (Button) findViewById(R.id.button_confirm);
		imageview_back = (ImageView) findViewById(R.id.imageview_back);
		textview_back = (TextView) findViewById(R.id.textview_meituan);

		button_confirm.setOnClickListener(this);
		imageview_back.setOnClickListener(this);
		textview_back.setOnClickListener(this);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.imageview_back:
		case R.id.textview_meituan:
			finish();
			break;
		case R.id.button_confirm:// 确认注册
            if(NetUtil.isCheckNet(this)==false) {
                Toast.makeText(this, "无网络连接...", Toast.LENGTH_SHORT).show();
                break;
            }
			final String username = edittext_username.getText().toString();
			final String password = edittext_password.getText().toString();
            final String confirmPassword = edittext_confirmPassword.getText().toString();
            final String email = edittext_email.getText().toString();
            final String question = edittext_question.getText().toString();
            final String answer = edittext_answer.getText().toString();

			if (!username.equals("") && !password.equals("")&&!confirmPassword.equals("") &&
                !email.equals("")&&!question.equals("") && !answer.equals("")) {

				NetRequestConstant nrc = new NetRequestConstant();
				// post请求
				nrc.setType(HttpRequestType.POST);
				NetRequestConstant.requestUrl = NetUrlConstant.REGISTERURL;
				NetRequestConstant.context = this;
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("uid", username);
				map.put("password", password);
                map.put("confirmPassword", confirmPassword);
                map.put("email", email);
                map.put("question", question);
                map.put("answer", answer);

				NetRequestConstant.map = map;

                //创建一个匿名内部类
				getServer(new Netcallback() {

					public void preccess(Object res, boolean flag) {
						if (res != null) {
							try {
                                Log.d("BarcloudsDebug", "RegisterActivity.onCreate:RES=" + (String) res);
								JSONObject object = new JSONObject((String) res);
								String success = object.optString("success");
								if (success.equals("1")) {
									Intent data = new Intent();
									data.putExtra("username", username);
									data.putExtra("password", password);
									setResult(RESULT_OK, data);
									Toast.makeText(RegisterActivity.this,
                                            "注册成功", Toast.LENGTH_LONG)
											.show();
									finish();
								} else {
									Toast.makeText(RegisterActivity.this,
                                            "该账号已被注册", Toast.LENGTH_LONG)
											.show();
								}

							} catch (JSONException e) {
								e.printStackTrace();
							}

						}

					}
				}, nrc);

			}

			break;
		default:
			break;
		}

	}

}
