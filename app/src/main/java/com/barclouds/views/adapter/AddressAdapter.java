package com.barclouds.views.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.barclouds.R;
import com.barclouds.views.domain.Address;

import java.util.List;

public class AddressAdapter extends BaseAdapter {
	private Context context;
	private List<Address> addresses;

	public AddressAdapter(Context context, List<Address> addresses) {
		this.context = context;
		this.addresses = addresses;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return addresses.size();
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

	public void setData(List<Address> addresses) {
		this.addresses = addresses;
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		HolderView holderView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.address_cell, null);
			holderView = new HolderView();
			holderView.name = (TextView) convertView.findViewById(R.id.name);
			holderView.phone = (TextView) convertView.findViewById(R.id.phone);
			holderView.area = (TextView) convertView.findViewById(R.id.area);
			holderView.address = (TextView) convertView
					.findViewById(R.id.address);
			holderView.postcode = (TextView) convertView
					.findViewById(R.id.postcode);
			convertView.setTag(holderView);
		} else {
			holderView = (HolderView) convertView.getTag();
		}
		holderView.name.setText("姓名 ：" + addresses.get(position).getName());
		holderView.phone.setText("电话 ：" + addresses.get(position).getPhone());
		holderView.area.setText("省市 ：" + addresses.get(position).getProvince()
				+ addresses.get(position).getCity()
				+ addresses.get(position).getArea());
        holderView.address.setText("地址 ："+addresses.get(position).getAddress());
        holderView.postcode.setText("邮编 ："+addresses.get(position).getPostcode());
		return convertView;
	}

	class HolderView {
		TextView name;
		TextView phone;
		TextView area;
		TextView postcode;
		TextView address;
	}

}
