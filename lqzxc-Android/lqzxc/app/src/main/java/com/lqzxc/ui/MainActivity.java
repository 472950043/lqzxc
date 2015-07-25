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
 * 程序主界面，利用: 自定义title + 自定义tab + ViewPager 左右切换
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
		// 1.4版本:此处需新增参数，传入应用程序的全局context，可通过activity的getApplicationContext方法获取
		mAppContext = (AppContext) getApplicationContext();
		
		// 加载自定义title
		widget_title = (RelativeLayout) findViewById(R.id.widget_title);
		mViewTitle = new ViewTitle(this, null, "路桥公共自行车", "关于");
		widget_title.addView(mViewTitle);
		
		// 加载自定义tab
		widget_tab = (RelativeLayout) findViewById(R.id.widget_tab);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 获取分辨率宽度
		mViewTab = new ViewTab(this, screenW);
		widget_tab.addView(mViewTab);

		// 设置自定义tab的点击事件
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

		// 设置ViewPager左右滑动效果
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
	 * 退出时清空数据。
	 * @author QQ472950043
	 **/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 按下键盘上返回按钮
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			mDialogBase=new DialogBase(this, "提示","您要退出吗？" ,"确认");
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
