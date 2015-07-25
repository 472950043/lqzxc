package com.lqzxc.map.ui;

import android.os.AsyncTask;

import com.baidu.mapapi.map.PopupClickListener;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义地图UI控制模块
 * @author QQ472950043
 */
public class ViewUIBtn extends RelativeLayout {
	
	Context mContext;
	AppContext mAppContext;
	
	public Button btn_research;// 在这搜
	public Button btn_delete;// 删除控件
	// 自定义缩放控件
	public ImageButton btn_zoomin;// 放大
	public ImageButton btn_zoomout;// 缩小
	
	public Button locate_button;// 定位控件
	// 浏览节点控件
	public Button btn_pre;// 上一个节点
	public Button btn_next;// 下一个节点

	public int start;// 节点左边界
	public int end;// 节点右边界
	TextView node_title;
	GeoPoint nodePoint;
	String nodeText;
	/**
	 * 构造方法初始化自定义地图UI控制部件，并设定这7个UI控制按钮
	 * @author QQ472950043
	 */
	public ViewUIBtn(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_nav_btn, this, true);
		btn_research = (Button) findViewById(R.id.btn_research);
		btn_delete = (Button) findViewById(R.id.btn_delete);
		btn_zoomin = (ImageButton) findViewById(R.id.zoominBtn);
		btn_zoomout = (ImageButton) findViewById(R.id.zoomoutBtn);
		locate_button = (Button) findViewById(R.id.locate_button);
		btn_pre = (Button) findViewById(R.id.pre);
		btn_next = (Button) findViewById(R.id.next);

