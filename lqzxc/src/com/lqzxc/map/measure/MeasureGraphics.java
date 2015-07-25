package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * �Զ����༸��ͼ��MeasureGraphics��
 * ��ο��ٶ�Android SDK API�ֲ��Geometry��Graphic��GraphicsOverlay��֮��Ĺ�ϵ
 * @author QQ472950043
 **/
public class MeasureGraphics{
	/**
	 * �õ�����ͼ��getPointGraphic(GeoPoint mGeoPoint)��
	 * ��ο��ٶ�Android SDK API�ֲ��Graphic��
	 * @author QQ472950043
	 **/
	public static Graphic getPointGraphic(GeoPoint mGeoPoint) {
		// TODO Auto-generated method stub
		//��һ������������ͼ����Geometry,�½�һ����
		Geometry mPoint = new Geometry();
		//�ڶ��������ø��Ի�ͼ��Ϊһ��6px�ĵ�
		mPoint.setPoint(mGeoPoint, 6);
		//��������ͨ������Ԫ��(Geometry)�����Ӧ����ʽ(Symbol)������ͼ����Graphic
		Graphic pointGraphic = new Graphic(mPoint, MeasureSymbol.getPointSymbol());
		//���Ĳ���Graphic�����ͨ��GraphicsOvelay��ӵ�MapView�С�
		return pointGraphic;
	}

	/**
	 * �õ������ͼ��getLineGraphic(GeoPoint stGeoPoint,GeoPoint enGeoPoint)��
	 * ��ο��ٶ�Android SDK API�ֲ��GraphicsOverlay��
	 * @author QQ472950043
	 **/
	public static Graphic getLineGraphic(GeoPoint stGeoPoint,GeoPoint enGeoPoint) {
		// TODO Auto-generated method stub
		//��һ������������ͼ����Geometry,�½�һ����
		Geometry mLine = new Geometry();
		//�ڶ��������������ߵ������յ�
		mLine.setPolyLine(new GeoPoint[] {stGeoPoint, enGeoPoint});
		//��������ͨ������Ԫ��(Geometry)�����Ӧ����ʽ(Symbol)������ͼ����Graphic
		Graphic lineGraphic = new Graphic(mLine, MeasureSymbol.getLineSymbol());
		//���Ĳ���Graphic�����ͨ��GraphicsOvelay��ӵ�MapView�С�
		return lineGraphic;
	}
	
}