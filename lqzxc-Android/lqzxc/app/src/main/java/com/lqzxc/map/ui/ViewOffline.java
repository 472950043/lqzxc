package com.lqzxc.map.ui;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.MKOLSearchRecord;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.AppContext;
import com.lqzxc.R;
/**
 * 离线地图模块
 * @author QQ472950043
 **/
public class ViewOffline extends LinearLayout {
	
	Context mContext;
	AppContext mAppContext;
	String size;
	TextView title;
	TextView update;
	TextView ratio;
	ProgressBar progressbar_ensable;
	ProgressBar progressbar_disable;
	ImageView enabled;
	ImageView disabled;
	ImageView pause;

	public ViewOffline(Context mContext) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		LayoutInflater.from(mContext).inflate(R.layout.baidu_map_widget_update, this, true);

		title = (TextView) findViewById(R.id.title);
		update = (TextView) findViewById(R.id.update);
		ratio = (TextView) findViewById(R.id.ratio);

		progressbar_ensable = (ProgressBar) findViewById(R.id.progressbar_ensable);
		progressbar_disable = (ProgressBar) findViewById(R.id.progressbar_disable);
		enabled = (ImageView) findViewById(R.id.enabled);
		disabled = (ImageView) findViewById(R.id.disabled);
		pause = (ImageView) findViewById(R.id.pause);
		
		// 初始化mElement在没有获得任何离线地图更新的时候也能手动执行下载
		mAppContext.mElement = new MKOLUpdateElement();
		// 31代表浙江省，获取浙江省的离线地图的城市列表records
		List<MKOLSearchRecord> records = mAppContext.mOffline.getOfflineCityList().get(31).childCities;
		for (int i = 0; i < records.size(); i++)
			if ("台州市".equals(records.get(i).cityName)) {
				// 获取台州市的离线地图record，并手动初始化离线地图更新信息记录
				MKOLSearchRecord record = records.get(i);
				mAppContext.mElement.cityID = record.cityID;
				mAppContext.mElement.cityName = record.cityName;
				mAppContext.mElement.geoPt = new GeoPoint(28662335, 121427180);
				mAppContext.mElement.level = 13;
				mAppContext.mElement.ratio = 0;
				mAppContext.mElement.serversize = record.size;
				mAppContext.mElement.size = 0;
				mAppContext.mElement.status = 3;
				mAppContext.mElement.update = true;
				break;
			}
		initListener();

