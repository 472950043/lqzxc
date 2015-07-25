package com.lqzxc.map.search;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.adapter.SelectAdapter;
import com.lqzxc.map.modal.Option;
/**
 * ���յ��б�ѡ��ģ��
 * @author QQ472950043
 **/
public class DialogSelect extends Dialog {

	Context mContext;
	TextView dialog_title;
	String title;
	AppContext mAppContext;
	ListView listview_select;

	List<Option> options;
	SelectAdapter mSelectAdapter;

	public DialogSelect(Context mContext) {
		super(mContext, R.style.mDialog);
		// TODO Auto-generated constructor stub
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		mSelectAdapter = new SelectAdapter(mContext);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.baidu_map_widget_search);

		Window window = getWindow();
		LayoutParams lp = window.getAttributes();
		DisplayMetrics dm = new DisplayMetrics();
		window.getWindowManager().getDefaultDisplay().getMetrics(dm);
		lp.width = dm.widthPixels * 9 / 10;
		lp.height = dm.heightPixels * 8 / 10;
		window.setAttributes(lp);
		
		dialog_title = (TextView) findViewById(R.id.dialog_title);
		dialog_title.setText(title);
		
		listview_select = (ListView) findViewById(R.id.listview_select);
		listview_select.setAdapter(mSelectAdapter);
		listview_select.setOnItemClickListener(new OnItemClickListener(){
			// �����¼�
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
				// TODO Auto-generated method stub
				TextView textview = (TextView) view.findViewById(R.id.name);
				Option mOption=(Option) textview.getTag();
				if (mAppContext.stPois != null && mAppContext.stPois.size() > 1) {
					mAppContext.stNode.name = mOption.getName();
					mAppContext.stNode.pt = mOption.getmGeoPoint();
				} else if (mAppContext.enPois != null && mAppContext.enPois.size() > 1) {
					mAppContext.enNode.name = mOption.getName();
					mAppContext.enNode.pt = mOption.getmGeoPoint();
				} else if (mAppContext.stCities != null) {
					mAppContext.stCity = mOption.getName();
				} else if (mAppContext.enCities != null) {
					mAppContext.enCity = mOption.getName();
				}
				dismiss();
				mAppContext.mViewMode.routeSearch();
			}
		});
	}

	public void setPoiAddr() {
		// TODO Auto-generated method stub
		if (mAppContext.stPois != null && mAppContext.stPois.size() > 1) {
			title="��ѡ�����";
			mSelectAdapter.setPois(mAppContext.stPois);
		} else if (mAppContext.enPois != null && mAppContext.enPois.size() > 1) {
			title="��ѡ���յ�";
			mSelectAdapter.setPois(mAppContext.enPois);
		} else if (mAppContext.stCities != null) {
			title="��ѡ��������";
			mSelectAdapter.setCityLists(mAppContext.stCities);
		} else if (mAppContext.enCities != null) {
			title="��ѡ���յ����";
			mSelectAdapter.setCityLists(mAppContext.enCities);
		}
	}

}