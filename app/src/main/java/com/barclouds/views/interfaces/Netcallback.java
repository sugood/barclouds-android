package com.barclouds.views.interfaces;

public interface Netcallback {
	//res为请求网络成功后的Json串，返回的请求网络
	void preccess(Object res, boolean flag);
}
