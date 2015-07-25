package com.lqzxc.map.bike;

import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * �Զ�����г��������ֿ�BikeTextItem����ο��ٶ�Android SDK API�ֲ��TextItem��
 * @author QQ472950043
 **/
public class BikeTextItem{
	/**
	 * �õ����г��������ֿ�getBikeTextItem(GeoPoint sitePt,String name)����ο��ٶ�Android SDK API�ֲ��TextItem��
	 * @author QQ472950043
	 **/
	public static TextItem getBikeTextItem(GeoPoint sitePt,String name) {
		// TODO Auto-generated method stub
		TextItem bikeTextItem = new TextItem();
		bikeTextItem.fontColor = new Symbol().new Color(255,51,51,51);  
    	bikeTextItem.bgColor = new Symbol().new Color(150,255,255,255);
		bikeTextItem.fontSize = 22;
		bikeTextItem.text = name;
		bikeTextItem.align = TextItem.ALIGN_CENTER;
		bikeTextItem.pt = sitePt;
		return bikeTextItem;
	}
	
	/**
	 * �õ����г��������ֿ�getBikeTextItem2(GeoPoint sitePt,String name)����ο��ٶ�Android SDK API�ֲ��TextItem��
	 * @author QQ472950043
	 **/
	public static TextItem getBikeTextItem2(GeoPoint sitePt,String name) {
		// TODO Auto-generated method stub
		TextItem bikeTextItem = new TextItem();
		bikeTextItem.fontColor = new Symbol().new Color(150,255,0,0);
    	bikeTextItem.bgColor = new Symbol().new Color(150,255,255,255);
		bikeTextItem.fontSize = 22;
		bikeTextItem.text = name;
		bikeTextItem.align = TextItem.ALIGN_CENTER;
		bikeTextItem.pt = sitePt;
		return bikeTextItem;
	}
}