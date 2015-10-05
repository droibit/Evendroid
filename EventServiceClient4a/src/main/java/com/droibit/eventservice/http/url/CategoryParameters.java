/**
 * 
 */
package com.droibit.eventservice.http.url;

/**
 * カテゴリ検索レスポンスのキー
 *
 * @author kumagai
 *
 */
public enum CategoryParameters implements IParameterKey  {
	FORMAT("format");
	
	/** URLパラメータのキー値 */
	private final String key;
	
	/**
	 * プライベートコンストラクタ
	 * 
	 * @param key URLパラメータのキー値
	 */
	private CategoryParameters(String key) {
		this.key = key;
	}
	
	/** {@inheritDoc} */
	public String getKey() {
		return key;
	}
}
