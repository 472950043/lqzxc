package com.lqzxc.map.search;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.ExpandableListView.OnChildClickListener;

import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.search.MKAddrInfo;
import com.baidu.mapapi.search.MKBusLineResult;
import com.baidu.mapapi.search.MKCityListInfo;
import com.baidu.mapapi.search.MKDrivingRouteResult;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKPoiResult;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKSearchListener;
import com.baidu.mapapi.search.MKShareUrlResult;
import com.baidu.mapapi.search.MKSuggestionResult;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.mapapi.search.MKWalkingRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.map.modal.RouteResult;
/**
 * 自定义搜索模块
 * @author QQ472950043
 **/
public class MapSearch{
	
	Context mContext;
	AppContext mAppContext;

	ViewTransit view_transit;// 公交列表
	ViewDriving view_driving;// 驾车列表
	ViewWalking view_walking;// 步行列表

	DialogSelect mDialogSelect;//起终点选择
	/**
	 * 构造方法初始化自定义搜索类
	 * @author QQ472950043
	 **/
	public MapSearch(Context mContext){
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		mAppContext.mSearch = new MKSearch();

		// 设定起终点城市
		mAppContext.city = "台州市";
		mAppContext.stCity = "台州市";
		mAppContext.enCity = "台州市";
		// 设定起点
		mAppContext.stNode = new MKPlanNode();
		mAppContext.stNode.pt = new GeoPoint(28662335, 121427180);
		mAppContext.stNode.name = "台州市";
		// 设定终点
		mAppContext.enNode = new MKPlanNode();
		mAppContext.enNode.pt = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.enNode.name = mAppContext.mBikeStie.getNetName();
		
		initMapSearch();
	}
	
	/**
	 * 初始化搜索模块，注册事件监听
	 * 
	 * @param MKEVENT_BUS_DETAIL 15 公交详情搜索事件
	 * @param MKEVENT_MAP_MOVE_FINISH 14 地图移动完成事件
	 * @param MKEVENT_POIDETAILSHAREURL 18 poi共享短串
	 * @param MKEVENT_POIRGCSHAREURL 17 反地理编码共享短url事件
	 * @param MKEVENT_SUGGESTION 16 联想词搜索事件
	 * 
	 * @param ERROR_NETWORK_CONNECT 2 网络连接错误
	 * @param ERROR_NETWORK_DATA 3 网络数据错误
	 * @param ERROR_PERMISSION_DENIED 300 授权验证失败
	 * @param ERROR_RESULT_NOT_FOUND 100 未找到搜索结果
	 * @param ERROR_ROUTE_ADDR 4 路线搜索起点或终点有歧义
	 */
	public void initMapSearch() {
		// TODO Auto-generatedEethod stub
		mAppContext.mSearch.init(mAppContext.mBMapManager, new MKSearchListener() {
			// 地址信息搜索结果
			public void onGetAddrResult(MKAddrInfo res, int error) {
				// TODO Auto-generated method stub
				try{
					if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
						// 设定起点
						mAppContext.stNode = new MKPlanNode();
						mAppContext.stNode.pt = mAppContext.locatCenter;
						mAppContext.stNode.name = "我的位置";
						// 弹出泡泡
						mAppContext.location_title.setText("我的位置（精确到"+(int)mAppContext.mData.accuracy+"米）");
						mAppContext.location_content.setText(res.strAddr);
						if(mAppContext.mLocationPopup!=null)
							mAppContext.mLocationOverlay.showPopup(mAppContext.location_view,mAppContext.locatCenter,5);
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// 公交车详情信息搜索结果
			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				// 起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR) {// error == 4
					// 遍历所有地址，选择起终点地址
					openSelectDialog(
						res.getAddrResult().mStartPoiList,
						res.getAddrResult().mEndPoiList,
						res.getAddrResult().mStartCityList,
						res.getAddrResult().mEndCityList);
					return;
				}else if (error != 0 || res == null) {
					mAppContext.ToastMessage(mContext, getError(error));
					return;
				}
				// 显示路径结果
				mAppContext.transitRouteResults = res;
				showTransitRouteResult();
			}

			// 驾车结果返回
			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				//起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					// 遍历所有地址，选择起终点地址
					openSelectDialog(
							res.getAddrResult().mStartPoiList,
							res.getAddrResult().mEndPoiList,
							res.getAddrResult().mStartCityList,
							res.getAddrResult().mEndCityList);
					return;
				}
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					mAppContext.ToastMessage(mContext, getError(error));
					return;
				}
				// 显示路径结果
				mAppContext.routeResults = new ArrayList<RouteResult>();
				for (int i = 0; i < res.getNumPlan(); i++) {
					for (int j = 0; j < res.getPlan(i).getNumRoutes(); j++) {
						RouteResult routeResult = new RouteResult();
						routeResult.setStart(res.getStart());
						routeResult.setEnd(res.getEnd());
						routeResult.setTaxiPrice(res.getTaxiPrice());// 这个其实没有实际意义，本来就是驾车了的。。
						routeResult.setNumPlan(res.getNumPlan());
						routeResult.setRoute(res.getPlan(i).getRoute(j));
						routeResult.setTime(res.getPlan(i).getTime());
						routeResult.setDistance(res.getPlan(i).getDistance());
						mAppContext.routeResults.add(routeResult);
					}
				}
				showDrivingRouteResult();
			}

