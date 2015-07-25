package com.lqzxc.map.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.MKCityListInfo;
import com.baidu.mapapi.search.MKPoiInfo;
import com.lqzxc.R;
import com.lqzxc.map.modal.Option;

/**
 * 搜索列表适配器
 */
public class SelectAdapter extends BaseAdapter {
	// TODO Auto-generated method stub
	List<Option> options;
	Context mContext;
	LayoutInflater mInflater;

	public SelectAdapter(Context mContext) {
		this.mContext = mContext;
		this.mInflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(options==null)
			return 0;
		else
			return options.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return options.get(position);
	}

	@Override
	public long getItemId(int index) {
		// TODO Auto-generated method stub
		return index;
	}

	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		view = mInflater.inflate(R.layout.baidu_map_widget_pois_list_item, null);
		// 注册相关控件
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView address = (TextView) view.findViewById(R.id.address);
		name.setText((char) (index % 26 + 65) + " " + options.get(index).getName());
		name.setTag(options.get(index));
		if(options.get(index).getAddress()==null)
			address.setVisibility(View.GONE);
		else
			address.setText(options.get(index).getAddress());
		return view;
	}

	public void setPois(List<MKPoiInfo> pois) {
		// TODO Auto-generated method stub
		options = new ArrayList<Option>();
		for (int i = 0; i < pois.size(); i++) {
			Option option = new Option();
			option.setName(pois.get(i).name);
			option.setePoiType(pois.get(i).ePoiType);
			option.setAddress(setByPoiType(pois.get(i).ePoiType) + pois.get(i).address);
			option.setmGeoPoint(pois.get(i).pt);
			options.add(option);
		}
	}

	public void setCityLists(List<MKCityListInfo> cityLists) {
		// TODO Auto-generated method stub
		options = new ArrayList<Option>();
		for (int i = 0; i < cityLists.size(); i++) {
			Option option = new Option();
			option.setName(cityLists.get(i).city + "(" + cityLists.get(i).num + ")");
			options.add(option);
		}
	}
	
	public String setByPoiType(int ePoiType) {
		// TODO Auto-generated method stub
		switch (ePoiType) {
		case 0:// 0：普通点
			return "地址:";
		case 1:
			return "公交站:";
		case 2:
			return "公交线路:";
		case 3:
			return "地铁站:";
		case 4:
			return "地铁线路:";
		default:
			return "";
		}
	}
}