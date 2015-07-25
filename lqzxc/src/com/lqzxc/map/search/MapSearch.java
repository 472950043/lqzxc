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
 * �Զ�������ģ��
 * @author QQ472950043
 **/
public class MapSearch{
	
	Context mContext;
	AppContext mAppContext;

	ViewTransit view_transit;// �����б�
	ViewDriving view_driving;// �ݳ��б�
	ViewWalking view_walking;// �����б�

	DialogSelect mDialogSelect;//���յ�ѡ��
	/**
	 * ���췽����ʼ���Զ���������
	 * @author QQ472950043
	 **/
	public MapSearch(Context mContext){
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		mAppContext.mSearch = new MKSearch();

		// �趨���յ����
		mAppContext.city = "̨����";
		mAppContext.stCity = "̨����";
		mAppContext.enCity = "̨����";
		// �趨���
		mAppContext.stNode = new MKPlanNode();
		mAppContext.stNode.pt = new GeoPoint(28662335, 121427180);
		mAppContext.stNode.name = "̨����";
		// �趨�յ�
		mAppContext.enNode = new MKPlanNode();
		mAppContext.enNode.pt = mAppContext.mBikeStie.getmGeoPoint();
		mAppContext.enNode.name = mAppContext.mBikeStie.getNetName();
		
		initMapSearch();
	}
	
