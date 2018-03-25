package com.barclouds.views.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.barclouds.R;
import com.barclouds.views.zxing.CaptureActivity;

public class SearchActivity extends Activity implements OnClickListener {
    private EditText edittext_search;
    private Button button_search;
    private Button button_scan;
    @Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
		setContentView(R.layout.search_activity);


		this.initView();
	}

	public void initView() {
		//返回
        edittext_search = (EditText) findViewById(R.id.edittext_search);
        button_search=(Button) findViewById(R.id.button_search);
        button_scan=(Button) findViewById(R.id.button_scan);
        button_scan.setOnClickListener(this);
        button_search.setOnClickListener(this);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.button_search:
            final String searchChar = edittext_search.getText().toString();
            if(!searchChar.equals("") && searchChar!=null) {
                Intent data = new Intent();
                data.putExtra("searchchar", searchChar);
                setResult(RESULT_OK, data);
                Log.d("SearchActivity", "data=" + searchChar);
                finish();
            }
			break;
        case R.id.button_scan:
            Intent intent1 = new Intent(SearchActivity.this,
                    CaptureActivity.class);//SUGOOD
            startActivityForResult(intent1, 0);//此处的0为一个依据，可以写其他的值，但一定要>=0
            break;
		default:
			break;
		}
	}

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) { //resultCode为回传的标记，我在B中回传的是RESULT_OK
            case RESULT_OK:
                Bundle b=data.getExtras(); //data为B中回传的Intent
                String searchChar=b.getString("str1");//str即为回传的值

                if(!searchChar.equals("") && searchChar!=null) {
                    Intent datas = new Intent();
                    datas.putExtra("searchchar", searchChar);
                    setResult(RESULT_OK, datas);
                    Log.d("SearchActivity", "data=" + searchChar);
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
