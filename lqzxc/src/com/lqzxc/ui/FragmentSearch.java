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
 * 搜索Fragment
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
    	// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
		View view = inflater.inflate(R.layout.fragment_search, container, false);// 租车点
		mAppContext = (AppContext) getActivity().getApplicationContext();
		if(mAppContext.isWifiConnect())
			mAppContext.data.edit().putBoolean("isShowPhoto", true).commit();
		if(!mAppContext.data.getBoolean("isShowPhoto", false)){
			mDialogBase = new DialogBase(getActivity(), "下载提示","您尚未下载自行车网点图片(共约9MB)\n确认:在非WIFI下载\n取消:下次提醒" ,"确认");
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
