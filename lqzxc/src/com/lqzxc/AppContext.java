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
 * ȫ�����ã������������ں�Ӧ�ó���һ����
 * @author QQ472950043
 */
public class AppContext extends Application {
	/**
	 * ȫ�ֳ���
     */
	public final static int TIMEOUT_CONNECTION = 8000;
	public static final String dirPath = Environment.getExternalStorageDirectory() + "/DCIM";
    /*ע�⣺Ϊ�˸��û��ṩ����ȫ�ķ���Android SDK��v2.1.3�汾��ʼ������ȫ�µ�Key��֤��ϵ��
	��ˣ�����ѡ��ʹ��v2.1.3��֮��汾��SDKʱ����Ҫ���µ�Key����ҳ�����ȫ��Key�����룬
	���뼰����������ο�����ָ�ϵĶ�Ӧ�½�*/
    public static final String strKey = "1s4hDzrOfiC4adMHSIM2U2g7";
	//2E:70:44:AE:17:2B:26:A9:83:F9:68:1A:9D:92:80:10:BD:AE:59:C0;com.lqzxc
    
	public ApiClient mApiClient;
	public SharedPreferences data;
	public BMapManager mBMapManager = null;
	boolean m_bKeyRight = true;
	@Override
	public void onCreate() {
		super.onCreate();
		// ��ʼ��ApiClient
		mApiClient = new ApiClient(this);
		data = getSharedPreferences("data", 0);
		// ��ʼ��BMapManager
		initEngineManager(this);
		// ��ʼ��ImageLoader
		initImageLoader(this);
	}

	/**
     * ʹ�õ�ͼsdkǰ���ȳ�ʼ��BMapManager.
     * BMapManager��ȫ�ֵģ���Ϊ���MapView���ã�����Ҫ��ͼģ�鴴��ǰ������
     * ���ڵ�ͼ��ͼģ�����ٺ����٣�ֻҪ���е�ͼģ����ʹ�ã�BMapManager�Ͳ�Ӧ������
     */
	public void initEngineManager(Context mContext) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(mContext);
        }
        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
        	ToastMessage(this, "BMapManager  ��ʼ������!");
        }
	}
	
	/**
	 *  �����¼���������������ͨ�������������Ȩ��֤�����
	 * @author QQ472950043
	 */
    public class MyGeneralListener implements MKGeneralListener {
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
            	isNetError = true;
            	ToastMessage(AppContext.this, "���������������");
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
            	ToastMessage(AppContext.this, "������ȷ�ļ���������");
            }
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //��ȨKey����
            	ToastMessage(AppContext.this, "���� AppContext.java�ļ�������ȷ����ȨKey��");
                m_bKeyRight = false;
            }
        }
    }
   	
    public ImageLoader mImageLoader;
   	DisplayImageOptions logo_options;
   	ImageLoaderConfiguration config;
   	public ImageLoadingListener animateFirstListener;

    /**
   	 * ͼƬ����ImageLoader�������
   	 */
   	public void initImageLoader(Context mContext) {
   		// TODO Auto-generated method stub
   		// ͼƬ���ص�һ����ʾ������ 
   		animateFirstListener= new AnimateFirstDisplayListener();
   		// ��ʾ����ѡ��
		logo_options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.jpg_photo_loading)// ����ͼƬ�������ڼ���ʾ��ͼƬ
				.showImageForEmptyUri(R.drawable.wangdian_img)// ����ͼƬUriΪ�ջ��Ǵ����ʱ����ʾ��ͼƬ
				.showImageOnFail(R.drawable.wangdian_img)// ����ͼƬ����/��������д���ʱ����ʾ��ͼƬ
				.resetViewBeforeLoading()// ����ͼƬ������ǰ�Ƿ����ã���λ
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)// ����ͼƬ����εı��뷽ʽ��ʾ
				.bitmapConfig(Bitmap.Config.RGB_565)// ����ͼƬ�Ľ�������
				.cacheInMemory()// �������ص�ͼƬ�Ƿ񻺴����ڴ���
				.cacheOnDisc()// �������ص�ͼƬ�Ƿ񻺴���SD����
				.displayer(new RoundedBitmapDisplayer(5))// ����ͼƬ����ʾ��ʽ ,ͼƬԲ��
				.build();
		// ����ѡ��
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
   	
	// ֱ�ӽ�bitmap���ø�ImageViewȻ����ⷵ��ԭ����Bitmap������ʾ�ڴ滺��ͼƬ�Ĳ����ͽ�����
   	public class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
   		List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());
   		/**
   		 * @param imageUri ����url
   		 * @param view ��Ҫ��ʾ��view
   		 * @param loadedImage ���غõ�ͼ��
   		 */
   		@Override
   		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
   			if (loadedImage != null) {
   				ImageView imageView = (ImageView) view;
   				boolean firstDisplay = !displayedImages.contains(imageUri);
   				if (firstDisplay) {
   					// ��ʾͼ��ġ�����������
   					FadeInBitmapDisplayer.animate(imageView, 100);
   					displayedImages.add(imageUri);
   				}
   				Log.v("size", "width:" + imageView.getWidth() + " height:" + imageView.getHeight());
   			}
   		}
   	}


   	// ʹ��DisplayImageOptions.Builder()����Ĭ�ϵ�DisplayImageOptions
