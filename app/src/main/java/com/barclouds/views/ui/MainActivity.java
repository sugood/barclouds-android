package com.barclouds.views.ui;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.barclouds.R;
import com.barclouds.views.app.AppContext;

public class MainActivity extends TabActivity implements OnClickListener {

	private TabHost host;
	private final static String DATA_STRING = "DATA_STRING";//历史
	private final static String IO_STRING = "IO_STRING";//导入
	private final static String COLLECT_STRING = "COLLECT_STRING";//盘点
	private final static String INFO_STRING = "INFO_STRING";//资料
    private final static String MINE_STRING = "MINE_STRING";//我的
	private ImageView img_data;
	private ImageView img_io;
	private ImageView img_collect;
	private ImageView img_info;
    private ImageView img_mine;
	private TextView text_data;
	private TextView text_io;
	private TextView text_collect;
	private TextView text_info;
    private TextView text_mine;
	private LinearLayout linearlayout_data;
	private LinearLayout linearlayout_io;
	private LinearLayout linearlayout_collect;
	private LinearLayout linearlayout_info;
    private LinearLayout linearlayout_mine;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        System.out.println("<----Debug:MainActivity.onCreate---->");
		setContentView(R.layout.activity_main);
		
		this.getScreenDisplay();
		
		this.initView();
		host = getTabHost();
		host.setup();
		setDataTab();
		setIoTab();
        setInfoTab();
        setMineTab();
		setCollectTab();
		host.setCurrentTabByTag(COLLECT_STRING);//默认盘点数据

	}

	public void initView(){
		img_data=(ImageView) findViewById(R.id.img_data);
        img_io=(ImageView) findViewById(R.id.img_io);
        img_collect=(ImageView) findViewById(R.id.img_collect);
        img_info=(ImageView) findViewById(R.id.img_info);
        img_mine=(ImageView)findViewById(R.id.img_mine);
        img_data.setOnClickListener(this);
        img_io.setOnClickListener(this);
        img_collect.setOnClickListener(this);
        img_info.setOnClickListener(this);
        img_mine.setOnClickListener(this);

        text_data=(TextView) findViewById(R.id.text_data);
        text_io=(TextView) findViewById(R.id.text_io);
        text_collect=(TextView) findViewById(R.id.text_collect);
        text_info=(TextView) findViewById(R.id.text_info);
        text_mine=(TextView)findViewById(R.id.text_mine);

        linearlayout_data=(LinearLayout) findViewById(R.id.linearlayout_data);
        linearlayout_io=(LinearLayout) findViewById(R.id.linearlayout_io);
        linearlayout_collect=(LinearLayout) findViewById(R.id.linearlayout_collect);
        linearlayout_info=(LinearLayout) findViewById(R.id.linearlayout_info);
        linearlayout_mine=(LinearLayout) findViewById(R.id.linearlayout_mine);

        linearlayout_data.setOnClickListener(this);
        linearlayout_io.setOnClickListener(this);
        linearlayout_collect.setOnClickListener(this);
        linearlayout_info.setOnClickListener(this);
        linearlayout_mine.setOnClickListener(this);
	}

    private void setDataTab() {
        TabSpec tabSpec = host.newTabSpec(DATA_STRING);
        tabSpec.setIndicator(DATA_STRING);
        Intent intent = new Intent(MainActivity.this, DataViewActivity.class);
        tabSpec.setContent(intent);
        host.addTab(tabSpec);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    private void setIoTab() {
		TabSpec tabSpec = host.newTabSpec(IO_STRING);
		tabSpec.setIndicator(IO_STRING);
        Intent intent = new Intent(MainActivity.this, IoActivity.class);//SUGOOD
        tabSpec.setContent(intent);
		host.addTab(tabSpec);
	}

	private void setCollectTab() {
		TabSpec tabSpec = host.newTabSpec(COLLECT_STRING);
		tabSpec.setIndicator(COLLECT_STRING);
        Intent intent = new Intent(MainActivity.this, CollectListViewActivity.class);//SUGOOD
        tabSpec.setContent(intent);
		host.addTab(tabSpec);
	}

	private void setInfoTab() {
		TabSpec tabSpec = host.newTabSpec(INFO_STRING);
		tabSpec.setIndicator(INFO_STRING);
        Intent intent = new Intent(MainActivity.this, InfoListViewActivity.class);//SUGOOD
        tabSpec.setContent(intent);
		host.addTab(tabSpec);

	}

    private void setMineTab() {
        TabSpec tabSpec = host.newTabSpec(MINE_STRING);
        tabSpec.setIndicator(MINE_STRING);
        Intent intent = new Intent(MainActivity.this, MineActivity.class);//SUGOOD
        tabSpec.setContent(intent);
        host.addTab(tabSpec);

    }
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.linearlayout_data:
		case R.id.img_data:
			host.setCurrentTabByTag(DATA_STRING);
            img_data.setBackgroundResource(R.mipmap.category_press_data);
            text_data.setTextColor(getResources().getColor(R.color.blue));
            img_io.setBackgroundResource(R.mipmap.category_unpress_io);
            text_io.setTextColor(getResources().getColor(R.color.textgray));
            img_collect.setBackgroundResource(R.mipmap.category_unpress_collect);
            text_collect.setTextColor(getResources().getColor(R.color.textgray));
            img_info.setBackgroundResource(R.mipmap.category_unpress_info);
            text_info.setTextColor(getResources().getColor(R.color.textgray));
            img_mine.setBackgroundResource(R.mipmap.category_unpress_mine);
            text_mine.setTextColor(getResources().getColor(R.color.textgray));
			break;

		case R.id.linearlayout_io:
		case R.id.img_io:
			host.setCurrentTabByTag(IO_STRING);
            img_data.setBackgroundResource(R.mipmap.category_unpress_data);
            text_data.setTextColor(getResources().getColor(R.color.textgray));
            img_io.setBackgroundResource(R.mipmap.category_press_io);
            text_io.setTextColor(getResources().getColor(R.color.blue));
            img_collect.setBackgroundResource(R.mipmap.category_unpress_collect);
            text_collect.setTextColor(getResources().getColor(R.color.textgray));
            img_info.setBackgroundResource(R.mipmap.category_unpress_info);
            text_info.setTextColor(getResources().getColor(R.color.textgray));
            img_mine.setBackgroundResource(R.mipmap.category_unpress_mine);
            text_mine.setTextColor(getResources().getColor(R.color.textgray));
			break;

		case R.id.linearlayout_collect:
		case R.id.img_collect:
			host.setCurrentTabByTag(COLLECT_STRING);
            img_data.setBackgroundResource(R.mipmap.category_unpress_data);
            text_data.setTextColor(getResources().getColor(R.color.textgray));
            img_io.setBackgroundResource(R.mipmap.category_unpress_io);
            text_io.setTextColor(getResources().getColor(R.color.textgray));
            img_collect.setBackgroundResource(R.mipmap.category_press_collect);
            text_collect.setTextColor(getResources().getColor(R.color.blue));
            img_info.setBackgroundResource(R.mipmap.category_unpress_info);
            text_info.setTextColor(getResources().getColor(R.color.textgray));
            img_mine.setBackgroundResource(R.mipmap.category_unpress_mine);
            text_mine.setTextColor(getResources().getColor(R.color.textgray));
			break;

		case R.id.linearlayout_info:
		case R.id.img_info:
			host.setCurrentTabByTag(INFO_STRING);
            img_data.setBackgroundResource(R.mipmap.category_unpress_data);
            text_data.setTextColor(getResources().getColor(R.color.textgray));
            img_io.setBackgroundResource(R.mipmap.category_unpress_io);
            text_io.setTextColor(getResources().getColor(R.color.textgray));
            img_collect.setBackgroundResource(R.mipmap.category_unpress_collect);
            text_collect.setTextColor(getResources().getColor(R.color.textgray));
            img_info.setBackgroundResource(R.mipmap.category_press_info);
            text_info.setTextColor(getResources().getColor(R.color.blue));
            img_mine.setBackgroundResource(R.mipmap.category_unpress_mine);
            text_mine.setTextColor(getResources().getColor(R.color.textgray));
			break;

            case R.id.linearlayout_mine:
            case R.id.img_mine:
                host.setCurrentTabByTag(MINE_STRING);
                img_data.setBackgroundResource(R.mipmap.category_unpress_data);
                text_data.setTextColor(getResources().getColor(R.color.textgray));
                img_io.setBackgroundResource(R.mipmap.category_unpress_io);
                text_io.setTextColor(getResources().getColor(R.color.textgray));
                img_collect.setBackgroundResource(R.mipmap.category_unpress_collect);
                text_collect.setTextColor(getResources().getColor(R.color.textgray));
                img_info.setBackgroundResource(R.mipmap.category_unpress_info);
                text_info.setTextColor(getResources().getColor(R.color.textgray));
                img_mine.setBackgroundResource(R.mipmap.category_press_mine);
                text_mine.setTextColor(getResources().getColor(R.color.blue));
                break;
		default:
			break;
		}
	}
	
	private void getScreenDisplay(){
		 Display display=this.getWindowManager().getDefaultDisplay();
	     int screenWidth = display.getWidth();
	     int screenHeight=display.getHeight();
	     
	     AppContext appContext=(AppContext) getApplicationContext();
	     appContext.setScreenWidth(screenWidth);
	     appContext.setScreenHeight(screenHeight);
	}
}
