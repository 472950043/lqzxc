package com.lqzxc.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.lqzxc.R;
import com.lqzxc.modal.BikeStie;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/** 
 * 自定义列表适配器 ListAdapter
 * @author QQ472950043
 **/  
public class ListAdapter extends BaseAdapter {

	TextView netName;
	TextView address;
	TextView bicycleCapacity;
	TextView bicycleNum;
	
	Context mContext;
	LayoutInflater mInflater;
	List<BikeStie> mData ;

	public ListAdapter(Context context) {
		this.mContext=context;
		this.mInflater = LayoutInflater.from(context);
		this.mData = new ArrayList<BikeStie>();
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
		view = mInflater.inflate(R.layout.item_listview, null);
		netName = (TextView) view.findViewById(R.id.netName);
		address = (TextView) view.findViewById(R.id.address);
		bicycleNum = (TextView) view.findViewById(R.id.bicycleNum);
		bicycleCapacity = (TextView) view.findViewById(R.id.bicycleCapacity);
		netName.setTag(mData.get(position));
		try {
			if(mData.get(position).getNetStatus().equals("正常")){
				netName.setText(Html.fromHtml(mData.get(position).getId()+"."+mData.get(position).getNetName()));
			}
			else{
				netName.setText(Html.fromHtml("<font color=\"#ff0000\">"
						+ mData.get(position).getId() + "."
						+ mData.get(position).getNetName() + "("
						+ mData.get(position).getNetStatus()+ ")</font>"));
			}
			address.setText(mData.get(position).getAddress());

			int borrow = mData.get(position).getBicycleNum();
			if(borrow-5<=0)
				bicycleNum.setText(Html.fromHtml("<font color=\"#ff0000\">可借:<b>"+borrow+"</b></font> "));
			else
				bicycleNum.setText(Html.fromHtml("可借:<b>"+borrow+"</b> "));
			int revert=mData.get(position).getBicycleCapacity()-mData.get(position).getBicycleNum();
			if(revert-5<=0)
				bicycleCapacity.setText(Html.fromHtml("<font color=\"#ff0000\">可还:<b>"+revert+"</b></font> "));
			else
				bicycleCapacity.setText(Html.fromHtml("可还:<b>"+revert+"</b> "));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return view;
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//年月日 时分秒 格式
	public String getTime(String time) {
		return df.format(new Date(Long.parseLong(time)));
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