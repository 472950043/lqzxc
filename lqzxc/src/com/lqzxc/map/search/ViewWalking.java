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
 * �����������ģ��
 * @author QQ472950043
 **/
public class ViewWalking extends RelativeLayout {

	AppContext mAppContext;
	
	LinearLayout headview;
	ViewHead mViewHead;
	
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// �����б�������
	
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

		// ����
		mViewHead.title_text.setText("���з���");
		// ��ʾ�����յ�
		mViewHead.start_point.setText(mAppContext.stNode.name);
		mViewHead.end_point.setText(mAppContext.enNode.name);
		// ���ص���ģ��
		mViewHead.btn_nav.setVisibility(View.GONE);

		// footview
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// ���ش򳵷���
		mViewFoot.cost.setVisibility(View.GONE);
		mViewFoot.taxi_cost.setText("�򳵷��òο�ֵ" + taxiPrice + "Ԫ");// �����ռݳ���̾�����㣩

		// ������������ģ��
		mViewFoot.btn.setVisibility(View.GONE);
		
		mExpandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
		transitAdapter = new MapExpandableListAdapter(mContext);
		transitAdapter.setData();
		mExpandableListView.setGroupIndicator(null);// ���ؼ�Ĭ�ϵ���߼�ͷȥ��
		mExpandableListView.setAdapter(transitAdapter);
	}
	
	public void setOnKeyListener(View.OnKeyListener onKeyListener) {
		mExpandableListView.setOnKeyListener(onKeyListener);
	}
}