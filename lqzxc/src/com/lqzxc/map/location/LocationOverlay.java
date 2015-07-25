package com.lqzxc.map.location;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;

/**
 * �Է�װ�Ķ�λͼ��LocationOverlay���̳аٶ�Android SDK API��MyLocationOverlay��
 * @author QQ472950043
 **/
public class LocationOverlay extends MyLocationOverlay {

	/**
	 * ���췽��ʵ������λͼ��LocationOverlay����ο��ٶ�Android SDK API�ֲ��MyLocationOverlay��
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
	 * ��дdispatchTapʵ�ֵ��������ο��ٶ�Android SDK API�ֲ��MyLocationOverlay��
	 * @author QQ472950043
	 */
	@Override
	protected boolean dispatchTap() {
		// TODO Auto-generated method stub
		// �������¼���������λͼ�����ݣ���ʾ��λ���ݲ���
		if(mAppContext.mLocationPopup!=null)
			showPopup(mAppContext.location_view,mAppContext.locatCenter,5);
		return true;
	}

	/**
	 * ����ͼƬPopup
	 * @param view - ��������
	 * @param point - ������λ��(ê���ڴ��ڵ����·���ˮƽ����Center����ֱ����Bottom)
	 * @param yOffset - ������y���ϵ�ƫ����,ȡֵ��ΧyOffset>=0����λ������
	 * @author QQ472950043
	 */
	public void showPopup(View view, GeoPoint point, int yOffset) {
		//��view �õ�ͼƬ
        view.destroyDrawingCache();
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = view.getDrawingCache(true);

        mAppContext.mLocationPopup.showPopup(bitmap,point,yOffset);
	}
}