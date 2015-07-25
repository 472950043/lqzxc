package com.lqzxc.map.search;

import com.baidu.mapapi.navi.BaiduMapAppNotSupportNaviException;
import com.baidu.mapapi.navi.BaiduMapNavigation;
import com.baidu.mapapi.navi.IllegalNaviArgumentException;
import com.baidu.mapapi.navi.NaviPara;
import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.widget.DialogBase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * �������ͷ��ģ��
 * @author QQ472950043
 **/
public class ViewHead extends LinearLayout {
	Context mContext;
	AppContext mAppContext;
	
	ImageView title_btn_left;
	TextView title_text;
	TextView start_point;
	TextView end_point;
	LinearLayout btn_nav;
	RelativeLayout btn_car;
	RelativeLayout btn_simulate_car;
	DialogBase mNavDialog;
	
	public ViewHead(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext=mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow_head, this, true);
		
		title_btn_left = (ImageView) findViewById(R.id.title_btn_left);
		title_text = (TextView) findViewById(R.id.title_text);
		
		start_point = (TextView) findViewById(R.id.start_point);
		end_point = (TextView) findViewById(R.id.end_point);
		
		btn_nav = (LinearLayout) findViewById(R.id.btn_nav);
		btn_car = (RelativeLayout) findViewById(R.id.btn_car);
		btn_simulate_car = (RelativeLayout) findViewById(R.id.btn_simulate_car);
		title_btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.mPopupWindow.dismiss();
			}
		});

		btn_car.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initNaviPara();
			}
		});
		btn_simulate_car.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				initNaviPara();
			}
		});
	}
	
	/**  
	 * ��������  
	 * ���������յ㲻��Ϊ�գ���GPS����ʱ�������û�λ�õ��յ��ĵ�����  
	 * ��GPS������ʱ����������㵽�յ���ģ�⵼����  
	 **/  
	public void initNaviPara() {
		// TODO Auto-generated method stub
		NaviPara para = new NaviPara();   
		para.startPoint = mAppContext.stNode.pt;//�������   
		para.startName= "�����￪ʼ";   
		para.endPoint  = mAppContext.enNode.pt;//�յ�����   
		para.endName   = "���������";         
		try {   
		   //����ٶȵ�ͼ�ͻ��˵�������,����thisΪActivity��    
	        BaiduMapNavigation.openBaiduMapNavi(para, (Activity)mContext);   
		} catch (IllegalNaviArgumentException  e) {   
	        e.printStackTrace();   
		} catch (BaiduMapAppNotSupportNaviException  e) {   
	        e.printStackTrace();   
	        //�����°汾�ٶȵ�ͼ����ҳ�湦��,����thisΪActivity��   
	        mNavDialog=new DialogBase(mContext, "������ʾ","\n����δ��װ�ٶȵ�ͼapp��app�汾���ͣ����ȷ�����أ�\n" ,"ȷ��");
	        mNavDialog.setOnConfirmListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					BaiduMapNavigation.GetLatestBaiduMapApp((Activity)mContext);
					mNavDialog.dismiss();
				}
			});
	        mNavDialog.show();
		} 
	}
}