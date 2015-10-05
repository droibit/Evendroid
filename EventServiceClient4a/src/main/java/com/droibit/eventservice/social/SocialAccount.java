/**
 * 
 */
package com.droibit.eventservice.social;

import android.net.Uri;

import com.droibit.eventservice.http.url.WebPage;

import java.io.Serializable;

/**
 * ソーシャルなアカウント情報を格納するクラス。
 * イベント支援サービスおよび外部サービスのアカウント情報。
 *
 * @author kumagai
 *
 */
public class SocialAccount implements Serializable {

    /** シリアルID */
    private static final long serialVersionUID = 1443099484542187762L;

    /** サービス名 */
	private String mServiceName;

	/** ユーザページのURI */
	private Uri mUserUri;

	/**
	 * サービス名を取得する
	 * 
	 * @return サービス名
	 */
	public String getServiceName() {
		return mServiceName;
	}

	/**
	 * ユーザページのURIを取得する
	 * 
	 * @return ユーザページのURI
	 */
	public Uri getUserUri() {
		return mUserUri;
	}
	
	/**
	 * 新しいインスタンスを作成する
	 */
	public SocialAccount() {
		this("", null);
	}
	
	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param serviceName ソーシャルサービス名
	 * @param userUri ユーザページのURI
	 */
	public SocialAccount(String serviceName, Uri userUri) {
		this.mServiceName = serviceName;
		this.mUserUri = userUri;
	}
	
	/**
	 * {@link EventServiceAccount}から{@link SocialAccount}オブジェクトを作成する
	 * 
	 * @param account イベント支援サービスのアカウント
	 * @return {@link SocialAccount}オブジェクト
	 */
	public static final SocialAccount valueOf(EventServiceAccount account) {
		final SocialAccount socialAccount = new SocialAccount();
		socialAccount.mServiceName = account.getServiceType().getName(); 
		socialAccount.mUserUri = WebPage.getSocialPageUri(account.getServiceType(), account.getUserId());
		
		return socialAccount;
	}
}