			// 步行结果返回
			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				//起点或终点有歧义，需要选择具体的城市列表或地址列表
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					// 遍历所有地址，选择起终点地址
					openSelectDialog(
							res.getAddrResult().mStartPoiList,
							res.getAddrResult().mEndPoiList,
							res.getAddrResult().mStartCityList,
							res.getAddrResult().mEndCityList);
					return;
				}
				// 错误号可参考MKEvent中的定义
				if (error != 0 || res == null) {
					mAppContext.ToastMessage(mContext, getError(error));
					return;
				}
				// 显示路径结果
				mAppContext.routeResults = new ArrayList<RouteResult>();
				for (int i = 0; i < res.getNumPlan(); i++) {
					for (int j = 0; j < res.getPlan(i).getNumRoutes(); j++) {
						RouteResult routeResult = new RouteResult();
						routeResult.setStart(res.getStart());
						routeResult.setEnd(res.getEnd());
						routeResult.setTaxiPrice(res.getTaxiPrice());
						routeResult.setNumPlan(res.getNumPlan());
						routeResult.setRoute(res.getPlan(i).getRoute(j));
						routeResult.setTime(res.getPlan(i).getTime());
						routeResult.setDistance(res.getPlan(i).getDistance());
						mAppContext.routeResults.add(routeResult);
					}
				}
				showWalkingRouteResult();
			}

			// 公交路线结果返回
			public void onGetPoiResult(MKPoiResult res, int type, int error) {
				// TODO Auto-generated method stub
			}

			public void onGetBusDetailResult(MKBusLineResult result, int iError) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGetSuggestionResult(MKSuggestionResult res, int arg1) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGetPoiDetailSearchResult(int type, int iError) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onGetShareUrlResult(MKShareUrlResult result, int type, int error) {
				// TODO Auto-generated method stub
			}

			/**
			 * 弹出选择对话框
			 */
			public void openSelectDialog(List<MKPoiInfo> mStartPoiList, List<MKPoiInfo> mEndPoiList, List<MKCityListInfo> mStartCityList, List<MKCityListInfo> mEndCityList) {
				// TODO Auto-generated method stub
				mAppContext.stPois = mStartPoiList;
				mAppContext.enPois = mEndPoiList;
				mAppContext.stCities = mStartCityList;
				mAppContext.enCities = mEndCityList;
				mDialogSelect = new DialogSelect(mContext);
				if (mStartPoiList == null && mEndPoiList == null && mStartCityList == null && mEndCityList == null)
					mAppContext.ToastMessage(mContext, "抱歉，"+mAppContext.city+"未找到结果");
				else{
					mDialogSelect.setPoiAddr();
					mDialogSelect.show();
				}
			}
			
			public String getError(int error){
				switch (error) {
				case 2:
					return "网络连接错误";
				case 3:
					return "网络数据错误";
				case 300:
					return "授权验证失败";
				case 100:
					return "抱歉，未找到结果";
				default:
					return "抱歉，未找到结果";
				}
			}
		});
	}
	
	/**
	 * 公交车详情信息搜索结果
	 */
	public void showTransitRouteResult() {
		// TODO Auto-generated method stub
		view_transit = new ViewTransit(mContext);
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		view_transit.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					mAppContext.mPopupWindow.dismiss();
					break;
				}
				return true;
			}
		});
		if(mAppContext.mPopupWindow!=null)
			mAppContext.mPopupWindow.dismiss();
		mAppContext.mPopupWindow = new PopupWindow(view_transit, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		mAppContext.mPopupWindow.setAnimationStyle(R.style.popuStyle);
		mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0);  
	}
	
	/**
	 * 驾车详情信息搜索结果
	 */
	public void showDrivingRouteResult() {
		view_driving = new ViewDriving(mContext);
		view_driving.mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				mAppContext.mPopupWindow.dismiss();
				
				showRouteOverlay(groupPosition);
				// 重置路线节点索引，节点浏览时使用
				mAppContext.nodeIndex = childPosition;
				return false;
			}
		});  
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		view_driving.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					mAppContext.mPopupWindow.dismiss();
					break;
				}
				return true;
			}
		});
		if(mAppContext.mPopupWindow!=null)
			mAppContext.mPopupWindow.dismiss();
		mAppContext.mPopupWindow = new PopupWindow(view_driving, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		mAppContext.mPopupWindow.setAnimationStyle(R.style.popuStyle);
		mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0);  
	}
	
	/**
	 * 步行详情信息搜索结果
	 */
	public void showWalkingRouteResult() {
		view_walking = new ViewWalking(mContext);
		view_walking.mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				mAppContext.mPopupWindow.dismiss();
				
				showRouteOverlay(groupPosition);
				// 重置路线节点索引，节点浏览时使用
				mAppContext.nodeIndex = childPosition;
				return false;
			}
		});  
		// 监听返回按钮事件，因为此时焦点在popupwindow上，如果不监听，返回按钮没有效果
		view_walking.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				switch (keyCode) {
				case KeyEvent.KEYCODE_BACK:
					mAppContext.mPopupWindow.dismiss();
					break;
				}
				return true;
			}
		});
		
		if(mAppContext.mPopupWindow!=null)
			mAppContext.mPopupWindow.dismiss();
		mAppContext.mPopupWindow = new PopupWindow(view_walking, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT, true);
		mAppContext.mPopupWindow.setAnimationStyle(R.style.popuStyle);
		mAppContext.mPopupWindow.showAtLocation(mAppContext.main_layout, Gravity.BOTTOM,0, 0); 
	}
	
	public void showRouteOverlay(int groupPosition) {
		// TODO Auto-generated method stub
		mAppContext.mViewUIBtn.deleteSearchOverlay();
	    // 添加 驾车/步行/公交路线 路线图层
		mAppContext.routeOverlay = new RouteOverlay((Activity) mContext, mAppContext.mMapView);
		// 此处仅展示一个方案作为示例
//		mAppContext.route = ;
		mAppContext.routeOverlay.setData(mAppContext.routeResults.get(groupPosition).getRoute());
		mAppContext.mMapView.getOverlays().add(mAppContext.routeOverlay);
		mAppContext.isShowRouteOverlay = true;
		// 执行刷新使生效
		mAppContext.mMapView.refresh();
		// 使用zoomToSpan()绽放地图，使路线能完全显示在地图上
		mAppContext.mMapView.getController().zoomToSpan(mAppContext.routeOverlay.getLatSpanE6(),mAppContext.routeOverlay.getLonSpanE6());

		mAppContext.mViewUIBtn.start = 0;
		mAppContext.mViewUIBtn.end = mAppContext.routeOverlay.getAllItem().size();
		mAppContext.mViewUIBtn.nodeClick(null);
		mAppContext.mViewUIBtn.btn_pre.setVisibility(View.VISIBLE);
		mAppContext.mViewUIBtn.btn_next.setVisibility(View.VISIBLE);
		mAppContext.mViewUIBtn.btn_delete.setVisibility(View.VISIBLE);
	}
}