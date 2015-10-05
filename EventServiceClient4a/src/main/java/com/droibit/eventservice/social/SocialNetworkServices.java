/**
 * 
 */
package com.droibit.eventservice.social;


/**
 * イベント支援サービスと連携するSNSサービスの種類
 * 
 * @author kumagai
 * 
 */
public enum SocialNetworkServices {
	TWITTER("Twitter"), FACEBOOK("Facebook"), GOOGLE_PLUS("Google+");

	/** イベント支援サービス名 */
	private final String name;

	/***
	 * プライベートコンストラクタ
	 * 
	 * @param name サービス名
	 */
	SocialNetworkServices(String name) {
		this.name = name;
	}

	/**
	 * SNS名を取得する
	 * 
	 * @return SNS名
	 */
	public final String getName() {
		return name;
	}

	/**
	 * サービス名からサービスの種類を取得する
	 * 
	 * @param name サービス名
	 * @return サービスの種類
	 */
	public static SocialNetworkServices from(String name) {
		for (SocialNetworkServices type : values()) {
			if (type.name.equals(name)) {
				return type;
			}
		}
		throw new IllegalArgumentException();
	}
}
