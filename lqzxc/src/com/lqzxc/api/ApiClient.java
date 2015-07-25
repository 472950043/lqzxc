package com.lqzxc.api;

import java.util.Collections;
import java.util.Comparator;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.lqzxc.AppContext;
import com.lqzxc.db.LqzxcDB;
import com.lqzxc.modal.BikeStie;

/**
 * 联网基类
 * @author QQ472950043
 */
public class ApiClient {

	AppContext mAppContext;
	Context mContext;
	
	LqzxcDB db;
	Cursor cursor;
   	
	/**
	 * 联网基类
	 * @author QQ472950043
	 */
	public ApiClient(Context mContext) {
		this.mContext = mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
	}
	
	public void get(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		AsyncHttpClient oneClient = new AsyncHttpClient();
		oneClient.setTimeout(AppContext.TIMEOUT_CONNECTION);
		oneClient.get(mContext, url, params, handler);
	}
	
	public void post(String url, RequestParams params, AsyncHttpResponseHandler handler) {
		AsyncHttpClient oneClient = new AsyncHttpClient();
		oneClient.setTimeout(AppContext.TIMEOUT_CONNECTION);
		oneClient.post(mContext, url, params, handler);
	}
	
	/**
	 * 第一次联网下载数据
	 * @param mContext
	 */
	public void firstDownload() {
		// TODO Auto-generated method stub
		get("http://www.luqiaobike.com/branch/bmap", null, new AsyncHttpResponseHandler() {
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				mAppContext.ToastMessage(mContext,"网络不给力，初始化失败，请下拉重试！");
				try {
					mAppContext.fragmentSearch.mViewSearch.updateSearchs();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				mAppContext.data.edit().putBoolean("isDownload", false).commit();
				super.onFinish();
			}

			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				mAppContext.data.edit().putBoolean("isDownload", true).commit();
				super.onStart();
			}
			
			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				System.out.println("Apiclient:firstDownlaod下载成功");
				try{
					parseData(arg1);
					sort();
					createDB();
					// 储存更新时间
					String updateTime = "" + System.currentTimeMillis();
					int updateCount = mAppContext.data.getInt("updateCount", 0);
					mAppContext.data.edit().putString("updateTime", updateTime).commit();
					mAppContext.data.edit().putInt("updateCount", updateCount + 1).commit();
					// 更新中间页的UI
					mAppContext.fragmentSearch.scrollview.onRefreshComplete(updateTime);
					mAppContext.fragmentSearch.mViewSearch.updateSearchs();
					System.out.println("Apiclient:UI更新成功");
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.onSuccess(arg0, arg1);
			}
		});
	}

	/**
	 * 联网下载数据
	 * @param mContext
	 */
	public void download(){
		// TODO Auto-generated method stub
		get("http://www.luqiaobike.com/branch/bmap", null, new AsyncHttpResponseHandler() {
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				super.onStart();
			}
			
			@Override
			public void onFailure(Throwable arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1);
				mAppContext.ToastMessage(mContext,"网络不给力");
				try{
					mAppContext.fragmentSearch.scrollview.onRefreshComplete();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			@Override
			public void onSuccess(int arg0, String arg1) {
				// TODO Auto-generated method stub
				super.onSuccess(arg0, arg1);
				System.out.println("Apiclient:download下载成功");
				try{
					parseData(arg1);
					sort();
					updateDB();
					// 储存更新时间
					String updateTime = "" + System.currentTimeMillis();
					int updateCount = mAppContext.data.getInt("updateCount", 0);
					int updateFlow = mAppContext.data.getInt("updateFlow", 0);
					mAppContext.data.edit().putString("updateTime", updateTime).commit();
					mAppContext.data.edit().putInt("updateCount", updateCount + 1).commit();
					if(!mAppContext.isWifiConnect())
						mAppContext.data.edit().putInt("updateFlow", updateFlow + arg1.length()).commit();
					// 更新三页的UI
					mAppContext.fragmentSearch.scrollview.onRefreshComplete(updateTime);
					mAppContext.fragmentSearch.mViewSearch.updateSearchs();
					mAppContext.fragmentSearch.mViewSearch.updateSearch();
					mAppContext.fragmentVisit.updateVisit();
					mAppContext.fragmentHistory.updateHistory();
					System.out.println("Apiclient:UI更新成功");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				super.onFinish();
			}
		});
	}

	/**
	 * 解析联网数据并储存到全局变量download里面
	 * @param data
	 */
	public void parseData(String data) {
		// TODO Auto-generated method stub
		try{
			data = data.substring(data.indexOf("points[")+7,data.indexOf("nowRunlClass"));
			data = data.replaceAll("\r|\n|\t", "");
			String[] s=data.split("points");
			for(int i=0;i<s.length-1;i++){
				if(mAppContext.download.size()<i+1){
					mAppContext.download.add(new BikeStie());
					mAppContext.download.get(i).setTime(""+System.currentTimeMillis());
					mAppContext.download.get(i).setVisited(0);
				}
//				mAppContext.download.get(i).setId(Integer.parseInt(s[i].substring(s[i].indexOf("NO.")+3, s[i].indexOf("名称")).trim()));
				mAppContext.download.get(i).setId(i+1);
				mAppContext.download.get(i).setMid(substring(s[i], "id : \"", "\",gpsx"));
				mAppContext.download.get(i).setGpsx(substring(s[i], "gpsx : parseFloat(\"", "\"),gpsy"));
				mAppContext.download.get(i).setGpsy(substring(s[i], "gpsy : parseFloat(\"", "\")  ,netName"));
				mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint(mAppContext.download.get(i).getGpsy(),mAppContext.download.get(i).getGpsx()));
				mAppContext.download.get(i).setNetName(substring(s[i], "netName : \"", "\"  ,netType"));
				mAppContext.download.get(i).setNetType(substring(s[i], "netType : \"", "\"  ,netStatus"));
				mAppContext.download.get(i).setNetStatus(substring(s[i], "netStatus : \"", "\"  ,openDate"));
				mAppContext.download.get(i).setNetLevel(substring(s[i], "netLevel : \"", "\"  ,address"));
				mAppContext.download.get(i).setAddress(substring(s[i], "address : \"", "\"  ,trafficInfo"));
				mAppContext.download.get(i).setTrafficInfo(substring(s[i], "trafficInfo : \"", "\"  ,bicycleCapacity"));
				mAppContext.download.get(i).setBicycleCapacity(Integer.parseInt(substring(s[i], "bicycleCapacity:\"", "\"  ,bicycleNum")));
				mAppContext.download.get(i).setBicycleNum(Integer.parseInt(substring(s[i], "bicycleNum:\"", "\"  ,imageAttach")));
				if(s[i].indexOf(".jpg'")<0)
					mAppContext.download.get(i).setImageAttach("");
				else
					mAppContext.download.get(i).setImageAttach(s[i].substring(s[i].indexOf("imageAttach : '")+15, s[i].indexOf(".jpg'};"))+".jpg");
//				correct(i);
			}
			System.out.println("Apiclient:解析成功");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Apiclient:解析失败");
		}
	}

	public String substring(String data, String start, String end) {
		try {
			return data.substring(data.indexOf(start) + start.length() ,data.indexOf(end));
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	/**
	 * 对下载的download数据进行筛选，储存到全局变量visit，mBikeStie，history并进行相应的排序
	 */
	public void sort() {
		// TODO Auto-generated method stub
		int totalCapacity = 0;
		int totalNum = 0;
		for (int i = 0; i < mAppContext.download.size(); i++) {
			totalCapacity += mAppContext.download.get(i).getBicycleCapacity();
			totalNum += mAppContext.download.get(i).getBicycleNum();
		}
		mAppContext.totalCapacity = totalCapacity;
		mAppContext.totalNum = totalNum;
		
		mAppContext.visit.clear();
		for (int i = 0; i < mAppContext.download.size(); i++) {
			if (mAppContext.download.get(i).getVisited() > 0)
				mAppContext.visit.add(mAppContext.download.get(i));
		}
		// 按访问数从大到小
		Collections.sort(mAppContext.visit, new Comparator<BikeStie>() {
			@Override
			public int compare(BikeStie o1, BikeStie o2) {
				// TODO Auto-generated method stub
				if (o1.getVisited() < o2.getVisited())
					return 1;
				return -1;
			}
		});

		mAppContext.mBikeSties.clear();
		mAppContext.mBikeSties.addAll(mAppContext.download);
		// 按id从小到大
		Collections.sort(mAppContext.mBikeSties, new Comparator<BikeStie>() {
			@Override
			public int compare(BikeStie o1, BikeStie o2) {
				// TODO Auto-generated method stub
				if (o1.getId() < o2.getId())
					return -1;
				return 1;
			}
		});
		
		mAppContext.history.clear();
		for (int i = 0; i < mAppContext.download.size(); i++) {
			if (mAppContext.download.get(i).getVisited() > 0)
				mAppContext.history.add(mAppContext.download.get(i));
		}
		// 按访问时间从大到小
		Collections.sort(mAppContext.history, new Comparator<BikeStie>() {
			@Override
			public int compare(BikeStie o1, BikeStie o2) {
				// TODO Auto-generated method stub
				if (Long.parseLong(o1.getTime()) < Long.parseLong(o2.getTime()))
					return 1;
				return -1;
			}
		});
		System.out.println("Apiclient:数据排序成功");
	}

	/**
	 * 更新最常访问和历史记录
	 */
	public void visitUpdate(String mid){
		// TODO Auto-generated method stub
		int updateVisit = mAppContext.data.getInt("updateVisit", 0);
		mAppContext.data.edit().putInt("updateVisit", updateVisit).commit();
		for(int i=0;i<mAppContext.download.size();i++){
			if(mAppContext.download.get(i).getMid().equals(mid)){
				mAppContext.download.get(i).setVisited(mAppContext.download.get(i).getVisited()+1);
				mAppContext.download.get(i).setTime(""+System.currentTimeMillis());
				break;
			}
		}
		updateVisited();
	}

	/**
	 * 用异步方法将更新的download数据，更新数据库里面，并更新相应的UI
	 * @param mContext
	 */
	public void updateVisited(){
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try{
					db=new LqzxcDB(mContext);
					for(int i=0;i<mAppContext.download.size();i++)
						db.updateVisited(mAppContext.download.get(i));
					if(db!= null){  
						db.close();
				    }
				} catch (Exception e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
				
			}
		}.execute();
		sort();

		// 更新两边页的UI
		mAppContext.fragmentVisit.updateVisit();
		mAppContext.fragmentHistory.updateHistory();
		System.out.println("Apiclient:UI更新成功");
	}

	/**
	 * 用异步方法将下载的download数据，储存到新创建的数据库里面
	 * @param mContext
	 */
	public void createDB() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try{
					db=new LqzxcDB(mContext);
					for(int i=0;i<mAppContext.download.size();i++)
						db.addBike(mAppContext.download.get(i));
					if(db!= null){  
						db.close();
				    }
					if(mAppContext.download.size()>0)
						mAppContext.data.edit().putBoolean("app_first", false).commit();
					System.out.println("Apiclient:数据库创建成功");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Apiclient:数据库创建失败");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
			}
		}.execute();
	}

	//数据库里面按照download存
	public void updateDB() {
		// TODO Auto-generated method stub
		new AsyncTask<Void, Void, Void>() {
			protected Void doInBackground(Void... params) {
				try{
					db=new LqzxcDB(mContext);
					for(int i=0;i<mAppContext.download.size();i++)
						db.updateBike(mAppContext.download.get(i));
					if(db!= null){  
						db.close();
				    }
					System.out.println("Apiclient:数据库更新成功");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("Apiclient:数据库更新成功");
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void result) {
			}
		}.execute();
	}

	/**
	 * 从数据库里面读取数据并储存到全局变量download里面
	 * @param mContext
	 */
	public void readDB() {
		// TODO Auto-generated method stub
		try {
			db=new LqzxcDB(mContext);
			cursor=db.selectBike();
			mAppContext.download.clear();
			while(cursor.moveToNext()){
				BikeStie modal=new BikeStie();
				String s=cursor.getString(cursor.getColumnIndex("imageAttach"));
				modal.setId(Integer.parseInt(s.substring(s.indexOf("NO.")+3, s.indexOf("名称")).trim()));
				modal.setMid(cursor.getString(cursor.getColumnIndex("mid")));
				modal.setGpsx(cursor.getString(cursor.getColumnIndex("gpsx")));
				modal.setGpsy(cursor.getString(cursor.getColumnIndex("gpsy")));
				modal.setmGeoPoint(stringToGeoPoint(modal.getGpsy(), modal.getGpsx()));
				modal.setNetName(cursor.getString(cursor.getColumnIndex("netName")));
				modal.setNetType(cursor.getString(cursor.getColumnIndex("netType")));
				modal.setNetStatus(cursor.getString(cursor.getColumnIndex("netStatus")));
				modal.setNetLevel(cursor.getString(cursor.getColumnIndex("netLevel")));
				modal.setAddress(cursor.getString(cursor.getColumnIndex("address")));
				modal.setTrafficInfo(cursor.getString(cursor.getColumnIndex("trafficInfo")));
				modal.setBicycleCapacity(cursor.getInt(cursor.getColumnIndex("bicycleCapacity")));
				modal.setBicycleNum(cursor.getInt(cursor.getColumnIndex("bicycleNum")));
				modal.setImageAttach(cursor.getString(cursor.getColumnIndex("imageAttach")));
				modal.setVisited(cursor.getInt(cursor.getColumnIndex("visited")));
				modal.setTime(cursor.getString(cursor.getColumnIndex("time")));
				mAppContext.download.add(modal);
			}
			if(null!=cursor){
				cursor.close();
			}
			if(db!= null){  
				db.close();
		    }
			System.out.println("Apiclient:数据库读取成功");
			sort();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Apiclient:数据库读取失败");
		}	
	}
	
//	public void openMap(Context mContext,BikeStie mBikeStie) {
//		// TODO Auto-generated method stub
//		AppContext.mBikeStie = mBikeStie;
//		mContext.startActivity(new Intent(mContext, class));
//		System.out.println("x:" + mBikeStie.getGpsx() + "y:" + mBikeStie.getGpsy());
//	}
	
//	public void correct(int i) {
//		// TODO Auto-generated method stub
//		if(mAppContext.download.get(i).getMid().equals("810")){
//			mAppContext.download.get(i).setNetName("世纪广场");
//			mAppContext.download.get(i).setGpsx("121.381277");
//			mAppContext.download.get(i).setGpsy("28.594999");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.594999", "121.381277"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("813")){
//			mAppContext.download.get(i).setNetName("耀江广场");
//			mAppContext.download.get(i).setGpsx("121.369383");
//			mAppContext.download.get(i).setGpsy("28.594804");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.594804", "121.369383"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("807")){
//			mAppContext.download.get(i).setNetName("罗曼大酒店");
//			mAppContext.download.get(i).setGpsx("121.380666");
//			mAppContext.download.get(i).setGpsy("28.589682");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.589682", "121.380666"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("808")){
//			mAppContext.download.get(i).setNetName("山马工业区");
//			mAppContext.download.get(i).setGpsx("121.361649");
//			mAppContext.download.get(i).setGpsy("28.587073");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.587073", "121.361649"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("814")){
//			mAppContext.download.get(i).setNetName("名门嘉苑");
//			mAppContext.download.get(i).setAddress("银安街");
//			mAppContext.download.get(i).setGpsx("121.360014");
//			mAppContext.download.get(i).setGpsy("28.573981");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.573981", "121.360014"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("831")){
//			mAppContext.download.get(i).setNetName("金桥花园");
//			mAppContext.download.get(i).setGpsx("121.388337");
//			mAppContext.download.get(i).setGpsy("28.574195");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.574195", "121.388337"));
//		}
//		else if(mAppContext.download.get(i).getMid().equals("787")){
//			mAppContext.download.get(i).setNetName("街心公园");
//			mAppContext.download.get(i).setGpsx("121.389433");
//			mAppContext.download.get(i).setGpsy("28.582204");
//			mAppContext.download.get(i).setmGeoPoint(stringToGeoPoint("28.582204", "121.389433"));
//		}
//	}

	/** 
     * 将纬度lat和经度lng转换为微度的整数，再转换为GeoPoint对象
     **/  
	public GeoPoint stringToGeoPoint(String lat, String lng) {
		// TODO Auto-generated method stub
		return new GeoPoint(
				(int) (Double.parseDouble(lat) * 1E6),
				(int) (Double.parseDouble(lng) * 1E6));
	}
}
