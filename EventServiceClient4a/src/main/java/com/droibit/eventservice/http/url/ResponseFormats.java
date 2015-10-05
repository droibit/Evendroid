/**
 * 
 */
package com.droibit.eventservice.http.url;

/**
 * レスポンス形式の種類の列挙体
 *
 * @author kumagai
 *
 */
public enum ResponseFormats implements IParameterKey {
	JSON("json"), XML("xml"), JSONP("jsonp");
	
	/** URLパラメータのキー値 */
	private final String key;
	
	/**
	 * プライベートコンストラクタ
	 * 
	 * @param key URLパラメータのキー値
	 */
	private ResponseFormats(String key) {
		this.key = key;
	}
	
	/** {@inheritDoc} */
	public String getKey() {
		return key;
	}
	
	/**
	 * 文字列から{@link ResponseFormats}を作成する
	 * 
	 * @param format レスポンス形式の文字列
	 * @return レスポンス形式の列挙体
	 */
	public static final ResponseFormats from(String format) {
		for (ResponseFormats value : values()) {
			if (value.getKey().equals(format)) {
				return value;
			}
		}
		throw new IllegalArgumentException();
	}
}
