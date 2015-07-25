package com.lqzxc.map.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * �Զ����ͼ������������transit��drive��walk��������mode
 * @author QQ472950043
 **/
public class ViewMode extends RelativeLayout {

	Context mContext;
	AppContext mAppContext;
	TextView transit; // ��������
	TextView drive; // �ݳ�����
	TextView walk; // ��������
	RelativeLayout btn_left;// ����
	RelativeLayout btn_transit;// ��������
	RelativeLayout btn_drive;// �ݳ�����
	RelativeLayout btn_walk;// ��������
	RelativeLayout btn_right;// ����
	/**
	 * ��¼������mode������1����transit 2�ݳ�drive 3����walk
	 */
	int searchType;
	
	/**
	 * ���췽����ʼ���Զ����ͼ�������������趨left��transit��drive��walk��right��5����ť
	 * @author QQ472950043
	 **/
	public ViewMode(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_mode, this, true);

		btn_left = (RelativeLayout)findViewById(R.id.btn_left);
		btn_transit = (RelativeLayout)findViewById(R.id.btn_transit);
		btn_drive = (RelativeLayout)findViewById(R.id.btn_drive);
		btn_walk = (RelativeLayout)findViewById(R.id.btn_walk);
        transit = (TextView)findViewById(R.id.mode_transit);
        drive = (TextView)findViewById(R.id.mode_drive);
        walk = (TextView)findViewById(R.id.mode_walk);
        btn_right = (RelativeLayout)findViewById(R.id.btn_right);
        
        initViewMode();
	}
	
	/**
	 * �趨left��transit��drive��walk��right��5����ť
	 * @author QQ472950043
	 **/
	public void initViewMode() {
		// TODO Auto-generated method stub
		//�������
        btn_left.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)mContext).finish();
			}
		});
        btn_transit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setSearchType(1);
				if (mAppContext.transitRouteResults == null)
					routeSearch();
				else if(searchType == 1 && isSearched())
					mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0);  
				else
					routeSearch();
			}
		});
        btn_drive.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setSearchType(2);
				if (mAppContext.routeResults == null)
					routeSearch();
				else if(searchType == 2 && isSearched())
					mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0);  
				else
					routeSearch();
			}
		});
        btn_walk.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setSearchType(3);
				if (mAppContext.routeResults == null)
					routeSearch();
				else if(searchType == 3 && isSearched())
					mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0);  
				else
					routeSearch();
			}
		});
		//�Ҽ�����
        btn_right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.setClassName(mContext, PreferencesActivity.class.getName());
				mContext.startActivity(intent);
			}
		});
	}

	/**
	 * ����searchType����mode�趨transit��drive��walk��3����ť�ı���ͼƬ
	 * @author QQ472950043
	 **/
	public void setSearchType(int searchType) {
		// TODO Auto-generated method stub
		this.searchType = searchType;
		switch(searchType) {
	        case 0:
	    		transit.setBackgroundResource(R.drawable.baidu_map_mode_transit);
	    		drive.setBackgroundResource(R.drawable.baidu_map_mode_driving);
	    		walk.setBackgroundResource(R.drawable.baidu_map_mode_walk);
	            break;
	        case 1:
	    		transit.setBackgroundResource(R.drawable.baidu_map_mode_transit_on);
	    		drive.setBackgroundResource(R.drawable.baidu_map_mode_driving);
	    		walk.setBackgroundResource(R.drawable.baidu_map_mode_walk);
	            break;
	        case 2:
	    		transit.setBackgroundResource(R.drawable.baidu_map_mode_transit);
	    		drive.setBackgroundResource(R.drawable.baidu_map_mode_driving_on);
	    		walk.setBackgroundResource(R.drawable.baidu_map_mode_walk);
	            break;
	        case 3:
	    		transit.setBackgroundResource(R.drawable.baidu_map_mode_transit);
	    		drive.setBackgroundResource(R.drawable.baidu_map_mode_driving);
	    		walk.setBackgroundResource(R.drawable.baidu_map_mode_walk_on);
	            break;
	    }
	}
	
	/**
	 * ����searchType�ж�stNode��enNode��û�б仯��û�б仯˵����������ֱ�ӵ�����ʾ����
	 * @author QQ472950043
	 */
	public boolean isSearched() {
		// TODO Auto-generated method stub
		if (searchType == 1
				&& mAppContext.stNode.name.equals(mAppContext.transitRouteResults.getStart().name)
				&& mAppContext.enNode.name.equals(mAppContext.transitRouteResults.getEnd().name))
			return true;
		else if ((searchType == 2 || searchType == 3)
				&& mAppContext.stNode.name.equals(mAppContext.routeResults.get(0).getStart().name) 
				&& mAppContext.enNode.name.equals(mAppContext.routeResults.get(0).getEnd().name))
			return true;
		return false;
	}
	
	/**
	 * ����searchType����mode��ִ��·�߹滮mSearch
	 * @author QQ472950043
	 */
	public void routeSearch() {
		// TODO Auto-generated method stub
		mAppContext.showLoading(mContext);
		switch (searchType) {
		case 1:
			mAppContext.mSearch.transitSearch(mAppContext.city, mAppContext.stNode, mAppContext.enNode);
			break;
		case 2:
			mAppContext.mSearch.drivingSearch(mAppContext.stCity, mAppContext.stNode, mAppContext.enCity, mAppContext.enNode);
			break;
		case 3:
			mAppContext.mSearch.walkingSearch(mAppContext.stCity, mAppContext.stNode, mAppContext.enCity, mAppContext.enNode);
			break;
		}
	}
}
