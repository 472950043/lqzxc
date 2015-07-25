package com.lqzxc.map.bike;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.baidu.mapapi.map.ItemizedOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;

/**
 * 自封装的自行车标注点图层BikeItemizedOverlay，继承自百度Android SDK API的ItemizedOverlay<OverlayItem>类
 * @author QQ472950043
 **/
public class BikeItemizedOverlay extends ItemizedOverlay<OverlayItem>{
	
	/**
	 * 自行车标注点图层BikeItemizedOverlay构造方法，继承自百度Android SDK API的ItemizedOverlay<OverlayItem>类
	 * @author QQ472950043
	 **/
	public BikeItemizedOverlay(Drawable defaultMarker, MapView mMapView) {
		super(defaultMarker, mMapView);
		// TODO Auto-generated method stub
	}

	Context mContext;
	AppContext mAppContext;

	public void setAppContext(Context mContext) {
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
	}
	
	@Override
	public boolean onTap(int index) {
		// TODO Auto-generated method stub
		mAppContext.mBikeStie = mAppContext.mBikeSties.get(index);
		mAppContext.enNode=new MKPlanNode();
		mAppContext.enNode.pt = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.enNode.name= mAppContext.mBikeStie.getNetName();
		// 设置地图中心点
		mAppContext.bikeMapStatus.targetGeo = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.mMapView.getController().setMapStatusWithAnimation(mAppContext.bikeMapStatus, 500);
		mAppContext.mApiClient.visitUpdate(mAppContext.mBikeSties.get(index).getMid());
		// 显示自行车网点窗口
		mAppContext.widget_window.setVisibility(View.VISIBLE);
		mAppContext.mViewWindow.initViewWindow();
//		mAppContext.ToastMessage(mContext,mAppContext.mBikeSties.get(index).getId() + "." + mAppContext.mBikeSties.get(index).getNetName());
		return true;
	}
	
	@Override
	public boolean onTap(GeoPoint pt, MapView mMapView) {
		// TODO Auto-generated method stub
		return false;
	}
}