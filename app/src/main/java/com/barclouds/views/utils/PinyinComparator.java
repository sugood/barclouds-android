package com.barclouds.views.utils;


import com.barclouds.views.domain.City;

import java.util.Comparator;


public class PinyinComparator implements Comparator<City> {

	public int compare(City lhs, City rhs) {
		String str1 =  PingYinUtil.getPingYin(lhs.getName());
		String str2 =  PingYinUtil.getPingYin(rhs.getName());
		
//		compa
		
		return str1.compareTo(str2);
	}
}
