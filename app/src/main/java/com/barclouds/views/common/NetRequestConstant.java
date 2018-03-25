package com.barclouds.views.common;

import android.content.Context;

import com.barclouds.views.ui.BaseActivity.HttpRequestType;

import java.util.Map;

public class NetRequestConstant {

	public static Context context;
	public static String requestUrl;
	public static Map<String, Object> map;

	private HttpRequestType type;

	public HttpRequestType getType() {
		return type;
	}

	public void setType(HttpRequestType type) {
		this.type = type;
	}
	
	public static void setMap(Map map){
		NetRequestConstant.map = map;
	}

	

}
