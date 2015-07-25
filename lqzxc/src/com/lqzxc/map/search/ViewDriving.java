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
 * �ݳ��������ģ��
 * @author QQ472950043
 **/
public class ViewDriving extends RelativeLayout {

	AppContext mAppContext;
	
	LinearLayout headview;
	ViewHead mViewHead;
	
	ExpandableListView mExpandableListView;
	MapExpandableListAdapter transitAdapter;// �����б�������
	
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

		// ����
		mViewHead.title_text.setText("�ݳ�����");
		// ��ʾ�����յ�
		mViewHead.start_point.setText(mAppContext.routeResults.get(0).getStart().name);
		mViewHead.end_point.setText(mAppContext.routeResults.get(0).getEnd().name);
		// ��ʾ����ģ��
		mViewHead.btn_nav.setVisibility(View.VISIBLE);

		// footview
		footview = (LinearLayout) findViewById(R.id.footview);
		mViewFoot = new ViewFoot(mContext);
		footview.addView(mViewFoot);

		// ���ش򳵷���
		mViewFoot.cost.setVisibility(View.GONE);

		// ��ʾ��������ģ��
		mViewFoot.btn.setVisibility(View.VISIBLE);
		mViewFoot.txt_1.setText("����ʱ��");
		mViewFoot.txt_2.setText("��̾���");
		mViewFoot.txt_3.setText("�ܿ�ӵ��");
		mViewFoot.txt_4.setText("���߸���");
		mViewFoot.setImg(mAppContext.select);
		
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