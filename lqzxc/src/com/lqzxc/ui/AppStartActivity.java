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
 * ����ӭ����
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
//		// StrictMode�ֶ���android4.0.3�����ԣ�������ӵĻ��������ӻᱨ��
//		StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
//				.detectDiskReads().detectDiskWrites().detectNetwork() // or .detectAll() for all detectable problems
//				.penaltyLog().build());
//		StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
//				.detectLeakedSqlLiteObjects() // ̽��SQLite���ݿ����
//				.penaltyLog() // ��ӡlogcat
//				.penaltyDeath().build());
		super.onCreate(savedInstanceState);

		this.requestWindowFeature(Window.FEATURE_NO_TITLE);// ȥ��������
		setContentView(R.layout.activity_appstart);// ��ӭ����

		mAppContext = (AppContext) getApplicationContext();
		
	    UmengUpdateAgent.update(this.getApplicationContext());
	    
	    welcome = (RelativeLayout) findViewById(R.id.welcome);// ָ��������textView��ID
		alphaAnimation = AnimationUtils.loadAnimation(AppStartActivity.this, R.anim.myanim);// ָ������Ч��
		alphaAnimation.setFillEnabled(true); // ����Fill����
		alphaAnimation.setFillAfter(true); // ���ö��������һ֡�Ǳ�����View����
		welcome.setAnimation(alphaAnimation);// ΪRelativeLayoutʵ��ָ������Ч��
		alphaAnimation.setAnimationListener(AppStartActivity.this); // Ϊ�������ü���
		textView = (TextView) findViewById(R.id.welcome_textView);
		
		if (mAppContext.data.getBoolean("app_first", true)){//��һ�ΰ�װapp����
			mAppContext.mApiClient.firstDownload();
		}
		else{//��һ�δ�app
			textView.setText("��������...");
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
	 * ��������ʱ������ӭ���沢ת�������������
	 */
	public void onAnimationEnd(Animation arg0) {
		// TODO Auto-generated method stub
		// ��ת����½����MainActivity
		startActivity(new Intent(AppStartActivity.this, MainActivity.class));
		// ������رյ�ǰ�Ļ���ֺö��ҳ��
		finish();
	}
	
	/**
	 * �ڻ�ӭ��������back��
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