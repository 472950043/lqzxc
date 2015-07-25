package com.lqzxc.widget;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.lqzxc.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
/**
 * 下拉刷新ScrollView
 * @author QQ472950043
 */
public class RefreshScrollView extends ScrollView{

	//状态变量组
	private final static int PULL_To_REFRESH = 0;// 下拉刷新标志
	private final static int RELEASE_To_REFRESH = 1;// 松开刷新标志
	private final static int REFRESHING = 2;// 正在刷新标志
	private final static int DONE = 3;// 刷新完成标志

	//布局文件
	private LayoutInflater inflater;
	//内部布局，ScrollView只能有一个child
	private LinearLayout innerLayout;
	
	private LinearLayout headView;
	//头部列表
	private TextView tipsTextview;
	private TextView lastUpdatedTextView;
	private ImageView arrowImageView;
	private ProgressBar progressBar;
	// 用来设置箭头图标动画效果
	private RotateAnimation animation;
	private RotateAnimation reverseAnimation;

	// 用于保证startY的值在一个完整的touch事件中只被记录一次
	private boolean isRecored;

	//头部view的长宽
//	private int headContentWidth;
	private int headContentHeight;
	
	// 实际的padding的距离与界面上偏移距离
	private int headContentOriginalTopPadding;

	private int startY;

	private int state;

	private boolean isBack;

	//刷新时间
	public OnRefreshListener refreshListener;

	public RefreshScrollView(Context context) {
		super(context);
		init(context);
	}

