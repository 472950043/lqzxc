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
 * ��ͼ�¼�����ģ��
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
        //��ʼ����๦��
		mMeasure = new Measure(mAppContext.mMapView);

		regMapStatusChangeListener();
		regMapTouchListner();
		regMapViewListener();
	}

	public void regMapStatusChangeListener() {
		// TODO Auto-generated method stub
		// MKMapStatusChangeListener ע���ͼ״̬�����ߡ�
		mAppContext.mMapView.regMapStatusChangeListener(new MKMapStatusChangeListener() {
			//ʵʱ������ͼ״̬�ı�
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
		     * ��¼��ͼ�����ż�����ת�Ƕȡ����ӽǶ�
		     * @param mapStatus
			 * @author QQ472950043
		     */
			public void setMapConfig(MKMapStatus mapStatus) {  
				mAppContext.mMapStatus=mapStatus;
			    //��¼��setting��
				mAppContext.setting.edit().putString("preferences_zoomlevel", ""+mapStatus.zoom).commit();
				mAppContext.setting.edit().putString("preferences_rotateangle", ""+mapStatus.rotate).commit();
				mAppContext.setting.edit().putString("preferences_overlookangle", -mapStatus.overlooking+"").commit();
			}
		});
	}

	/**
	 * MKMapTouchListener ע���ͼ������¼�
	 */
	public void regMapTouchListner() {
		// TODO Auto-generated method stub
		mAppContext.mMapView.regMapTouchListner(new MKMapTouchListener() {
			@Override
			public void onMapClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "����";
				if (mMeasure.isMeasuringDistance) {
					// һ������Ӳ��ĵ�
					mMeasure.addPoint(geoPoint);
				}
				if (mAppContext.mLocationPopup != null) {// ����pop
					mAppContext.mLocationPopup.hidePop();
				}
			}

			@Override
			public void onMapDoubleClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "˫��";
				if (mMeasure.isMeasuringDistance) {
					mAppContext.ToastMessage(mContext, "������");
					mMeasure.finish();
					// ����˫���Ŵ�
					mAppContext.mMapView.setDoubleClickZooming(true);
				}
			}

			@Override
			public void onMapLongClick(GeoPoint geoPoint) {
				// TODO Auto-generated method stub
				touchType = "����";
				// mCenter = point;
			}
		});
	}

	/**
	 * MKMapViewListener ���ڴ����ͼ��������¼��ص�
	 * MapView������������Activityͬ������activity����ʱ�����MapView.onPause()
	 */
	public void regMapViewListener() {
		// TODO Auto-generated method stub
        //���õ�ͼ�Ƿ���Ӧ����¼�
		mAppContext.mMapView.getController().enableClick(true);
		mAppContext.mMapView.regMapViewListener(mAppContext.mBMapManager, new MKMapViewListener() {
			//�ڴ˴����ͼ�ƶ���ɻص� ���ţ�ƽ�ƵȲ�����ɺ󣬴˻ص�������
			@Override
			public void onMapMoveFinish() {
				// TODO Auto-generated method stub
			}

			//��ͼ��ɴ������Ĳ�������: animationTo()���󣬴˻ص�������
			@Override
			public void onMapAnimationFinish() {
				// TODO Auto-generated method stub
			}

			//�ڴ˴����ͼ������¼�
			@Override
			public void onMapLoadFinish() {
				// TODO Auto-generated method stub
				System.out.println("onMapLoadFinish:��ͼ�������");
			}

			/**
			 * �ڴ˴����ͼpoi����¼�����ʾ��ͼpoi���Ʋ��ƶ����õ� ���ù���
			 * mMapController.enableClick(true); ʱ���˻ص����ܱ�����
			 */
			@Override
			public void onClickMapPoi(MapPoi mapPoiInfo) {
				// TODO Auto-generated method stub
				if (mapPoiInfo != null) {
					if (mMeasure.isMeasuringDistance) {
						// һ������Ӳ��ĵ�
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
			 * �����ù� mMapView.getCurrentMap()�󣬴˻ص��ᱻ���� ���ڴ˱����ͼ���洢�豸
			 */
			@Override
			public void onGetCurrentMap(Bitmap b) {
				// TODO Auto-generated method stub
				// ����ȷ���ļ���·�����ڣ����򱣴�
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
					mAppContext.ToastMessage(mContext,"��Ļ��ͼ�ɹ���ͼƬ����: " + file.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
	}
	
}