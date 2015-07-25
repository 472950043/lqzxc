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
 * ���ߵ�ͼģ��
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
		
		// ��ʼ��mElement��û�л���κ����ߵ�ͼ���µ�ʱ��Ҳ���ֶ�ִ������
		mAppContext.mElement = new MKOLUpdateElement();
		// 31�����㽭ʡ����ȡ�㽭ʡ�����ߵ�ͼ�ĳ����б�records
		List<MKOLSearchRecord> records = mAppContext.mOffline.getOfflineCityList().get(31).childCities;
		for (int i = 0; i < records.size(); i++)
			if ("̨����".equals(records.get(i).cityName)) {
				// ��ȡ̨���е����ߵ�ͼrecord�����ֶ���ʼ�����ߵ�ͼ������Ϣ��¼
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
	 * ��ʼ�����ߵ�ͼģ��
	 */
    public void initListener() {
		// TODO Auto-generated method stub
		enabled.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// ����Ǹ��£�ֻ����ɾ����������
				if (mAppContext.mElement.status == 4 && mAppContext.mElement.ratio == 100)
					mAppContext.mOffline.remove(mAppContext.mElement.cityID);
				mAppContext.mOffline.start(mAppContext.mElement.cityID);
		    	updateElement();
				// �������ؽ��ȸ�����ʾ
				setElement();
				System.out.println("mOffline.start:" + mAppContext.mElement.cityID);
				mAppContext.ToastMessage(mContext, "��ʼ����"+mAppContext.mElement.cityName);
			}
		});
		pause.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mAppContext.mOffline.pause(mAppContext.mElement.cityID);
		    	updateElement();
				// �������ؽ��ȸ�����ʾ
				setElement();
			}
		});
	}
    
    /**
	 * ��ȡstart�������ߵ�ͼ����Ϣ
	 */
    public void updateElement(){
		// TODO Auto-generated method stub
		try{
	    	mAppContext.mElements = mAppContext.mOffline.getAllUpdateInfo();
	    	// ��ȡ���ߵ�ͼ����Ϣ������mElement����
			for (int i = 0; i < mAppContext.mElements.size(); i++)
				if ("̨����".equals(mAppContext.mElements.get(i).cityName)) {
					// ���ߵ�ͼ������Ϣ��¼�ṹ
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
		// ��ʼ��update
		setUpdate();
		// ��ʼ��ratio
		setRatio();
		// ��ʼ��indicator
		setIndicator();
		// ��ʼ��progressbar
		setProgressbar();
	}
	
	public void setUpdate(){
		// TODO Auto-generated constructor stub
		if (mAppContext.mElement.update) {
			update.setText("�ɸ���");
		} else {
			update.setText("����");
		}
	}

	//MKOLUpdateElement.UNDEFINED 0 δ����
	//MKOLUpdateElement.DOWNLOADING 1 ��������
	//MKOLUpdateElement.WAITING 2 �ȴ�����
	//MKOLUpdateElement.SUSPENDED 3 ��ͣ����
	//MKOLUpdateElement.FINISHED 4 �������
	//MKOLUpdateElement.eOLDSMd5Error 5  У��ʧ��
	//MKOLUpdateElement.eOLDSNetError 6  �����쳣
	//MKOLUpdateElement.eOLDSIOError 7  ��д�쳣
	//MKOLUpdateElement.eOLDSWifiError 8 wifi�����쳣
	//MKOLUpdateElement.eOLDSMissData 9  ���ݶ�ʧ
	String[] error = new String[] { "δ����", "��������", "�ȴ�����", "�������", "У��ʧ��",
			"�����쳣", "��д�쳣", "wifi�����쳣", "���ݶ�ʧ" };

	public void setRatio() {
		// TODO Auto-generated constructor stub
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100))
			ratio.setText("�������� " + size + " " + mAppContext.mElement.ratio + "%");
		else if (mAppContext.mElement.status == 2 || (mAppContext.mElement.status == 3 && mAppContext.mElement.ratio == 0))
			ratio.setText("�ȴ����� " + size + " " + mAppContext.mElement.ratio + "%");
		else if (mAppContext.mElement.status == 3)
			ratio.setText("��ͣ���� " + size + " " + mAppContext.mElement.ratio + "%");
		else if ((mAppContext.mElement.status == 1 || mAppContext.mElement.status == 4) && mAppContext.mElement.ratio == 100) {
			ratio.setText("������� " + size + " " + mAppContext.mElement.ratio + "%");
		}
		else 
			ratio.setText(error[mAppContext.mElement.status]);
	}

	public void setIndicator() {
		// TODO Auto-generated constructor stub
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100) || mAppContext.mElement.status == 2) {// �������� ���� �ȴ�����
			enabled.setVisibility(View.GONE);
			disabled.setVisibility(View.GONE);
			pause.setVisibility(View.VISIBLE);
		} else if (!mAppContext.mElement.update && (mAppContext.mElement.status == 1 || mAppContext.mElement.status == 4) && mAppContext.mElement.ratio == 100) {// ���� �� �������
			enabled.setVisibility(View.GONE);
			disabled.setVisibility(View.VISIBLE);
			pause.setVisibility(View.GONE);
		} else if (mAppContext.mElement.update || mAppContext.mElement.status != 4) {// �ɸ��� ���� δ���;
			enabled.setVisibility(View.VISIBLE);
			disabled.setVisibility(View.GONE);
			pause.setVisibility(View.GONE);
		}
	}
	
	public void setProgressbar() {
		// TODO Auto-generated constructor stub
		progressbar_ensable.setProgress(mAppContext.mElement.ratio);
		progressbar_disable.setProgress(mAppContext.mElement.ratio);
		if ((mAppContext.mElement.status == 1 && mAppContext.mElement.ratio != 100) || mAppContext.mElement.status == 2){// �������� ���� �ȴ�����
			progressbar_ensable.setVisibility(View.VISIBLE);
			progressbar_disable.setVisibility(View.GONE);
		}else{
			progressbar_ensable.setVisibility(View.GONE);
			progressbar_disable.setVisibility(View.VISIBLE);
		}
	}
}