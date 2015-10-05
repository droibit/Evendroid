package com.droibit.utils;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * 
 * @author kumagai
 * 
 */
public class Debug {

	private static final String TAG_DEBUG = "Debug";

	/** ログ表示のためのタグ */
	private static final String TAG = Debug.class.getSimpleName();

	/**
	 * デバッグログを出力する
	 * 
	 * @param msg メッセージ
	 */
	public static final void log(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			Log.d(TAG_DEBUG, msg);
		}
	}

	/**
	 * 
	 * デバッグログを出力する
	 * 
	 * @param format 
	 * @param args 
	 */
	public static final void log(String format, Object... args) {
		if (args.length == 0) {
			Log.d(TAG, format);
		} else {
			Log.d(TAG, String.format(format, args));
		}
	}

	/**
	 * フラグが真ではない場合にアサートエラーをスローする
	 * 
	 * @param cond 条件式
	 */
	public static final void assertTrue(boolean cond) {
		if (!cond) {
			throw new AssertionError();
		}
	}

	/**
	 * フラグが真ではない場合にアサートエラーをスローする
	 * 
	 * @param cond 条件式
	 * @param message 
	 * @param args
	 */
	public static final void assertTrue(boolean cond, String message, Object... args) {
		if (!cond) {
			throw new AssertionError(TextUtils.isEmpty(message) ? message
					: String.format(message, args));
		}
	}

	/**
	 * オブジェクトがnullの場合にアサートエラーをスローする
	 * 
	 * @param object 対象のオブジェクト
	 */
	public static void assertNotNull(Object object) {
		if (object == null) {
			throw new AssertionError();
		}
	}

	/**
	 * オブジェクトがnullの場合にアサートエラーをスローする
	 * 
	 * @param object 対象のオブジェクト
	 * @param message
	 * @param args
	 */
	public static final void assertNotNull(Object object, String message, Object... args) {
		if (object == null) {
			throw new AssertionError(TextUtils.isEmpty(message) ? message
					: String.format(message, args));
		}
	}

	/**
	 * ビューのツリー構造をログに出力する
	 * 
	 * @param v ビュー
	 * @param padding 階層の深さを表すための幅
	 */
	public static void dumpViewTree(View v, String padding) {
		Log.d(TAG, padding + v.getClass().getName());
		if (v instanceof ViewGroup) {
			final ViewGroup g = (ViewGroup) v;
			for (int i = 0; i < g.getChildCount(); i++) {
				dumpViewTree(g.getChildAt(i), padding + " ");
			}
		}
	}
}
