package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Symbol;
import com.baidu.mapapi.map.TextItem;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 自定义的测距文本框MeasureTextItem，请参考百度Android SDK API手册的TextItem类
 * @param 得到测距文本框 getMeasureTextItem(GeoPoint mGeoPoint,String text)
 * @author QQ472950043
 **/
public class MeasureTextItem{
	/**
	 * 得到测距文本框 getMeasureTextItem(GeoPoint mGeoPoint,String text) 请参考百度Android SDK API手册的TextItem类
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