package com.lqzxc.map.measure;

import com.baidu.mapapi.map.Symbol;

/**
 * �Զ����ͼ��ʽ measureSymbol
 * ��ο��ٶ�Android SDK API�ֲ��Symbol��
 * @param ������ʽ getPointSymbol()
 * @param �������ʽ getLineSymbol()
 * @author QQ472950043
 **/
public class MeasureSymbol {
	/**
	 * �õ�������ʽ getPointSymbol() ��ο��ٶ�Android SDK API�ֲ��Symbol��
	 * @author QQ472950043
	 **/
	public static Symbol getPointSymbol() {
		// TODO Auto-generated method stub
		Symbol pointSymbol = new Symbol();
		pointSymbol.setPointSymbol(pointSymbol.new Color(150,255,0,0));
		return pointSymbol;
	}

	/**
	 * �õ��������ʽgetLineSymbol() ��ο��ٶ�Android SDK API�ֲ��Symbol��
	 * @author QQ472950043
	 **/
	public static Symbol getLineSymbol() {
		// TODO Auto-generated method stub
		Symbol lineSymbol = new Symbol();
		lineSymbol.setLineSymbol(lineSymbol.new Color(150,0,0,0), 3);
		return lineSymbol;
	}
}