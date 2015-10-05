package com.droibit.utils;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Point;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

/**
 * 画面を操作するためのヘルパークラス
 * 
 * @author kumagaishinya
 *
 */
public class ScreenHelper {

	/**
	 * スクリーンをランドスケープに固定する
	 * 
	 * @param activity アクティビティ
	 */
	public static final void fixOrientationLandscape(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
	}
	
	/**
	 * スクリーンをポートレイトに固定する
	 * 
	 * @param activity アクティビティ
	 */
	public static final void fixOrientationPortrait(Activity activity) {
		activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	/**
	 * フルスクリーンで表示する
	 * 
	 * @param activity アクティビティ
	 */
	public static final void applyFullScreen(Activity activity) {
		final Window window = activity.getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		window.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		window.requestFeature(Window.FEATURE_NO_TITLE);
	}
	
	/**
	 * スクリーンのサイズを取得する。<br>
	 * このメソッドはアプリケーションのAPIレベル 以上を必要とする。
	 * 
	 * @param context コンテキスト
	 * @return スクリーンのサイズ
	 */
	@TargetApi(13)
	public static final Point getScreenSize(Context context) {
		final WindowManager wm = SystemServices.getWindowManager(context);
		final Display disp = wm.getDefaultDisplay();
		final Point size = new Point();
		disp.getSize(size);
		return size;
	}

	/**
	 * 画面サイズの種類を取得する
	 * 
	 * @param context コンテキスト
	 * @return 画面サイズの種類
	 */
	public int getScreenLayoutSize(Context context) {
		final Configuration config = context.getResources().getConfiguration();
		return config.screenLayout & SCREENLAYOUT_SIZE_MASK;
	}
}