		updateElement();
		setElement();
	}
	
	/**
	 * 初始化离线地图模块
	 */
    public void initListener() {
		// TODO Auto-generated method stub
		enabled.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 如果是更新，只能先删掉，后下载
				if (mAppContext.mElement.status == 4 && mAppContext.mElement.ratio == 100)
					mAppContext.mOffline.remove(mAppContext.mElement.cityID);
				mAppContext.mOffline.start(mAppContext.mElement.cityID);
		    	updateElement();
				// 处理下载进度更新提示
				setElement();
				System.out.println("mOffline.start:" + mAppContext.mElement.cityID);
				mAppContext.ToastMessage(mContext, "开始下载"+mAppContext.mElement.cityName);
			}
		});
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.mOffline.pause(mAppContext.mElement.cityID);
		    	updateElement();
				// 处理下载进度更新提示
				setElement();
			}
		});
	}
    
    /**
	 * 获取start过的离线地图的信息
	 */
    public void updateElement(){
		// TODO Auto-generated method stub
		try{
	    	mAppContext.mElements = mAppContext.mOffline.getAllUpdateInfo();
	    	// 获取离线地图的信息并存入mElement当中
			for (int i = 0; i < mAppContext.mElements.size(); i++)
				if ("台州市".equals(mAppContext.mElements.get(i).cityName)) {
					// 离线地图更新信息记录结构
					mAppContext.mElement = mAppContext.mElements.get(i);
					break;
				}
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
	public void setElement() {
		this.size = mAppContext.formatDataSize(mAppContext.mElement.size);
		System.out.println("e:" + mAppContext.mElement.cityID + " "
				+ mAppContext.mElement.cityName + " "
				+ mAppContext.mElement.update + " "
				+ mAppContext.mElement.status + " " 
				+ mAppContext.mElement.size + " " 
				+ mAppContext.mElement.ratio);
		title.setText(mAppContext.mElement.cityName);
		// 初始化update
		setUpdate();
		// 初始化ratio
		setRatio();
		// 初始化indicator
		setIndicator();
		// 初始化progressbar
		setProgressbar();
	}
	
	public void setUpdate(){
		// TODO Auto-generated constructor stub
		if (mAppContext.mElement.update) {
			update.setText("可更新");
		} else {
			update.setText("最新");
		}
	}

	//MKOLUpdateElement.UNDEFINED 0 未定义
	//MKOLUpdateElement.DOWNLOADING 1 正在下载
	//MKOLUpdateElement.WAITING 2 等待下载
	//MKOLUpdateElement.SUSPENDED 3 暂停下载
	//MKOLUpdateElement.FINISHED 4 下载完成
	//MKOLUpdateElement.eOLDSMd5Error 5  校验失败
	//MKOLUpdateElement.eOLDSNetError 6  网络异常
	//MKOLUpdateElement.eOLDSIOError 7  读写异常
	//MKOLUpdateElement.eOLDSWifiError 8 wifi网络异常
	//MKOLUpdateElement.eOLDSMissData 9  数据丢失
	String[] error = new String[] { "未下载", "正在下载", "等待下载", "下载完成", "校验失败",
			"网络异常", "读写异常", "wifi网络异常", "数据丢失" };

	public void setRatio() {
		// TODO Auto-generated constructor stub
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100))
			ratio.setText("正在下载 " + size + " " + mAppContext.mElement.ratio + "%");
		else if (mAppContext.mElement.status == 2 || (mAppContext.mElement.status == 3 && mAppContext.mElement.ratio == 0))
			ratio.setText("等待下载 " + size + " " + mAppContext.mElement.ratio + "%");
		else if (mAppContext.mElement.status == 3)
			ratio.setText("暂停下载 " + size + " " + mAppContext.mElement.ratio + "%");
		else if ((mAppContext.mElement.status == 1 || mAppContext.mElement.status == 4) && mAppContext.mElement.ratio == 100) {
			ratio.setText("下载完成 " + size + " " + mAppContext.mElement.ratio + "%");
		}
		else 
			ratio.setText(error[mAppContext.mElement.status]);
	}

	public void setIndicator() {
		// TODO Auto-generated constructor stub
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100) || mAppContext.mElement.status == 2) {// 正在下载 或者 等待下载
			enabled.setVisibility(View.GONE);
			disabled.setVisibility(View.GONE);
			pause.setVisibility(View.VISIBLE);
		} else if (!mAppContext.mElement.update && (mAppContext.mElement.status == 1 || mAppContext.mElement.status == 4) && mAppContext.mElement.ratio == 100) {// 最新 且 下载完成
			enabled.setVisibility(View.GONE);
			disabled.setVisibility(View.VISIBLE);
			pause.setVisibility(View.GONE);
		} else if (mAppContext.mElement.update || mAppContext.mElement.status != 4) {// 可更新 或者 未完成;
			enabled.setVisibility(View.VISIBLE);
			disabled.setVisibility(View.GONE);
			pause.setVisibility(View.GONE);
		}
	}
	
	public void setProgressbar() {
		// TODO Auto-generated constructor stub
		progressbar_ensable.setProgress(mAppContext.mElement.ratio);
		progressbar_disable.setProgress(mAppContext.mElement.ratio);
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100) || mAppContext.mElement.status == 2){// 正在下载 或者 等待下载
			progressbar_ensable.setVisibility(View.VISIBLE);
			progressbar_disable.setVisibility(View.GONE);
		}else{
			progressbar_ensable.setVisibility(View.GONE);
			progressbar_disable.setVisibility(View.VISIBLE);
		}
	}
}