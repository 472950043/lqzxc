package com.lqzxc;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.LocationData;
import com.baidu.mapapi.map.MKEvent;
import com.baidu.mapapi.map.MKMapStatus;
import com.baidu.mapapi.map.MKOLUpdateElement;
import com.baidu.mapapi.map.MKOfflineMap;
import com.baidu.mapapi.map.MapController;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.PopupOverlay;
import com.baidu.mapapi.map.RouteOverlay;
import com.baidu.mapapi.map.TextOverlay;
import com.baidu.mapapi.map.TransitOverlay;
import com.baidu.mapapi.search.MKCityListInfo;
import com.baidu.mapapi.search.MKPlanNode;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.mapapi.search.MKSearch;
import com.baidu.mapapi.search.MKTransitRouteResult;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.lqzxc.api.ApiClient;
import com.lqzxc.map.bike.Bike;
import com.lqzxc.map.bike.BikeItemizedOverlay;
import com.lqzxc.map.location.Location;
import com.lqzxc.map.location.LocationOverlay;
import com.lqzxc.map.modal.RouteResult;
import com.lqzxc.map.ui.ViewMode;
import com.lqzxc.map.ui.ViewOffline;
import com.lqzxc.map.ui.ViewUIBtn;
import com.lqzxc.map.ui.ViewWindow;
import com.lqzxc.modal.BikeStie;
import com.lqzxc.ui.FragmentHistory;
import com.lqzxc.ui.FragmentSearch;
import com.lqzxc.ui.FragmentVisit;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
/**
 * 全局配置，它的生命周期和应用程序一样长
 * @author QQ472950043
 */
public class AppContext extends Application {
	/**
	 * 全局常量
     */
	public final static int TIMEOUT_CONNECTION = 8000;
	public static final String dirPath = Environment.getExternalStorageDirectory() + "/DCIM";
    /*注意：为了给用户提供更安全的服务，Android SDK自v2.1.3版本开始采用了全新的Key验证体系。
	因此，当您选择使用v2.1.3及之后版本的SDK时，需要到新的Key申请页面进行全新Key的申请，
	申请及配置流程请参考开发指南的对应章节*/
    public static final String strKey = "1s4hDzrOfiC4adMHSIM2U2g7";
	//2E:70:44:AE:17:2B:26:A9:83:F9:68:1A:9D:92:80:10:BD:AE:59:C0;com.lqzxc
    
	public ApiClient mApiClient;
	public SharedPreferences data;
	public BMapManager mBMapManager = null;
	boolean m_bKeyRight = true;
	@Override
	public void onCreate() {
		super.onCreate();
		// 初始化ApiClient
		mApiClient = new ApiClient(this);
		data = getSharedPreferences("data", 0);
		// 初始化BMapManager
		initEngineManager(this);
		// 初始化ImageLoader
		initImageLoader(this);
	}

