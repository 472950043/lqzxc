package com.lqzxc.map.measure;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * 测距Measure工具
 * @param 1.在onCreate函数中用构造方法实例化Measure mMeasure=new Measure(mMapView);
 * @param 2.开始调用mMeasure.start();
 * @param 3.增加中间点调用mMeasure.addPoint(GeoPoint mGeoPoint);
 * @param 4.结束调用mMeasure.finish();
 * @author QQ472950043
 */
public class Measure{
	/**
	 * 地图View
	 */
	MapView mMapView;
	/**
	 * 是否测距模式
	 */
	public boolean isMeasuringDistance;
	/**
	 * 测距总长
	 */
	public double distanceSum;
	/**
	 * 测距缓存路径点
	 */
	List<GeoPoint> mGeoPoints;
	/**
	 * 测距文本图层
	 */
	TextOverlay pointTextOverlay;
	/**
	 * 测距点图层
	 */
	GraphicsOverlay pointGraphicsOverlay;
	/**
	 * 测距线图层
	 */
	GraphicsOverlay lineGraphicsOverlay;
	/**
	 * 构造方法实例化测距Measure工具，请参考百度Android SDK API手册的TextOverlay类和GraphicsOverlay类
	 * @param mMapView
	 * @author QQ472950043
	 */
	public Measure(MapView mMapView){
		// 缓存路径点
		mGeoPoints = new ArrayList<GeoPoint>();
		// 添加测距文本图层
		pointTextOverlay = new TextOverlay(mMapView);
		mMapView.getOverlays().add(pointTextOverlay);
		// 添加测距点图层
		pointGraphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(pointGraphicsOverlay);
		// 添加测距线图层
		lineGraphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(lineGraphicsOverlay);
		// 初始化测距工具变量
		isMeasuringDistance = false;
		distanceSum = 0.0;
		this.mMapView = mMapView;
	}

	/**
	 * 开始测距
	 */
	public void start(){
		// TODO Auto-generated method stub
		//开启测距模式
		isMeasuringDistance = true;
		//禁用地图的双击放大
		mMapView.setDoubleClickZooming(false);
	}

	/**
	 * 用addPoint(GeoPoint mGeoPoint)方法在mGeoPoint点上增加文本、点、线，
	 * 请参考百度Android SDK API手册的TextItem类和Graphic类
	 */
	public void addPoint(GeoPoint mGeoPoint) {
		// TODO Auto-generated method stub
		//准备数据
		String text;
		if (mGeoPoints.isEmpty()) {//缓存路径点为空的时候是起点
			text= "起点";
		}
		else{
			double distance = DistanceUtil.getDistance(mGeoPoints.get(mGeoPoints.size() - 1), mGeoPoint);
			distanceSum += distance;
	        DecimalFormat df = new DecimalFormat("#0.00");
	        text=df.format(distanceSum/1000)+"公里";
			//从第二个点开始添加线
			Graphic lineGraphic=MeasureGraphics.getLineGraphic(mGeoPoints.get(mGeoPoints.size() - 1), mGeoPoint);
			lineGraphicsOverlay.setData(lineGraphic);
		}
		//添加起点文本
		TextItem pointTextItem = MeasureTextItem.getMeasureTextItem(mGeoPoint, text);
		pointTextOverlay.addText(pointTextItem);
    	//添加起点
		Graphic getPointGraphic = MeasureGraphics.getPointGraphic(mGeoPoint);
		pointGraphicsOverlay.setData(getPointGraphic);
		//刷新地图
		mMapView.refresh();
		//缓存路径点
		mGeoPoints.add(mGeoPoint);
	}

	/**
	 * 结束测距
	 */
	public void finish(){
		// TODO Auto-generated method stub
		//清除缓存的文本
		pointTextOverlay.removeAll();
		//清除缓存的图形（点和线）
		pointGraphicsOverlay.removeAll();
		lineGraphicsOverlay.removeAll();
		//初始化测距工具变量，便于下次测距
		isMeasuringDistance = false;
		distanceSum = 0;
		mGeoPoints.clear();
		//刷新地图
		mMapView.refresh();
	}
	
//	//MKMapTouchListener 注册地图点击等事件
//	mMapView.regMapTouchListner(new MKMapTouchListener() {
//		@Override
//		public void onMapClick(GeoPoint geoPoint) {
//			// TODO Auto-generated method stub
//			touchType = "单击";
//			if (mMeasure.isMeasuringDistance) {
//				//一步步添加测距的点
//				mMeasure.addPoint(geoPoint);
//			}
//		}
//
//		@Override
//		public void onMapDoubleClick(GeoPoint geoPoint) {
//			// TODO Auto-generated method stub
//			touchType = "双击";
//			if (mMeasure.isMeasuringDistance) {
//				ApiClient.ToastMessage(MapSearch.this, "测距结束");
//				mMeasure.finish();
//				//启用双击放大
//		    	mMapView.setDoubleClickZooming(true);
//			}
//		}
	
//  //如果要设置poi点也能测距，需监听并重写regMapViewListener中的onClickMapPoi事件
//	@Override
//	public void onClickMapPoi(MapPoi mapPoiInfo) {
//		// TODO Auto-generated method stub
//		if (mapPoiInfo != null) {
//			if (mMeasure.isMeasuringDistance) {
//				// 一步步添加测距的点
//				mMeasure.addPoint(mapPoiInfo.geoPt);
//				ApiClient.ToastMessage(MapSearch.this, mapPoiInfo.strText);
//			} else {
//				mMapController.animateTo(mapPoiInfo.geoPt);
//				ApiClient.ToastMessage(MapSearch.this, mapPoiInfo.strText);
//			}
//		}
//	}
}