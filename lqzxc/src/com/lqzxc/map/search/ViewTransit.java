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
 * �����������ģ��
 * @author QQ472950043
 **/
public class ViewTransit extends RelativeLayout {

	Context mContext;
	AppContext mAppContext;
	
	// headview����
	LinearLayout headview;
	ViewHead mViewHead;
	
	// footview����
	LinearLayout footview;
	ViewFoot mViewFoot;
	
	// �����б�
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// �����б�������
	
	public ViewTransit(Context mContext){
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		// ׼������
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		MKPlanNode stNode = mAppContext.transitRouteResults.getStart();// ���
		MKPlanNode enNode = mAppContext.transitRouteResults.getEnd();// �յ�
		int taxiPrice = mAppContext.transitRouteResults.getTaxiPrice();// �򳵷��òο�ֵ
		// ���ز���
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_popupwindow, this, true);
		// headview����
		headview = (LinearLayout) findViewById(R.id.headview);
		mViewHead = new ViewHead(mContext);
		headview.addView(mViewHead);

		// ���ñ���
		mViewHead.title_text.setText("��������");
		// ��ʾ�����յ�
		mViewHead.start_point.setText(stNode.name);
		mViewHead.end_point.setText(enNode.name);
		// ���ص���ģ��
		mViewHead.btn_nav.setVisibility(View.GONE);

		// footview����
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// ��ʾ�򳵷���
		mViewFoot.cost.setVisibility(View.VISIBLE);
		mViewFoot.taxi_cost.setText("�򳵷��òο�ֵ" + taxiPrice + "Ԫ");// �����ռݳ���̾�����㣩

		// ��ʾ��������ģ��
		mViewFoot.btn.setVisibility(View.VISIBLE);
		mViewFoot.txt_1.setText("�Ͽ��");
		mViewFoot.txt_2.setText("�ٻ���");
		mViewFoot.txt_3.setText("�ٲ���");
		mViewFoot.txt_4.setText("��������");
		mViewFoot.setImg(mAppContext.select);
		
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		transitAdapter = new MapExpandableListAdapter(mContext);
		transitAdapter.setData(mAppContext.transitRouteResults);
		mExpandableListView.setGroupIndicator(null);// ���ؼ�Ĭ�ϵ���߼�ͷȥ��
		mExpandableListView.setAdapter(transitAdapter);
		setListener();
	}

	/**
	 * ���÷����б�������
	 */
	public void setListener() {
		// TODO Auto-generated method stub
		mExpandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				// ����·�߹滮չʾ���PopupWindow����
				mAppContext.mPopupWindow.dismiss();
				// ɾ���������������ͼ��
				mAppContext.mViewUIBtn.deleteSearchOverlay();
      		    // ���ù���ͼ���·��
				mAppContext.transitOverlay = new TransitOverlay((Activity) mContext, mAppContext.mMapView);
				mAppContext.transitOverlay.setData(mAppContext.transitRouteResults.getPlan(groupPosition));
			    // ��ӹ���ͼ��
				mAppContext.mMapView.getOverlays().add(mAppContext.transitOverlay);
				mAppContext.isShowTransitOverlay = true;
				// ִ��ˢ��ʹ��Ч
				mAppContext.mMapView.refresh();
      		    // ʹ��zoomToSpan()���ŵ�ͼ��ʹ·������ȫ��ʾ�ڵ�ͼ��
				mAppContext.mMapView.getController().zoomToSpan(mAppContext.transitOverlay.getLatSpanE6(), mAppContext.transitOverlay.getLonSpanE6());
				// �趨·�߽ڵ��������ڵ����ʱʹ��
				mAppContext.nodeIndex = childPosition;
				// �趨·�߽ڵ�ı߽�
				mAppContext.mViewUIBtn.start = 0;
				mAppContext.mViewUIBtn.end = mAppContext.transitOverlay.getAllItem().size();
				// ��ʾ�ڵ������ť
				mAppContext.mViewUIBtn.btn_pre.setVisibility(View.VISIBLE);
				mAppContext.mViewUIBtn.btn_next.setVisibility(View.VISIBLE);
				// ���б���
				mAppContext.mViewUIBtn.nodeClick(null);
				return false;
			}
		});  
	}
	
}