	/**
	 * ��ʼ������ģ�飬ע���¼�����
	 * 
	 * @param MKEVENT_BUS_DETAIL 15 �������������¼�
	 * @param MKEVENT_MAP_MOVE_FINISH 14 ��ͼ�ƶ�����¼�
	 * @param MKEVENT_POIDETAILSHAREURL 18 poi����̴�
	 * @param MKEVENT_POIRGCSHAREURL 17 ��������빲���url�¼�
	 * @param MKEVENT_SUGGESTION 16 ����������¼�
	 * 
	 * @param ERROR_NETWORK_CONNECT 2 �������Ӵ���
	 * @param ERROR_NETWORK_DATA 3 �������ݴ���
	 * @param ERROR_PERMISSION_DENIED 300 ��Ȩ��֤ʧ��
	 * @param ERROR_RESULT_NOT_FOUND 100 δ�ҵ��������
	 * @param ERROR_ROUTE_ADDR 4 ·�����������յ�������
	 */
	public void initMapSearch() {
		// TODO Auto-generatedEethod stub
		mAppContext.mSearch.init(mAppContext.mBMapManager, new MKSearchListener() {
			// ��ַ��Ϣ�������
			public void onGetAddrResult(MKAddrInfo res, int error) {
				// TODO Auto-generated method stub
				try{
					if (res.type == MKAddrInfo.MK_REVERSEGEOCODE) {
						// �趨���
						mAppContext.stNode = new MKPlanNode();
						mAppContext.stNode.pt = mAppContext.locatCenter;
						mAppContext.stNode.name = "�ҵ�λ��";
						// ��������
						mAppContext.location_title.setText("�ҵ�λ�ã���ȷ��"+(int)mAppContext.mData.accuracy+"�ף�");
						mAppContext.location_content.setText(res.strAddr);
						if(mAppContext.mLocationPopup!=null)
							mAppContext.mLocationOverlay.showPopup(mAppContext.location_view,mAppContext.locatCenter,5);
					}
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// ������������Ϣ�������
			public void onGetTransitRouteResult(MKTransitRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				// �����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR) {// error == 4
					// �������е�ַ��ѡ�����յ��ַ
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
				// ��ʾ·�����
				mAppContext.transitRouteResults = res;
				showTransitRouteResult();
			}

			// �ݳ��������
			public void onGetDrivingRouteResult(MKDrivingRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					// �������е�ַ��ѡ�����յ��ַ
					openSelectDialog(
							res.getAddrResult().mStartPoiList,
							res.getAddrResult().mEndPoiList,
							res.getAddrResult().mStartCityList,
							res.getAddrResult().mEndCityList);
					return;
				}
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					mAppContext.ToastMessage(mContext, getError(error));
					return;
				}
				// ��ʾ·�����
				mAppContext.routeResults = new ArrayList<RouteResult>();
				for (int i = 0; i < res.getNumPlan(); i++) {
					for (int j = 0; j < res.getPlan(i).getNumRoutes(); j++) {
						RouteResult routeResult = new RouteResult();
						routeResult.setStart(res.getStart());
						routeResult.setEnd(res.getEnd());
						routeResult.setTaxiPrice(res.getTaxiPrice());// �����ʵû��ʵ�����壬�������Ǽݳ��˵ġ���
						routeResult.setNumPlan(res.getNumPlan());
						routeResult.setRoute(res.getPlan(i).getRoute(j));
						routeResult.setTime(res.getPlan(i).getTime());
						routeResult.setDistance(res.getPlan(i).getDistance());
						mAppContext.routeResults.add(routeResult);
					}
				}
				showDrivingRouteResult();
			}

			// ���н������
			public void onGetWalkingRouteResult(MKWalkingRouteResult res, int error) {
				// TODO Auto-generated method stub
				mAppContext.hideLoading();
				//�����յ������壬��Ҫѡ�����ĳ����б���ַ�б�
				if (error == MKEvent.ERROR_ROUTE_ADDR){
					// �������е�ַ��ѡ�����յ��ַ
					openSelectDialog(
							res.getAddrResult().mStartPoiList,
							res.getAddrResult().mEndPoiList,
							res.getAddrResult().mStartCityList,
							res.getAddrResult().mEndCityList);
					return;
				}
				// ����ſɲο�MKEvent�еĶ���
				if (error != 0 || res == null) {
					mAppContext.ToastMessage(mContext, getError(error));
					return;
				}
				// ��ʾ·�����
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

			// ����·�߽������
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
			 * ����ѡ��Ի���
			 */
			public void openSelectDialog(List<MKPoiInfo> mStartPoiList, List<MKPoiInfo> mEndPoiList, List<MKCityListInfo> mStartCityList, List<MKCityListInfo> mEndCityList) {
				// TODO Auto-generated method stub
				mAppContext.stPois = mStartPoiList;
				mAppContext.enPois = mEndPoiList;
				mAppContext.stCities = mStartCityList;
				mAppContext.enCities = mEndCityList;
				mDialogSelect = new DialogSelect(mContext);
				if (mStartPoiList == null && mEndPoiList == null && mStartCityList == null && mEndCityList == null)
					mAppContext.ToastMessage(mContext, "��Ǹ��"+mAppContext.city+"δ�ҵ����");
				else{
					mDialogSelect.setPoiAddr();
					mDialogSelect.show();
				}
			}
			
			public String getError(int error){
				switch (error) {
				case 2:
					return "�������Ӵ���";
				case 3:
					return "�������ݴ���";
				case 300:
					return "��Ȩ��֤ʧ��";
				case 100:
					return "��Ǹ��δ�ҵ����";
				default:
					return "��Ǹ��δ�ҵ����";
				}
			}
		});
	}
	
	/**
	 * ������������Ϣ�������
	 */
	public void showTransitRouteResult() {
		// TODO Auto-generated method stub
		view_transit = new ViewTransit(mContext);
		// �������ذ�ť�¼�����Ϊ��ʱ������popupwindow�ϣ���������������ذ�ťû��Ч��
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
	 * �ݳ�������Ϣ�������
	 */
	public void showDrivingRouteResult() {
		view_driving = new ViewDriving(mContext);
		view_driving.mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				mAppContext.mPopupWindow.dismiss();
				
				showRouteOverlay(groupPosition);
				// ����·�߽ڵ��������ڵ����ʱʹ��
				mAppContext.nodeIndex = childPosition;
				return false;
			}
		});  
		// �������ذ�ť�¼�����Ϊ��ʱ������popupwindow�ϣ���������������ذ�ťû��Ч��
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
	 * ����������Ϣ�������
	 */
	public void showWalkingRouteResult() {
		view_walking = new ViewWalking(mContext);
		view_walking.mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				mAppContext.mPopupWindow.dismiss();
				
				showRouteOverlay(groupPosition);
				// ����·�߽ڵ��������ڵ����ʱʹ��
				mAppContext.nodeIndex = childPosition;
				return false;
			}
		});  
		// �������ذ�ť�¼�����Ϊ��ʱ������popupwindow�ϣ���������������ذ�ťû��Ч��
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
	    // ��� �ݳ�/����/����·�� ·��ͼ��
		mAppContext.routeOverlay = new RouteOverlay((Activity) mContext, mAppContext.mMapView);
		// �˴���չʾһ��������Ϊʾ��
//		mAppContext.route = ;
		mAppContext.routeOverlay.setData(mAppContext.routeResults.get(groupPosition).getRoute());
		mAppContext.mMapView.getOverlays().add(mAppContext.routeOverlay);
		mAppContext.isShowRouteOverlay = true;
		// ִ��ˢ��ʹ��Ч
		mAppContext.mMapView.refresh();
		// ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
		mAppContext.mMapView.getController().zoomToSpan(mAppContext.routeOverlay.getLatSpanE6(),mAppContext.routeOverlay.getLonSpanE6());

		mAppContext.mViewUIBtn.start = 0;
		mAppContext.mViewUIBtn.end = mAppContext.routeOverlay.getAllItem().size();
		mAppContext.mViewUIBtn.nodeClick(null);
		mAppContext.mViewUIBtn.btn_pre.setVisibility(View.VISIBLE);
		mAppContext.mViewUIBtn.btn_next.setVisibility(View.VISIBLE);
		mAppContext.mViewUIBtn.btn_delete.setVisibility(View.VISIBLE);
	}
}