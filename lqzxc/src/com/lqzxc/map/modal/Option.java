package com.lqzxc.map.modal;

import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * ��λ����Ϣ
 * @author QQ472950043
 **/
public class Option {
	String name;
	String address;
	int ePoiType;// Poi����
	GeoPoint mGeoPoint;// ��γ�ȱ�ʶ
	
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