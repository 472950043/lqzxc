package com.lqzxc.map.ui;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MKOfflineMapListener;
import com.baidu.mapapi.map.MapView;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.bike.Bike;
import com.lqzxc.map.config.MapConfig;
import com.lqzxc.map.listener.MapListener;
import com.lqzxc.map.location.Location;
import com.lqzxc.map.search.MapSearch;
import com.lqzxc.widget.DialogBase;

/**
 * 演示MapView的基本用法
 * @author QQ472950043
 */
public class MapActivity extends Activity implements MKOfflineMapListener {

	AppContext mAppContext;// 全局配置

	// UI界面
	RelativeLayout widget_title;// 标题栏
	RelativeLayout widget_ui_btn;// 地图UI控制按钮
	RelativeLayout widget_offline;// 离线地图

	MapConfig mMapConfig;// 自定义地图配置
	MapSearch mMapSearch;// 自定义地图搜索类
	MapListener mMapListener;// 自定义地图事件监听
	DialogBase mOfflineDialog;// 离线地图对话框
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BMapManager已经在Application中的initEngineManager()初始化了
        mAppContext = (AppContext) this.getApplicationContext();
        //由于MapView在setContentView()中初始化,所以它需要在BMapManager初始化之后
        setContentView(R.layout.baidu_map_activity_main);
		// 获取地图mMapView
    	mAppContext.mMapView = (MapView) findViewById(R.id.bmapView);
		// 获取地图控制器mMapController
    	mAppContext.mMapController = mAppContext.mMapView.getController();
		// 初始化自定义地图配置
    	mMapConfig = new MapConfig(this);
		try{
			// 初始化自行车站点
			mAppContext.mBike = new Bike(this);
			addBikeSite();

			// 初始化标题栏
			widget_title = (RelativeLayout) findViewById(R.id.baidu_map_widget_title);
			mAppContext.mViewMode = new ViewMode(this);
			widget_title.addView(mAppContext.mViewMode);

			// 初始化地图UI控制按钮
			widget_ui_btn = (RelativeLayout) findViewById(R.id.baidu_map_widget_nav_btn);
			mAppContext.mViewUIBtn = new ViewUIBtn(this);
			widget_ui_btn.addView(mAppContext.mViewUIBtn);
			
			// 初始化站点窗口
			mAppContext.widget_window = (RelativeLayout) findViewById(R.id.baidu_map_widget_window);
			mAppContext.mViewWindow = new ViewWindow(this);
			mAppContext.widget_window.addView(mAppContext.mViewWindow);

			// 公交/驾车/步行/公交路线 路线图层初始化
			mAppContext.main_layout = (RelativeLayout) findViewById(R.id.baidu_map_main_layout);
			mMapSearch = new MapSearch(this);
			
			// 初始化定位
			mAppContext.mLocation = new Location(this);
			// 初始化地图事件监听
			mMapListener = new MapListener(this);
			
			// 初始化离线地图模块
			widget_offline = (RelativeLayout) findViewById(R.id.baidu_map_widget_offline);
			mAppContext.mOffline = new MKOfflineMap();
			mAppContext.mOffline.init(mAppContext.mMapController, this);
			mAppContext.mViewOffline = new ViewOffline(this);
			initOffline();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MapView is create");
	}
    
	/**
	 * 初始化自行车站点
	 */
	public void addBikeSite() {
		// TODO Auto-generated method stub
		// 设置地图中心点
		mAppContext.bikeMapStatus = new MKMapStatus();
		mAppContext.bikeMapStatus.zoom = 17;
		mAppContext.bikeMapStatus.rotate = 0;
		mAppContext.bikeMapStatus.overlooking = 0;
		mAppContext.bikeMapStatus.targetGeo = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.mMapController.setCenter(mAppContext.mBikeStie.getmGeoPoint());
		// 添加自行车站点
		mAppContext.mBike.addBikeSiteList();
	}
	
