/**
 * 
 */
package com.droibit.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

/**
 * 親ビューからタッチ操作可能なWebビュー
 * 
 * @author kumagaishinya
 * 
 */
public class TouchableWebView extends WebView {

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public TouchableWebView(Context context) {
		super(context);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public TouchableWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public TouchableWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		requestDisallowInterceptTouchEvent(true);
		return super.onTouchEvent(event);
	}
}
