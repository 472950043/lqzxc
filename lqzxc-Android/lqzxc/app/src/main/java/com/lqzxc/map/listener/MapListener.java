package com.lqzxc.map.listener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKMapStatusChangeListener;
import com.baidu.mapapi.map.MKMapTouchListener;
import com.baidu.mapapi.map.MKMapViewListener;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.map.measure.Measure;

/**
 * 地图事件监听模块
 * @author QQ472950043
 */
public class MapListener {

	Context mContext;
	AppContext mAppContext;
	public Measure mMeasure;
	String touchType;

	public MapListener(Context mContext) {
		// TODO Auto-generated method stub
		this.mContext = mContext;
        mAppContext = (AppContext) mContext.getApplicationContext();
        //初始化测距功能
		mMeasure = new Measure(mAppContext.mMapView);

		regMapStatusChangeListener();
		regMapTouchListner();
		regMapViewListener();
	}

	public void regMapStatusChangeListener() {
		// TODO Auto-generated method stub
		// MKMapStatusChangeListener 注册地图状态监听者。
		mAppContext.mMapView.regMapStatusChangeListener(new MKMapStatusChangeListener() {
			//实时监听地图状态改变
			@Override
			public void onMapStatusChange(MKMapStatus mapStatus) {
				// TODO Auto-generated method stub
				setMapConfig(mapStatus);
				try{
					if (mapStatus.zoom >= 15 && !mAppContext.isShowBikeText) {
						mAppContext.mMapView.getOverlays().add(mAppContext.bikeTextOverlay);
						mAppContext.isShowBikeText = true;
						mAppContext.mMapView.refresh();
					} else if (mapStatus.zoom < 15 && mAppContext.isShowBikeText) {
						mAppContext.mMapView.getOverlays().remove(mAppContext.bikeTextOverlay);
						mAppContext.isShowBikeText = false;
						mAppContext.mMapView.refresh();
					}
					if (mapStatus.zoom >= 13 && !mAppContext.isShowBikeItemized) {
						mAppContext.mMapView.getOverlays().add(mAppContext.bikeItemizedOverlay);
						mAppContext.isShowBikeItemized = true;
						mAppContext.mMapView.refresh();
					} else if (mapStatus.zoom < 13 && mAppContext.isShowBikeItemized) {
						mAppContext.mMapView.getOverlays().remove(mAppContext.bikeItemizedOverlay);
						mAppContext.isShowBikeItemized = false;
						mAppContext.mMapView.refresh();
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		    
		    /**
		     * 记录地图的缩放级别、旋转角度、俯视角度
		     * @param mapStatus
			 * @author QQ472950043
		     */
			public void setMapConfig(MKMapStatus mapStatus) {  
				mAppContext.mMapStatus=mapStatus;
			    //记录到setting中
				mAppContext.setting.edit().putString("preferences_zoomlevel", ""+mapStatus.zoom).commit();
				mAppContext.setting.edit().putString("preferences_rotateangle", ""+mapStatus.rotate).commit();
				mAppContext.setting.edit().putString("preferences_overlookangle", -mapStatus.overlooking+"").commit();
			}
		});
	}

	/**
	 * MKMapTouchListener 注册地图点击等事件
	 */
	public void regMapTouchListner() {
		// TODO Auto-generated method stub
		mAppContext.mMapView.regMapTouchListner(new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "单击";
				if (mMeasure.isMeasuringDistance) {
					// 一步步添加测距的点
					mMeasure.addPoint(geoPoint);
				}
				if (mAppContext.mLocationPopup != null) {// 消隐pop
					mAppContext.mLocationPopup.hidePop();
				}
			}

			@Override
			public void onMapDoubleClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "双击";
				if (mMeasure.isMeasuringDistance) {
					mAppContext.ToastMessage(mContext, "测距结束");
					mMeasure.finish();
					// 启用双击放大
					mAppContext.mMapView.setDoubleClickZooming(true);
				}
			}

			@Override
			public void onMapLongClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "长按";
				// mCenter = point;
			}
		});
	}

	/**
	 * MKMapViewListener 用于处理地图操作完成事件回调
	 * MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	 */
	public void regMapViewListener() {
		// TODO Auto-generated method stub
        //设置地图是否响应点击事件
		mAppContext.mMapView.getController().enableClick(true);
		mAppContext.mMapView.regMapViewListener(mAppContext.mBMapManager, new MKMapViewListener() {
			//在此处理地图移动完成回调 缩放，平移等操作完成后，此回调被触发
			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}

			//地图完成带动画的操作（如: animationTo()）后，此回调被触发
			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
			}

			//在此处理地图载完成事件
			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				System.out.println("onMapLoadFinish:地图加载完成");
			}

			/**
			 * 在此处理底图poi点击事件，显示底图poi名称并移动至该点 设置过：
			 * mMapController.enableClick(true); 时，此回调才能被触发
			 */
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				if (mapPoiInfo != null) {
					if (mMeasure.isMeasuringDistance) {
						// 一步步添加测距的点
						mMeasure.addPoint(mapPoiInfo.geoPt);
						mAppContext.ToastMessage(mContext, mapPoiInfo.strText);
					} else {
						mAppContext.mMapView.getController().animateTo(mapPoiInfo.geoPt);
						mAppContext.ToastMessage(mContext, mapPoiInfo.strText);
						mAppContext.enNode=new MKPlanNode();
						mAppContext.enNode.pt = mapPoiInfo.geoPt;
						mAppContext.enNode.name= mapPoiInfo.strText;
					}
				}
			}

			/**
			 * 当调用过 mMapView.getCurrentMap()后，此回调会被触发 可在此保存截图至存储设备
			 */
			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				// 必须确保文件夹路径存在，否则保存
				File file = new File(AppContext.dirPath, "map.png");
				if (!file.exists()) {
					File mDirPath = file.getParentFile(); 
					mDirPath.mkdirs();
				}
				FileOutputStream out;
				try {
					out = new FileOutputStream(file);
					if (b.compress(Bitmap.CompressFormat.PNG, 70, out)) {
						out.flush();
						out.close();
					}
					mAppContext.ToastMessage(mContext,"屏幕截图成功，图片存在: " + file.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}