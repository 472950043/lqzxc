package com.lqzxc.map.search;

import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKPlanNode;
import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.adapter.MapExpandableListAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ExpandableListView.OnChildClickListener;
/**
 * 公交搜索结果模块
 * @author QQ472950043
 **/
public class ViewTransit extends RelativeLayout {

	Context mContext;
	AppContext mAppContext;
	
	// headview部件
	LinearLayout headview;
	ViewHead mViewHead;
	
	// footview部件
	LinearLayout footview;
	ViewFoot mViewFoot;
	
	// 方案列表
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// 公交列表适配器
	
	public ViewTransit(Context mContext){
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		// 准备数据
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		MKPlanNode stNode = mAppContext.transitRouteResults.getStart();// 起点
		MKPlanNode enNode = mAppContext.transitRouteResults.getEnd();// 终点
		int taxiPrice = mAppContext.transitRouteResults.getTaxiPrice();// 打车费用参考值
		// 加载部件
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow, this, true);
		// headview部件
		headview = (LinearLayout) findViewById(R.id.headview);
		mViewHead = new ViewHead(mContext);
		headview.addView(mViewHead);

		// 设置标题
		mViewHead.title_text.setText("公交方案");
		// 显示起点和终点
		mViewHead.start_point.setText(stNode.name);
		mViewHead.end_point.setText(enNode.name);
		// 隐藏导航模块
		mViewHead.btn_nav.setVisibility(View.GONE);

		// footview部件
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// 显示打车费用
		mViewFoot.cost.setVisibility(View.VISIBLE);
		mViewFoot.taxi_cost.setText("打车费用参考值" + taxiPrice + "元");// （按照驾车最短距离计算）

		// 显示搜索策略模块
		mViewFoot.btn.setVisibility(View.VISIBLE);
		mViewFoot.txt_1.setText("较快捷");
		mViewFoot.txt_2.setText("少换乘");
		mViewFoot.txt_3.setText("少步行");
		mViewFoot.txt_4.setText("不坐地铁");
		mViewFoot.setImg(mAppContext.select);
		
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		transitAdapter = new MapExpandableListAdapter(mContext);
		transitAdapter.setData(mAppContext.transitRouteResults);
		mExpandableListView.setGroupIndicator(null);// 将控件默认的左边箭头去掉
		mExpandableListView.setAdapter(transitAdapter);
		setListener();
	}

	/**
	 * 设置方案列表点击监听
	 */
	public void setListener() {
		// TODO Auto-generated method stub
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				// 隐藏路线规划展示结果PopupWindow部件
				mAppContext.mPopupWindow.dismiss();
				// 删除其他的搜索结果图层
				mAppContext.mViewUIBtn.deleteSearchOverlay();
      		    // 设置公交图层的路线
				mAppContext.transitOverlay = new TransitOverlay((Activity) mContext, mAppContext.mMapView);
				mAppContext.transitOverlay.setData(mAppContext.transitRouteResults.getPlan(groupPosition));
			    // 添加公交图层
				mAppContext.mMapView.getOverlays().add(mAppContext.transitOverlay);
				mAppContext.isShowTransitOverlay = true;
				// 执行刷新使生效
				mAppContext.mMapView.refresh();
      		    // 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
				mAppContext.mMapView.getController().zoomToSpan(mAppContext.transitOverlay.getLatSpanE6(), mAppContext.transitOverlay.getLonSpanE6());
				// 设定路线节点索引，节点浏览时使用
				mAppContext.nodeIndex = childPosition;
				// 设定路线节点的边界
				mAppContext.mViewUIBtn.start = 0;
				mAppContext.mViewUIBtn.end = mAppContext.transitOverlay.getAllItem().size();
				// 显示节点浏览按钮
				mAppContext.mViewUIBtn.btn_pre.setVisibility(View.VISIBLE);
				mAppContext.mViewUIBtn.btn_next.setVisibility(View.VISIBLE);
				// 由列表点击
				mAppContext.mViewUIBtn.nodeClick(null);
				return false;
			}
		});  
	}
	
}