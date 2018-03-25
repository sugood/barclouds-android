package com.barclouds.views.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import com.barclouds.R;
import com.barclouds.views.adapter.DiaryMenuAdapter;
import com.barclouds.views.adapter.IoMenuAdapter;
import com.barclouds.views.app.AppContext;
import com.barclouds.views.common.NetUrlConstant;
import com.barclouds.views.dialog.CustomProgressDialog;
import com.barclouds.views.domain.InfoIndex;
import com.barclouds.views.domain.User;
import com.barclouds.views.filesChooser.FileChooserActivity;
import com.barclouds.views.myview.MyGridView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.FileNotFoundException;

public class IoActivity extends BaseActivity implements
        OnItemClickListener, OnClickListener {

    private static String TAG = "IoActivity";

    private MyGridView gridview_menu_in;
    private MyGridView gridview_menu_out;
	private IoMenuAdapter adapter_in;
    private DiaryMenuAdapter adapter_out;
	private ImageButton imagebutton_setting;
	private SharedPreferences preferences;
	private AppContext appContext;
    private User user;

    private Intent fileChooserIntent ;
    private static final int REQUEST_CODE_IN = 1;   //请求码
    private static final int REQUEST_CODE_OUT = 2;   //请求码
    public static final String EXTRA_FILE_CHOOSER = "file_chooser";
	// gridView数据源
	private int[] i_in = new int[] { R.mipmap.io_up_layout_2003,
			R.mipmap.io_up_layout_2007,R.mipmap.io_up_layout_txt};

    private int[] i_out = new int[] { R.mipmap.io_down_layout_2003,
            R.mipmap.io_down_layout_2007,R.mipmap.io_down_layout_txt };

    private String[] names_in = new String[]{"导入Excel2003","导入Excel2007","导入TXT"};

    private String[] names_out = new String[]{"导出Excel2003","导出Excel2007","导出TXT"};

    long exitTime=0;

    private CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		gridview_menu_in.setAdapter(adapter_in);

        gridview_menu_out.setAdapter(adapter_out);
        Log.d("BarcloudsDebug","information:DiaryActivity.onCreate");
	}

    void init() {
		setContentView(R.layout.io_activity);
		gridview_menu_in = (MyGridView) findViewById(R.id.gridview_menu_in);
        gridview_menu_out = (MyGridView) findViewById(R.id.gridview_menu_out);
        imagebutton_setting = (ImageButton) findViewById(R.id.imagebutton_setting);

        imagebutton_setting.setOnClickListener(this);
		adapter_in = new IoMenuAdapter(this, i_in,names_in);
        adapter_out = new DiaryMenuAdapter(this, i_out,names_out);

        //获取当前用户数据列名
        appContext = (AppContext) getApplicationContext();
        InfoIndex infoIndex = appContext.getInfoIndex();
        user =appContext.getUser();

        fileChooserIntent =  new Intent(this ,
                FileChooserActivity.class);

		gridview_menu_in.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				switch (position) {

				case 0:// 拍照生成日记
                    if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
                        startActivityForResult(fileChooserIntent , REQUEST_CODE_IN);
                    else
                        toast(getText(R.string.sdcard_unmonted_hint));
					break;
				case 1:// 布局生成日记
                    toast("正在开发中...");
					break;
				case 2:// 自动生成日记
                    toast("正在开发中...");
					break;

				default:
					break;
				}
			}
		});


        gridview_menu_out.setOnItemClickListener(new OnItemClickListener() {

            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int position, long arg3) {
                // TODO Auto-generated method stub
                switch (position) {

                    case 0:// 拍照生成日记
//                        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
//                            startActivityForResult(fileChooserIntent , REQUEST_CODE_OUT);
//                        else
//                            toast(getText(R.string.sdcard_unmonted_hint));
//                        Log.d("BarcloudsDebug","浏览文件out");
//                        break;
                        toast("正在开发中...");
                        break;
                    case 1:// 布局生成日记
                        toast("正在开发中...");
                        break;
                    case 2:// 自动生成日记
                        toast("正在开发中...");
                        break;

                    default:
                        break;
                }
            }
        });
        Log.d("BarcloudsDebug","information:DiaryActivity.init");
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
        Log.d("BarcloudsDebug","information:DiaryActivity.onItemClick-start");
		// TODO Auto-generated method stub
//		Intent intent = new Intent(this, GroupBuyDetailsActivity.class);
//		intent.setAction("GroupBuyActivity");
//		intent.putExtra("position", position);
//		startActivity(intent);
        Log.d("BarcloudsDebug","information:DiaryActivity.onItemClick-end");
	}

	public void onClick(View v) {
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-start");
		switch (v.getId()) {
		case R.id.imagebutton_setting:
			startActivity(new Intent(this, SearchActivity.class));
			break;
		default:
			break;
		}
        Log.d("BarcloudsDebug","information:DiaryActivity.onClick-end");
	}

    public void uploadExcel2003(String url,String filePath)
    {
        //手机端要上传的文件，首先要保存你手机上存在该文件

        AsyncHttpClient httpClient = new AsyncHttpClient();

        RequestParams param = new RequestParams();
        try
        {
            param.put("file", new File(filePath));
            param.put("content", "liucanwen");
            param.put("uid", user.getUsername());

            Log.d(TAG,"uploadExcel2003");
            Log.d(TAG,"USERNAME="+user.getUsername());
            httpClient.post(url, param, new AsyncHttpResponseHandler() {
                @Override
                public void onStart() {
                    super.onStart();
                }

                @Override
                public void onSuccess(String arg0) {
                    super.onSuccess(arg0);

                    Log.i("ck", "success>" + arg0);

                    if (arg0.equals("success")) {
                        toast("上传成功！");
                        if(dialog!=null)
                            dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Throwable arg0, String arg1) {
                    super.onFailure(arg0, arg1);
                    toast("上传失败！");
                    if(dialog!=null)
                        dialog.dismiss();
                }
            });

        } catch (FileNotFoundException e)
        {
            e.printStackTrace();
            toast("上传文件不存在！");
        }
    }
    private void toast(CharSequence hint){
        Toast.makeText(this, hint , Toast.LENGTH_SHORT).show();
    }

    public void onActivityResult(int requestCode , int resultCode , Intent data){

        Log.v(TAG, "onActivityResult#requestCode:"+ requestCode  + "#resultCode:" +resultCode);
        if(resultCode == RESULT_CANCELED){
            toast(getText(R.string.open_file_none));
            return ;
        }
        if(resultCode == RESULT_OK && requestCode == REQUEST_CODE_IN){
            //获取路径名
            String pptPath = data.getStringExtra(EXTRA_FILE_CHOOSER);
            Log.v(TAG, "onActivityResult_in # pptPath : "+ pptPath );
            if(pptPath != null){
                toast("Choose File : " + pptPath);
                //服务器端地址
                dialog =new CustomProgressDialog(IoActivity.this, "正在上传文件...",R.anim.frame);
                dialog.show();
                //上传excel2003
                uploadExcel2003(NetUrlConstant.UPLOAD,pptPath);
            }
            else
                toast(getText(R.string.open_file_failed));
        }else if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_OUT){
            //获取路径名
            String pptPath = data.getStringExtra(EXTRA_FILE_CHOOSER);
            Log.v(TAG, "onActivityResult_out # pptPath : "+ pptPath );
            if(pptPath != null){
                toast("Choose File : " + pptPath);
                uploadExcel2003(NetUrlConstant.UPLOAD,pptPath);

            }
            else
                toast(getText(R.string.open_file_failed));
        }
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