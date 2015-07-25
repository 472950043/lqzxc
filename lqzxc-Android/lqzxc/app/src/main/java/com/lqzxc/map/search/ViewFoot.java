package com.lqzxc.map.search;

import com.baidu.mapapi.search.MKSearch;
import com.lqzxc.AppContext;
import com.lqzxc.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 步行搜索结果模块
 * @author QQ472950043
 **/
public class ViewFoot extends LinearLayout {

	Context mContext;
	AppContext mAppContext;
	
	LinearLayout cost;
	TextView taxi_cost;
	LinearLayout btn;
	ImageView img_1;
	TextView txt_1;
	RelativeLayout btn_1;
	ImageView img_2;
	TextView txt_2;
	RelativeLayout btn_2;
	ImageView img_3;
	TextView txt_3;
	RelativeLayout btn_3;
	ImageView img_4;
	TextView txt_4;
	RelativeLayout btn_4;
	
	public ViewFoot(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext=mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow_foot, this, true);
		cost = (LinearLayout) findViewById(R.id.cost);
		taxi_cost = (TextView) findViewById(R.id.taxi_cost);
		btn = (LinearLayout) findViewById(R.id.btn);
		btn_1 = (RelativeLayout) findViewById(R.id.btn_1);
		img_1 = (ImageView) findViewById(R.id.img_1);
		txt_1 = (TextView) findViewById(R.id.txt_1);
		btn_2 = (RelativeLayout) findViewById(R.id.btn_2);
		img_2 = (ImageView) findViewById(R.id.img_2);
		txt_2 = (TextView) findViewById(R.id.txt_2);
		btn_3 = (RelativeLayout) findViewById(R.id.btn_3);
		img_3 = (ImageView) findViewById(R.id.img_3);
		txt_3 = (TextView) findViewById(R.id.txt_3);
		btn_4 = (RelativeLayout) findViewById(R.id.btn_4);
		img_4 = (ImageView) findViewById(R.id.img_4);
		txt_4 = (TextView) findViewById(R.id.txt_4);
		
		btn_1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.select=1;
				search();
				setImg(1);
			}
		});
		btn_2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.select=2;
				search();
				setImg(2);
			}
		});
		btn_3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.select=3;
				search();
				setImg(3);
			}
		});
		btn_4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.select=4;
				search();
				setImg(4);
			}
		});
	}

	/**
  	 * 改变显示策略
  	 */
  	public void setImg(int select) {
  		// TODO Auto-generated method stub
  		//根据检索策略选择相应的常量
  		switch (select) {
		case 1:
  			txt_1.setTextColor(Color.WHITE);
  			txt_2.setTextColor(Color.GRAY);   
  			txt_3.setTextColor(Color.GRAY);   
  			txt_4.setTextColor(Color.GRAY);   
  			img_1.setImageResource(R.drawable.baidu_map_btn_nav_tab_on);
  			img_2.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_3.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_4.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);   
  			break;
		case 2:
  			txt_1.setTextColor(Color.GRAY);
  			txt_2.setTextColor(Color.WHITE);
  			txt_3.setTextColor(Color.GRAY);
  			txt_4.setTextColor(Color.GRAY);
  			img_1.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_2.setImageResource(R.drawable.baidu_map_btn_nav_tab_on);
  			img_3.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_4.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);  
  			break;
		case 3:
  			txt_1.setTextColor(Color.GRAY);
  			txt_2.setTextColor(Color.GRAY);
  			txt_3.setTextColor(Color.WHITE);
  			txt_4.setTextColor(Color.GRAY);
  			img_1.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_2.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_3.setImageResource(R.drawable.baidu_map_btn_nav_tab_on);
  			img_4.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			break;
		case 4:
  			txt_1.setTextColor(Color.GRAY);
  			txt_2.setTextColor(Color.GRAY);
  			txt_3.setTextColor(Color.GRAY);
  			txt_4.setTextColor(Color.WHITE);
  			img_1.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_2.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_3.setImageResource(R.drawable.baidu_map_btn_nav_tab_off);
  			img_4.setImageResource(R.drawable.baidu_map_btn_nav_tab_on);
  		}
  	}
  	
  	/**
	 * 根据select执行路线规划策略
	 */
	public void search() {
		// TODO Auto-generated method stub
		switch (mAppContext.select) {
		case 1:
			mAppContext.mSearch.setTransitPolicy(MKSearch.EBUS_TIME_FIRST);
			mAppContext.mSearch.setDrivingPolicy(MKSearch.ECAR_TIME_FIRST);  
			break;
		case 2:
			mAppContext.mSearch.setTransitPolicy(MKSearch.EBUS_TRANSFER_FIRST);
			mAppContext.mSearch.setDrivingPolicy(MKSearch.ECAR_DIS_FIRST);   
			break;
		case 3:
			mAppContext.mSearch.setTransitPolicy(MKSearch.EBUS_WALK_FIRST);
			mAppContext.mSearch.setDrivingPolicy(MKSearch.ECAR_FEE_FIRST);   
			break;
		case 4:
			mAppContext.mSearch.setTransitPolicy(MKSearch.EBUS_NO_SUBWAY);
			mAppContext.mSearch.setDrivingPolicy(MKSearch.ECAR_AVOID_JAM);   
			break;
		}
		mAppContext.mViewMode.routeSearch();
	}
}