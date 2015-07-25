package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Geometry;
import com.baidu.mapapi.map.Graphic;
import com.baidu.platform.comapi.basestruct.GeoPoint;
/**
 * 自定义测距几何图形MeasureGraphics，
 * 请参考百度Android SDK API手册的Geometry、Graphic、GraphicsOverlay类之间的关系
 * @author QQ472950043
 **/
public class MeasureGraphics{
	/**
	 * 得到测距点图层getPointGraphic(GeoPoint mGeoPoint)，
	 * 请参考百度Android SDK API手册的Graphic类
	 * @author QQ472950043
	 **/
	public static Graphic getPointGraphic(GeoPoint mGeoPoint) {
		// TODO Auto-generated method stub
		//第一步：创建几何图形类Geometry,新建一个点
		Geometry mPoint = new Geometry();
		//第二步：设置该自绘图形为一个6px的点
		mPoint.setPoint(mGeoPoint, 6);
		//第三步：通过几何元素(Geometry)和相对应的样式(Symbol)构建绘图对象：Graphic
		Graphic pointGraphic = new Graphic(mPoint, MeasureSymbol.getPointSymbol());
		//第四步：Graphic对象可通过GraphicsOvelay添加到MapView中。
		return pointGraphic;
	}

	/**
	 * 得到测距线图层getLineGraphic(GeoPoint stGeoPoint,GeoPoint enGeoPoint)，
	 * 请参考百度Android SDK API手册的GraphicsOverlay类
	 * @author QQ472950043
	 **/
	public static Graphic getLineGraphic(GeoPoint stGeoPoint,GeoPoint enGeoPoint) {
		// TODO Auto-generated method stub
		//第一步：创建几何图形类Geometry,新建一条线
		Geometry mLine = new Geometry();
		//第二步：设置这条线的起点和终点
		mLine.setPolyLine(new GeoPoint[] {stGeoPoint, enGeoPoint});
		//第三步：通过几何元素(Geometry)和相对应的样式(Symbol)构建绘图对象：Graphic
		Graphic lineGraphic = new Graphic(mLine, MeasureSymbol.getLineSymbol());
		//第四步：Graphic对象可通过GraphicsOvelay添加到MapView中。
		return lineGraphic;
	}
	
}