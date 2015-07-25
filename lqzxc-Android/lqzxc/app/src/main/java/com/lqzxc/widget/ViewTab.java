package com.lqzxc.widget;

import com.lqzxc.AppContext;
import com.lqzxc.R;
import com.lqzxc.ui.AboutActivity;
import com.lqzxc.ui.MainActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
/**
 * 自定义是用仿MIUI V5的滑动效果，与ViewPager左右切换相呼应
 * @author QQ472950043
 **/
public class ViewTab extends RelativeLayout {
	
	// 加载资源部件
	LinearLayout widget_tab;
	TextView textview_tab0;
	TextView textview_tab1;
	TextView textview_tab2;
	ImageView imageview_cursor;

	Context mContext;
	AppContext mAppContext;
	DialogBase mDialog;
	int screenW;// 分辨率宽度
	int bmpW;// 动画图片宽度
	int offset;// 动画图片偏移量
	int distance;// 页卡间距

	int page = 0;// 当前页卡编号
	Animation animation;
	public MyOnPageChangeListener mOnPageChangeListener;
	int zero;// 页卡0偏移量
	int one;// 页卡1 偏移量
	int two;// 页卡2 偏移量
	int start;// 起始位置/当前位置
	int end = 0;// 结束位置/目标位置
	
	public ViewTab(Context context) {
		super(context);
	}

	/**
	 * 自定义tab
	 * @param mContext
	 * @param screenW 屏幕宽度，用于计算偏移量
	 * @author QQ472950043
	 **/
	public ViewTab(Context mContext,int screenW) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		this.mContext=mContext;
		mAppContext = (AppContext) mContext.getApplicationContext();
		this.screenW=screenW;
		initAnimation();
		LayoutInflater.from(mContext).inflate(R.layout.widget_tab, this, true);
		initTab();
	}

	/**
	 * 初始化动画，这里的核心是计算偏移量
	 * @author QQ472950043
	 */
	public void initAnimation() {
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.widget_tab_select).getWidth();// 获取图片宽度
		offset = (screenW / 3 - bmpW) / 2;// 计算偏移量
		zero = offset + bmpW / 4;// 页卡0偏移量
		one = offset * 2 + bmpW + zero;// 页卡1 偏移量
		two = offset * 4 + bmpW * 2 + zero;// 页卡2 偏移量
		mOnPageChangeListener=new MyOnPageChangeListener();
	}

	/**
	 * 初始化资源部件，设定动画的起始位置，并为view绑定动画效果
	 * @author QQ472950043
	 */
	public void initTab() {
		textview_tab0 = (TextView) findViewById(R.id.textview_tab0);
		textview_tab1 = (TextView) findViewById(R.id.textview_tab1);
		textview_tab2 = (TextView) findViewById(R.id.textview_tab2);
		// 设置初始位置 Matrix.postTranslate(start,0)不知道为什么不能用，所以用一个动画先走到zero的位置
		start = one;
		imageview_cursor = (ImageView) findViewById(R.id.imageview_cursor);
		animation = new TranslateAnimation(0, start, 0, 0);
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(0);
		imageview_cursor.startAnimation(animation);
		
		mDialog = new DialogBase(mContext, "提示", "确定清除常用访问和历史记录吗？", "确定");
		mDialog.setOnConfirmListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mAppContext.download.size(); i++) {
					mAppContext.download.get(i).setVisited(0);
					mAppContext.download.get(i).setTime("" + System.currentTimeMillis());
				}
				mAppContext.mApiClient.updateVisited();
				mDialog.dismiss();
				mAppContext.ToastMessage(mContext, "清除成功");
			}
		});
	}
	
	public void setOnTab0Listener(View.OnClickListener onTab0Listener) {
		textview_tab0.setOnClickListener(onTab0Listener);
	}

	public void setOnTab1Listener(View.OnClickListener onTab1Listener) {
		textview_tab1.setOnClickListener(onTab1Listener);
	}
	
	public void setOnTab2Listener(View.OnClickListener onTab2Listener) {
		textview_tab2.setOnClickListener(onTab2Listener);
	}

	/**
	 * 页卡切换监听，切换之后改变tab显示状态
	 * @author QQ472950043
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				textview_tab0.setTextColor(Color.rgb(144,144,144));// 设置页卡0颜色
				textview_tab1.setTextColor(Color.rgb(201,201,201));
				textview_tab2.setTextColor(Color.rgb(201,201,201));
				MainActivity.mViewTitle.textView_title_right.setText("清除");
				MainActivity.mViewTitle.setOnRightListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.show();
					}
				});
				break;
			case 1:
				textview_tab0.setTextColor(Color.rgb(201,201,201));
				textview_tab1.setTextColor(Color.rgb(144,144,144));// 设置页卡1颜色
				textview_tab2.setTextColor(Color.rgb(201,201,201));
				MainActivity.mViewTitle.textView_title_right.setText("关于");
				MainActivity.mViewTitle.setOnRightListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mContext.startActivity(new Intent(mContext, AboutActivity.class));
					}
				});
				break;
			case 2:
				textview_tab0.setTextColor(Color.rgb(201,201,201));
				textview_tab1.setTextColor(Color.rgb(201,201,201));
				textview_tab2.setTextColor(Color.rgb(144,144,144));// 设置页卡2颜色
				MainActivity.mViewTitle.textView_title_right.setText("清除");
				MainActivity.mViewTitle.setOnRightListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.show();
					}
				});
				break;
			}
		}
		
		/**
		 * 表示在前一个页面滑动到后一个页面的时候，在前一个页面滑动前调用的方法。
		 * @param arg0 前一个页的索引，从0开始
		 * @param arg1 下一页page可见的宽度所占屏幕的比例 
		 * @param arg2 下一页page可见的宽度
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			//根据前一个页的索引,设置动画end的位置
			if(arg0==0){
				end=zero;
			}else if(arg0==1){
				end=one;
			}else if(arg0==2){
				end=two;
			}
			end = end + (int) ((offset * 2 + bmpW) * arg1);//根据arg1的比例计算动画end的位置
			
			animation = new TranslateAnimation(start, end, 0, 0);
			animation.setFillAfter(true);// True:图片停在动画结束位置
			animation.setDuration(0);//动画延迟，单位毫秒
			imageview_cursor.startAnimation(animation);
			start = end;//这次动画的终点是下次动画的开始点
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}

	