package com.barclouds.views.Parser;

import com.barclouds.views.domain.Address;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AddressParser {
	
	
    public List<Address> getAddress(String res){
    	List<Address> addresses = new ArrayList<Address>();
    	try {
			JSONObject object = new JSONObject(res);
			JSONArray array = object.getJSONArray("address");
			for(int i = 0; i<array.length();i++){
				JSONObject object2 = array.getJSONObject(i);
				String name = object2.optString("name");
				String phone = object2.optString("phone");
				String username = object2.optString("username");
				String province = object2.optString("province");
				String city = object2.optString("city");
				String area = object2.optString("area");
				String postcode = object2.optString("postcode");
				String address = object2.optString("address");
				
				Address address2 = new Address();
				address2.setName(name);
				address2.setPhone(phone);
				address2.setUsername(username);
				address2.setProvince(province);
				address2.setCity(city);
				address2.setArea(area);
				address2.setPostcode(postcode);
				address2.setAddress(address);
				addresses.add(address2);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	return addresses;
    }
}
