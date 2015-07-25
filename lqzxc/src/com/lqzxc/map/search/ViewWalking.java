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
 * 步行搜索结果模块
 * @author QQ472950043
 **/
public class ViewWalking extends RelativeLayout {

	AppContext mAppContext;
	
	LinearLayout headview;
	ViewHead mViewHead;
	
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// 公交列表适配器
	
	LinearLayout footview;
	ViewFoot mViewFoot;
	
	public ViewWalking(Context mContext){
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		mAppContext = (AppContext) mContext.getApplicationContext();

		int taxiPrice = mAppContext.routeResults.get(0).getTaxiPrice();
		
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow, this, true);
		// headview
		headview = (LinearLayout) findViewById(R.id.headview);
		mViewHead = new ViewHead(mContext);
		headview.addView(mViewHead);

		// 标题
		mViewHead.title_text.setText("步行方案");
		// 显示起点和终点
		mViewHead.start_point.setText(mAppContext.stNode.name);
		mViewHead.end_point.setText(mAppContext.enNode.name);
		// 隐藏导航模块
		mViewHead.btn_nav.setVisibility(View.GONE);

		// footview
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// 隐藏打车费用
		mViewFoot.cost.setVisibility(View.GONE);
		mViewFoot.taxi_cost.setText("打车费用参考值" + taxiPrice + "元");// （按照驾车最短距离计算）

		// 隐藏搜索策略模块
		mViewFoot.btn.setVisibility(View.GONE);
		
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