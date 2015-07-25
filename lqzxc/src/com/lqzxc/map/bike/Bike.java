package com.lqzxc.map.bike;

import android.content.Context;

import com.baidu.mapapi.map.OverlayItem;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.modal.BikeStie;

/**
 * 自定义自行车模块
 * @author QQ472950043
 **/
public class Bike {

	Context mContext;
	AppContext mAppContext;

	/**
	 * 构造方法实例化自行车文字图层和自行车标注图层，请参考百度Android SDK
	 * API手册的TextOverlay类和ItemizedOverlay类
	 * @param mContext
	 * @param mMapView
	 * @author QQ472950043
	 */
	public Bike(Context mContext) {
		// TODO Auto-generated method stub
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		// 加入文本图层
		mAppContext.bikeTextOverlay = new TextOverlay(mAppContext.mMapView);
		mAppContext.mMapView.getOverlays().add(mAppContext.bikeTextOverlay);
		// 加入标注点图层
		mAppContext.bikeItemizedOverlay = new BikeItemizedOverlay(mContext.getResources().getDrawable(R.drawable.baidu_map_icon_gcoding), mAppContext.mMapView);
		mAppContext.bikeItemizedOverlay.setAppContext(mContext);
		mAppContext.mMapView.getOverlays().add(mAppContext.bikeItemizedOverlay);
	}

	/**
	 * 添加所有自行车的网点
	 * @author QQ472950043
	 **/
	public void addBikeSiteList() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mAppContext.mBikeSties.size(); i++) {
			// 准备overlay数据
			BikeStie mBikeStie = mAppContext.mBikeSties.get(i);
			// 添加一个点
			addBike(mBikeStie.getmGeoPoint(), mBikeStie.getId() + "." + mBikeStie.getNetName() ,mBikeStie.getNetStatus());
		}
		// 显示这两个图层，在MapListener中监听，改变其显示和隐藏状态
		mAppContext.isShowBikeText = true;
		mAppContext.isShowBikeItemized = true;
		System.out.println("bike网点全部添加成功");
		// 图层添加完毕，刷新显示
		mAppContext.mMapView.refresh();
	}
	
	/**
	 * 添加自行车文字图层和自行车标注图层，请参考百度Android SDK API手册的TextItem类和OverlayItem类
	 * @param mGeoPoint
	 * @param title
	 * @author QQ472950043
	 **/
	public void addBike(GeoPoint mGeoPoint, String title, String netStatus) {
		// TODO Auto-generated method stub
		// 添加bike文本图层
		TextItem bikeTextItem;
		if(netStatus.equals("正常")){
			bikeTextItem = BikeTextItem.getBikeTextItem(mGeoPoint, title);
		}else{
			bikeTextItem = BikeTextItem.getBikeTextItem2(mGeoPoint, title);
		}
		mAppContext.bikeTextOverlay.addText(bikeTextItem);// 注意： 同一个item只能add一次
		// 添加自定义标注图层
		OverlayItem bikeOverlayItem = new OverlayItem(mGeoPoint, title, "");
		if(!netStatus.equals("正常")){
			 //设置overlay图标，如不设置，则使用创建ItemizedOverlay时的默认图标.
			bikeOverlayItem.setMarker(mContext.getResources().getDrawable(R.drawable.baidu_map_icon_gcoding2));
		}
		mAppContext.bikeItemizedOverlay.addItem(bikeOverlayItem);
	}
}