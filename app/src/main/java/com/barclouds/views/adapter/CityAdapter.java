package com.barclouds.views.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import com.barclouds.R;
import com.barclouds.views.domain.City;
import com.barclouds.views.utils.PinyinComparator;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CityAdapter extends BaseAdapter implements SectionIndexer {

	private Context context;
	private List<City> cities;
	
	
	
	
	
	
	
	
	

	public CityAdapter(Context context, List<City> cities) {
		this.cities = cities;
		this.context = context;
		Collections.sort(cities, new PinyinComparator());
	}

	public int getCount() {
		return cities.size();
	}

	public Object getItem(int position) {
		return cities.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		HolderView holder = null;
		if (convertView == null) {
			holder = new HolderView();
			convertView = LayoutInflater.from(context).inflate(R.layout.select_city_item, null);
			holder.title = (TextView) convertView.findViewById(R.id.tv_citiy_title);
			holder.name = (TextView) convertView.findViewById(R.id.tv_city_name);
			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		
		City city = cities.get(position);
		
		String catalog = converterToFirstSpell(city.getName().substring(0,1));
		if(position == 0){
			holder.title.setVisibility(View.VISIBLE);
			holder.title.setText(catalog);
		}else{

			String name = cities.get(position - 1).getName();
			String lastCatalogString = converterToFirstSpell(name.substring(0,1));
			
			if(catalog.equals(lastCatalogString)){
				holder.title.setVisibility(View.GONE);
			}else{
				holder.title.setVisibility(View.VISIBLE);
				holder.title.setText(catalog);
			}
		}

		holder.name.setText(city.getName());
		
		return convertView;
	}

	public Object[] getSections() {
		return null;
	}

	public int getPositionForSection(int section) {
		
		
		for (int i = 0; i < cities.size(); i++) {
			String l = converterToFirstSpell(cities.get(i).getName()).substring(0, 1);
			char firstChar = l.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}

	public int getSectionForPosition(int position) {
		return 0;
	}

	class HolderView {
		TextView title;
		TextView name;
	}
	
	/**
	 * 汉字转换位汉语拼音首字母，英文字符不变
	 * 
	 * @param chines
	 *            汉字
	 * @return 拼音
	 */
	public static String converterToFirstSpell(String chines) {
		String pinyinName = "";
		char[] nameChar = chines.toCharArray();
		HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
		defaultFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
		defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
		defaultFormat.setVCharType(HanyuPinyinVCharType.WITH_V);
		
		Pattern p = Pattern.compile("^[\u4E00-\u9FA5A-Za-z_]+$");
		Matcher matcher = p.matcher(chines.substring(0, 1));
		if(matcher.find()){
			for (int i = 0; i < nameChar.length; i++) {
				int ch = nameChar[0];
				if (ch >= 128) {
					try {
						char cha = nameChar[0];
						pinyinName += PinyinHelper.toHanyuPinyinStringArray(cha, defaultFormat)[0].charAt(0);
					} catch (BadHanyuPinyinOutputFormatCombination e) {
						e.printStackTrace();
					}
				}
				else {
					pinyinName += nameChar[0];
				}
			}
		}else{
			pinyinName = "#";
		}
		return pinyinName;
	}
}
