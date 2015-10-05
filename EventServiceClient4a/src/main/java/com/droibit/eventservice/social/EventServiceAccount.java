/**
 * 
 */
package com.droibit.eventservice.social;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;

import com.droibit.eventservice.events.EventServices;

/**
 * イベント支援サービスユーザのアカウント情報を格納するクラス。<br>
 * イベント支援サービスのユーザページURLは{@link #getAllAcounts()}でSNSアカウントを取得した際に得られる。
 *
 * @author kumagai
 *
 */
public class EventServiceAccount implements Serializable {

    /** シリアルID */
    private static final long serialVersionUID = -2819216491146226887L;

    /** 外部のSNSアカウント */
	private List<SocialAccount> snsAccounts;

	/** イベント支援サービスの種類 */
	private EventServices serviceType;
		
	/** ユーザID */
	private String userId;
	
	/** ユーザ名 */
	private String name;
	
	/**
	 * 新しいコミットを作成する
	 * 
	 * @param serviceType イベント支援サービスの種類
	 */
	public EventServiceAccount(EventServices serviceType) {
		this.serviceType = serviceType;
		this.snsAccounts = new ArrayList<SocialAccount>(5);
	}
	
	/**
	 * 外部のSNSアカウントを追加する
	 * 
	 * @param account SNSアカウント
	 */
	public void addSocialAccount(SocialAccount account) {
		if (account == null) {
			throw new IllegalArgumentException("SNSアカウントが存在しません");
		}
		snsAccounts.add(account);
	}
	
	/**
	 * イベント支援サービスのソーシャルアカウントを含む全アカウント情報を取得する。
	 * イベント支援サービスのアカウントはリストの先頭になる。
	 * 
	 * @return 全アカウント情報
	 */
	public List<SocialAccount> getAllAcounts() {
		final SocialAccount account = SocialAccount.valueOf(this);
		final List<SocialAccount> allAcounts = new ArrayList<SocialAccount>(snsAccounts);
		
		// イベント支援サービスのアカウントを先頭にする
		allAcounts.add(0, account);
		
		return allAcounts;
	}
	
	/**
	 * 外部SNSのアカウントを取得する
	 * 
	 * @return 外部SNSのアカウントリスト
	 */
	List<SocialAccount> getSocialNetworkServiceAccounts() {
		return snsAccounts;
	}

	/**
	 * ユーザ名を取得する
	 * 
	 * @return ユーザ名
	 */
	public String getName() {
		return name;
	}

	/**
	 * ユーザ名を設定する
	 * 
	 * @param name ユーザ名
	 */
	public void setName(String name) {
		if (TextUtils.isEmpty(name)) {
			throw new IllegalArgumentException("ユーザIDが存在しません");
		}
		this.name = name;
	}

	/**
	 * ユーザIDを取得する
	 * 
	 * @return ユーザID
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * ユーザIDを設定する
	 * 
	 * @param userId ユーザID
	 */
	public void setUserId(String userId) {
		if (TextUtils.isEmpty(userId)) {
			throw new IllegalArgumentException("ユーザIDが空です");
		}
		this.userId = userId;
	}

	/**
	 * イベント支援サービスの種類を取得する
	 * 
	 * @return イベント支援サービスの種類
	 */
	public EventServices getServiceType() {
		return serviceType;
	}
}