	public void initOffline() {
		// TODO Auto-generated method stub
		mAppContext.mViewOffline.updateElement();
		if(mAppContext.mElement.status==0){
			mOfflineDialog=new DialogBase(this, "下载提示","下载“台州市”离线地图（"+mAppContext.formatDataSize(mAppContext.mElement.serversize)+"）\n" +
					"1、可以减少90%数据流量\n" +
					"2、极大加快地图加载速度\n" +
					"3、可在不联网的状态下查看公共自行车网点分布\n" +
					"作者强烈建议您在WIFI联网时下载！","确定");
			mOfflineDialog.setOnConfirmListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					widget_offline.addView(mAppContext.mViewOffline);
					mOfflineDialog.dismiss();
				}
			});
			mOfflineDialog.show();
		} else if (mAppContext.mElement.update || mAppContext.mElement.status != 4) {
			widget_offline.addView(mAppContext.mViewOffline);
		}
	}

	/** 
     * 屏幕旋转时调用此方法 
     **/  
    @Override  
    public void onConfigurationChanged(Configuration newConfig){  
		// TODO Auto-generated method stub
        //newConfig.orientation获取当前屏幕状态时横向或者竖向  
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){  
            System.out.println("现在是横屏"+mAppContext.mMapView.getHeight()+mAppContext.mMapView.getWidth());
            widget_title.setVisibility(View.GONE);
        }  
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){  
            System.out.println("现在是竖屏"+mAppContext.mMapView.getHeight()+mAppContext.mMapView.getWidth());
            widget_title.setVisibility(View.VISIBLE);
        } 
        super.onConfigurationChanged(newConfig);  
    }
	
	@Override
	public void onGetOfflineMapState(int type, int state) {
		// TODO Auto-generated method stub
		switch (type) {
		case MKOfflineMap.TYPE_DOWNLOAD_UPDATE: 
			mAppContext.mViewOffline.updateElement();
			// 处理下载进度更新提示
	    	mAppContext.mViewOffline.setElement();
			if (mAppContext.mElement.status == 1 && mAppContext.mElement.ratio == 100) {
				mAppContext.ToastMessage(MapActivity.this, "下载完成");
				// 设置点击隐藏
				widget_offline.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						widget_offline.setVisibility(View.GONE);
					}
				});
			}
			break;
		case MKOfflineMap.TYPE_NEW_OFFLINE:
			// 有新离线地图安装
			System.out.println("有新离线地图安装num:"+ state);
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
		    // 版本更新提示
			System.out.println("版本更新提示");
            //	MKOLUpdateElement e = mOffline.getUpdateInfo(state);
			break;
		}
	}
	
    /**
	 *  MapView的生命周期与Activity同步，当activity挂起时需调用MapView.onPause()
	 */
    @Override
    protected void onPause() {
		// TODO Auto-generated method stub
    	mAppContext.mMapView.onPause();
		System.out.println("MapView is pause");
        super.onPause();
    }

	/**
	 *  MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//MapView的生命周期与Activity同步，当activity恢复时需调用MapView.onResume()
		mAppContext.mMapView.onResume();
		try{
			//配置地图
			mMapConfig.setMapConfig();

			//开始测距
			if(mAppContext.isMeasure){
				mAppContext.isMeasure=false;
				mMapListener.mMeasure.start();
			}
			// 截图，在MKMapViewListener中保存图片
			if(mAppContext.isSavescreen){
				mAppContext.isSavescreen=false;
				mAppContext.mMapView.getCurrentMap();
			}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("MapView is resume");
		super.onResume();
	}
	
	/**
	 *  MapView的生命周期与Activity同步，当activity销毁时需调用MapView.destroy() 退出时，销毁离线地图模块
	 */
    @Override
    protected void onDestroy() {
		// TODO Auto-generated method stub
    	mAppContext.mOffline.destroy();
    	mAppContext.mLocation.destroy();		
		mAppContext.mMapView.destroy();
		System.out.println("MapView is destroy");
        super.onDestroy();
    }
}
