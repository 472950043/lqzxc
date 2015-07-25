package com.lqzxc.map.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;

/**
 * �Է�װ�Ķ�λ����LocationListenner��ʵ�ְٶ�Android SDK API��BDLocationListener�ӿ�
 * @author QQ472950043
 **/
public class LocationListenner implements BDLocationListener {
	Context mContext;
	AppContext mAppContext;

	public void setAppContext(Context mContext) {
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
	}
	
	/**
	 * ��д��λ������غ���onReceiveLocation����ο��ٶ�Android SDK API�ֲ��BDLocationListener�ӿ�
	 * @author QQ472950043
	 **/
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		mAppContext.mData.latitude = location.getLatitude();
		mAppContext.mData.longitude = location.getLongitude();
		// �������ʾ��λ����Ȧ����accuracy��ֵΪ0����
		mAppContext.mData.accuracy = location.getRadius();
		mAppContext.mData.direction = location.getDerect();
		// �������������GPS�������磬˵����λ�ɹ�
		if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
			// ���¶�λ����
			mAppContext.mLocationOverlay.setData(mAppContext.mData);
			// ����ͼ������ִ��ˢ�º���Ч
			mAppContext.mMapView.refresh();
			// ���ö�λ��
			mAppContext.locatCenter = new GeoPoint( (int) (mAppContext.mData.latitude * 1e6), (int) (mAppContext.mData.longitude * 1e6));
			// �ƶ���ͼ����λ��
			if (mAppContext.isAutoRequest)
				mAppContext.mMapView.getController().animateTo(mAppContext.locatCenter);
			// ��Geo������������λ����mLocationPopup
			mAppContext.mSearch.reverseGeocode(mAppContext.locatCenter);
		} else {
			mAppContext.ToastMessage(mContext, "��λʧ�ܣ�code=" + location.getLocType());
		}
		// �����ٴζ�λ
		mAppContext.mViewUIBtn.locate_button.setEnabled(true);
	}

	public void onReceivePoi(BDLocation poiLocation) {
		if (poiLocation == null) {
			return;
		}
	}
}