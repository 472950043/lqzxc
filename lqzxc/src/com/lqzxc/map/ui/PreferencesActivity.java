package com.lqzxc.map.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

/**
 * Preferences�����ļ�����Activity
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
		
		//���ĵ�ֵ�����Զ����浽data/data/����/shared_prefs/·����/SharePreferences/����_preferences.xml
		addPreferencesFromResource(R.xml.baidu_map_preferences);
		savescreen = findPreference("preferences_savescreen");
		savescreen.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mAppContext.ToastMessage(PreferencesActivity.this, "���ڽ�ȡ��ĻͼƬ...");
				mAppContext.isSavescreen = true;
				finish();
				return false;
			}
		});
		measure = findPreference("preferences_measure");
		measure.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				mAppContext.ToastMessage(PreferencesActivity.this, "������ʼ��࣬˫���������");
				mAppContext.isMeasure = true;
				finish();
				return false;
			}
		});
	}
}
