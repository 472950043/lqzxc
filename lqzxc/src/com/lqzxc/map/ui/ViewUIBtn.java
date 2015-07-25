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
 * �Զ����ͼUI����ģ��
 * @author QQ472950043
 */
public class ViewUIBtn extends RelativeLayout {
	
	Context mContext;
	AppContext mAppContext;
	
	public Button btn_research;// ������
	public Button btn_delete;// ɾ���ؼ�
	// �Զ������ſؼ�
	public ImageButton btn_zoomin;// �Ŵ�
	public ImageButton btn_zoomout;// ��С
	
	public Button locate_button;// ��λ�ؼ�
	// ����ڵ�ؼ�
	public Button btn_pre;// ��һ���ڵ�
	public Button btn_next;// ��һ���ڵ�

	public int start;// �ڵ���߽�
	public int end;// �ڵ��ұ߽�
	TextView node_title;
	GeoPoint nodePoint;
	String nodeText;
	/**
	 * ���췽����ʼ���Զ����ͼUI���Ʋ��������趨��7��UI���ư�ť
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
				// �ڴ˴���pop����¼���indexΪ�����������,�����������������
			}
		});
        initViewUIBtn();
	}
	
	/**
	 * �趨��ͼ�ϵ�7����ͼUI���ư�ť
	 * @author QQ472950043
	 **/
	public void initViewUIBtn() {
		// TODO Auto-generated method stub
		// ������
		btn_research.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.bikeTextOverlay.removeAll();
				mAppContext.bikeItemizedOverlay.removeAll();
				mAppContext.showLoading(mContext);
				// ������г�վ��
				new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {
						mAppContext.mApiClient.download();
						mAppContext.mBike.addBikeSiteList();
						mAppContext.hideLoading();
							mAppContext.isShowBikeText = true;
							mAppContext.isShowBikeItemized = true;
							System.out.println("Bikeͼ����ӳɹ�");
					    //ִ��ˢ��ʹ��Ч
						mAppContext.mMapView.refresh();
						return null;
					}
					@Override
					protected void onPostExecute(Void result) {
					}
				}.execute();
			}
		});
		// ɾ���ؼ�
		btn_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteAll();
			}
		});

		// ���Ű�ť
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

		// ����ڵ�ؼ�
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
	 * �������ͼ��
	 * @author QQ472950043
	 **/
	public void deleteAll() {
		// TODO Auto-generated method stub
		// ����������ͼ��
		deleteSearchOverlay();
		// ��ԭ����������
		mAppContext.mViewMode.setSearchType(0);
		
		// ���bikeͼ��
		mAppContext.bikeTextOverlay.removeAll();
		mAppContext.bikeItemizedOverlay.removeAll();
//		mAppContext.mMapView.getOverlays().remove(mAppContext.bikeTextOverlay);
//		mAppContext.mMapView.getOverlays().remove(mAppContext.bikeItemizedOverlay);
		mAppContext.isShowBikeText = false;
		mAppContext.isShowBikeItemized = false;
		// �����λͼ��
		mAppContext.mLocationPopup.hidePop();
		mAppContext.mLocationOverlay.setData(null);
	    //ִ��ˢ��ʹ��Ч
		mAppContext.mMapView.refresh();
	}

	/**
	 * ����������ͼ��
	 * @author QQ472950043
	 **/
	public void deleteSearchOverlay() {
		// TODO Auto-generated method stub
		// �������·��ͼ��
		if (mAppContext.isShowTransitOverlay) {
			mAppContext.mMapView.getOverlays().remove(mAppContext.transitOverlay);
			mAppContext.isShowTransitOverlay = false;
		}
		// �ݳ�/����/����·�� ����ͼ��
		if (mAppContext.isShowRouteOverlay) {
			mAppContext.mMapView.getOverlays().remove(mAppContext.routeOverlay);
			mAppContext.isShowRouteOverlay = false;
		}
	}
	
	/**
	 * �ڵ����ʾ��
	 * @param v ��һ������һ��
	 */
	public void nodeClick(View v){
		// TODO Auto-generated method stub
		if (mAppContext.nodeIndex < start || mAppContext.nodeIndex >= end)// �����߽�
			return;
		else if (btn_pre.equals(v) && mAppContext.nodeIndex > start) // ��һ���ڵ�
			mAppContext.nodeIndex--;// ������
		else if (btn_next.equals(v) && mAppContext.nodeIndex < (end - 1)) // ��һ���ڵ�
			mAppContext.nodeIndex++;// ������
		
		if (mAppContext.mViewMode.searchType == 1) {
			// ��������ʹ�õ����ݽṹ��������ͬ����˵�������ڵ����
			if (mAppContext.transitOverlay == null)
				return;
			nodeText = mAppContext.transitOverlay.getItem(mAppContext.nodeIndex).getTitle();
			nodePoint = mAppContext.transitOverlay.getItem(mAppContext.nodeIndex).getPoint();
		} else if (mAppContext.mViewMode.searchType == 2 || mAppContext.mViewMode.searchType == 3 || mAppContext.mViewMode.searchType == 4) {
			// �ݳ�������ʹ�õ����ݽṹ��ͬ���������Ϊ�ݳ����У��ڵ����������ͬ
			if (mAppContext.routeOverlay == null)
				return;
			nodeText = mAppContext.routeOverlay.getItem(mAppContext.nodeIndex).getTitle();
			nodePoint = mAppContext.routeOverlay.getItem(mAppContext.nodeIndex).getPoint();
		}
		// ����/������һ������һ��
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
		// �ƶ���ָ������������
		mAppContext.mMapView.getController().animateTo(nodePoint);
		node_title.setText("" + nodeText);
		mAppContext.nodePopupOverlay.showPopup(node_title, nodePoint, 5);
	}
}
