package com.lqzxc.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.widget.DialogBase;
import com.lqzxc.widget.RefreshScrollView;
import com.lqzxc.widget.RefreshScrollView.OnRefreshListener;
import com.lqzxc.widget.ViewSearch;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
/**
 * ����Fragment
 * @author QQ472950043
 **/
public class FragmentSearch extends Fragment{

	AppContext mAppContext;
	DialogBase mDialogBase;
	public ViewSearch mViewSearch;
	public RefreshScrollView scrollview;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        System.out.println("FragmentSearch onCreate");	
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// ��һ�����������Fragment��Ҫ��ʾ�Ľ��沼��,�ڶ������������Fragment������Activity,�����������Ǿ�����fragment�Ƿ�����Activity
		View view = inflater.inflate(R.layout.fragment_search, container, false);// �⳵��
		mAppContext = (AppContext) getActivity().getApplicationContext();
		if(mAppContext.isWifiConnect())
			mAppContext.data.edit().putBoolean("isShowPhoto", true).commit();
		if(!mAppContext.data.getBoolean("isShowPhoto", false)){
			mDialogBase = new DialogBase(getActivity(), "������ʾ","����δ�������г�����ͼƬ(��Լ9MB)\nȷ��:�ڷ�WIFI����\nȡ��:�´�����" ,"ȷ��");
	        mDialogBase.setOnConfirmListener(new View.OnClickListener() {
				public void onClick(View v) {
					// TODO Auto-generated method stub
					mAppContext.data.edit().putBoolean("isShowPhoto", true).commit();
					mAppContext.mApiClient.download();
					mDialogBase.dismiss();
				}
			});
	        mDialogBase.show();
		}
		scrollview = (RefreshScrollView) view.findViewById(R.id.scrollview);
		mViewSearch = new ViewSearch(getActivity());
		scrollview.addChild(mViewSearch, 1);
		scrollview.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(mAppContext.isWifiConnect())
					mAppContext.data.edit().putBoolean("isShowPhoto", true).commit();
				mAppContext.mApiClient.download();
			}
		});
		String updateTime = mAppContext.data.getString("updateTime", null);
		if (null != updateTime) {
			scrollview.onRefreshComplete(updateTime);
		}
		return view;
    }
    
    public void onResume(){
		// TODO Auto-generated method stub
        super.onResume();
        System.out.println("FragmentSearch onResume");
        mViewSearch.site.clearFocus();
    	InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mViewSearch.site.getWindowToken(), 0);
    }
    
    @Override
    public void onPause(){
		// TODO Auto-generated method stub
        super.onPause();
        System.out.println("FragmentSearch onPause");
    }
    
    @Override
    public void onStop(){
		// TODO Auto-generated method stub
        super.onStop();
        System.out.println("FragmentSearch onStop");
    }
    
}
