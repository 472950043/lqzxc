package com.lqzxc.map.location;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.R;

/**
 * 自定义定位模块
 * @author QQ472950043
 **/
public class Location{

	// 定位对象
	LocationClient mLocationClient;
	
	int LatitudeSpan;// 地图当前纬度跨度，上边缘到下边缘
	int LongitudeSpan;// 地图当前经度跨度，左边缘到右边缘
	int minLat;// 下边缘
	int maxLat;// 上边缘
	int minLng;// 左边缘
	int maxLng;// 右边缘

	AppContext mAppContext;
	/**
     * 构造方法，初始化定位模块
     * @author QQ472950043
     */
	public Location(Context mContext){
		mAppContext = (AppContext) mContext.getApplicationContext();
		
		// 初始化定位点view
		mAppContext.location_view = LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_location, null);
		mAppContext.location_title = (TextView) mAppContext.location_view.findViewById(R.id.location_title);
		mAppContext.location_content = (TextView) mAppContext.location_view.findViewById(R.id.location_content);
		// 创建pop对象，注册点击事件监听接口
		mAppContext.mLocationPopup = new PopupOverlay(mAppContext.mMapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				// 在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
			}
		});
		
		// 定位图层初始化
		mAppContext.mLocationOverlay = new LocationOverlay(mAppContext.mMapView);
		mAppContext.mLocationOverlay.setAppContext(mContext);
		// 开启定位图层接受方向数据功能，当定位数据中有方向时，定位图标会旋转至该方向
		mAppContext.mLocationOverlay.enableCompass();
		// 添加定位图层
		mAppContext.mMapView.getOverlays().add(mAppContext.mLocationOverlay);
		
		// 初始化定位对象
		mLocationClient = new LocationClient(mContext);
		mLocationClient.setAK(AppContext.strKey);// key:在开发者网站对应该APP已申请的AccessKey
		// 注册监听函数
		LocationListenner mLocationListenner=new LocationListenner();
		mLocationListenner.setAppContext(mContext);
		mLocationClient.registerLocationListener(mLocationListenner);
		LocationClientOption mOption = new LocationClientOption();
		mOption.setProdName("com.lqzxc");
		mOption.setPriority(LocationClientOption.GpsFirst);
		mOption.setOpenGps(true);
		mOption.setCoorType("bd09ll");
		// 可进行热切换配置参数
		mLocationClient.setLocOption(mOption);
		// 开启定位服务，如果未注册会抛出NullPointerException异常，请开发者捕捉相应的异常信息。
		mLocationClient.start();
		
		mAppContext.mData = new LocationData();
		mAppContext.isAutoRequest = true;
	}
	
	/**
     * 手动触发一次定位请求
     * @author QQ472950043
     */
	public void requestLocation() {
		// TODO Auto-generated method stub
		mAppContext.mViewUIBtn.locate_button.setEnabled(false);
		mLocationClient.requestLocation();
	}
	

	/**
	 * 获取当前屏幕显示的经纬度范围
	 * @author QQ472950043
	 **/
	public void getBorder() {
		// TODO Auto-generated method stub
		GeoPoint mCenter = mAppContext.mMapView.getMapCenter();
		LatitudeSpan = mAppContext.mMapView.getLatitudeSpan();// 地图当前纬度跨度，上边缘到下边缘
		LongitudeSpan = mAppContext.mMapView.getLongitudeSpan();// 地图当前经度跨度，左边缘到右边缘
		minLng = mCenter.getLongitudeE6() - LongitudeSpan / 2;// 左边缘
		minLat = mCenter.getLatitudeE6() - LatitudeSpan / 2;// 下边缘
		maxLng = mCenter.getLongitudeE6() + LongitudeSpan / 2;// 右边缘
		maxLat = mCenter.getLatitudeE6() + LatitudeSpan / 2;// 上边缘
	}
	
	/**
	 * 修改位置图标
	 * @param marker
     * @author QQ472950043
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// TODO Auto-generated method stub
		// 当传入marker为null时，使用默认图标绘制
		mAppContext.mLocationOverlay.setMarker(marker);
		// 修改图层，需要刷新MapView生效
		mAppContext.mMapView.refresh();
	}
	
	/**
	 * 退出时销毁定位
     * @author QQ472950043
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		if (mLocationClient != null)
			mLocationClient.stop();
	}
}