package com.lqzxc.ui;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.adapter.ListAdapter;
import com.lqzxc.map.ui.MapActivity;
import com.lqzxc.modal.BikeStie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 最近访问Fragment
 * @author QQ472950043
 */
public class FragmentVisit extends Fragment {

	AppContext mAppContext;
	TextView none;
	ListView listview;
	ListAdapter mListAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
		View view = inflater.inflate(R.layout.fragment_listview, container, false);
		mAppContext = (AppContext) getActivity().getApplicationContext();
		none = (TextView) view.findViewById(R.id.none);
		listview = (ListView) view.findViewById(R.id.listview);
		mListAdapter = new ListAdapter(getActivity());
		listview.setOnItemClickListener(new OnItemClickListener(){
			// 监听事件
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
				// TODO Auto-generated method stub
				TextView textview = (TextView) view.findViewById(R.id.netName);
				mAppContext.mBikeStie = (BikeStie) textview.getTag();
				mAppContext.mApiClient.visitUpdate(mAppContext.mBikeStie.getMid());
				startActivity(new Intent(getActivity(), MapActivity.class));
				System.out.println("x:" +mAppContext.mBikeStie.getGpsx() + "y:" + mAppContext.mBikeStie.getGpsy());
			}
		});
		updateVisit();
		return view;
	}
	public void updateVisit(){
		// TODO Auto-generated method stub
		if(mAppContext.visit.size()==0){
			none.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}else{
			none.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		listview.setAdapter(mListAdapter);
		mListAdapter.setData(mAppContext.visit);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("FragmentHot onCreate");
	}

	public void onResume() {
		super.onResume();
		System.out.println("FragmentHot onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		System.out.println("FragmentHot onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		System.out.println("FragmentHot onStop");
	}
}
