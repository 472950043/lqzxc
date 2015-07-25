package com.lqzxc.map.measure;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import com.baidu.mapapi.map.Graphic;
import com.baidu.mapapi.map.GraphicsOverlay;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.TextItem;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.utils.DistanceUtil;
import com.baidu.platform.comapi.basestruct.GeoPoint;

/**
 * ���Measure����
 * @param 1.��onCreate�������ù��췽��ʵ����Measure mMeasure=new Measure(mMapView);
 * @param 2.��ʼ����mMeasure.start();
 * @param 3.�����м�����mMeasure.addPoint(GeoPoint mGeoPoint);
 * @param 4.��������mMeasure.finish();
 * @author QQ472950043
 */
public class Measure{
	/**
	 * ��ͼView
	 */
	MapView mMapView;
	/**
	 * �Ƿ���ģʽ
	 */
	public boolean isMeasuringDistance;
	/**
	 * ����ܳ�
	 */
	public double distanceSum;
	/**
	 * ��໺��·����
	 */
	List<GeoPoint> mGeoPoints;
	/**
	 * ����ı�ͼ��
	 */
	TextOverlay pointTextOverlay;
	/**
	 * ����ͼ��
	 */
	GraphicsOverlay pointGraphicsOverlay;
	/**
	 * �����ͼ��
	 */
	GraphicsOverlay lineGraphicsOverlay;
	/**
	 * ���췽��ʵ�������Measure���ߣ���ο��ٶ�Android SDK API�ֲ��TextOverlay���GraphicsOverlay��
	 * @param mMapView
	 * @author QQ472950043
	 */
	public Measure(MapView mMapView){
		// ����·����
		mGeoPoints = new ArrayList<GeoPoint>();
		// ��Ӳ���ı�ͼ��
		pointTextOverlay = new TextOverlay(mMapView);
		mMapView.getOverlays().add(pointTextOverlay);
		// ��Ӳ���ͼ��
		pointGraphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(pointGraphicsOverlay);
		// ��Ӳ����ͼ��
		lineGraphicsOverlay = new GraphicsOverlay(mMapView);
		mMapView.getOverlays().add(lineGraphicsOverlay);
		// ��ʼ����๤�߱���
		isMeasuringDistance = false;
		distanceSum = 0.0;
		this.mMapView = mMapView;
	}

	/**
	 * ��ʼ���
	 */
	public void start(){
		// TODO Auto-generated method stub
		//�������ģʽ
		isMeasuringDistance = true;
		//���õ�ͼ��˫���Ŵ�
		mMapView.setDoubleClickZooming(false);
	}

	/**
	 * ��addPoint(GeoPoint mGeoPoint)������mGeoPoint���������ı����㡢�ߣ�
	 * ��ο��ٶ�Android SDK API�ֲ��TextItem���Graphic��
	 */
	public void addPoint(GeoPoint mGeoPoint) {
		// TODO Auto-generated method stub
		//׼������
		String text;
		if (mGeoPoints.isEmpty()) {//����·����Ϊ�յ�ʱ�������
			text= "���";
		}
		else{
			double distance = DistanceUtil.getDistance(mGeoPoints.get(mGeoPoints.size() - 1), mGeoPoint);
			distanceSum += distance;
	        DecimalFormat df = new DecimalFormat("#0.00");
	        text=df.format(distanceSum/1000)+"����";
			//�ӵڶ����㿪ʼ�����
			Graphic lineGraphic=MeasureGraphics.getLineGraphic(mGeoPoints.get(mGeoPoints.size() - 1), mGeoPoint);
			lineGraphicsOverlay.setData(lineGraphic);
		}
		//�������ı�
		TextItem pointTextItem = MeasureTextItem.getMeasureTextItem(mGeoPoint, text);
		pointTextOverlay.addText(pointTextItem);
    	//������
		Graphic getPointGraphic = MeasureGraphics.getPointGraphic(mGeoPoint);
		pointGraphicsOverlay.setData(getPointGraphic);
		//ˢ�µ�ͼ
		mMapView.refresh();
		//����·����
		mGeoPoints.add(mGeoPoint);
	}

	/**
	 * �������
	 */
	public void finish(){
		// TODO Auto-generated method stub
		//���������ı�
		pointTextOverlay.removeAll();
		//��������ͼ�Σ�����ߣ�
		pointGraphicsOverlay.removeAll();
		lineGraphicsOverlay.removeAll();
		//��ʼ����๤�߱����������´β��
		isMeasuringDistance = false;
		distanceSum = 0;
		mGeoPoints.clear();
		//ˢ�µ�ͼ
		mMapView.refresh();
	}
	
//	//MKMapTouchListener ע���ͼ������¼�
//	mMapView.regMapTouchListner(new MKMapTouchListener() {
//		@Override
//		public void onMapClick(GeoPoint geoPoint) {
//			// TODO Auto-generated method stub
//			touchType = "����";
//			if (mMeasure.isMeasuringDistance) {
//				//һ������Ӳ��ĵ�
//				mMeasure.addPoint(geoPoint);
//			}
//		}
//
//		@Override
//		public void onMapDoubleClick(GeoPoint geoPoint) {
//			// TODO Auto-generated method stub
//			touchType = "˫��";
//			if (mMeasure.isMeasuringDistance) {
//				ApiClient.ToastMessage(MapSearch.this, "������");
//				mMeasure.finish();
//				//����˫���Ŵ�
//		    	mMapView.setDoubleClickZooming(true);
//			}
//		}
	
//  //���Ҫ����poi��Ҳ�ܲ�࣬���������дregMapViewListener�е�onClickMapPoi�¼�
//	@Override
//	public void onClickMapPoi(MapPoi mapPoiInfo) {
//		// TODO Auto-generated method stub
//		if (mapPoiInfo != null) {
//			if (mMeasure.isMeasuringDistance) {
//				// һ������Ӳ��ĵ�
//				mMeasure.addPoint(mapPoiInfo.geoPt);
//				ApiClient.ToastMessage(MapSearch.this, mapPoiInfo.strText);
//			} else {
//				mMapController.animateTo(mapPoiInfo.geoPt);
//				ApiClient.ToastMessage(MapSearch.this, mapPoiInfo.strText);
//			}
//		}
//	}
}