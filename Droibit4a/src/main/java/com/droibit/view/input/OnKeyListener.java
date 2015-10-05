package com.droibit.view.input;

import android.view.KeyEvent;

/**
 * キー入力イベントが発生したときに呼ばれるイベントリスナ
 * 
 * @author kumagaishinya
 *
 */
public interface OnKeyListener {

	/**
	 * キーが押下されたときに呼ばれるイベントリスナ
	 * 
	 * @param keyCode キーコード
	 * @param event キーイベント
	 * @return 
	 */
	public boolean onKeyDown(int keyCode, KeyEvent event);
	
	/**
	 * キー押下が解放されたときに呼ばれるイベントリスナ
	 * 
	 * @param keyCode キーコード
	 * @param event キーイベント
	 * @return
	 */
	public boolean onKeyUp(int keyCode, KeyEvent event);
}
