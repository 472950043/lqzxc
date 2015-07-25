package com.lqzxc.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.widget.DialogBase;
import com.lqzxc.widget.ViewTab;
import com.lqzxc.widget.ViewTitle;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

/**
 * ���������棬����: �Զ���title + �Զ���tab + ViewPager �����л�
 * @author QQ472950043
 **/
public class MainActivity extends FragmentActivity {

	AppContext mAppContext;
	RelativeLayout widget_title;
	public static ViewTitle mViewTitle;
	
	RelativeLayout widget_tab;
	ViewTab mViewTab;

	FragmentAdapter mAdapter;
	ViewPager mPager;
	
	DialogBase mDialogBase;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 1.4�汾:�˴�����������������Ӧ�ó����ȫ��context����ͨ��activity��getApplicationContext������ȡ
		mAppContext = (AppContext) getApplicationContext();
		
		// �����Զ���title
		widget_title = (RelativeLayout) findViewById(R.id.widget_title);
		mViewTitle = new ViewTitle(this, null, "·�Ź������г�", "����");
		widget_title.addView(mViewTitle);
		
		// �����Զ���tab
		widget_tab = (RelativeLayout) findViewById(R.id.widget_tab);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// ��ȡ�ֱ��ʿ��
		mViewTab = new ViewTab(this, screenW);
		widget_tab.addView(mViewTab);

		// �����Զ���tab�ĵ���¼�
		mViewTab.setOnTab0Listener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(0);
			}
		});
		mViewTab.setOnTab1Listener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(1);
			}
		});
		mViewTab.setOnTab2Listener(new OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mPager.setCurrentItem(2);
			}
		});

		// ����ViewPager���һ���Ч��
		mPager = (ViewPager) findViewById(R.id.viewpager);
		mAdapter = new FragmentAdapter(getSupportFragmentManager());
		mAdapter.setAppContext(this);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(mViewTab.mOnPageChangeListener);
		mPager.setCurrentItem(1);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		startActivity(new Intent(MainActivity.this, AboutActivity.class));
		return false;
	}

	public void onResume(){
		// TODO Auto-generated method stub
        super.onResume();
    }

	/**
	 * �˳�ʱ������ݡ�
	 * @author QQ472950043
	 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// ���¼����Ϸ��ذ�ť
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mDialogBase=new DialogBase(this, "��ʾ","��Ҫ�˳���" ,"ȷ��");
	        mDialogBase.setOnConfirmListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
					mDialogBase.dismiss();
				}
			});
	        mDialogBase.show();
		}
		return false;
	}

}
