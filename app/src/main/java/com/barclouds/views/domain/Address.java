package com.barclouds.views.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Address implements Serializable, Parcelable {
	private String name;
	private String username;
	private String province;
	private String city;
	private String postcode;
	private String phone;
	private String area;
	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPostcode() {
		return postcode;
	}

	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(this.name);
		arg0.writeString(this.phone);
		arg0.writeString(this.province);
		arg0.writeString(this.city);
		arg0.writeString(this.area);
		arg0.writeString(this.address);
		arg0.writeString(this.postcode);
		arg0.writeString(this.username);
	}

	public static final Creator<Address> CREATOR = new Creator<Address>() {

		@Override
		public Address[] newArray(int size) {
			return new Address[size];
		}

		@Override
		public Address createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			Address address = new Address();
			address.setName(source.readString());
			address.setPhone(source.readString());
			address.setProvince(source.readString());
			address.setCity(source.readString());
			address.setArea(source.readString());
			address.setAddress(source.readString());
			address.setPostcode(source.readString());
			address.setUsername(source.readString());
			return address;
		}
	};

}
