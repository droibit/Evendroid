/**
 * 
 */
package com.droibit.eventservice.http.url;

import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.social.SocialNetworkServices;

/**
 * Webサービスのホスト名の列挙体
 *
 * @author kumagai
 *
 */
public enum UrlHosts {
	TWITTER("twitter.com", null),
	FACEBOOK("facebook.com", null),
	GOOGLE_PLUS("plus.google.com", null),
	ATND("atnd.org", "api.atnd.org"),
	CONNPASS("connpass.com", "connpass.com"),
	ZUSAAR("www.zusaar.com", "www.zusaar.com");
	
	
	/** ホスト名 */
	private final String host;

    /** WebAPIを使用するためのホスト */
    private final String apiHost;
	
	/**
	 * プライベートコンストラクタ
	 * 
	 * @param host ホスト名
	 */
	private UrlHosts(String host, String apiHost) {
		this.host = host;
        this.apiHost = apiHost;
	}
	
	/**
	 * ホスト名を取得する
	 * 
	 * @return ホスト名
	 */
	public String getHost() {
		return host;
	}

    /**
     * WebAPIを実行するためのホスト名を取得する
     *
     * @return ホスト名
     */
    public String getApiHost() { return apiHost; }
	
	/**
	 * {@link SocialNetworkServices}からサービスのホスト名を取得する
	 * 
	 * @param serviceType SNSの種類
	 * @return サービスのホスト名
	 */
	public static final UrlHosts from(SocialNetworkServices serviceType) {
		switch (serviceType) {
		case TWITTER:
			return UrlHosts.TWITTER;
		case FACEBOOK:
			return UrlHosts.FACEBOOK;
		case GOOGLE_PLUS:
			return UrlHosts.GOOGLE_PLUS;
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * {@link EventServices}からサービスのホスト名を取得する
	 * 
	 * @param serviceType イベント支援サービスの種類
	 * @return サービスのホスト名
	 */
	public static final UrlHosts from(EventServices serviceType) {
		switch (serviceType) {
		case ATND:
			return UrlHosts.ATND;
		case CONNPASS:
			return UrlHosts.CONNPASS;
		case ZUSAAR:
			return UrlHosts.ZUSAAR;
		default:
			throw new IllegalArgumentException();
		}
	}
}
