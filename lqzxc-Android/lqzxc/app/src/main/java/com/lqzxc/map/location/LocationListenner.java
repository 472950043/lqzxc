package com.lqzxc.map.location;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;

/**
 * 自封装的定位监听LocationListenner，实现百度Android SDK API的BDLocationListener接口
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
	 * 重写定位结果返回函数onReceiveLocation，请参考百度Android SDK API手册的BDLocationListener接口
	 * @author QQ472950043
	 **/
	@Override
	public void onReceiveLocation(BDLocation location) {
		if (location == null)
			return;
		mAppContext.mData.latitude = location.getLatitude();
		mAppContext.mData.longitude = location.getLongitude();
		// 如果不显示定位精度圈，将accuracy赋值为0即可
		mAppContext.mData.accuracy = location.getRadius();
		mAppContext.mData.direction = location.getDerect();
		// 返回类型如果是GPS或者网络，说明定位成功
		if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
			// 更新定位数据
			mAppContext.mLocationOverlay.setData(mAppContext.mData);
			// 更新图层数据执行刷新后生效
			mAppContext.mMapView.refresh();
			// 设置定位点
			mAppContext.locatCenter = new GeoPoint( (int) (mAppContext.mData.latitude * 1e6), (int) (mAppContext.mData.longitude * 1e6));
			// 移动地图到定位点
			if (mAppContext.isAutoRequest)
				mAppContext.mMapView.getController().animateTo(mAppContext.locatCenter);
			// 反Geo搜索，弹出定位泡泡mLocationPopup
			mAppContext.mSearch.reverseGeocode(mAppContext.locatCenter);
		} else {
			mAppContext.ToastMessage(mContext, "定位失败，code=" + location.getLocType());
		}
		// 可以再次定位
		mAppContext.mViewUIBtn.locate_button.setEnabled(true);
	}

	public void onReceivePoi(BDLocation poiLocation) {
		if (poiLocation == null) {
			return;
		}
	}
}