package com.lqzxc.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.umeng.update.UmengUpdateAgent;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 程序欢迎界面
 * @author QQ472950043
 **/
public class AppStartActivity extends Activity implements AnimationListener {

	AppContext mAppContext;
	TextView textView;
	RelativeLayout welcome;
	Animation alphaAnimation;
	Intent intent;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
//		// StrictMode字段是android4.0.3的特性，如果不加的话网络连接会报错
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectDiskReads().detectDiskWrites().detectNetwork() // or .detectAll() for all detectable problems
//				.penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//				.detectLeakedSqlLiteObjects() // 探测SQLite数据库操作
//				.penaltyLog() // 打印logcat
//				.penaltyDeath().build());
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// 去掉标题栏
		setContentView(R.layout.activity_appstart);// 欢迎界面

		mAppContext = (AppContext) getApplicationContext();
		
	    UmengUpdateAgent.update(this.getApplicationContext());
	    
	    welcome = (RelativeLayout) findViewById(R.id.welcome);// 指定动画的textView的ID
		alphaAnimation = AnimationUtils.loadAnimation(AppStartActivity.this, R.anim.myanim);// 指定动画效果
		alphaAnimation.setFillEnabled(true); // 启动Fill保持
		alphaAnimation.setFillAfter(true); // 设置动画的最后一帧是保持在View上面
		welcome.setAnimation(alphaAnimation);// 为RelativeLayout实现指定动画效果
		alphaAnimation.setAnimationListener(AppStartActivity.this); // 为动画设置监听
		textView = (TextView) findViewById(R.id.welcome_textView);
		
		if (mAppContext.data.getBoolean("app_first", true)){//第一次安装app并打开
			mAppContext.mApiClient.firstDownload();
		}
		else{//第一次打开app
			textView.setText("加载数据...");
			mAppContext.mApiClient.readDB();
			mAppContext.mApiClient.download();
		}
	}
	
	public void onAnimationStart(Animation arg0) {
		// TODO Auto-generated method stub
	}
	
	public void onAnimationRepeat(Animation arg0) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * 动画结束时结束欢迎界面并转到软件的主界面
	 */
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		// 跳转到登陆界面MainActivity
		startActivity(new Intent(AppStartActivity.this, MainActivity.class));
		// 如果不关闭当前的会出现好多个页面
		finish();
	}
	
	/**
	 * 在欢迎界面屏蔽back键
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}