//   	public DisplayImageOptions default_options = DisplayImageOptions.createSimple();
//   		DisplayImageOptions default_options = new DisplayImageOptions.Builder()
//   				.cacheInMemory()
//   				.cacheOnDisc()
//   				.build();

   	/**
   	 * ͼƬ�����ŷ�ʽ
   	 * 
   	 * @param imageScaleType
   	 */
   	// imageScaleType(ImageScaleType imageScaleType)
   	// imageScaleType:
   	// EXACTLY :ͼ����ȫ��������С��Ŀ���С
   	// EXACTLY_STRETCHED:ͼƬ�����ŵ�Ŀ���С��ȫ
   	// IN_SAMPLE_INT:ͼ�񽫱����β�����������
   	// IN_SAMPLE_POWER_OF_2:ͼƬ������2����ֱ����һ���ٲ��裬ʹͼ���С��Ŀ���С
   	// NONE:ͼƬ�������
   	/**
   	 * ����ͼƬ����ʾ��ʽ
   	 * 
   	 * @param displayer
   	 */
   	// displayer(BitmapDisplayer displayer)
   	// displayer��
   	// RoundedBitmapDisplayer��int roundPixels������Բ��ͼƬ
   	// FakeBitmapDisplayer���������ʲô��û��
   	// FadeInBitmapDisplayer��int durationMillis������ͼƬ���Ե�ʱ��
   	// SimpleBitmapDisplayer()������ʾһ��ͼƬ
   	// //����ͼƬ����ǰ���ӳ�
   	// delayBeforeLoading(int delayInMillis)
   	// //���ö�������ݸ�ImageDownloader
   	// extraForDownloader(Object extra)
   	// //����ͼƬ���뻺��ǰ����bitmap��������
   	// preProcessor(BitmapProcessor preProcessor)
   	// //������ʾǰ��ͼƬ����ʾ�����ͼƬһֱ�����ڻ�����
   	// postProcessor(BitmapProcessor postProcessor)

	/**
	 * ȫ����������
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
	 * �ٶȵ�ͼȫ�ֱ���
	 */
   	public MapView mMapView;// ��ͼView
   	public MapController mMapController;//��MapController��ɵ�ͼ���� 
   	public BikeStie mBikeStie;// �б��ݸ���ͼ������/��ǰ����Ĺ������г�������

	// bike��עͼ��
   	public Bike mBike;
	public boolean isShowBikeText;// �Ƿ���ʾbike�ı�ͼ��
	public TextOverlay bikeTextOverlay;// bike�ı�ͼ��
	public boolean isShowBikeItemized;// �Ƿ���ʾbike��ע��ͼ��
	public BikeItemizedOverlay bikeItemizedOverlay;// bike��ע��ͼ��
   	
	public ViewMode mViewMode;// �Զ�����������
	public ViewUIBtn mViewUIBtn;// �Զ����ͼUI���ư�ť
	public RelativeLayout widget_window;// �м�����  ��ʾ/����
	public ViewWindow mViewWindow;// �м����㴰�ڲ���
	public MKMapStatus bikeMapStatus;// �����ͼ״̬��ʾ
	public RelativeLayout main_layout;// ���ؽ�� ��ʾ/����
	public PopupWindow mPopupWindow;// ���ؽ������

	public MKSearch mSearch;// ����ģ�飬Ҳ��ȥ����ͼģ�����ʹ��
	public MKPlanNode stNode;// ���
	public MKPlanNode enNode;// �յ�
	public List<MKPoiInfo> stPois;// ����б�
	public List<MKPoiInfo> enPois;// �յ��б�
	public String city;// ������
	public String stCity;// ������
	public String enCity;// �յ����
	public List<MKCityListInfo> stCities;// ������ģ���б�
	public List<MKCityListInfo> enCities;// �յ����ģ���б�
	public int select;
	
	// ���ؽ��ͼ��
	public TransitOverlay transitOverlay;// ����·��ͼ��
	public RouteOverlay routeOverlay;// �ݳ�/����/����·�� ����ͼ��
	// ���ؽ�����ݣ�������ڵ�ʱʹ��
	public MKTransitRouteResult transitRouteResults;// ���湫�����ݵı���
	public List<RouteResult> routeResults;// ����ݳ�/����/����·�����ݵı���
	//����ڵ�����ͼ��
	public PopupOverlay nodePopupOverlay;
	public boolean isShowTransitOverlay;
	public boolean isShowRouteOverlay;

	//���·�߽ڵ����
	public int nodeIndex = 0;//�ڵ�����,������ڵ�ʱʹ��
    
	public MKMapStatus mMapStatus;
	public Location mLocation;// ��λ������
	//��ͼ��������
	public SharedPreferences setting;
	
	// ��λ��popup
	public PopupOverlay mLocationPopup;
	// ��λͼ��
	public LocationOverlay mLocationOverlay;
	// ��λview
	public View location_view;
	public TextView location_title;
	public TextView location_content;
	public LocationData mData;// ��λ��Ϣ
	public GeoPoint locatCenter;// ��λ��
	public boolean isAutoRequest;// �Ƿ��Զ���λ
	
	public boolean isSavescreen;
	public boolean isMeasure;//


	public ViewOffline mViewOffline;
	public MKOfflineMap mOffline;// ���ߵ�ͼ����
	public List<MKOLUpdateElement> mElements;// ���ߵ�ͼ������Ϣ��¼�ṹ
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
	 * ��ʽ����װ����С
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
	 * ���wifi����
	 * @author QQ472950043
	 **/
	public boolean isWifiConnect() {
		ConnectivityManager connManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		return mWifi.isConnected();
	}

	/**
	 * ���SD���Ƿ����
	 * @author QQ472950043
	 **/
	public boolean checkSDCard() {
		if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}
}