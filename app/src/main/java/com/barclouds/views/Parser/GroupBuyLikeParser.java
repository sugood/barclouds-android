package com.barclouds.views.Parser;

import com.barclouds.views.domain.Good;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupBuyLikeParser {

	public ArrayList<Good> getGoods(String res) throws JSONException {
		ArrayList<Good> goods = null;
		JSONObject object = new JSONObject(res);
		JSONArray array = object.getJSONArray("goods");
		if (array != null && !array.equals("[]")) {
			goods = new ArrayList<Good>();
			for (int i = 0; i < array.length(); i++) {
				Good good = new Good();
				JSONObject object2 = array.optJSONObject(i);
				good.setGoods_id(Integer.parseInt(object2.optString("goods_id")));
				good.setSeller_id(Integer.parseInt(object2.optString("seller_id")));

				good.setGoods_name(object2.optString("goods_name"));

				good.setAllCategories_id(Integer.parseInt(object2
                        .optString("allCategories_id")));

				good.setGoods_isLatest(Integer.parseInt(object2
                        .optString("goods_isLatest")));

				good.setGoods_isFreeOrder(Integer.parseInt(object2
                        .optString("goods_isFreeOrder")));

				good.setGoods_saleInfo(object2.optString("goods_saleInfo"));

				good.setGoods_salerNum(Integer.parseInt(object2
                        .optString("goods_saleNum")));

				good.setGoods_oldPrice(Float.parseFloat(object2
                        .optString("goods_oldPrice")));

				good.setGoods_price(Float.parseFloat(object2
                        .optString("goods_price")));

				good.setGoods_picturePath(object2
						.optString("goods_picturePath"));
                good.setSeller_id(object2.optInt("seller_id"));
				goods.add(good);

			}

		}

		return goods;
	}
}
