package com.barclouds.views.Parser;

import com.barclouds.views.domain.Details;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DetailParser {

	public List<Details> getDetail(String res) {
		List<Details> details = new ArrayList<Details>();
		JSONObject object;
		try {
			object = new JSONObject(res);
			JSONArray array = object.getJSONArray("details");
            for(int i = 0;i<array.length();i++){
            	JSONObject object2 = array.getJSONObject(i);
            	Details details2 = new Details();
            	details2.setDetails_id(Integer.parseInt(object2.optString("details_id")));
            	details2.setUser_id(Integer.parseInt(object2.optString("user_id")));
            	details2.setGood_id(Integer.parseInt(object2.optString("good_id")));
            	details2.setDetails_isPay(Integer.parseInt(object2.optString("details_isPay")));
            	details2.setDetails_prices(Double.parseDouble(object2.optString("details_prices")));
            	details2.setDetails_quantity(Integer.parseInt(object2.optString("details_quantity")));
            	
            	details.add(details2);
            	
            }
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return details;

	}

}
