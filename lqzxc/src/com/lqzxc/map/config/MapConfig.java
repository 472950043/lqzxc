package com.lqzxc.map.config;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.preference.PreferenceManager;
import android.view.View;

import com.baidu.mapapi.map.MKMapStatus;
import com.lqzxc.AppContext;
import com.lqzxc.R;
/**
 * 自定义地图配置模块
 * @author QQ472950043
 */
public class MapConfig{

	//初始化菜单全局控制变量
	AppContext mAppContext;
	Context mContext;
	
	/**
	 * 构造方法实例化自定义地图配置模块
	 * @author QQ472950043
	 */
	public MapConfig(Context mContext){
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		mAppContext.setting = PreferenceManager.getDefaultSharedPreferences(mContext);
		mAppContext.mMapStatus = new MKMapStatus();
		
	}

	/**
	 * 设置地图配置
	 * @author QQ472950043
	 */
	public void setMapConfig() {
		// TODO Auto-generated method stub
    	//设置底图模式
		setMapMode();
		//设置地图基本控制
		setMapControl();
        //设置地图UI手势
		setMapUISetting();
        //设置地图UI控制功能
		setMapUIControl();
	}
	
	/**
	 * 设置底图显示模式
	 * @author QQ472950043
	 */
	public void setMapMode() {
		// TODO Auto-generated method stub
		//当其他地方需要使用配置时，可以直接调用preference.getXXX()方法来获取配置信息（preference为xml文件名）。
		//设置街道图和卫星图
		if("normal".equals(mAppContext.setting.getString("list_preferences_layers", "normal")))
			mAppContext.mMapView.setSatellite(false);
		else if("statellite".equals(mAppContext.setting.getString("list_preferences_layers", "normal")))
			mAppContext.mMapView.setSatellite(true);
		//设置交通图
		mAppContext.mMapView.setTraffic(mAppContext.setting.getBoolean("preferences_layers_traffic", false));
		//设置允许横屏
		if(mAppContext.setting.getBoolean("preferences_sensor", true)){
			((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		else{
			((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	/**
	 *  地图操作：缩放级别、旋转角度、俯视角度
	 * @author QQ472950043
	 */
	public void setMapControl() {
		// TODO Auto-generated method stub
		String errorMsg="";
		//V2.2.0以后可同时设置地图等级、地图中心点、旋转角度等
		try {
			//处理缩放 sdk 缩放级别范围： [3.0,19.0]
			errorMsg="请输入正确的缩放级别";
			mAppContext.mMapStatus.zoom = Float.parseFloat(mAppContext.setting.getString("preferences_zoomlevel", "16"));
			//处理旋转 旋转角范围： -180 ~ 180 ,0 ~ 360 均可 单位：度   逆时针旋转
			errorMsg="请输入正确的旋转角度";
			mAppContext.mMapStatus.rotate = Integer.parseInt(mAppContext.setting.getString("preferences_rotateangle", "0"));
			//处理俯视 俯角范围： -45 ~ 0 , 单位： 度
			errorMsg="请输入正确的俯视角度";
			mAppContext.mMapStatus.overlooking = -Integer.parseInt(mAppContext.setting.getString("preferences_overlookangle", "0"));
			
			mAppContext.mMapView.getController().setMapStatusWithAnimation(mAppContext.mMapStatus, 1000);
//			mAppContext.ToastMessage(mContext, "zoom"+mAppContext.mMapStatus.zoom+"rotate"+mAppContext.mMapStatus.rotate+"overlooking"+mAppContext.mMapStatus.overlooking);
		} catch (NumberFormatException e) {
			mAppContext.ToastMessage(mContext, errorMsg);
		}
	}
	
	/**
	 *  3D手势：缩放、平移、双击放大、旋转、俯视
	 * @author QQ472950043
	 */
    public void setMapUISetting(){
		// TODO Auto-generated method stub
    	//当其他地方需要使用配置时，可以直接调用preference.getXXX()方法来获取配置信息（preference为xml文件名）。
		//是否启用缩放手势
    	mAppContext.mMapView.getController().setZoomGesturesEnabled(mAppContext.setting.getBoolean("preferences_zoom", true));
		//是否启用平移手势
    	mAppContext.mMapView.getController().setScrollGesturesEnabled(mAppContext.setting.getBoolean("preferences_scroll", true));
		//是否启用双击放大
    	mAppContext.mMapView.setDoubleClickZooming(mAppContext.setting.getBoolean("preferences_doubleClick", true));
		//是否启用旋转手势
    	mAppContext.mMapView.getController().setRotationGesturesEnabled(mAppContext.setting.getBoolean("preferences_rotate", true)); 	
		//是否启用俯视手势	
    	mAppContext. mMapView.getController().setOverlookingGesturesEnabled(mAppContext.setting.getBoolean("preferences_overlook", true)); 
    }

    /**
     * 设置地图UI控件
	 * @author QQ472950043
     */
    public void setMapUIControl() {
		// TODO Auto-generated method stub
        //设置地图是否拥有比例尺.
    	mAppContext.mMapView.showScaleControl(mAppContext.setting.getBoolean("preferences_scale", true));
  		//自动定位
    	mAppContext.isAutoRequest = mAppContext.setting.getBoolean("preferences_auto_locate", true);
        //设置指南针位置,指南针在3D模式下自动显现
  		if("lefttop".equals(mAppContext.setting.getString("list_preferences_compass_position", "lefttop")))
          	//设置指南针显示在左上角
  			mAppContext.mMapView.getController().setCompassMargin(100, 100);
  		else if("righttop".equals(mAppContext.setting.getString("list_preferences_compass_position", "lefttop")))
          	//设置指南针显示在右上角
  			mAppContext.mMapView.getController().setCompassMargin(mAppContext.mMapView.getWidth() - 100, 100);
  		//设置“我的位置”图标
  		if("system_location".equals(mAppContext.setting.getString("list_preferences_location", "system_location")))
  			mAppContext.mLocation.modifyLocationOverlayIcon(null);
  		else if("custom_location".equals(mAppContext.setting.getString("list_preferences_location", "system_location")))
  			mAppContext.mLocation.modifyLocationOverlayIcon(((Activity)mContext).getResources().getDrawable(R.drawable.baidu_map_icon_geo));
		//是否显示缩放控件
  		if(mAppContext.setting.getBoolean("preferences_zoom_control", true)){
  			//显示自定义缩放控件
  			if("zoom_custom".equals(mAppContext.setting.getString("list_preferences_zoom_control", "zoom_custom"))){
  				mAppContext.mMapView.setBuiltInZoomControls(false); 
  		        mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.VISIBLE);
  		        mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.VISIBLE);
  			}
  			//显示自定义缩放控件
  			else if("zoom_system".equals(mAppContext.setting.getString("list_preferences_zoom_control", "zoom_system"))){
  				mAppContext.mMapView.setBuiltInZoomControls(true); 
  		    	mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.GONE);
  		    	mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.GONE);
  			}
  		}
  		else{//什么都不显示
  			mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.GONE);
  			mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.GONE);
  			mAppContext.mMapView.setBuiltInZoomControls(false); 
  		}
	}
    
}