	/**
     * 使用地图sdk前需先初始化BMapManager.
     * BMapManager是全局的，可为多个MapView共用，它需要地图模块创建前创建，
     * 并在地图地图模块销毁后销毁，只要还有地图模块在使用，BMapManager就不应该销毁
     */
	public void initEngineManager(Context mContext) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(mContext);
        }
        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
        	ToastMessage(this, "BMapManager  初始化错误!");
        }
	}
	
	/**
	 *  常用事件监听，用来处理通常的网络错误，授权验证错误等
	 * @author QQ472950043
	 */
    public class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
            	isNetError = true;
            	ToastMessage(AppContext.this, "您的网络出错啦！");
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
            	ToastMessage(AppContext.this, "输入正确的检索条件！");
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
            	ToastMessage(AppContext.this, "请在 AppContext.java文件输入正确的授权Key！");
                m_bKeyRight = false;
            }
        }
    }
   	
    public ImageLoader mImageLoader;
   	DisplayImageOptions logo_options;
   	ImageLoaderConfiguration config;
   	public ImageLoadingListener animateFirstListener;

    /**
   	 * 图片下载ImageLoader相关配置
   	 */
   	public void initImageLoader(Context mContext) {
   		// TODO Auto-generated method stub
   		// 图片加载第一次显示监听器 
   		animateFirstListener= new AnimateFirstDisplayListener();
   		// 显示配置选项
		logo_options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.jpg_photo_loading)// 设置图片在下载期间显示的图片
				.showImageForEmptyUri(R.drawable.wangdian_img)// 设置图片Uri为空或是错误的时候显示的图片
				.showImageOnFail(R.drawable.wangdian_img)// 设置图片加载/解码过程中错误时候显示的图片
				.resetViewBeforeLoading()// 设置图片在下载前是否重置，复位
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// 设置图片以如何的编码方式显示
				.bitmapConfig(Bitmap.Config.RGB_565)// 设置图片的解码类型
				.cacheInMemory()// 设置下载的图片是否缓存在内存中
				.cacheOnDisc()// 设置下载的图片是否缓存在SD卡中
				.displayer(new RoundedBitmapDisplayer(5))// 设置图片的显示方式 ,图片圆角
				.build();
		// 下载选项
		config = new ImageLoaderConfiguration.Builder(mContext)
				.memoryCacheExtraOptions(480, 800)// default = device screen dimensions
				.discCacheExtraOptions(800, 1280, CompressFormat.JPEG, 75)
				.threadPoolSize(3)// default
				.threadPriority(Thread.NORM_PRIORITY - 1)// default
				.tasksProcessingOrder(QueueProcessingType.FIFO)// default
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new LruMemoryCache(5 * 1024 * 1024))
				.memoryCacheSize(5 * 1024 * 1024)
				.discCache(new UnlimitedDiscCache(StorageUtils.getCacheDirectory(mContext)))// default
				.discCacheSize(50 * 1024 * 1024).discCacheFileCount(100)
				.discCacheFileNameGenerator(new HashCodeFileNameGenerator())// default
				.imageDownloader(new BaseImageDownloader(mContext))// default
				.imageDecoder(new BaseImageDecoder())// default
				.defaultDisplayImageOptions(logo_options).enableLogging()
				.build();
   		ImageLoader.getInstance().init(config);
		if(null==mImageLoader){
	   		mImageLoader=ImageLoader.getInstance();
		}
   	}
   	
	// 直接将bitmap设置给ImageView然后对外返回原来的Bitmap，到显示内存缓存图片的操作就结束了
   	public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
   		List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
   		/**
   		 * @param imageUri 下载url
   		 * @param view 需要显示的view
   		 * @param loadedImage 下载好的图像
   		 */
   		@Override
   		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
   			if (loadedImage != null) {
   				ImageView imageView = (ImageView) view;
   				boolean firstDisplay = !displayedImages.contains(imageUri);
   				if (firstDisplay) {
   					// 显示图像的“淡出”动画
   					FadeInBitmapDisplayer.animate(imageView, 100);
   					displayedImages.add(imageUri);
   				}
   				Log.v("size", "width:" + imageView.getWidth() + " height:" + imageView.getHeight());
   			}
   		}
   	}


   	// 使用DisplayImageOptions.Builder()创建默认的DisplayImageOptions
