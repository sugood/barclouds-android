package com.barclouds.views.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.domain.User;
import com.barclouds.views.updata.CurrentVersion;
import com.barclouds.views.updata.GetUpdateInfo;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SettingActivity extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
    private static final int VERSIONS = 1;
	private static final String TAG = "Update";
	private LinearLayout linearLayout_versions;
    private LinearLayout linearLayout_about;
    private LinearLayout linearLayout_cancel;
    private TextView textview_versions;
	private ProgressDialog pBar;
	private String downPath = NetUrlConstant.APPUPDATA;
	private String appName = "BarClouds.apk";
	private String appVersion = "version.json";
	private int newVerCode = 0;
	private String newVerName = "";
	private Handler handler=new Handler();
    private AppContext appContext;
    private User user;
    private boolean updataFlag=false;

    private Handler mHandler = new Handler()
    {
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                // 正在下载
                case VERSIONS:
                    try{
                        if(updataFlag){
                            int currentCode = CurrentVersion.getVerCode(SettingActivity.this);
                            if(newVerCode > currentCode)
                            {//Current Version is old
                                //弹出更新提示对话框
                                Log.d("UpdataApp", "UpdateAppActivity.checkToUpdate:准备更新");
                                    showUpdateDialog();
                            }else{
                                Toast.makeText(SettingActivity.this,
                                        "当前是最新版本!", Toast.LENGTH_LONG)
                                        .show();
                            }

                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        };
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        appContext = (AppContext) getApplicationContext();
        user = appContext.getUser();

        linearLayout_versions = (LinearLayout)findViewById(R.id.linearLayout_versions);
        linearLayout_about = (LinearLayout)findViewById(R.id.linearLayout_about);
        linearLayout_cancel = (LinearLayout)findViewById(R.id.linearLayout_cancel);
        textview_versions = (TextView)findViewById(R.id.textview_versions);

        linearLayout_versions.setOnClickListener(this);
        linearLayout_about.setOnClickListener(this);
        linearLayout_cancel.setOnClickListener(this);

        textview_versions.setText(getVersion());
    }
    //check the Network is available
    private static boolean isNetworkAvailable(Context context) {
		// TODO Auto-generated method stub
    	try{
    	
    		ConnectivityManager cm = (ConnectivityManager)context
    				.getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo netWorkInfo = cm.getActiveNetworkInfo();
    		return (netWorkInfo != null && netWorkInfo.isAvailable());//检测网络是否可用
    	}catch(Exception e){
    		e.printStackTrace();
    		return false;
    	}
	}
	//check new version and update
	private void checkToUpdate() throws NameNotFoundException {
		// TODO Auto-generated method stub
        new Thread() {
            public void run() {
                if(getServerVersion())
                    updataFlag=true;
                else
                    updataFlag=false;
                mHandler.sendEmptyMessage(VERSIONS);
            }
        }.start();
//		if(getServerVersion()){
//			int currentCode = CurrentVersion.getVerCode(this);
//			if(newVerCode > currentCode)
//			{//Current Version is old
//				//弹出更新提示对话框
//                Log.d("UpdataApp", "UpdateAppActivity.checkToUpdate:准备更新");
//				showUpdateDialog();
//			}
//            Log.d("UpdataApp", "UpdateAppActivity.checkToUpdate:更新完成");
//		}
	}
	//show Update Dialog
	private void showUpdateDialog() throws NameNotFoundException {
		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();
		sb.append("当前版本：");
		sb.append(CurrentVersion.getVerName(this));
		sb.append("VerCode:");
		sb.append(CurrentVersion.getVerCode(this));
		sb.append("\n");
		sb.append("发现新版本：");
		sb.append(newVerName);
		sb.append("NewVerCode:");
		sb.append(newVerCode);
		sb.append("\n");
		sb.append("是否更新？");
		Dialog dialog = new AlertDialog.Builder(SettingActivity.this)
		.setTitle("软件更新")
		.setMessage(sb.toString())
		.setPositiveButton("更新", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				showProgressBar();//更新当前版本
			}
		})
		.setNegativeButton("暂不更新", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		}).create();
		dialog.show();
	}
	protected void showProgressBar() {
		// TODO Auto-generated method stub
		pBar = new ProgressDialog(SettingActivity.this);
		pBar.setTitle("正在下载");
		pBar.setMessage("请稍后...");
		pBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		downAppFile(downPath + appName);
	}
    /**
     * 下载apk文件
     */
    Runnable getUpdateInfo = new Runnable() {
        public void run() {
        }
    };

	//Get ServerVersion from GetUpdateInfo.getUpdateVerJSON
	private boolean getServerVersion() {
		// TODO Auto-generated method stub
        Log.d("UpdataApp", "UpdateAppActivity.getServerVersion");
		try{
			String newVerJSON = GetUpdateInfo.getUpdataVerJSON(downPath + appVersion);
            Log.d("UpdataApp", "UpdateAppActivity.getServerVersion1");
			JSONArray jsonArray = new JSONArray(newVerJSON);
            Log.d("UpdataApp", "UpdateAppActivity.getServerVersion2");
			if(jsonArray.length() > 0){
				JSONObject obj = jsonArray.getJSONObject(0);
				try{
					newVerCode = Integer.parseInt(obj.getString("verCode"));
					newVerName = obj.getString("verName");
				}catch(Exception e){
					Log.e(TAG, e.getMessage());
					newVerCode = -1;
					newVerName = "";
					return false;
				}
			}
		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	protected void downAppFile(final String url) {
		pBar.show();
		new Thread(){
			public void run(){
				HttpClient client = new DefaultHttpClient();
				HttpGet get = new HttpGet(url);
				HttpResponse response;
				try {
					response = client.execute(get);
					HttpEntity entity = response.getEntity();
					long length = entity.getContentLength();
					Log.isLoggable("DownTag", (int) length);
					InputStream is = entity.getContent();
					FileOutputStream fileOutputStream = null;
					if(is == null){
						throw new RuntimeException("isStream is null");
					}
					File file = new File(Environment.getExternalStorageDirectory(),appName);
					fileOutputStream = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int ch = -1;
					do{
						ch = is.read(buf);
						if(ch <= 0)break;
						fileOutputStream.write(buf, 0, ch);
					}while(true);
					is.close();
					fileOutputStream.close();
					haveDownLoad();
					}catch(ClientProtocolException e){
						e.printStackTrace();
						}catch(IOException e){
						e.printStackTrace();
						}
				}
		}.start();
	}
	//cancel progressBar and start new App
	protected void haveDownLoad() {
		// TODO Auto-generated method stub
		handler.post(new Runnable(){
			public void run(){
				pBar.cancel();
				//弹出警告框 提示是否安装新的版本
				Dialog installDialog = new AlertDialog.Builder(SettingActivity.this)
				.setTitle("下载完成")
				.setMessage("是否安装新的应用")
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						installNewApk();
						finish();
						}
					})
					.setNegativeButton("取消", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							finish();
							}
						})
						.create();
				installDialog.show();
				}
			});
		}
	//安装新的应用
	protected void installNewApk() {
		// TODO Auto-generated method stub
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setDataAndType(Uri.fromFile(
                        new File(Environment.getExternalStorageDirectory(), appName)),
				"application/vnd.android.package-archive");
		startActivity(intent);
	}
    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "version:v"+version;
        } catch (Exception e) {
            e.printStackTrace();
            return "version:v1.0.0";
        }
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.linearLayout_versions:// 检查更新
                try {
                    if(isNetworkAvailable(this) == false){
                        return;
                    }else{
                        Log.d("UpdataApp", "UpdateAppActivity.onCreate");
                        checkToUpdate();
                    }
                } catch (NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
            case R.id.linearLayout_about:// 关于
                    Intent intent4 = new Intent(SettingActivity.this,
                            AboutActivity.class);
                    startActivity(intent4);
                break;
            case R.id.linearLayout_cancel:// 退出
                Dialog dialogChoose=new AlertDialog.Builder(SettingActivity.this)
                    .setTitle("注销用户")
                    .setIcon(R.mipmap.appicon)
                    .setMessage("确认注销吗？")
                            //相当于点击确认按钮
                    .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogChoose, int which) {
                            user.setPassword(null);
                            appContext.setUser(user);
                            Intent intent5 = new Intent(SettingActivity.this,
                                    LoginActivity.class);
                            startActivity(intent5);
                            SettingActivity.this.finish();
                        }
                    })
                            //相当于点击取消按钮
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub

                        }
                    })
                    .create();
                dialogChoose.show();
                break;
            default:
                break;
        }

    }
}