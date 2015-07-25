package com.lqzxc.widget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.adapter.SearchAdapter;
import com.lqzxc.map.ui.MapActivity;
import com.lqzxc.modal.BikeStie;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
/**
 * 搜素UI模块
 * @author QQ472950043
 **/
public class ViewSearch extends LinearLayout {
	Context mContext;	
	AppContext mAppContext;
	//数据列表
	public EditText site;
	RelativeLayout btn_search;
	ListView listview;
	ListView result;
	LinearLayout widget_more;
	ProgressBar moreProgressBar;
	TextView loadMoreView;

	SearchAdapter mSearchAdapter;
	SearchAdapter mResultAdapter;
	String key;
	
	public ViewSearch(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		this.mContext = context;
		setWillNotDraw(false);
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(context).inflate(R.layout.widget_search, this, true);
		
		site = (EditText) findViewById(R.id.site);
		btn_search = (RelativeLayout) findViewById(R.id.btn_search);
		listview = (ListView) findViewById(R.id.listView);
		result = (ListView) findViewById(R.id.result);
		widget_more = (LinearLayout) findViewById(R.id.widget_more);
		moreProgressBar = (ProgressBar) findViewById(R.id.progress_more);
		loadMoreView = (TextView) findViewById(R.id.textview_more_tips);

		result.setVisibility(View.GONE);
    	btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				updateSearch();
		        site.clearFocus();
		    	InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(site.getWindowToken(), 0);
			}
		});
		initListener(); 
		if(!mAppContext.data.getBoolean("isDownload", false))
			updateSearchs();
	}

	/**
	 * 设置监听
	 * @author QQ472950043
	 **/
	public void initListener() {
		// TODO Auto-generated method stub
		mSearchAdapter = new SearchAdapter(mContext);
		listview.setAdapter(mSearchAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView textview = (TextView) view.findViewById(R.id.netName);
				mAppContext.mBikeStie = (BikeStie) textview.getTag();
				mAppContext.mApiClient.visitUpdate(mAppContext.mBikeStie.getMid());
				mContext.startActivity(new Intent(mContext, MapActivity.class));
				System.out.println("x:" +mAppContext.mBikeStie.getGpsx() + "y:" + mAppContext.mBikeStie.getGpsy());
			}
		});

		// 查询结果
		mResultAdapter = new SearchAdapter(mContext);
		result.setAdapter(mResultAdapter);
		result.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				TextView textview = (TextView) view.findViewById(R.id.netName);
				mAppContext.mBikeStie = (BikeStie) textview.getTag();
				mAppContext.mApiClient.visitUpdate(mAppContext.mBikeStie.getMid());
				mContext.startActivity(new Intent(mContext, MapActivity.class));
				System.out.println("x:" +mAppContext.mBikeStie.getGpsx() + "y:" + mAppContext.mBikeStie.getGpsy());
			}
		});
	}
	/**
	 * 更新UI
	 * @author QQ472950043
	 **/
	public void updateSearchs() {
		// TODO Auto-generated method stub
		if(mAppContext.mBikeSties.size()==0){
			moreProgressBar.setVisibility(View.GONE);
			loadMoreView.setText("网络不给力，初始化失败，请下拉重试！");
			widget_more.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}else{
			widget_more.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		mSearchAdapter.setData(mAppContext.mBikeSties);
		setListViewHeightBasedOnChildren(listview);
	}

	/**
	 * 更新搜索UI
	 * @author QQ472950043
	 **/
	public void updateSearch() {
		// TODO Auto-generated method stub
		key = site.getText().toString();
		if (key.length() > 0) {
			listview.setVisibility(View.GONE);
			result.setVisibility(View.VISIBLE);
			List<BikeStie> temp = new ArrayList<BikeStie>();
			if (isInt(key)) {
				for (int i = 0; i < mAppContext.mBikeSties.size(); i++)
					if (i + 1 == toInt(key))
						temp.add(mAppContext.mBikeSties.get(i));
			} else {
				for (int i = 0; i < mAppContext.mBikeSties.size(); i++)
					if (mAppContext.mBikeSties.get(i).getNetName().indexOf(key) >= 0 || mAppContext.mBikeSties.get(i).getAddress().indexOf(key) >= 0)
						temp.add(mAppContext.mBikeSties.get(i));
			}
			if (temp.size() > 0) {
				widget_more.setVisibility(View.GONE);
				// id从小到大
				Collections.sort(temp, new Comparator<BikeStie>() {
					@Override
					public int compare(BikeStie o1, BikeStie o2) {
						// TODO Auto-generated method stub
						if (o1.getId() < o2.getId()) {
							return -1;
						}
						return 1;
					}
				});
				mResultAdapter.setData(temp);
				setListViewHeightBasedOnChildren(result);
			} else {
				loadMoreView.setText("对不起，未找到相关结果");
				moreProgressBar.setVisibility(View.GONE);
				widget_more.setVisibility(View.VISIBLE);
			}
		} else {
			listview.setVisibility(View.VISIBLE);
			result.setVisibility(View.GONE);
			widget_more.setVisibility(View.GONE);
		}
	}

	public static boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static int toInt(String str) {
		try {
			return Integer.parseInt(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 计算listview高度
	 * @author QQ472950043
	 **/
	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// TODO Auto-generated method stub
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}
}