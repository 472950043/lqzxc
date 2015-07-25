package com.lqzxc.map.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.modal.BikeStie;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
/**
 * �Զ������г����㴰��ģ��
 * @author QQ472950043
 */
public class ViewWindow extends LinearLayout {

	Context mContext;	
	AppContext mAppContext;
	//�����б�
	LinearLayout close;
	ImageView image;
	TextView netName;
	TextView netType;
	TextView netStatus;
	TextView address;
	TextView trafficInfo;
	TextView bicycleCapacity;
	TextView bicycleNum;
	/**
	 * ���췽����ʼ���Զ������г����㴰�ڲ��������趨�ⴰ�ڵ����ݺ͵���¼�
	 * @author QQ472950043
	 */
	public ViewWindow(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		this.mContext=context;
		setWillNotDraw(false);
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(context).inflate(R.layout.baidu_map_widget_window, this, true);
		image = (ImageView) findViewById(R.id.image);
		netName = (TextView) findViewById(R.id.netName);
		close = (LinearLayout) findViewById(R.id.close);
		netType = (TextView) findViewById(R.id.netType);
		netStatus = (TextView) findViewById(R.id.netStatus);
		address = (TextView) findViewById(R.id.address);
		trafficInfo = (TextView) findViewById(R.id.trafficInfo);
		bicycleCapacity = (TextView) findViewById(R.id.bicycleCapacity);
		bicycleNum = (TextView) findViewById(R.id.bicycleNum);
		
		initViewWindow();
    } 

	/**
	 * �趨�ⴰ�ڵ����ݺ͵���¼�
	 * @author QQ472950043
	 */
	public void initViewWindow() {
		// TODO Auto-generated method stub
		BikeStie mBikeStie = mAppContext.mBikeStie;
		String img = "http://www.luqiaobike.com/" + mBikeStie.getImageAttach();
		if(mAppContext.data.getBoolean("isShowPhoto", false)){
			if(mBikeStie.getImageAttach().length()>0){
				mAppContext.mImageLoader.displayImage(img, image);
				System.out.println("img:"+img);
			}
			else
				image.setImageResource(R.drawable.wangdian_img);
		}
			
		netName.setText(mBikeStie.getId() + "." + mBikeStie.getNetName());
		netType.setText("�������ͣ�" + mBikeStie.getNetType());
		if (mBikeStie.getNetStatus().equals("����"))
			netStatus.setText("����״̬��" + mBikeStie.getNetStatus());
		else
			netStatus.setText(Html.fromHtml("<font color=\"#ff0000\">����״̬��"+mBikeStie.getNetStatus()+"</font>"));
		address.setText("��ַ��" + mBikeStie.getAddress());

		trafficInfo.setText("�ܱ߹�����Ϣ��" + mBikeStie.getTrafficInfo());
		bicycleCapacity.setText(Html.fromHtml("�泵������<b>" + mBikeStie.getBicycleCapacity() + "</b> "));
		int borrow = mBikeStie.getBicycleNum();
		if(borrow-5<=0)
			bicycleNum.setText(Html.fromHtml("<font color=\"#ff0000\">��ǰ�泵����<b>"+borrow+"</b></font> "));
		else
			bicycleNum.setText(Html.fromHtml("��ǰ�泵����<b>"+borrow+"</b> "));
		
		close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(mAppContext.isShowBikeItemized)
					mAppContext.mMapController.setMapStatusWithAnimation(mAppContext.bikeMapStatus,500);
				mAppContext.widget_window.setVisibility(View.GONE);
			}
		});
	}
}