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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 历史Fragment
 * @author QQ472950043
 **/
public class FragmentHistory extends Fragment{

	AppContext mAppContext;
	TextView none;
	ListView listview;
	ListAdapter mListAdapter;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	// 第一个参数是这个Fragment将要显示的界面布局,第二个参数是这个Fragment所属的Activity,第三个参数是决定此fragment是否附属于Activity
    	View view=inflater.inflate(R.layout.fragment_listview, container, false);
		mAppContext = (AppContext) getActivity().getApplicationContext();
		none = (TextView) view.findViewById(R.id.none);
    	listview = (ListView) view.findViewById(R.id.listview);
    	mListAdapter = new ListAdapter(getActivity());
		listview.setOnItemClickListener(new OnItemClickListener(){
			// 监听事件
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
				TextView textview = (TextView) view.findViewById(R.id.netName);
				mAppContext.mBikeStie = (BikeStie) textview.getTag();
				mAppContext.mApiClient.visitUpdate(mAppContext.mBikeStie.getMid());
				startActivity(new Intent(getActivity(), MapActivity.class));
				System.out.println("x:" +mAppContext.mBikeStie.getGpsx() + "y:" + mAppContext.mBikeStie.getGpsy());
			}
		});
		updateHistory();
        return view;
    }
    
    /**
     * 更新历史UI
     * @author QQ472950043
     **/
	public void updateHistory(){
		// TODO Auto-generated method stub
		if(mAppContext.history.size()==0){
			none.setVisibility(View.VISIBLE);
			listview.setVisibility(View.GONE);
		}else{
			none.setVisibility(View.GONE);
			listview.setVisibility(View.VISIBLE);
		}
		listview.setAdapter(mListAdapter);
		mListAdapter.setData(mAppContext.history);
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
        System.out.println("FragmentHistory onCreate");
    }
    
	public void onResume(){
        super.onResume();
        System.out.println("FragmentHistory onResume");
    }
    
    @Override
    public void onPause(){
        super.onPause();
        System.out.println("FragmentHistory onPause");
    }
    
    @Override
    public void onStop(){
        super.onStop();
        System.out.println("FragmentHistory onStop");
    }
}
