package com.barclouds.views.Parser;

import com.barclouds.views.domain.AllCategories;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllCategoriesParser {

	public ArrayList<AllCategories> getAllCategories(Object res){
		ArrayList<AllCategories> allCategories= null;
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("allCategories");
			if (array != null && !array.equals("[]")) {
			allCategories=new ArrayList<AllCategories>();
				for(int i=0;i<array.length();i++){
					AllCategories allCategory=new AllCategories();
					JSONObject object2 = array.optJSONObject(i);
					
					allCategory.setAllCategories_id(object2.optInt("allCategories_id"));
					allCategory.setAllCategories_name(object2.optString("allCategories_name"));
					allCategories.add(allCategory);
				}
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return allCategories;
	}

}
