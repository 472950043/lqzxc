package com.lqzxc.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lqzxc.AppContext;
import com.lqzxc.ui.FragmentHistory;
import com.lqzxc.ui.FragmentVisit;
import com.lqzxc.ui.FragmentSearch;
/**
 * Fragment  ≈‰∆˜
 * @author QQ472950043
 **/
public class FragmentAdapter extends FragmentPagerAdapter {

	AppContext mAppContext;
	int mCount = 3;

	public FragmentAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setAppContext(Context mContext) {
		mAppContext = (AppContext) mContext.getApplicationContext();
		mAppContext.fragmentVisit = new FragmentVisit();
		mAppContext.fragmentSearch = new FragmentSearch();
		mAppContext.fragmentHistory = new FragmentHistory();
	}
	@Override
	public Fragment getItem(int position) {
		switch (position) {
		case 0:
			return mAppContext.fragmentVisit;
		case 1:
			return mAppContext.fragmentSearch;
		case 2:
			return mAppContext.fragmentHistory;
		default:
			return mAppContext.fragmentVisit;
		}
	}

	@Override
	public int getCount() {
		return mCount;
	}
}