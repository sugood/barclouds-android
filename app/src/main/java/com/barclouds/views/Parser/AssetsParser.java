package com.barclouds.views.Parser;

import com.barclouds.views.domain.City;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AssetsParser {
	
	
	public List<City> getCities(String res){
		List<City> cities = new ArrayList<City>();
		try {
			JSONArray array = new JSONArray(res);
			for(int i = 0;i<array.length();i++){
				JSONObject object = array.getJSONObject(i);
				String cityname = object.optString("cityname");
				City city = new City();
				city.setName(cityname);
				cities.add(city);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cities;
	}

}
