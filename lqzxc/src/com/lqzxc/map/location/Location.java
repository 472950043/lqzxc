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
 * �Զ��嶨λģ��
 * @author QQ472950043
 **/
public class Location{

	// ��λ����
	LocationClient mLocationClient;
	
	int LatitudeSpan;// ��ͼ��ǰγ�ȿ�ȣ��ϱ�Ե���±�Ե
	int LongitudeSpan;// ��ͼ��ǰ���ȿ�ȣ����Ե���ұ�Ե
	int minLat;// �±�Ե
	int maxLat;// �ϱ�Ե
	int minLng;// ���Ե
	int maxLng;// �ұ�Ե

	AppContext mAppContext;
	/**
     * ���췽������ʼ����λģ��
     * @author QQ472950043
     */
	public Location(Context mContext){
		mAppContext = (AppContext) mContext.getApplicationContext();
		
		// ��ʼ����λ��view
		mAppContext.location_view = LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_location, null);
		mAppContext.location_title = (TextView) mAppContext.location_view.findViewById(R.id.location_title);
		mAppContext.location_content = (TextView) mAppContext.location_view.findViewById(R.id.location_content);
		// ����pop����ע�����¼������ӿ�
		mAppContext.mLocationPopup = new PopupOverlay(mAppContext.mMapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				// �ڴ˴���pop����¼���indexΪ�����������,�����������������
			}
		});
		
		// ��λͼ���ʼ��
		mAppContext.mLocationOverlay = new LocationOverlay(mAppContext.mMapView);
		mAppContext.mLocationOverlay.setAppContext(mContext);
		// ������λͼ����ܷ������ݹ��ܣ�����λ�������з���ʱ����λͼ�����ת���÷���
		mAppContext.mLocationOverlay.enableCompass();
		// ��Ӷ�λͼ��
		mAppContext.mMapView.getOverlays().add(mAppContext.mLocationOverlay);
		
		// ��ʼ����λ����
		mLocationClient = new LocationClient(mContext);
		mLocationClient.setAK(AppContext.strKey);// key:�ڿ�������վ��Ӧ��APP�������AccessKey
		// ע���������
		LocationListenner mLocationListenner=new LocationListenner();
		mLocationListenner.setAppContext(mContext);
		mLocationClient.registerLocationListener(mLocationListenner);
		LocationClientOption mOption = new LocationClientOption();
		mOption.setProdName("com.lqzxc");
		mOption.setPriority(LocationClientOption.GpsFirst);
		mOption.setOpenGps(true);
		mOption.setCoorType("bd09ll");
		// �ɽ������л����ò���
		mLocationClient.setLocOption(mOption);
		// ������λ�������δע����׳�NullPointerException�쳣���뿪���߲�׽��Ӧ���쳣��Ϣ��
		mLocationClient.start();
		
		mAppContext.mData = new LocationData();
		mAppContext.isAutoRequest = true;
	}
	
	/**
     * �ֶ�����һ�ζ�λ����
     * @author QQ472950043
     */
	public void requestLocation() {
		// TODO Auto-generated method stub
		mAppContext.mViewUIBtn.locate_button.setEnabled(false);
		mLocationClient.requestLocation();
	}
	

	/**
	 * ��ȡ��ǰ��Ļ��ʾ�ľ�γ�ȷ�Χ
	 * @author QQ472950043
	 **/
	public void getBorder() {
		// TODO Auto-generated method stub
		GeoPoint mCenter = mAppContext.mMapView.getMapCenter();
		LatitudeSpan = mAppContext.mMapView.getLatitudeSpan();// ��ͼ��ǰγ�ȿ�ȣ��ϱ�Ե���±�Ե
		LongitudeSpan = mAppContext.mMapView.getLongitudeSpan();// ��ͼ��ǰ���ȿ�ȣ����Ե���ұ�Ե
		minLng = mCenter.getLongitudeE6() - LongitudeSpan / 2;// ���Ե
		minLat = mCenter.getLatitudeE6() - LatitudeSpan / 2;// �±�Ե
		maxLng = mCenter.getLongitudeE6() + LongitudeSpan / 2;// �ұ�Ե
		maxLat = mCenter.getLatitudeE6() + LatitudeSpan / 2;// �ϱ�Ե
	}
	
	/**
	 * �޸�λ��ͼ��
	 * @param marker
     * @author QQ472950043
	 */
	public void modifyLocationOverlayIcon(Drawable marker) {
		// TODO Auto-generated method stub
		// ������markerΪnullʱ��ʹ��Ĭ��ͼ�����
		mAppContext.mLocationOverlay.setMarker(marker);
		// �޸�ͼ�㣬��Ҫˢ��MapView��Ч
		mAppContext.mMapView.refresh();
	}
	
	/**
	 * �˳�ʱ���ٶ�λ
     * @author QQ472950043
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		if (mLocationClient != null)
			mLocationClient.stop();
	}
}