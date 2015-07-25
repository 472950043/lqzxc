package com.lqzxc.adapter;

import java.util.ArrayList;
import java.util.List;

import com.lqzxc.R;
import com.lqzxc.AppContext;
import com.lqzxc.modal.BikeStie;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * 自定义搜索适配器SearchAdapter
 * @author QQ472950043
 **/  
public class SearchAdapter extends BaseAdapter {

	ImageView image;
	TextView netName;
	TextView updatetime;
	TextView netType;
	TextView netStatus;
	TextView address;
	TextView trafficInfo;
	TextView bicycleCapacity;
	TextView bicycleNum;
	
	Context mContext;
	LayoutInflater mInflater;
	List<BikeStie> mData;
	AppContext mAppContext;

	public SearchAdapter(Context mContext) {
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
		this.mData = new ArrayList<BikeStie>();
		mAppContext = (AppContext) mContext.getApplicationContext();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mData.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = mInflater.inflate(R.layout.item_search, null);
		image = (ImageView) view.findViewById(R.id.image);
		netName = (TextView) view.findViewById(R.id.netName);
		updatetime = (TextView) view.findViewById(R.id.updatetime);
		netType = (TextView) view.findViewById(R.id.netType);
		netStatus = (TextView) view.findViewById(R.id.netStatus);
		address = (TextView) view.findViewById(R.id.address);
		trafficInfo = (TextView) view.findViewById(R.id.trafficInfo);
		bicycleCapacity = (TextView) view.findViewById(R.id.bicycleCapacity);
		bicycleNum = (TextView) view.findViewById(R.id.bicycleNum);
		netName.setTag(mData.get(position));
		try {
			String img="http://www.luqiaobike.com/"+mData.get(position).getImageAttach();
			if(mAppContext.data.getBoolean("isShowPhoto", false)){
				if(mData.get(position).getImageAttach().length()>0){
					mAppContext.mImageLoader.displayImage(img, image);
					System.out.println("img:"+img);
				}
				else
					image.setImageResource(R.drawable.wangdian_img);
			}
			netName.setText(mData.get(position).getId()+"."+mData.get(position).getNetName());
			updatetime.setText("");
			netType.setText("网点类型："+mData.get(position).getNetType());
			if(mData.get(position).getNetStatus().equals("正常"))
				netStatus.setText("网点状态："+mData.get(position).getNetStatus());
			else
				netStatus.setText(Html.fromHtml("<font color=\"#ff0000\">网点状态："+mData.get(position).getNetStatus()+"</font>"));
			address.setText("地址："+mData.get(position).getAddress());
			
			trafficInfo.setText("周边公交信息："+mData.get(position).getTrafficInfo());
			bicycleCapacity.setText(Html.fromHtml("存车容量：<b>"+mData.get(position).getBicycleCapacity()+"</b> "));
			int borrow = mData.get(position).getBicycleNum();
			if(borrow-5<=0)
				bicycleNum.setText(Html.fromHtml("<font color=\"#ff0000\">当前存车数：<b>"+borrow+"</b></font> "));
			else
				bicycleNum.setText(Html.fromHtml("当前存车数：<b>"+borrow+"</b> "));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	public List<BikeStie> getData() {
		return mData;
	}
	
	public void setData(List<BikeStie> mData) {
		this.mData = mData;
		notifyDataSetChanged();
	}

	public void addData(List<BikeStie> mData) {
		this.mData.addAll(mData);
		notifyDataSetChanged();
	}
}