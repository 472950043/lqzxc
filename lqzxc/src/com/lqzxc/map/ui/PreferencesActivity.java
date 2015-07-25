package com.lqzxc.map.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Preferences配置文件设置Activity
 * @author QQ472950043
 */
public class PreferencesActivity extends PreferenceActivity{

	AppContext mAppContext;
	Preference savescreen;
	Preference measure;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		mAppContext = (AppContext) this.getApplicationContext();
		
		//所的的值将会自动保存到data/data/包名/shared_prefs/路径下/SharePreferences/包名_preferences.xml
		addPreferencesFromResource(R.xml.baidu_map_preferences);
		savescreen = findPreference("preferences_savescreen");
		savescreen.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mAppContext.ToastMessage(PreferencesActivity.this, "正在截取屏幕图片...");
				mAppContext.isSavescreen = true;
				finish();
				return false;
			}
		});
		measure = findPreference("preferences_measure");
		measure.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mAppContext.ToastMessage(PreferencesActivity.this, "单击开始测距，双击结束测距");
				mAppContext.isMeasure = true;
				finish();
				return false;
			}
		});
	}
}
