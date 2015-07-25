package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * �Զ���Ĳ���ı���MeasureTextItem����ο��ٶ�Android SDK API�ֲ��TextItem��
 * @param �õ�����ı��� getMeasureTextItem(GeoPoint mGeoPoint,String text)
 * @author QQ472950043
 **/
public class MeasureTextItem{
	/**
	 * �õ�����ı��� getMeasureTextItem(GeoPoint mGeoPoint,String text) ��ο��ٶ�Android SDK API�ֲ��TextItem��
	 * @author QQ472950043
	 **/
	public static TextItem getMeasureTextItem(GeoPoint mGeoPoint,String text) {
		// TODO Auto-generated method stub
		TextItem measureTextItem = new TextItem();
		measureTextItem.fontColor = new Symbol().new Color(255, 255, 0, 0);
    	measureTextItem.bgColor = new Symbol().new Color(150, 255, 255, 255); 
		measureTextItem.fontSize = 28;
		measureTextItem.text = text;
		measureTextItem.align = TextItem.ALIGN_CENTER;
		measureTextItem.pt = mGeoPoint;
		return measureTextItem;
	}
}