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
 * �Զ����ͼ����ģ��
 * @author QQ472950043
 */
public class MapConfig{

	//��ʼ���˵�ȫ�ֿ��Ʊ���
	AppContext mAppContext;
	Context mContext;
	
	/**
	 * ���췽��ʵ�����Զ����ͼ����ģ��
	 * @author QQ472950043
	 */
	public MapConfig(Context mContext){
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		mAppContext.setting = PreferenceManager.getDefaultSharedPreferences(mContext);
		mAppContext.mMapStatus = new MKMapStatus();
		
	}

	/**
	 * ���õ�ͼ����
	 * @author QQ472950043
	 */
	public void setMapConfig() {
		// TODO Auto-generated method stub
    	//���õ�ͼģʽ
		setMapMode();
		//���õ�ͼ��������
		setMapControl();
        //���õ�ͼUI����
		setMapUISetting();
        //���õ�ͼUI���ƹ���
		setMapUIControl();
	}
	
	/**
	 * ���õ�ͼ��ʾģʽ
	 * @author QQ472950043
	 */
	public void setMapMode() {
		// TODO Auto-generated method stub
		//�������ط���Ҫʹ������ʱ������ֱ�ӵ���preference.getXXX()��������ȡ������Ϣ��preferenceΪxml�ļ�������
		//���ýֵ�ͼ������ͼ
		if("normal".equals(mAppContext.setting.getString("list_preferences_layers", "normal")))
			mAppContext.mMapView.setSatellite(false);
		else if("statellite".equals(mAppContext.setting.getString("list_preferences_layers", "normal")))
			mAppContext.mMapView.setSatellite(true);
		//���ý�ͨͼ
		mAppContext.mMapView.setTraffic(mAppContext.setting.getBoolean("preferences_layers_traffic", false));
		//�����������
		if(mAppContext.setting.getBoolean("preferences_sensor", true)){
			((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
		}
		else{
			((Activity)mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
	}
	
	/**
	 *  ��ͼ���������ż�����ת�Ƕȡ����ӽǶ�
	 * @author QQ472950043
	 */
	public void setMapControl() {
		// TODO Auto-generated method stub
		String errorMsg="";
		//V2.2.0�Ժ��ͬʱ���õ�ͼ�ȼ�����ͼ���ĵ㡢��ת�Ƕȵ�
		try {
			//�������� sdk ���ż���Χ�� [3.0,19.0]
			errorMsg="��������ȷ�����ż���";
			mAppContext.mMapStatus.zoom = Float.parseFloat(mAppContext.setting.getString("preferences_zoomlevel", "16"));
			//������ת ��ת�Ƿ�Χ�� -180 ~ 180 ,0 ~ 360 ���� ��λ����   ��ʱ����ת
			errorMsg="��������ȷ����ת�Ƕ�";
			mAppContext.mMapStatus.rotate = Integer.parseInt(mAppContext.setting.getString("preferences_rotateangle", "0"));
			//������ ���Ƿ�Χ�� -45 ~ 0 , ��λ�� ��
			errorMsg="��������ȷ�ĸ��ӽǶ�";
			mAppContext.mMapStatus.overlooking = -Integer.parseInt(mAppContext.setting.getString("preferences_overlookangle", "0"));
			
			mAppContext.mMapView.getController().setMapStatusWithAnimation(mAppContext.mMapStatus, 1000);
//			mAppContext.ToastMessage(mContext, "zoom"+mAppContext.mMapStatus.zoom+"rotate"+mAppContext.mMapStatus.rotate+"overlooking"+mAppContext.mMapStatus.overlooking);
		} catch (NumberFormatException e) {
			mAppContext.ToastMessage(mContext, errorMsg);
		}
	}
	
	/**
	 *  3D���ƣ����š�ƽ�ơ�˫���Ŵ���ת������
	 * @author QQ472950043
	 */
    public void setMapUISetting(){
		// TODO Auto-generated method stub
    	//�������ط���Ҫʹ������ʱ������ֱ�ӵ���preference.getXXX()��������ȡ������Ϣ��preferenceΪxml�ļ�������
		//�Ƿ�������������
    	mAppContext.mMapView.getController().setZoomGesturesEnabled(mAppContext.setting.getBoolean("preferences_zoom", true));
		//�Ƿ�����ƽ������
    	mAppContext.mMapView.getController().setScrollGesturesEnabled(mAppContext.setting.getBoolean("preferences_scroll", true));
		//�Ƿ�����˫���Ŵ�
    	mAppContext.mMapView.setDoubleClickZooming(mAppContext.setting.getBoolean("preferences_doubleClick", true));
		//�Ƿ�������ת����
    	mAppContext.mMapView.getController().setRotationGesturesEnabled(mAppContext.setting.getBoolean("preferences_rotate", true)); 	
		//�Ƿ����ø�������	
    	mAppContext. mMapView.getController().setOverlookingGesturesEnabled(mAppContext.setting.getBoolean("preferences_overlook", true)); 
    }

    /**
     * ���õ�ͼUI�ؼ�
	 * @author QQ472950043
     */
    public void setMapUIControl() {
		// TODO Auto-generated method stub
        //���õ�ͼ�Ƿ�ӵ�б�����.
    	mAppContext.mMapView.showScaleControl(mAppContext.setting.getBoolean("preferences_scale", true));
  		//�Զ���λ
    	mAppContext.isAutoRequest = mAppContext.setting.getBoolean("preferences_auto_locate", true);
        //����ָ����λ��,ָ������3Dģʽ���Զ�����
  		if("lefttop".equals(mAppContext.setting.getString("list_preferences_compass_position", "lefttop")))
          	//����ָ������ʾ�����Ͻ�
  			mAppContext.mMapView.getController().setCompassMargin(100, 100);
  		else if("righttop".equals(mAppContext.setting.getString("list_preferences_compass_position", "lefttop")))
          	//����ָ������ʾ�����Ͻ�
  			mAppContext.mMapView.getController().setCompassMargin(mAppContext.mMapView.getWidth() - 100, 100);
  		//���á��ҵ�λ�á�ͼ��
  		if("system_location".equals(mAppContext.setting.getString("list_preferences_location", "system_location")))
  			mAppContext.mLocation.modifyLocationOverlayIcon(null);
  		else if("custom_location".equals(mAppContext.setting.getString("list_preferences_location", "system_location")))
  			mAppContext.mLocation.modifyLocationOverlayIcon(((Activity)mContext).getResources().getDrawable(R.drawable.baidu_map_icon_geo));
		//�Ƿ���ʾ���ſؼ�
  		if(mAppContext.setting.getBoolean("preferences_zoom_control", true)){
  			//��ʾ�Զ������ſؼ�
  			if("zoom_custom".equals(mAppContext.setting.getString("list_preferences_zoom_control", "zoom_custom"))){
  				mAppContext.mMapView.setBuiltInZoomControls(false); 
  		        mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.VISIBLE);
  		        mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.VISIBLE);
  			}
  			//��ʾ�Զ������ſؼ�
  			else if("zoom_system".equals(mAppContext.setting.getString("list_preferences_zoom_control", "zoom_system"))){
  				mAppContext.mMapView.setBuiltInZoomControls(true); 
  		    	mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.GONE);
  		    	mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.GONE);
  			}
  		}
  		else{//ʲô������ʾ
  			mAppContext.mViewUIBtn.btn_zoomin.setVisibility(View.GONE);
  			mAppContext.mViewUIBtn.btn_zoomout.setVisibility(View.GONE);
  			mAppContext.mMapView.setBuiltInZoomControls(false); 
  		}
	}
    
}