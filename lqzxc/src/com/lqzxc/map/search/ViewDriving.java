package com.lqzxc.map.search;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.adapter.MapExpandableListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
/**
 * 驾车搜索结果模块
 * @author QQ472950043
 **/
public class ViewDriving extends RelativeLayout {

	AppContext mAppContext;
	
	LinearLayout headview;
	ViewHead mViewHead;
	
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// 公交列表适配器
	
	LinearLayout footview;
	ViewFoot mViewFoot;
	
	public ViewDriving(Context mContext){
		super(mContext);
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow, this, true);
		// headview
		headview = (LinearLayout) findViewById(R.id.headview);
		mViewHead = new ViewHead(mContext);
		headview.addView(mViewHead);

		// 标题
		mViewHead.title_text.setText("驾车方案");
		// 显示起点和终点
		mViewHead.start_point.setText(mAppContext.routeResults.get(0).getStart().name);
		mViewHead.end_point.setText(mAppContext.routeResults.get(0).getEnd().name);
		// 显示导航模块
		mViewHead.btn_nav.setVisibility(View.VISIBLE);

		// footview
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// 隐藏打车费用
		mViewFoot.cost.setVisibility(View.GONE);

		// 显示搜索策略模块
		mViewFoot.btn.setVisibility(View.VISIBLE);
		mViewFoot.txt_1.setText("最少时间");
		mViewFoot.txt_2.setText("最短距离");
		mViewFoot.txt_3.setText("避开拥堵");
		mViewFoot.txt_4.setText("不走高速");
		mViewFoot.setImg(mAppContext.select);
		
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		transitAdapter = new MapExpandableListAdapter(mContext);
		transitAdapter.setData();
		mExpandableListView.setGroupIndicator(null);// 将控件默认的左边箭头去掉
		mExpandableListView.setAdapter(transitAdapter);
	}
	
	public void setOnKeyListener(View.OnKeyListener onKeyListener) {
		mExpandableListView.setOnKeyListener(onKeyListener);
	}
}