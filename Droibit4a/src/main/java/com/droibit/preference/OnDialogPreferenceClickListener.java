package com.droibit.preference;

import android.preference.Preference;

/**
 * ダイアログプレファレンスのボタンがクリックされた時に呼ばれるイベントリスナ
 * 
 * @author kumagaishinya
 * 
 */
public interface OnDialogPreferenceClickListener {

	/**
	 * ダイアログのボタンがクリックされた時に呼ばれる処理
	 *
     * @param preference 呼び出し元のプレファレンスプレファレンス
	 * @param positiveResult クリックされたのボタン種類
	 */
	public void onDialogPreferenceClick(Preference preference, boolean positiveResult);
}
