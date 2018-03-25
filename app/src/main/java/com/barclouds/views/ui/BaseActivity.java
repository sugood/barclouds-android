package com.barclouds.views.ui;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.barclouds.views.common.NetRequestConstant;
import com.barclouds.views.http.ThreadPool;
import com.barclouds.views.interfaces.Netcallback;
import com.barclouds.views.utils.NetUtil;

/**
 * 如果要请求网络的话，就继承BaseActivity，其余的不用
 * @author hp
 */
public abstract class BaseActivity extends Activity {
	
	private NetRequestConstant nrc;
	private Handler handler;
	public static final int SUCCESS = 10001; 
	public static final int FAIL = 10002; 
	public static final int ERROR = 10003; 

	abstract void init();
	
	public enum HttpRequestType{
		GET,POST;
	}
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.init();
	}
	
	class RunnableTask implements Runnable {
		
		private NetRequestConstant nrc;
		private Handler handler;
		
		public RunnableTask(NetRequestConstant nrc , Handler handler) {
			this.nrc = nrc;
			this.handler = handler;
		}

		//通过HTTP与服务器交互，获取网络信息
		public void run() {
			String res=null;
            if(NetUtil.isCheckNet(getApplicationContext())){
            	if(nrc.getType()== HttpRequestType.POST){//Post请求
            		res = NetUtil.httpPost(nrc);
				}else if(nrc.getType()== HttpRequestType.GET){//get请求
				    res = NetUtil.httpGet(nrc);
				}
                //如果请求成功则向hangdler发送一个请求成功的标志和内容
				Message message = Message.obtain();
				message.obj = res;
				message.what = SUCCESS;
				handler.sendMessage(message);
				
			}else{
                //如果请求失败则向hangdler发送一个请求失败的标志和内容
				Message message = Message.obtain();
				message.what = ERROR;
				handler.sendMessage(message);
			}
		}
	}
	//使用一个handler来接收网络请求的消息。
	class BaseHandler extends Handler {

		private Netcallback callBack;
		
		
		public BaseHandler(Netcallback callBack) {
			this.callBack = callBack;
		}
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			
			switch (msg.what) {
			case SUCCESS:// 网络请求成功后回调
				callBack.preccess(msg.obj, true);
				
				break;
				
			case FAIL:// 网络请求失败后回调
				
			case ERROR:
				callBack.preccess(msg.obj, false);
				break;

			default:
				break;
			}
			
			super.handleMessage(msg);
		}
		
	}
	
	protected void getServer(Netcallback callBack ,NetRequestConstant nrc){

        //创建一个Handler类
		Handler handler = new BaseHandler(callBack);
        //创建一个网络连接的进程类
		RunnableTask task = new RunnableTask(nrc, handler);
        //创建一个线程池来处理任务
		ThreadPool.getInstance().addTask(task);
		
	}
}
