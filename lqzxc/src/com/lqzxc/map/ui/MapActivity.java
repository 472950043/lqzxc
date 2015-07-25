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
 * ��ʾMapView�Ļ����÷�
 * @author QQ472950043
 */
public class MapActivity extends Activity implements MKOfflineMapListener {

	AppContext mAppContext;// ȫ������

	// UI����
	RelativeLayout widget_title;// ������
	RelativeLayout widget_ui_btn;// ��ͼUI���ư�ť
	RelativeLayout widget_offline;// ���ߵ�ͼ

	MapConfig mMapConfig;// �Զ����ͼ����
	MapSearch mMapSearch;// �Զ����ͼ������
	MapListener mMapListener;// �Զ����ͼ�¼�����
	DialogBase mOfflineDialog;// ���ߵ�ͼ�Ի���
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // BMapManager�Ѿ���Application�е�initEngineManager()��ʼ����
        mAppContext = (AppContext) this.getApplicationContext();
        //����MapView��setContentView()�г�ʼ��,��������Ҫ��BMapManager��ʼ��֮��
        setContentView(R.layout.baidu_map_activity_main);
		// ��ȡ��ͼmMapView
    	mAppContext.mMapView = (MapView) findViewById(R.id.bmapView);
		// ��ȡ��ͼ������mMapController
    	mAppContext.mMapController = mAppContext.mMapView.getController();
		// ��ʼ���Զ����ͼ����
    	mMapConfig = new MapConfig(this);
		try{
			// ��ʼ�����г�վ��
			mAppContext.mBike = new Bike(this);
			addBikeSite();

			// ��ʼ��������
			widget_title = (RelativeLayout) findViewById(R.id.baidu_map_widget_title);
			mAppContext.mViewMode = new ViewMode(this);
			widget_title.addView(mAppContext.mViewMode);

			// ��ʼ����ͼUI���ư�ť
			widget_ui_btn = (RelativeLayout) findViewById(R.id.baidu_map_widget_nav_btn);
			mAppContext.mViewUIBtn = new ViewUIBtn(this);
			widget_ui_btn.addView(mAppContext.mViewUIBtn);
			
			// ��ʼ��վ�㴰��
			mAppContext.widget_window = (RelativeLayout) findViewById(R.id.baidu_map_widget_window);
			mAppContext.mViewWindow = new ViewWindow(this);
			mAppContext.widget_window.addView(mAppContext.mViewWindow);

			// ����/�ݳ�/����/����·�� ·��ͼ���ʼ��
			mAppContext.main_layout = (RelativeLayout) findViewById(R.id.baidu_map_main_layout);
			mMapSearch = new MapSearch(this);
			
			// ��ʼ����λ
			mAppContext.mLocation = new Location(this);
			// ��ʼ����ͼ�¼�����
			mMapListener = new MapListener(this);
			
			// ��ʼ�����ߵ�ͼģ��
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
	 * ��ʼ�����г�վ��
	 */
	public void addBikeSite() {
		// TODO Auto-generated method stub
		// ���õ�ͼ���ĵ�
		mAppContext.bikeMapStatus = new MKMapStatus();
		mAppContext.bikeMapStatus.zoom = 17;
		mAppContext.bikeMapStatus.rotate = 0;
		mAppContext.bikeMapStatus.overlooking = 0;
		mAppContext.bikeMapStatus.targetGeo = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.mMapController.setCenter(mAppContext.mBikeStie.getmGeoPoint());
		// ������г�վ��
		mAppContext.mBike.addBikeSiteList();
	}
	
	public void initOffline() {
		// TODO Auto-generated method stub
		mAppContext.mViewOffline.updateElement();
		if(mAppContext.mElement.status==0){
			mOfflineDialog=new DialogBase(this, "������ʾ","���ء�̨���С����ߵ�ͼ��"+mAppContext.formatDataSize(mAppContext.mElement.serversize)+"��\n" +
					"1�����Լ���90%��������\n" +
					"2������ӿ��ͼ�����ٶ�\n" +
					"3�����ڲ�������״̬�²鿴�������г�����ֲ�\n" +
					"����ǿ�ҽ�������WIFI����ʱ���أ�","ȷ��");
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
     * ��Ļ��תʱ���ô˷��� 
     **/  
    @Override  
    public void onConfigurationChanged(Configuration newConfig){  
		// TODO Auto-generated method stub
        //newConfig.orientation��ȡ��ǰ��Ļ״̬ʱ�����������  
        if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){  
            System.out.println("�����Ǻ���"+mAppContext.mMapView.getHeight()+mAppContext.mMapView.getWidth());
            widget_title.setVisibility(View.GONE);
        }  
        if(newConfig.orientation==Configuration.ORIENTATION_PORTRAIT){  
            System.out.println("����������"+mAppContext.mMapView.getHeight()+mAppContext.mMapView.getWidth());
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
			// �������ؽ��ȸ�����ʾ
	    	mAppContext.mViewOffline.setElement();
			if (mAppContext.mElement.status == 1 && mAppContext.mElement.ratio == 100) {
				mAppContext.ToastMessage(MapActivity.this, "�������");
				// ���õ������
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
			// �������ߵ�ͼ��װ
			System.out.println("�������ߵ�ͼ��װnum:"+ state);
			break;
		case MKOfflineMap.TYPE_VER_UPDATE:
		    // �汾������ʾ
			System.out.println("�汾������ʾ");
            //	MKOLUpdateElement e = mOffline.getUpdateInfo(state);
			break;
		}
	}
	
    /**
	 *  MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
	 */
    @Override
    protected void onPause() {
		// TODO Auto-generated method stub
    	mAppContext.mMapView.onPause();
		System.out.println("MapView is pause");
        super.onPause();
    }

	/**
	 *  MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
	 */
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		//MapView������������Activityͬ������activity�ָ�ʱ�����MapView.onResume()
		mAppContext.mMapView.onResume();
		try{
			//���õ�ͼ
			mMapConfig.setMapConfig();

			//��ʼ���
			if(mAppContext.isMeasure){
				mAppContext.isMeasure=false;
				mMapListener.mMeasure.start();
			}
			// ��ͼ����MKMapViewListener�б���ͼƬ
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
	 *  MapView������������Activityͬ������activity����ʱ�����MapView.destroy() �˳�ʱ���������ߵ�ͼģ��
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
