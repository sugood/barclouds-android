package com.barclouds.views.utils;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;

import com.barclouds.views.http.ThreadPool;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ImageUtils {

	private final static int CAPACITY = 10;

	//软应用和硬应用都是用来缓存图片的，如果硬应用存储空间已满的话，再放到软应用中，如果软应用长期不用的话，就会从最后一个开始自动删除

	// 软引用
	static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftCache = new ConcurrentHashMap<String, SoftReference<Bitmap>>(
			CAPACITY);
	// 硬应用
	static HashMap<String, Bitmap> mHardCache = new LinkedHashMap<String, Bitmap>(
			CAPACITY, 0.75f, true) {
		private static final long serialVersionUID = 1L;

		@Override
		protected boolean removeEldestEntry(
				Entry<String, Bitmap> eldest) {
			// 缓存中硬应用已满，放在软应用中
			if (mHardCache.size() > CAPACITY) {
				mSoftCache.put(eldest.getKey(), new SoftReference<Bitmap>(
						eldest.getValue()));
				return true;
			}
			return false;
		};
	};

	// 得到歌曲的SD卡路径
	public static String getSDImgUrl(String imgUrl) {
		String imgSDUrl = Environment.getExternalStorageDirectory()
				+ "/pictures/";
		int index = imgUrl.lastIndexOf(".");
		//文件后缀名
		String lastName = imgUrl.substring(index);

		//final String sdImgUrl  = (imgSDUrl == null) ? md5(imgUrl).concat(lastName) : imgSDUrl + md5(imgUrl).concat(lastName);
		String sdImgUrl=null;
		//加密
		if(!(imgSDUrl==null)){
			sdImgUrl= imgSDUrl + md5(imgUrl).concat(lastName);
		}
		return sdImgUrl;
	}

	// 获取图片的函数（优先级：1.先从缓存中拿，2.从SD卡中拿，3.从网络中拿）
	public static Bitmap loadImage( final String imgUrl,  final int position,
			final ImageCallBack callBack) {
		// http://localhost:8080/music/abc.jpg
		if (imgUrl == null || imgUrl.lastIndexOf(".") < 0
				|| "".equals(imgUrl.trim())) {
			return null;
		}
		System.out.println("imgUrl"+imgUrl);
		//从缓存中拿
		Bitmap bitmap1 = getBitmapFromCache(ImageUtils.getSDImgUrl(imgUrl));
		if (bitmap1 != null) {
			System.out.println("bitmap1"+bitmap1);
			return bitmap1;
		} else {
			//从SD卡中拿
			Bitmap bitmap2 = getBitmapFromSDcard(ImageUtils.getSDImgUrl(imgUrl));
			if (bitmap2 != null) {
				System.out.println("bitmap2"+bitmap2);
				return bitmap2;
			} else {
				
				 final Handler handler = new Handler() {
						@Override
						public void handleMessage(Message msg) {
							// TODO Auto-generated method stub
							super.handleMessage(msg);
							Bitmap bitmap3 = (Bitmap) msg.obj;
							System.out.println("接口回调方法bitmap3"+bitmap3);
							if (bitmap3 != null) {
								// 接口回调方法
								callBack.getBitmap(bitmap3, position);
							}
						}
					}; 
					
					
				 Runnable runnable = new Runnable() {
					 
				 InputStream in=null;
					public void run() {
						// TODO Auto-generated method stub
						try {
							URL url = new URL(imgUrl);
							HttpURLConnection conn = (HttpURLConnection) url
									.openConnection();
							conn.setRequestMethod("GET");
							conn.setReadTimeout(5000);
							conn.connect();
							in = conn.getInputStream();
							Bitmap bitmap = BitmapFactory.decodeStream(in);
						
							if (bitmap != null) {
								// 1.把Bitmap用handler传递 出去
								Message msg = new Message();
								msg.obj = bitmap;
								handler.sendMessage(msg);
								// 2.把Bitmap加入缓存中
								ImageUtils
										.saveBitmapInCacheFromInternet(bitmap, imgUrl);
								// 3.把Bitmap加入SD卡中
								ImageUtils.saveBitmapInSDcardFromInternet(bitmap,
                                        imgUrl);
							}
						} catch (MalformedURLException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}finally{
							try {
								in.close();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				};
				// 启动线程
				ThreadPool.getInstance().addTask(runnable);
			}
		}
		return null;
	}

	// 从缓存中拿图片
	public static Bitmap getBitmapFromCache(String imgUrl) {

		synchronized (mHardCache) {
			// 如果硬应用还有空间，先从硬应用中拿
			if (mHardCache.size() <= 10) {
				synchronized (imgUrl) {
					if (imgUrl != null) {
						Bitmap bitmap = mHardCache.get(imgUrl);
						if (bitmap != null) {
							return bitmap;
						}
					}
				}
			} else {// 硬应用已满，放软应用中

				// 获取集合中的软引用对象的值
				SoftReference<Bitmap> reference = mSoftCache.get(imgUrl);

				if (reference != null) {
					Bitmap bitmap = reference.get();
					if (bitmap != null) {
						return bitmap;
					} else {
						// 如果bitmap为空的话 就是被系统回收掉了
						mSoftCache.remove(imgUrl);
					}
				}
			}
		}
		return null;
	}

	//从SD卡中拿图片
	public static Bitmap getBitmapFromSDcard(String imgUrl) {
		File file = new File(imgUrl);

		if (file.exists()) {
			Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
			if (bitmap != null) {
				return bitmap;
			}
		}
		return null;
	}

	
	// 从网络中下载图片，开线程，从handler传bitmap对象
	public static Bitmap getBitmapFromInternet(final String imgUrl,
			final int position, final ImageCallBack callBack) {
		return null;
	}

	
	
	// 把从网络上下载的图片保存在缓存中
	public static void saveBitmapInCacheFromInternet(Bitmap bitmap,
			String imgUrl) {
		// 如果硬应用空间已满，会自动回调加入软应用中
		synchronized (mHardCache) {
			if (bitmap != null) {
				mHardCache.put(imgUrl, bitmap);
			}
		}
	}

	// 把从网络上下载的图片保存在SD卡中
	public static void saveBitmapInSDcardFromInternet(Bitmap bitmap,
			String imgUrl) throws IOException {
		File f = new File(imgUrl);
		if (f.exists()) {
			return;
		} else {
			File parentFile = f.getParentFile();
			if (!parentFile.exists()) {
				parentFile.mkdirs();
			}
			f.createNewFile();
			FileOutputStream fos = new FileOutputStream(imgUrl);
			boolean result = bitmap.compress(CompressFormat.JPEG, 100, fos);
			if (result) {
				fos.flush();
				fos.close();
			} else {
				if (f.exists()) {
					f.delete();
				}
			}
		}
	}

	/**
	 * 图片回调接口
	 * 
	 * @author Administrator
	 */
	public interface ImageCallBack {
		void getBitmap(Bitmap bitmap, int position);
	}

	/**
	 * 将指定byte数组转换成16进制字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteToHexString(byte[] b) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			String hex = Integer.toHexString(b[i] & 0xFF);
			if (hex.length() == 1) {
				hex = '0' + hex;
			}
			hexString.append(hex.toUpperCase());
		}
		return hexString.toString();
	}

	/**
	 * md5 加密
	 * 
	 * @param paramString
	 * @return
	 */
	public static String md5(String paramString) {
		String returnStr;
		try {
			MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
			localMessageDigest.update(paramString.getBytes());
			returnStr = byteToHexString(localMessageDigest.digest());
			return returnStr;
		} catch (Exception e) {
			return paramString;
		}
	}

}
