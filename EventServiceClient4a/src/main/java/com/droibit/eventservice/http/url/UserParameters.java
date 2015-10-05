/**
 * 
 */
package com.droibit.eventservice.http.url;

/**
 * イベント支援サービスの出欠席リクエストURLパラメータキー。<br>
 * ATND, Zussar のみ利用可能。
 *
 * @author kumagai
 *
 */
public enum UserParameters implements IParameterKey {
	EVENT_ID("event_id"),
	USER_ID("user_id"),
	NICKNAME("nickname"),
	TWITTER_ID("twitter_id"),	// ATND
	OWNER_ID("owner_id"),
	OWNER_NICKNAME("owner_nickname"),
	OWNER_TWITTER_ID("owner_twitter_id"),	// ATND
	START("start"),
	ORDER("order"),
	COUNT("count"),
	FORMAT("format");
	
	/** URLパラメータのキー値 */
	private final String key;
	
	/**
	 * プライベートコンストラクタ
	 * 
	 * @param key URLパラメータのキー値
	 */
	private UserParameters(String key) {
		this.key = key;
	}
	
	/** {@inheritDoc} */
	public String getKey() {
		return key;
	}
}
