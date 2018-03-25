package com.barclouds.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.barclouds.R;

public class MapItemAdapter extends BaseAdapter {
	
	Context mContext=null;
	String[] strings1={"入住1晚,标准电脑房/单人电脑房2选1，可连续入住","豪华套房入住1晚，可连续入住，免费WIFI","入住1晚，标准间/单人间2选1，可连续入住"};
	String[] strings2={"158元","122元","86元"};
	
	public MapItemAdapter(Context context){
		this.mContext=context;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return strings1.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder=null;
		if(convertView==null){
			convertView= LayoutInflater.from(mContext).inflate(R.layout.map_overlay_item, null);
			holder=new ViewHolder();
			holder.textview_map_seller_content=(TextView) convertView.findViewById(R.id.textview_map_seller_content);
			holder.textview_map_seller_curPrice=(TextView) convertView.findViewById(R.id.textview_map_seller_curPrice);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.textview_map_seller_content.setText(strings1[position]);
		holder.textview_map_seller_curPrice.setText(strings2[position]);
		return convertView;
	}
	
	class ViewHolder{
		TextView textview_map_seller_content;
		TextView textview_map_seller_curPrice;
	}

}
