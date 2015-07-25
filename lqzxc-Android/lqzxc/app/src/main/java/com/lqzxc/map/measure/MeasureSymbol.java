package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Symbol;

/**
 * 自定义地图样式 measureSymbol
 * 请参考百度Android SDK API手册的Symbol类
 * @param 测距点样式 getPointSymbol()
 * @param 测距线样式 getLineSymbol()
 * @author QQ472950043
 **/
public class MeasureSymbol {
	/**
	 * 得到测距点样式 getPointSymbol() 请参考百度Android SDK API手册的Symbol类
	 * @author QQ472950043
	 **/
	public static Symbol getPointSymbol() {
		// TODO Auto-generated method stub
		Symbol pointSymbol = new Symbol();
		pointSymbol.setPointSymbol(pointSymbol.new Color(150,255,0,0));
		return pointSymbol;
	}

	/**
	 * 得到测距线样式getLineSymbol() 请参考百度Android SDK API手册的Symbol类
	 * @author QQ472950043
	 **/
	public static Symbol getLineSymbol() {
		// TODO Auto-generated method stub
		Symbol lineSymbol = new Symbol();
		lineSymbol.setLineSymbol(lineSymbol.new Color(150,0,0,0), 3);
		return lineSymbol;
	}
}