//   	public DisplayImageOptions default_options = DisplayImageOptions.createSimple();
//   		DisplayImageOptions default_options = new DisplayImageOptions.Builder()
//   				.cacheInMemory()
//   				.cacheOnDisc()
//   				.build();

   	/**
   	 * 图片的缩放方式
   	 * 
   	 * @param imageScaleType
   	 */
   	// imageScaleType(ImageScaleType imageScaleType)
   	// imageScaleType:
   	// EXACTLY :图像将完全按比例缩小的目标大小
   	// EXACTLY_STRETCHED:图片会缩放到目标大小完全
   	// IN_SAMPLE_INT:图像将被二次采样的整数倍
   	// IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
   	// NONE:图片不会调整
   	/**
   	 * 设置图片的显示方式
   	 * 
   	 * @param displayer
   	 */
   	// displayer(BitmapDisplayer displayer)
   	// displayer：
   	// RoundedBitmapDisplayer（int roundPixels）设置圆角图片
   	// FakeBitmapDisplayer（）这个类什么都没做
   	// FadeInBitmapDisplayer（int durationMillis）设置图片渐显的时间
   	// SimpleBitmapDisplayer()正常显示一张图片
   	// //设置图片下载前的延迟
   	// delayBeforeLoading(int delayInMillis)
   	// //设置额外的内容给ImageDownloader
   	// extraForDownloader(Object extra)
   	// //设置图片加入缓存前，对bitmap进行设置
   	// preProcessor(BitmapProcessor preProcessor)
   	// //设置显示前的图片，显示后这个图片一直保留在缓存中
   	// postProcessor(BitmapProcessor postProcessor)

	/**
	 * 全局生命周期
	 */
   	public FragmentVisit fragmentVisit;
	public FragmentSearch fragmentSearch;
	public FragmentHistory fragmentHistory;
	
   	public List<BikeStie> download = new ArrayList<BikeStie>();
   	public List<BikeStie> mBikeSties = new ArrayList<BikeStie>();

   	public List<BikeStie> visit = new ArrayList<BikeStie>();
   	public List<BikeStie> search = new ArrayList<BikeStie>();
   	public List<BikeStie> history = new ArrayList<BikeStie>();

   	public int totalCapacity = 0;
   	public int totalNum = 0;
   	public boolean isNetError;

    /**
	 * 百度地图全局变量
	 */
   	public MapView mMapView;// 地图View
   	public MapController mMapController;//用MapController完成地图控制 
   	public BikeStie mBikeStie;// 列表传递给地图的网点/当前点击的公共自行车的网点

	// bike标注图层
   	public Bike mBike;
	public boolean isShowBikeText;// 是否显示bike文本图层
	public TextOverlay bikeTextOverlay;// bike文本图层
	public boolean isShowBikeItemized;// 是否显示bike标注点图层
	public BikeItemizedOverlay bikeItemizedOverlay;// bike标注点图层
   	
	public ViewMode mViewMode;// 自定义搜索部件
	public ViewUIBtn mViewUIBtn;// 自定义地图UI控制按钮
	public RelativeLayout widget_window;// 中间网点  显示/隐藏
	public ViewWindow mViewWindow;// 中间网点窗口部件
	public MKMapStatus bikeMapStatus;// 网点地图状态表示
	public RelativeLayout main_layout;// 搜素结果 显示/隐藏
	public PopupWindow mPopupWindow;// 搜素结果弹窗

	public MKSearch mSearch;// 搜索模块，也可去掉地图模块独立使用
	public MKPlanNode stNode;// 起点
	public MKPlanNode enNode;// 终点
	public List<MKPoiInfo> stPois;// 起点列表
	public List<MKPoiInfo> enPois;// 终点列表
	public String city;// 起点城市
	public String stCity;// 起点城市
	public String enCity;// 终点城市
	public List<MKCityListInfo> stCities;// 起点城市模糊列表
	public List<MKCityListInfo> enCities;// 终点城市模糊列表
	public int select;
	
	// 搜素结果图层
	public TransitOverlay transitOverlay;// 公交路线图层
	public RouteOverlay routeOverlay;// 驾车/步行/公交路线 搜素图层
	// 搜素结果数据，供浏览节点时使用
	public MKTransitRouteResult transitRouteResults;// 保存公交数据的变量
	public List<RouteResult> routeResults;// 保存驾车/步行/公交路线数据的变量
	//浏览节点泡泡图层
	public PopupOverlay nodePopupOverlay;
	public boolean isShowTransitOverlay;
	public boolean isShowRouteOverlay;

	//浏览路线节点相关
	public int nodeIndex = 0;//节点索引,供浏览节点时使用
    
	public MKMapStatus mMapStatus;
	public Location mLocation;// 定位工具类
	//地图控制属性
	public SharedPreferences setting;
	
	// 定位点popup
	public PopupOverlay mLocationPopup;
	// 定位图层
	public LocationOverlay mLocationOverlay;
	// 定位view
	public View location_view;
	public TextView location_title;
	public TextView location_content;
	public LocationData mData;// 定位信息
	public GeoPoint locatCenter;// 定位点
	public boolean isAutoRequest;// 是否自动定位
	
	public boolean isSavescreen;
	public boolean isMeasure;//


	public ViewOffline mViewOffline;
	public MKOfflineMap mOffline;// 离线地图服务
	public List<MKOLUpdateElement> mElements;// 离线地图更新信息记录结构
	public MKOLUpdateElement mElement;
	
	public Dialog noticeDialog;  
	public void showLoading(Context mContext) {
		// TODO Auto-generated method stub
		if (noticeDialog != null && noticeDialog.isShowing()) {
			noticeDialog.dismiss();
		}
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);  
		builder.setView(LayoutInflater.from(mContext).inflate(R.layout.dialog_loading, null));
		noticeDialog = builder.create();
		noticeDialog.show(); 
	}

	public void hideLoading() {
		// TODO Auto-generated method stub
		noticeDialog.dismiss();
	}
	
	public void ToastMessage(Context mContext, String msg) {
		// TODO Auto-generated method stub
		Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
	}

	
	/**
	 * 格式化安装包大小
	 * @param size
	 * @return String
	 */
	public String formatDataSize(int size) {
		String ret = "";
		if (size < (1024 * 1024)) {
			ret = size / 1024+" KB";
		} else {
	        DecimalFormat df = new DecimalFormat("#.000");
			ret = df.format(size / (1024 * 1024.0))+" MB";
//		        System.out.println(df.format(size / (1024 * 1024.0)));
		}
		return ret;
	}
	
	/**
	 * 检查wifi连接
	 * @author QQ472950043
	 **/
	public boolean isWifiConnect() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	/**
	 * 检查SD卡是否存在
	 * @author QQ472950043
	 **/
	public boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
}