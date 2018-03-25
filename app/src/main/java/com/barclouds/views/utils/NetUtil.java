package com.barclouds.views.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.barclouds.views.common.NetRequestConstant;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class NetUtil {
	
	    //用get方式请求网络，返回响应的结果
		public static String httpGet(NetRequestConstant nrc) {
			String result=null;
			try {
				//建立HttpGet对象
				HttpGet httpRequest=new HttpGet(NetRequestConstant.requestUrl);
				
				//创建HttpParams对象，用来设置HTTP参数
				HttpParams httpParams=new BasicHttpParams();
				
				//创建一个HttpClient实例
				HttpClient httpClient=new DefaultHttpClient(httpParams);
				
				//发送请求并等待
				HttpResponse httpResponse=httpClient.execute(httpRequest);
				
				//如果状态码为200就OK了
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//读响应数据
					 result= EntityUtils.toString(httpResponse.getEntity());
				}else{
					 result="请求错误"+httpResponse.getStatusLine().toString();
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return result;
		}
		
		//post请求方式
		public static String httpPost(NetRequestConstant nrc){
			String result=null;
			try {
				//创建HttpParams对象，用来设置HTTP参数
				HttpParams httpParams=new BasicHttpParams();
				HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
				HttpConnectionParams.setSoTimeout(httpParams, 5000);
				
				//创建一个HttpClient实例
				HttpClient httpClient=new DefaultHttpClient(httpParams);
				
				//建立HttpPost对象
				HttpPost httpRequest=new HttpPost(NetRequestConstant.requestUrl);
				
				//发送请求的参数
	            Map<String, Object> map = NetRequestConstant.map;
				
				List<NameValuePair> parameters = new ArrayList<NameValuePair>();
				
				for(Map.Entry<String, Object> entry : map.entrySet()){
					NameValuePair pair = new BasicNameValuePair(entry.getKey(), (String) entry.getValue());
					parameters.add(pair);
				}
				
				//添加请求参数到请求对象
				httpRequest.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
				
				//发送请求并等待响应
				HttpResponse httpResponse=httpClient.execute(httpRequest);
				
				Logger.e( "NetUtil Code ：" + NetRequestConstant.requestUrl);
				//状态码为200就请求成功了
				if(httpResponse.getStatusLine().getStatusCode()==200){
					 //读响应数据
					 result= EntityUtils.toString(httpResponse.getEntity());
				}else{
					 result="请求错误"+httpResponse.getStatusLine().toString();
				}
				
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result;
		}

		public static boolean isCheckNet(Context context){
			 ConnectivityManager cm=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			 NetworkInfo info=cm.getActiveNetworkInfo();
			 if(info==null){
				 //没有联网
				 return false;	
			 }else{
				 //联网类型
				//String type=info.getTypeName();
			    return true;	
			 } 
		 }
		
}
