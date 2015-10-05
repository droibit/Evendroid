package com.droibit.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.CollapsibleActionView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.FrameLayout;

import com.droibit.widget.ToastManager;

/**
 * アクションビューのベースクラス
 * 
 * @author kumagai
 *
 */
public abstract class ActionView extends FrameLayout implements CollapsibleActionView,
	OnClickListener, OnLongClickListener {

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public ActionView(Context context) {
		this(context, null);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public ActionView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public ActionView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onLongClick(View v) {
		// アクションビューが長押しされた場合、トーストを表示する
		final CharSequence description = getContentDescription();
		if (!TextUtils.isEmpty(description)) {
			ToastManager.showShort(getContext(), description);
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void onActionViewExpanded() {
	}

	/** {@inheritDoc} */
	@Override
	public void onActionViewCollapsed() {
	}
}
