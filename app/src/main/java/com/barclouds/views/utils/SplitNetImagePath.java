package com.barclouds.views.utils;

public class SplitNetImagePath {
 
	public static String[] splitNetImagePath(String netImagePath){
		String[] strings=netImagePath.split("&");
		return strings;
	}
}
