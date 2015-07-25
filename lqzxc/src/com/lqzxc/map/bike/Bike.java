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
 * �Զ������г�ģ��
 * @author QQ472950043
 **/
public class Bike {

	Context mContext;
	AppContext mAppContext;

	/**
	 * ���췽��ʵ�������г�����ͼ������г���עͼ�㣬��ο��ٶ�Android SDK
	 * API�ֲ��TextOverlay���ItemizedOverlay��
	 * @param mContext
	 * @param mMapView
	 * @author QQ472950043
	 */
	public Bike(Context mContext) {
		// TODO Auto-generated method stub
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		// �����ı�ͼ��
		mAppContext.bikeTextOverlay = new TextOverlay(mAppContext.mMapView);
		mAppContext.mMapView.getOverlays().add(mAppContext.bikeTextOverlay);
		// �����ע��ͼ��
		mAppContext.bikeItemizedOverlay = new BikeItemizedOverlay(mContext.getResources().getDrawable(R.drawable.baidu_map_icon_gcoding), mAppContext.mMapView);
		mAppContext.bikeItemizedOverlay.setAppContext(mContext);
		mAppContext.mMapView.getOverlays().add(mAppContext.bikeItemizedOverlay);
	}

	/**
	 * ����������г�������
	 * @author QQ472950043
	 **/
	public void addBikeSiteList() {
		// TODO Auto-generated method stub
		for (int i = 0; i < mAppContext.mBikeSties.size(); i++) {
			// ׼��overlay����
			BikeStie mBikeStie = mAppContext.mBikeSties.get(i);
			// ���һ����
			addBike(mBikeStie.getmGeoPoint(), mBikeStie.getId() + "." + mBikeStie.getNetName() ,mBikeStie.getNetStatus());
		}
		// ��ʾ������ͼ�㣬��MapListener�м������ı�����ʾ������״̬
		mAppContext.isShowBikeText = true;
		mAppContext.isShowBikeItemized = true;
		System.out.println("bike����ȫ����ӳɹ�");
		// ͼ�������ϣ�ˢ����ʾ
		mAppContext.mMapView.refresh();
	}
	
	/**
	 * ������г�����ͼ������г���עͼ�㣬��ο��ٶ�Android SDK API�ֲ��TextItem���OverlayItem��
	 * @param mGeoPoint
	 * @param title
	 * @author QQ472950043
	 **/
	public void addBike(GeoPoint mGeoPoint, String title, String netStatus) {
		// TODO Auto-generated method stub
		// ���bike�ı�ͼ��
		TextItem bikeTextItem;
		if(netStatus.equals("����")){
			bikeTextItem = BikeTextItem.getBikeTextItem(mGeoPoint, title);
		}else{
			bikeTextItem = BikeTextItem.getBikeTextItem2(mGeoPoint, title);
		}
		mAppContext.bikeTextOverlay.addText(bikeTextItem);// ע�⣺ ͬһ��itemֻ��addһ��
		// ����Զ����עͼ��
		OverlayItem bikeOverlayItem = new OverlayItem(mGeoPoint, title, "");
		if(!netStatus.equals("����")){
			 //����overlayͼ�꣬�粻���ã���ʹ�ô���ItemizedOverlayʱ��Ĭ��ͼ��.
			bikeOverlayItem.setMarker(mContext.getResources().getDrawable(R.drawable.baidu_map_icon_gcoding2));
		}
		mAppContext.bikeItemizedOverlay.addItem(bikeOverlayItem);
	}
}