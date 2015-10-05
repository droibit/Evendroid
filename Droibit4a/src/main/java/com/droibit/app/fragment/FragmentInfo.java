package com.droibit.app.fragment;

import android.os.Bundle;
import android.app.Fragment;

import com.droibit.utils.Debug;

/**
 * フラグメントの情報を格納する。<br>
 * このクラスは{@link Fragment}に対応している。
 * 
 * @author kumagai
 *
 */
public class FragmentInfo {

	/** フラグメントのクラス */
	public Class<?> clazz;

	/** フラグメントに渡すた引数のバンドル */
	public Bundle args;

	/** 本体となるフラグメント */
	public Fragment fragment;

	/**
	 * コンストラクタ
	 * 
	 * @param clazz フラグメントのクラス
	 * @param args フラグメントに渡すた引数のバンドル
	 */
	public FragmentInfo(Class<?> clazz, Bundle args) {
		Debug.assertNotNull(clazz);

		this.clazz = clazz;
		this.args = args;
	}
}