		node_title =(TextView) LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_route_node, null).findViewById(R.id.node_title);
		mAppContext.nodePopupOverlay = new PopupOverlay(mAppContext.mMapView, new PopupClickListener() {
			@Override
			public void onClickedPopup(int index) {
				// 在此处理pop点击事件，index为点击区域索引,点击区域最多可有三个
			}
		});
        initViewUIBtn();
	}
	
	/**
	 * 设定地图上的7个地图UI控制按钮
	 * @author QQ472950043
	 **/
	public void initViewUIBtn() {
		// TODO Auto-generated method stub
		// 在这搜
		btn_research.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.bikeTextOverlay.removeAll();
				mAppContext.bikeItemizedOverlay.removeAll();
				mAppContext.showLoading(mContext);
				// 添加自行车站点
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						mAppContext.mApiClient.download();
						mAppContext.mBike.addBikeSiteList();
						mAppContext.hideLoading();
							mAppContext.isShowBikeText = true;
							mAppContext.isShowBikeItemized = true;
							System.out.println("Bike图层添加成功");
					    //执行刷新使生效
						mAppContext.mMapView.refresh();
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
					}
				}.execute();
			}
		});
		// 删除控件
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAll();
			}
		});

		// 缩放按钮
		OnClickListener zoomClickListener = new OnClickListener() {
			public void onClick(View view) {
				switch (view.getId()) {
				case R.id.zoominBtn:
					mAppContext.mMapView.getController().zoomIn();
					break;
				case R.id.zoomoutBtn:
					mAppContext.mMapView.getController().zoomOut();
					break;
				}
			}
		};
		btn_zoomin.setOnClickListener(zoomClickListener);
		btn_zoomout.setOnClickListener(zoomClickListener);
		locate_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.mLocation.requestLocation();
			}
		});

		// 浏览节点控件
        btn_pre.setVisibility(View.GONE);
		btn_next.setVisibility(View.GONE);
		OnClickListener nodeClickListener = new OnClickListener(){
			public void onClick(View v) {
				// TODO Auto-generated method stub
				nodeClick(v);
			}
        };
        btn_pre.setOnClickListener(nodeClickListener);
        btn_next.setOnClickListener(nodeClickListener);
	}
	
	/**
	 * 清除所有图层
	 * @author QQ472950043
	 **/
	public void deleteAll() {
		// TODO Auto-generated method stub
		// 清除搜索结果图层
		deleteSearchOverlay();
		// 还原标题搜索栏
		mAppContext.mViewMode.setSearchType(0);
		
		// 清除bike图层
		mAppContext.bikeTextOverlay.removeAll();
		mAppContext.bikeItemizedOverlay.removeAll();
//		mAppContext.mMapView.getOverlays().remove(mAppContext.bikeTextOverlay);
//		mAppContext.mMapView.getOverlays().remove(mAppContext.bikeItemizedOverlay);
		mAppContext.isShowBikeText = false;
		mAppContext.isShowBikeItemized = false;
		// 清除定位图层
		mAppContext.mLocationPopup.hidePop();
		mAppContext.mLocationOverlay.setData(null);
	    //执行刷新使生效
		mAppContext.mMapView.refresh();
	}

	/**
	 * 清除搜索结果图层
	 * @author QQ472950043
	 **/
	public void deleteSearchOverlay() {
		// TODO Auto-generated method stub
		// 清除公交路线图层
		if (mAppContext.isShowTransitOverlay) {
			mAppContext.mMapView.getOverlays().remove(mAppContext.transitOverlay);
			mAppContext.isShowTransitOverlay = false;
		}
		// 驾车/步行/公交路线 搜素图层
		if (mAppContext.isShowRouteOverlay) {
			mAppContext.mMapView.getOverlays().remove(mAppContext.routeOverlay);
			mAppContext.isShowRouteOverlay = false;
		}
	}
	
	/**
	 * 节点浏览示例
	 * @param v 上一个、下一个
	 */
	public void nodeClick(View v){
		// TODO Auto-generated method stub
		if (mAppContext.nodeIndex < start || mAppContext.nodeIndex >= end)// 超出边界
			return;
		else if (btn_pre.equals(v) && mAppContext.nodeIndex > start) // 上一个节点
			mAppContext.nodeIndex--;// 索引减
		else if (btn_next.equals(v) && mAppContext.nodeIndex < (end - 1)) // 下一个节点
			mAppContext.nodeIndex++;// 索引加
		
		if (mAppContext.mViewMode.searchType == 1) {
			// 公交换乘使用的数据结构与其他不同，因此单独处理节点浏览
			if (mAppContext.transitOverlay == null)
				return;
			nodeText = mAppContext.transitOverlay.getItem(mAppContext.nodeIndex).getTitle();
			nodePoint = mAppContext.transitOverlay.getItem(mAppContext.nodeIndex).getPoint();
		} else if (mAppContext.mViewMode.searchType == 2 || mAppContext.mViewMode.searchType == 3 || mAppContext.mViewMode.searchType == 4) {
			// 驾车、步行使用的数据结构相同，因此类型为驾车或步行，节点浏览方法相同
			if (mAppContext.routeOverlay == null)
				return;
			nodeText = mAppContext.routeOverlay.getItem(mAppContext.nodeIndex).getTitle();
			nodePoint = mAppContext.routeOverlay.getItem(mAppContext.nodeIndex).getPoint();
		}
		// 启用/禁用上一个、下一个
		if (mAppContext.nodeIndex == start) {
			nodeText = mAppContext.stNode.name;
			btn_pre.setEnabled(false);
		} else
			btn_pre.setEnabled(true);
		if (mAppContext.nodeIndex == end - 1) {
			nodeText = mAppContext.enNode.name;
			btn_next.setEnabled(false);
		} else
			btn_next.setEnabled(true);

		System.out.println(start + " " + mAppContext.nodeIndex + nodeText + " " + end);
		// 移动到指定索引的坐标
		mAppContext.mMapView.getController().animateTo(nodePoint);
		node_title.setText("" + nodeText);
		mAppContext.nodePopupOverlay.showPopup(node_title, nodePoint, 5);
	}
}
