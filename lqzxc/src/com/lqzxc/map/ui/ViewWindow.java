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
 * 自定义自行车网点窗口模块
 * @author QQ472950043
 */
public class ViewWindow extends LinearLayout {

	Context mContext;	
	AppContext mAppContext;
	//数据列表
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
	 * 构造方法初始化自定义自行车网点窗口部件，并设定这窗口的内容和点击事件
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
	 * 设定这窗口的内容和点击事件
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
		netType.setText("网点类型：" + mBikeStie.getNetType());
		if (mBikeStie.getNetStatus().equals("正常"))
			netStatus.setText("网点状态：" + mBikeStie.getNetStatus());
		else
			netStatus.setText(Html.fromHtml("<font color=\"#ff0000\">网点状态："+mBikeStie.getNetStatus()+"</font>"));
		address.setText("地址：" + mBikeStie.getAddress());

		trafficInfo.setText("周边公交信息：" + mBikeStie.getTrafficInfo());
		bicycleCapacity.setText(Html.fromHtml("存车容量：<b>" + mBikeStie.getBicycleCapacity() + "</b> "));
		int borrow = mBikeStie.getBicycleNum();
		if(borrow-5<=0)
			bicycleNum.setText(Html.fromHtml("<font color=\"#ff0000\">当前存车数：<b>"+borrow+"</b></font> "));
		else
			bicycleNum.setText(Html.fromHtml("当前存车数：<b>"+borrow+"</b> "));
		
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