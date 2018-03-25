package com.barclouds.views.http;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPool {
	
	    private static ThreadPool s = null;
		
		private static ExecutorService service;
		
		public synchronized static ThreadPool getInstance(){
			
			if(s == null){
				s = new ThreadPool();
				//固定创建线程
				int num = Runtime.getRuntime().availableProcessors();
				//双核*2，四核*4
				service = Executors.newFixedThreadPool(num * 2);
			}
			return s;
		}

		/**
		 * 再线程池里面执行我的传进来的任务
		 * @param run
		 */
		public void addTask(Runnable run){
			service.execute(run);
		}
}
