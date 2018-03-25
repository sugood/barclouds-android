package com.barclouds.views.Parser;

import com.barclouds.views.domain.SpecificCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SpecificCategoryParser {

	public ArrayList<SpecificCategories> getSpecificCategory(Object res){
		ArrayList<SpecificCategories> specificCategories= null;
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("specificCategories");
			if (array != null && !array.equals("[]")) {
				specificCategories=new ArrayList<SpecificCategories>();
				for(int i=0;i<array.length();i++){
					SpecificCategories specificCategory=new SpecificCategories();
					JSONObject object2 = array.optJSONObject(i);
					
					specificCategory.setSpecificCategories_id(object2.optInt("specificCategories_id"));  //详细分类ID
					specificCategory.setAllCategories_id(object2.optInt("allCategories_id"));  //全部分类ID
					specificCategory.setSpecificCategories_name(object2.optString("specificCategories_name"));//详细分类地址
					specificCategories.add(specificCategory);
				}
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return specificCategories;
	}
	
}
