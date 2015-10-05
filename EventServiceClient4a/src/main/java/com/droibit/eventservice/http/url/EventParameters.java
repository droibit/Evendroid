/**
 * 
 */
package com.droibit.eventservice.http.url;

/**
 * イベント支援サービスのリクエストURLパラメータキー
 *
 * @author kumagai
 *
 */
public enum EventParameters implements IParameterKey {
	EVENT_ID("event_id"),
	KEYWORD("keyword"),
	KEYWORD_OR("keyword_or"),
	YM("ym"),
	YMD("ymd"),
	USER_ID("user_id"),
	NICKNAME("nickname"),
	TWITTER_ID("twitter_id"),	// ATND, eventATND
	FACEBOOK_ID("facebook_id"),	// eventATND
	GOOGLE_PLUS_ID("google_plus_id"),	// eventATND
	OWNER_ID("owner_id"),
	OWNER_NICKNAME("owner_nickname"),
	OWNER_TWITTER_ID("owner_twitter_id"),	// ATND, eventATND
	OWNER_FACEBOOK_ID("owner_facebook_id"),	// eventATND
	OWNER_GOOGLE_PLUS_ID("owner_google_plus_id"),	// eventATND
	SERIES_ID("series_id"),	// connpass
	CATEGORY("category"),	// eventATND
	CATEGORY_DETAIL("category_detail"),	// eventATND
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
	private EventParameters(String key) {
		this.key = key;
	}
	
	/** {@inheritDoc} */
	public String getKey() {
		return key;
	}
}
