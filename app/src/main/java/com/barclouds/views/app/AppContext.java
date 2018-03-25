package com.barclouds.views.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager;
import android.util.Log;

import com.barclouds.views.domain.AllCategories;
import com.barclouds.views.domain.City;
import com.barclouds.views.domain.Good;
import com.barclouds.views.domain.InfoIndex;
import com.barclouds.views.domain.Seller;
import com.barclouds.views.domain.SpecificCategories;
import com.barclouds.views.domain.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class AppContext extends Application {
	private List<City> cities;
	private List<Good> goods;
	private List<Seller> seller;
	private List<SpecificCategories> specificCategories;
	private List<AllCategories> allCategories;
	private String city;
	private SharedPreferences preferences;
	private User user;
    private InfoIndex infoIndex;
    private Long dataSum;
    private Long infoSum;
	private int screenWidth;
	private int screenHeight;

    public Long getInfoSum() {
        return infoSum;
    }

    public void setInfoSum(Long infoSum) {
        this.infoSum = infoSum;
    }

    public Long getDataSum() {
        return dataSum;
    }

    public void setDataSum(Long dataSum) {
        this.dataSum = dataSum;
    }

	public List<SpecificCategories> getSpecificCategories() {
		return specificCategories;
	}

	public void setSpecificCategories(List<SpecificCategories> specificCategories) {
		this.specificCategories = specificCategories;
	}

	public List<AllCategories> getAllCategories() {
		return allCategories;
	}

	public void setAllCategories(List<AllCategories> allCategories) {
		this.allCategories = allCategories;
	}

	public int getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(int screenWidth) {
		this.screenWidth = screenWidth;
	}

	public int getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(int screenHeight) {
		this.screenHeight = screenHeight;
	}

	public void setCities(List<City> cities) {
		this.cities = cities;
	}

	public List<City> getCities() {
		return cities;
	}
	

	public List<Good> getGoods() {
		return goods;
	}

	public void setGoods(List<Good> goods) {
		this.goods = goods;
	}
	public List<Seller> getSeller() {
		return seller;
	}

	public void setSeller(List<Seller> seller) {
		this.seller = seller;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();

//		String as;
//		try {
//			as = getA();
//			AssetsParser parser = new AssetsParser();
//			List<City> cities = parser.getCities(as);
//			setCities(cities);
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

	}

	public String getA() throws IOException {
		AssetManager assetManager = this.getAssets();
		InputStream is = assetManager.open("city_coordinate.txt");
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int length = -1;
		while ((length = is.read(buffer)) != -1) {
			stream.write(buffer, 0, length);
		}
		return stream.toString();
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public SharedPreferences getPreferences() {
		return preferences;
	}

	public void setPreferences(SharedPreferences preferences) {
        Log.d("BarcloudsDebug","preferences2="+preferences);
		this.preferences = preferences;
	}

	public User getUser() {
        return user;
	}

    public User getPreferencesUser() {
        User users= new User();
        users.setUsername(preferences.getString("username",""));
        users.setPassword(preferences.getString("password",""));
        return users;
    }
	public void setUser(User user) {
		this.user = user;
        Log.d("BarcloudsDebug", "AppContext.setUser:username="+user.getUsername()+"password="+user.getPassword());
		Editor editor = preferences.edit(); //暂时没有初始化SharedPreferences，打开会报错，by sugood
		editor.putString("username", user.getUsername());
		editor.putString("password", user.getPassword());
		editor.commit();

	}

    public InfoIndex getInfoIndex() {
        return infoIndex;
    }

    public void setInfoIndex(InfoIndex infoIndex) {
        this.infoIndex = infoIndex;
    }
}
