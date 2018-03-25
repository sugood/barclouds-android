package com.barclouds.views.domain;

public class Details {
		
		private int  details_id;
		private int  user_id;
		private int  seller_id;
		private int  good_id;
		private String details_time;
		private  int details_quantity;
		private double details_prices;
		private int details_isPay;

		public int getDetails_id() {
			return details_id;
		}
		public void setDetails_id(int detailsId) {
			details_id = detailsId;
		}

		public int getUser_id() {
			return user_id;
		}
		public void setUser_id(int userId) {
			user_id = userId;
		}
		public int getSeller_id() {
			return seller_id;
		}
		public void setSeller_id(int sellerId) {
			seller_id = sellerId;
		}
		
		public int getGood_id() {
			return good_id;
		}
		public void setGood_id(int good_id) {
			this.good_id = good_id;
		}
		public String getDetails_time() {
			return details_time;
		}
		public void setDetails_time(String detailsTime) {
			details_time = detailsTime;
		}
		public int getDetails_quantity() {
			return details_quantity;
		}
		public void setDetails_quantity(int detailsQuantity) {
			details_quantity = detailsQuantity;
		}
		public double getDetails_prices() {
			return details_prices;
		}
		public void setDetails_prices(double detailsPrices) {
			details_prices = detailsPrices;
		}
		public int getDetails_isPay() {
			return details_isPay;
		}
		public void setDetails_isPay(int detailsIsPay) {
			details_isPay = detailsIsPay;
		}

}
