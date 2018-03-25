package com.barclouds.views.Parser;

import com.barclouds.views.domain.Seller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MerchantParser {
	public ArrayList<Seller> getMerchant(Object res){
		ArrayList<Seller> merchant= null;
		JSONObject object;
		try {
			object = new JSONObject(res.toString());
			JSONArray array = object.getJSONArray("sellers");
			if (array != null && !array.equals("[]")) {
				merchant=new ArrayList<Seller>();
				for(int i=0;i<array.length();i++){
					Seller seller=new Seller();
					JSONObject object2 = array.optJSONObject(i);
					
					seller.setSeller_id(object2.optInt("seller_id"));  
					seller.setSeller_name(object2.optString("seller_name"));  //商家名
					seller.setSeller_address(object2.optString("seller_address"));//商家地址
					seller.setSeller_phone(object2.optString("seller_phone"));//商家联系方式
					seller.setSeller_isSale(object2.optInt("seller_isSale"));   //是否团购
					seller.setSeller_comment(object2.optString("seller_comment"));
					seller.setSeller_latitude(object2.getDouble("seller_latitude"));
					seller.setSeller_longitude(object2.optDouble("seller_longitude"));
					seller.setSeller_picture(object2.getString("seller_picture"));
					seller.setSeller_rank(object2.getInt("seller_rank"));
					seller.setSeller_distance(object2.getInt("seller_distance"));
					merchant.add(seller);
				}
				
			}
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
		return merchant;
	}
	
}
