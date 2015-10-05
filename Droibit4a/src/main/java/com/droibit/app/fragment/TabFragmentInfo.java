package com.droibit.app.fragment;

import android.os.Bundle;

import com.droibit.utils.Debug;

/**
 * フラグメントの情報を格納する。<br>
 * タブ名も保持する。<br>
 * <br>
 * このクラスは{@link android.support.v4.app.Fragment}に対応している。
 * 
 * @author kumagai
 *
 */
public class TabFragmentInfo extends FragmentInfo {

	/** タブ名 */
	public String tabName;

	/**
	 * コンストラクタ
	 * 
	 * @param clazz フラグメントのクラス
	 * @param args フラグメントの引数のバンドル
	 * @param tabName タブ名
	 */
	public TabFragmentInfo(Class<?> clazz, Bundle args, String tabName) {
		super(clazz, args);

		Debug.assertNotNull(tabName);
		this.tabName = tabName;
	}
}