	public RefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public RefreshScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	private void init(Context context) {
		if (isInEditMode()) return;
		//向上的滑动动画
		animation = new RotateAnimation(0, -180,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setInterpolator(new LinearInterpolator());
		animation.setDuration(100);
		animation.setFillAfter(true);

		//回弹的滑动动画
		reverseAnimation = new RotateAnimation(-180, 0,RotateAnimation.RELATIVE_TO_SELF, 0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		reverseAnimation.setInterpolator(new LinearInterpolator());
		reverseAnimation.setDuration(100);
		reverseAnimation.setFillAfter(true);

		inflater = LayoutInflater.from(context);
		//创建innerLayout
		innerLayout = new LinearLayout(context);
		innerLayout.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		innerLayout.setOrientation(LinearLayout.VERTICAL);
		//创建headerView
		headView = (LinearLayout) inflater.inflate(R.layout.widget_listview_head, null);
		arrowImageView = (ImageView) headView.findViewById(R.id.imageview_head_arrow);
//		arrowImageView.setMinimumWidth(35);
//		arrowImageView.setMinimumHeight(25);
		progressBar = (ProgressBar) headView.findViewById(R.id.progressbar_head);
		tipsTextview = (TextView) headView.findViewById(R.id.textview_head_tips);
		lastUpdatedTextView = (TextView) headView.findViewById(R.id.textview_head_lastUpdated);

		//测量headView高度、宽度
		measureView(headView);
		headContentHeight = headView.getMeasuredHeight();
//		headContentWidth = headView.getMeasuredWidth();

		//设置headView间距
		headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());
		headView.invalidate();

//		headContentOriginalTopPadding = headView.getPaddingTop();
//		System.out.println("初始高度："+headContentHeight);
//		System.out.println("初始TopPad："+headContentOriginalTopPadding);

		//增加HeaderView至innerLayout顶部、设置监听
		innerLayout.addView(headView);
		addView(innerLayout);
	}

	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (getScrollY() == 0 && !isRecored) {
				startY = (int) event.getY();
				isRecored = true;
				System.out.println("当前-按下高度-ACTION_DOWN-Y："+startY);
			}
			break;
		case MotionEvent.ACTION_CANCEL:// 失去焦点&取消动作
		case MotionEvent.ACTION_UP:
			if (state != REFRESHING && state != REFRESHING) {
				if (state == DONE) {
					System.out.println("当前-抬起-ACTION_UP：DONE什么都不做");
				} else if (state == PULL_To_REFRESH) {
					state = DONE;
					changeHeaderViewByState();
					System.out.println("当前-抬起-ACTION_UP：PULL_To_REFRESH-->DONE-由下拉刷新状态到刷新完成状态");
				} else if (state == RELEASE_To_REFRESH) {
					state = REFRESHING;
					changeHeaderViewByState();
					onRefresh();
					System.out.println("当前-抬起-ACTION_UP：RELEASE_To_REFRESH-->REFRESHING-由松开刷新状态，到刷新完成状态");
				}
			}
			isRecored = false;
			isBack = false;
			break;
		case MotionEvent.ACTION_MOVE:
			int tempY = (int) event.getY();
			System.out.println("当前-滑动-ACTION_MOVE Y："+tempY);
			if (!isRecored && getScrollY() == 0) {
				isRecored = true;
				startY = tempY;
				System.out.println("当前-滑动-记录拖拽时的位置 Y："+tempY);
			}
			if (state != REFRESHING && isRecored && state != REFRESHING) {
				// 保证在设置padding的过程中，当前的位置一直是在head，否则如果当列表超出屏幕的话，当在上推的时候，列表会同时进行滚动
				// 可以松开刷新了
				if (state == RELEASE_To_REFRESH) {
					// 往上推，推到屏幕足够掩盖head的程度，但还没有全部掩盖
					if ((tempY - startY < headContentHeight + 20) && (tempY - startY) > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
						System.out.println("当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》PULL_To_REFRESH-由松开刷新状态转变到下拉刷新状态");
					}
					// 一下子推到顶
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
						System.out.println("当前-滑动-ACTION_MOVE：RELEASE_To_REFRESH--》DONE-由松开刷新状态转变到done状态");
					}
					// 往下拉，或者还没有上推到屏幕顶部掩盖head
					else {
						// 不用进行特别的操作，只用更新paddingTop的值就行了
					}
				}
				// 还没有到达显示松开刷新的时候,DONE或者是PULL_To_REFRESH状态
				else if (state == PULL_To_REFRESH) {
					// 下拉到可以进入RELEASE_TO_REFRESH的状态
					if (tempY - startY >= headContentHeight + 20) {
						state = RELEASE_To_REFRESH;
						isBack = true;
						changeHeaderViewByState();
						System.out.println("当前-滑动-PULL_To_REFRESH--》RELEASE_To_REFRESH-由done或者下拉刷新状态转变到松开刷新");
					}
					// 上推到顶了
					else if (tempY - startY <= 0) {
						state = DONE;
						changeHeaderViewByState();
						System.out.println("当前-滑动-PULL_To_REFRESH--》DONE-由Done或者下拉刷新状态转变到done状态");
					}
				}
				// done状态下
				else if (state == DONE) {
					if (tempY - startY > 0) {
						state = PULL_To_REFRESH;
						changeHeaderViewByState();
						System.out.println("当前-滑动-DONE--》PULL_To_REFRESH-由done状态转变到下拉刷新状态");
					}
				}

				// 更新headView的size
				if (state == PULL_To_REFRESH) {
					int topPadding = (int) ((-1 * headContentHeight + (tempY - startY)));
					headView.setPadding(headView.getPaddingLeft(), topPadding,headView.getPaddingRight(), headView.getPaddingBottom());
					headView.invalidate();
					System.out.println("当前-下拉刷新PULL_To_REFRESH-TopPad："+topPadding);
				}

				// 更新headView的paddingTop
				if (state == RELEASE_To_REFRESH) {
					int topPadding = (int) ((tempY - startY - headContentHeight));
					headView.setPadding(headView.getPaddingLeft(), topPadding, headView.getPaddingRight(),headView.getPaddingBottom());
					headView.invalidate();
					System.out.println("当前-释放刷新RELEASE_To_REFRESH-TopPad："+topPadding);
				}
			}
			break;
		}
		return super.onTouchEvent(event);
	}

	// 当状态改变时候，调用该方法，以更新界面
	private void changeHeaderViewByState() {
		switch (state) {
		case RELEASE_To_REFRESH:

			arrowImageView.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			arrowImageView.clearAnimation();
			arrowImageView.startAnimation(animation);

			tipsTextview.setText("松开可以刷新");

			// Log.v(TAG, "当前状态，松开刷新");
			break;
		case PULL_To_REFRESH:

			progressBar.setVisibility(View.GONE);
			tipsTextview.setVisibility(View.VISIBLE);
			lastUpdatedTextView.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.VISIBLE);
			if (isBack) {
				isBack = false;
				arrowImageView.clearAnimation();
				arrowImageView.startAnimation(reverseAnimation);
			}
			tipsTextview.setText("下拉可以刷新");

			// Log.v(TAG, "当前状态，下拉刷新");
			break;

		case REFRESHING:
			System.out.println("刷新REFRESHING-TopPad："+headContentOriginalTopPadding);
			headView.setPadding(headView.getPaddingLeft(),
					headContentOriginalTopPadding, headView.getPaddingRight(),
					headView.getPaddingBottom());
			headView.invalidate();

			progressBar.setVisibility(View.VISIBLE);
			arrowImageView.clearAnimation();
			arrowImageView.setVisibility(View.GONE);
			tipsTextview.setText("正在刷新...");
			lastUpdatedTextView.setVisibility(View.GONE);

			// Log.v(TAG, "当前状态,正在刷新...");
			break;
		case DONE:
			System.out.println("完成DONE-TopPad："+(-1 * headContentHeight));
			headView.setPadding(headView.getPaddingLeft(), -1 * headContentHeight, headView.getPaddingRight(), headView.getPaddingBottom());
			headView.invalidate();

			progressBar.setVisibility(View.GONE);
			arrowImageView.clearAnimation();
			// 此处更换图标
			arrowImageView.setImageResource(R.drawable.icon_head_arrow);

			tipsTextview.setText("下拉可以刷新");
			lastUpdatedTextView.setVisibility(View.VISIBLE);

			// Log.v(TAG, "当前状态，done");
			break;
		}
	}

	public void addChild(View child, int position) {
		innerLayout.addView(child, position);
	}
	
	// 点击刷新
	public void clickRefresh() {
		state = REFRESHING;
		changeHeaderViewByState();
		onRefresh();
	}

	public void setOnRefreshListener(OnRefreshListener refreshListener) {
		this.refreshListener = refreshListener;
	}
	
	public interface OnRefreshListener {
		public void onRefresh();
	}

	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());//年月日 时分秒 格式
	public String getTime(String time) {
		return df.format(new Date(Long.parseLong(time)));
	}
	
	public void onRefreshComplete(String update) {
		lastUpdatedTextView.setText("最近更新:" + getTime(update));
		onRefreshComplete();
	}

	public void onRefreshComplete() {
		state = DONE;
		changeHeaderViewByState();
	}

	private void onRefresh() {
		if (refreshListener != null) {
			refreshListener.onRefresh();
		}
	}

	// 计算headView的width及height值
	private void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;
		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

}