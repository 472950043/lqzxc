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
 * �Զ������÷�MIUI V5�Ļ���Ч������ViewPager�����л����Ӧ
 * @author QQ472950043
 **/
public class ViewTab extends RelativeLayout {
	
	// ������Դ����
	LinearLayout widget_tab;
	TextView textview_tab0;
	TextView textview_tab1;
	TextView textview_tab2;
	ImageView imageview_cursor;

	Context mContext;
	AppContext mAppContext;
	DialogBase mDialog;
	int screenW;// �ֱ��ʿ��
	int bmpW;// ����ͼƬ���
	int offset;// ����ͼƬƫ����
	int distance;// ҳ�����

	int page = 0;// ��ǰҳ�����
	Animation animation;
	public MyOnPageChangeListener mOnPageChangeListener;
	int zero;// ҳ��0ƫ����
	int one;// ҳ��1 ƫ����
	int two;// ҳ��2 ƫ����
	int start;// ��ʼλ��/��ǰλ��
	int end = 0;// ����λ��/Ŀ��λ��
	
	public ViewTab(Context context) {
		super(context);
	}

	/**
	 * �Զ���tab
	 * @param mContext
	 * @param screenW ��Ļ��ȣ����ڼ���ƫ����
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
	 * ��ʼ������������ĺ����Ǽ���ƫ����
	 * @author QQ472950043
	 */
	public void initAnimation() {
		bmpW = BitmapFactory.decodeResource(getResources(), R.drawable.widget_tab_select).getWidth();// ��ȡͼƬ���
		offset = (screenW / 3 - bmpW) / 2;// ����ƫ����
		zero = offset + bmpW / 4;// ҳ��0ƫ����
		one = offset * 2 + bmpW + zero;// ҳ��1 ƫ����
		two = offset * 4 + bmpW * 2 + zero;// ҳ��2 ƫ����
		mOnPageChangeListener=new MyOnPageChangeListener();
	}

	/**
	 * ��ʼ����Դ�������趨��������ʼλ�ã���Ϊview�󶨶���Ч��
	 * @author QQ472950043
	 */
	public void initTab() {
		textview_tab0 = (TextView) findViewById(R.id.textview_tab0);
		textview_tab1 = (TextView) findViewById(R.id.textview_tab1);
		textview_tab2 = (TextView) findViewById(R.id.textview_tab2);
		// ���ó�ʼλ�� Matrix.postTranslate(start,0)��֪��Ϊʲô�����ã�������һ���������ߵ�zero��λ��
		start = one;
		imageview_cursor = (ImageView) findViewById(R.id.imageview_cursor);
		animation = new TranslateAnimation(0, start, 0, 0);
		animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
		animation.setDuration(0);
		imageview_cursor.startAnimation(animation);
		
		mDialog = new DialogBase(mContext, "��ʾ", "ȷ��������÷��ʺ���ʷ��¼��", "ȷ��");
		mDialog.setOnConfirmListener(new View.OnClickListener() {
			public void onClick(View v) {
				// TODO Auto-generated method stub
				for (int i = 0; i < mAppContext.download.size(); i++) {
					mAppContext.download.get(i).setVisited(0);
					mAppContext.download.get(i).setTime("" + System.currentTimeMillis());
				}
				mAppContext.mApiClient.updateVisited();
				mDialog.dismiss();
				mAppContext.ToastMessage(mContext, "����ɹ�");
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
	 * ҳ���л��������л�֮��ı�tab��ʾ״̬
	 * @author QQ472950043
	 */
	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int arg0) {
			switch (arg0) {
			case 0:
				textview_tab0.setTextColor(Color.rgb(144,144,144));// ����ҳ��0��ɫ
				textview_tab1.setTextColor(Color.rgb(201,201,201));
				textview_tab2.setTextColor(Color.rgb(201,201,201));
				MainActivity.mViewTitle.textView_title_right.setText("���");
				MainActivity.mViewTitle.setOnRightListener(new OnClickListener() {
					public void onClick(View v) {
						// TODO Auto-generated method stub
						mDialog.show();
					}
				});
				break;
			case 1:
				textview_tab0.setTextColor(Color.rgb(201,201,201));
				textview_tab1.setTextColor(Color.rgb(144,144,144));// ����ҳ��1��ɫ
				textview_tab2.setTextColor(Color.rgb(201,201,201));
				MainActivity.mViewTitle.textView_title_right.setText("����");
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
				textview_tab2.setTextColor(Color.rgb(144,144,144));// ����ҳ��2��ɫ
				MainActivity.mViewTitle.textView_title_right.setText("���");
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
		 * ��ʾ��ǰһ��ҳ�滬������һ��ҳ���ʱ����ǰһ��ҳ�滬��ǰ���õķ�����
		 * @param arg0 ǰһ��ҳ����������0��ʼ
		 * @param arg1 ��һҳpage�ɼ��Ŀ����ռ��Ļ�ı��� 
		 * @param arg2 ��һҳpage�ɼ��Ŀ��
		 */
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			//����ǰһ��ҳ������,���ö���end��λ��
			if(arg0==0){
				end=zero;
			}else if(arg0==1){
				end=one;
			}else if(arg0==2){
				end=two;
			}
			end = end + (int) ((offset * 2 + bmpW) * arg1);//����arg1�ı������㶯��end��λ��
			
			animation = new TranslateAnimation(start, end, 0, 0);
			animation.setFillAfter(true);// True:ͼƬͣ�ڶ�������λ��
			animation.setDuration(0);//�����ӳ٣���λ����
			imageview_cursor.startAnimation(animation);
			start = end;//��ζ������յ����´ζ����Ŀ�ʼ��
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}
}

	