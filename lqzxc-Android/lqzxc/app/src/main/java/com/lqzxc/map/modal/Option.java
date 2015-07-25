package com.lqzxc.map.modal;

import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 定位点信息
 * @author QQ472950043
 **/
public class Option {
	String name;
	String address;
	int ePoiType;// Poi类型
	GeoPoint mGeoPoint;// 经纬度标识
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getePoiType() {
		return ePoiType;
	}
	public void setePoiType(int ePoiType) {
		this.ePoiType = ePoiType;
	}
	public GeoPoint getmGeoPoint() {
		return mGeoPoint;
	}
	public void setmGeoPoint(GeoPoint mGeoPoint) {
		this.mGeoPoint = mGeoPoint;
	}
}