/**
 * 
 */
package com.droibit.eventservice.http;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

/**
 * レスポンスを呼び出し元に返すためのクラス
 *
 * @author kumagai
 *
 */
public abstract class ResponseCallback<T extends IMappable> implements Listener<T>, ErrorListener {

	/** レスポンスをパースするクラス */
	private Class<T> mClazz;
		
	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param clazz レスポンスからパースするクラス
	 */
	public ResponseCallback(Class<T> clazz) {
		if (clazz == null) {
			throw new IllegalArgumentException("クラス情報が存在しません");
		} 
		mClazz = clazz;
	}
	
	/**
	 * レスポンスをパースするクラスを取得する
	 * 
	 * @return レスポンスからパースするクラス
	 */
	public Class<T> getClazz() {
		return mClazz;
	}
}
