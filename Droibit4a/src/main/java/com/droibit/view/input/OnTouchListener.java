package com.droibit.view.input;

import android.app.Activity;
import android.app.Fragment;
import android.view.MotionEvent;

/**
 * タッチイベントが発生した時に実行されるイベントリスナ。<br>
 * <br>
 * このイベントリスナは主に{@link Fragment}がタッチイベントを取得する場合に使用する。<br>
 * フラグメント上に存在するウィジェットはタッチイベントを取得することができないので、fragmentから伝える必要がある。
 * また、fragmenは{@link Activity#onTouchEvent(MotionEvent)}内でイベントをハンドルすることができる。
 * 
 * @author kumagai
 *
 */
public interface OnTouchListener {

	/**
	 * タッチイベントが発生した時に呼ばれる処理
	 * 
	 * @param event タッチイベント
	 */
	public boolean onTouchEvent(MotionEvent event);
}
