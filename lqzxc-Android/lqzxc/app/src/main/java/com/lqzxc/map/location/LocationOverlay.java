package com.lqzxc.map.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;

/**
 * 自封装的定位图层LocationOverlay，继承百度Android SDK API的MyLocationOverlay类
 * @author QQ472950043
 **/
public class LocationOverlay extends MyLocationOverlay {

	/**
	 * 构造方法实例化定位图层LocationOverlay，请参考百度Android SDK API手册的MyLocationOverlay类
	 * @author QQ472950043
	 **/
	public LocationOverlay(MapView mapView) {
		super(mapView);
		// TODO Auto-generated constructor stub
	}
	
	Context mContext;
	AppContext mAppContext;

	public void setAppContext(Context mContext) {
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
	}
	
	/**
	 * 重写dispatchTap实现点击处理，请参考百度Android SDK API手册的MyLocationOverlay类
	 * @author QQ472950043
	 */
	@Override
	protected boolean dispatchTap() {
		// TODO Auto-generated method stub
		// 处理点击事件，弹出定位图层泡泡，显示定位内容不变
		if(mAppContext.mLocationPopup!=null)
			showPopup(mAppContext.location_view,mAppContext.locatCenter,5);
		return true;
	}

	/**
	 * 弹出图片Popup
	 * @param view - 弹窗内容
	 * @param point - 弹窗的位置(锚点在窗口的中下方向。水平方向：Center；垂直方向：Bottom)
	 * @param yOffset - 弹窗在y轴上的偏移量,取值范围yOffset>=0。单位：像素
	 * @author QQ472950043
	 */
	public void showPopup(View view, GeoPoint point, int yOffset) {
		//从view 得到图片
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);

        mAppContext.mLocationPopup.showPopup(bitmap,point,yOffset);
	}
}