package com.barclouds.views.utils;

import android.util.Log;

public class Logger {
   public static final String TAG="Togger";
   public static final int V=1;
   public static final int E=2;
   public static final int D=3;
   public static final int I=4;
   public static final int W=5;
   
   public static final int log=0;
   
   public static boolean isPrint=true;
 
	public static void v(String string){
		if(log < V&&isPrint){
			Log.v("TAG", string);
		}
		
	}
	
	public static void d(String string){
		if(log < D&&isPrint){
			Log.d("TAG", string);
		}
		
	}
	
	public static void i(String string){
		if (log<I&&isPrint) {
			Log.i("TAG", string);
		}
	}
	
	public static void w(String string){
		if(log<W&&isPrint){
			Log.w("TAG", string);
		}
	}
	
	public static void e(String string){
		if (log<E&&isPrint) {
			Log.e("TAG", string);
		}
	}

}
