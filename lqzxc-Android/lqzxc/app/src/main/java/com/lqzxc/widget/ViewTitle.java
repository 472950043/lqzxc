package com.lqzxc.widget;

import com.lqzxc.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * 自定义title
 * @author QQ472950043
 **/
public class ViewTitle extends RelativeLayout {

	TextView textView_title_back;
	TextView textView_title;
	TextView textView_title_right;
	LinearLayout btn_back;
	LinearLayout btn_right;

	public ViewTitle(Context context) {
		super(context);
	}
	/**
	 * 自定义title
	 * @param mContext
	 * @param back 左边按钮文字，为null则隐藏
	 * @param title 标题文字，为空
	 * @param right 右边按钮文字，为null则隐藏
	 * @author QQ472950043
	 **/
	public ViewTitle(Context mContext,String back,String title,String right) {
		super(mContext);
		// TODO Auto-generated constructor stub
		if (isInEditMode()) {
			return;
		}
		setWillNotDraw(false);
		LayoutInflater.from(mContext).inflate(R.layout.widget_title, this, true);

		textView_title_back = (TextView) findViewById(R.id.textview_widget_title_back);
		textView_title = (TextView) findViewById(R.id.textview_widget_title);
		textView_title_right = (TextView) findViewById(R.id.textview_widget_title_right);
		btn_back = (LinearLayout) findViewById(R.id.btn_widget_title_back);
		btn_right = (LinearLayout) findViewById(R.id.btn_widget_title_right);
		
		// 左按钮，传null则隐藏
		if(null==back)
			btn_back.setVisibility(View.GONE);
		else
			textView_title_back.setText(back);
		// 设置标题
		textView_title.setText(title);
		// 右按钮，传null则隐藏
		if(null==right)
			btn_right.setVisibility(View.GONE);
		else
			textView_title_right.setText(right);
	}
	
	public void setOnBackListener(View.OnClickListener onBackListener) {
		btn_back.setOnClickListener(onBackListener);
	}

	public void setOnRightListener(View.OnClickListener onRightListener) {
		btn_right.setOnClickListener(onRightListener);
	}

}

	