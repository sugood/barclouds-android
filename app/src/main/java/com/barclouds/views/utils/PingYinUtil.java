package com.barclouds.views.utils;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PingYinUtil {
	/**
	 * 将字符串中的中文转化为拼音,其他字符不变
	 * 
	 * @param inputString
	 * @return
	 */
	public static String getPingYin(String inputString) {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		format.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z_]+$");
		Matcher matcher = p.matcher(inputString.substring(0, 1));
		if(matcher.find()){
			char[] input = inputString.trim().toCharArray();
			String output = "";
			
			try {
				for (int i = 0; i < input.length; i++) {
					if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {
						String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
						output += temp[0];
					} else
						output += Character.toString(input[i]);
				}
			} catch (BadHanyuPinyinOutputFormatCombination e) {
				e.printStackTrace();
			}
			return output;
		}else{
			return "";
		}

	}
	
	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
//	public static String converterToFirstSpell(String chines) {
//		String pinyinName = "";
//		char[] nameChar = chines.toCharArray();
//		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
//		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
//		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
//		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
//		
//		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z_]+$");
//		Matcher matcher = p.matcher(chines.substring(0, 1));
//		if(matcher.find()){
//			for (int i = 0; i < nameChar.length; i++) {
//				int ch = nameChar[0];
//				if (ch >= 128) {
//					try {
//						char cha = nameChar[0];
//						pinyinName += PinyinHelper.toHanyuPinyinStringArray(cha, defaultFormat)[0].charAt(0);
//					} catch (BadHanyuPinyinOutputFormatCombination e) {
//						e.printStackTrace();
//					}
//				}
//				else {
//					pinyinName += nameChar[0];
//				}
//			}
//		}else{
//			pinyinName = "#";
//		}
//		return pinyinName;
//	}

	
	
	

	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		for (int i = 0; i < nameChar.length; i++) {
			if (nameChar[i] > 128) {
				try {
					pinyinName += PinyinHelper.toHanyuPinyinStringArray(nameChar[i], defaultFormat)[0].charAt(0);
				} catch (BadHanyuPinyinOutputFormatCombination e) {
					e.printStackTrace();
				}
			} else {
				pinyinName += nameChar[i];
			}
		}
		return pinyinName;
	}

}
