package com.barclouds.views.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NetPathToBitmapUtils {
    private String netPath;

	public NetPathToBitmapUtils(String netPath) {
		super();
		this.netPath = netPath;
	}
     
	public void run() {
		// 把网络地址转换为BitMap
		URL picUrl;
		try {
			picUrl = new URL(netPath);
			System.out.println("网络图片地址"+picUrl);
		    Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
