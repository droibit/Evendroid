package com.droibit.view.input;

import android.content.Context;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.droibit.utils.SystemServices;

/**
 * 入力のユーティリティクラス
 * 
 * @author kumagai
 *
 */
public final class InputHelper {

	/**
	 * タッチされたかどうか調べる
	 * 
	 * @param event モーションイベント
	 * @return 結果
	 */
	public static final boolean isTouchDown(MotionEvent event) {
		return event.getAction() == MotionEvent.ACTION_DOWN;
	}

	/**
	 * タッチが離されたかどうか調べる
	 * 
	 * @param event モーションイベント
	 * @return 結果
	 */
	public static final boolean isTouchUp(MotionEvent event) {
		final int action = event.getAction();
		return action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP;
	}
	
	/**
	 * バックキーが押されたかどうか調べる
	 * 
	 * @param keyCode 入力されたキーコード
	 * @param event キーイベント
	 * @return 結果
	 */
	public static final boolean isKeyDownBack(int keyCode, KeyEvent event) {
		return isKeyDown(keyCode, KeyEvent.KEYCODE_BACK, event);
	}
	
	/**
	 * エンターキーが押されたかどうか調べる
	 * 
	 * @param keyCode キーコード
	 * @param event キーイベント
	 * @return 結果
	 */
	public static final boolean isKeyDownEnter(int keyCode, KeyEvent event) {
		return isKeyDown(keyCode, KeyEvent.KEYCODE_ENTER, event); 
	}
	
	/**
	 * キーが押下されたかどうか調べる
	 * 
	 * @param sourceKeyCode 入力されたキーコード
	 * @param targetKeyCode 入力されたコードと比較するキーコード
	 * @param event キーイベント
	 * @return 結果
	 */
	public static final boolean isKeyDown(int sourceKeyCode, int targetKeyCode, KeyEvent event) {
		return sourceKeyCode == targetKeyCode
				&& event.getAction() == KeyEvent.ACTION_DOWN;
	}
	
	/**
	 * キーが離されされたかどうか調べる
	 * 
	 * @param sourceKeyCode 入力されたキーコード
	 * @param targetKeyCode 入力されたコードと比較するキーコード
	 * @param event キーイベント
	 * @return 結果
	 */
	public static final boolean isKeyUp(int sourceKeyCode, int targetKeyCode, KeyEvent event) {
		return sourceKeyCode == targetKeyCode
				&& event.getAction() == KeyEvent.ACTION_UP;
	}

	/**
	 * 画面をドラッグしているかどうか調べる
	 * 
	 * @param event モーションイベント
	 * @return 結果
	 */
	public static final boolean isTouchMove(MotionEvent event) {
		return event.getAction() == MotionEvent.ACTION_MOVE;
	}
	
	/**
	 * ソフトウェアキーボードを閉じる
	 * 
	 * @param context コンテキスト
	 * @param v キーボード入力の対象のビュー
	 */
	public static final void closeSoftwareKeyboard(Context context, View v) {
		final InputMethodManager im = SystemServices.getInputMethodManager(context);
		im.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
