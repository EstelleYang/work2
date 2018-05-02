package com.lzjs.uappoint.myview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnDrawListener;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lzjs.uappoint.R;

public class CollapsibleTextView extends LinearLayout {
	private TextView contentTextView;
	/** 折叠高度：达到此行数后开始折叠 */
	private final static int collapsedLineCount = 5;
	/** 是否需要折叠 */
	private boolean expandable = false;
	/** 可点击的切换按钮 */
	private LinearLayout toggle;
	private TextView toggleTextView;
	private ImageView toggleImageView;
	/** 当前显示文本的行数 */
	private int lineCount;

	public CollapsibleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(
				R.layout.collapsible_textview, this);
		contentTextView = (TextView) view.findViewById(R.id.content_textview);
		toggle = (LinearLayout) view.findViewById(R.id.toggle_layout);
		toggleImageView = (ImageView) view.findViewById(R.id.toggle_imageview);
		toggleTextView = (TextView) view.findViewById(R.id.toggle_textview);
		toggle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				tipTextViewOnClick();
			}
		});
		contentTextView.setMaxLines(collapsedLineCount);
		initExpandable();
		getLineCount();
	}

	/** 在此实现对contentTextView的显示控制 */
	private void tipTextViewOnClick() {
		// TODO Auto-generated method stub
		if (lineCount <= collapsedLineCount) {
			return;
		}
		Animation heightAnimation = null;
		if (expandable) {
			// 点击全文的时候
			heightAnimation = new TextViewHeightAnimation(contentTextView,
					getExpandedHeight());
			toggleTextView.setText("收起");
			toggleImageView.setBackgroundResource(R.drawable.expand_up);
			contentTextView.setMaxLines(lineCount);
		} else {
			// 点击收起的时候
			heightAnimation = new TextViewHeightAnimation(contentTextView,
					getCollapsedHeight());
			toggleTextView.setText("全文");
			toggleImageView.setBackgroundResource(R.drawable.expand_down);
			contentTextView.setMaxLines(collapsedLineCount);
		}
		if (heightAnimation != null) {
			heightAnimation.setFillAfter(true);
			clearAnimation();
			startAnimation(heightAnimation);
		}
		expandable = !expandable;
	}

	/** 初始化expandable */
	private void initExpandable() {
		// TODO Auto-generated method stub
		String content = toggleTextView.getText().toString();
		if (!("全文".equals(content))) {
			toggleTextView.setText("全文");
			toggleImageView.setBackgroundResource(R.drawable.expand_down);
			expandable = true;
		}
	}

	/** 获取contentTextView的最大行数 */
	private void getLineCount() {
		contentTextView.post(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				ViewTreeObserver observer = contentTextView.getViewTreeObserver();
				observer.addOnDrawListener(new OnDrawListener() {

					@Override
					public void onDraw() {
						// TODO Auto-generated method stub
						lineCount = contentTextView.getLineCount();
						initContentTextView();
					}
				});
			}
		});
	}

	/** 初始化contentTextView */
	private void initContentTextView() {
		// TODO Auto-generated method stub
		if (lineCount == 0) {
			return;
		}
		if (lineCount > collapsedLineCount) {
			if (toggle.getVisibility() != VISIBLE) {
				toggle.setVisibility(VISIBLE);
			}
			contentTextView.setMaxLines(collapsedLineCount);
		} else {
			if (toggle.getVisibility() != GONE) {
				toggle.setVisibility(GONE);
			}
		}
	}

	/** 获取折叠时的文本高度 */
	private int getCollapsedHeight() {
		// TODO Auto-generated method stub
		int textHeight;
		if (lineCount > collapsedLineCount) {
			textHeight = contentTextView.getLayout().getLineTop(
					collapsedLineCount);
		} else {
			textHeight = contentTextView.getLayout().getLineTop(lineCount);
		}
		int padding = contentTextView.getCompoundPaddingTop()
				+ contentTextView.getCompoundPaddingBottom();
		return textHeight + padding;
	}

	/** 获取全显示的文本高度 */
	private int getExpandedHeight() {
		int textHeight = contentTextView.getLayout().getHeight();
		int padding = contentTextView.getCompoundPaddingTop()
				+ contentTextView.getCompoundPaddingBottom();
		return textHeight + padding;
	}

	/** 设置contentTextView的显示文本 */
	public void setText(String text) {
		// TODO Auto-generated method stub
		if (text == null) {
			return;
		}
		if (text.length() == 0) {
			setVisibility(GONE);
		} else {
			setVisibility(VISIBLE);
			contentTextView.setText(text);
		}
	}
}

