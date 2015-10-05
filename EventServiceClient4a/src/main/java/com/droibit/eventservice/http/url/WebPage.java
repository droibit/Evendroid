/**
 * 
 */
package com.droibit.eventservice.http.url;

import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.social.SocialNetworkServices;

import android.net.Uri;


/**
 * サービスのユーザページを表示するためのヘルパークラス 
 *
 * @author kumagai
 *
 */
public final class WebPage {

	/** HTTP スキーマ */
	private static final String HTTP = "http";
	
	/** HTTPS スキーマ */
	private static final String HTTPS = "https";
	
	/**
	 * SNSのユーザページのURIを取得する
	 * 
	 * @param serviceType SNSの種類
	 * @param userId サービスのユーザID 
	 * @return ユーザページURI
	 */
	public static final Uri getSocialPageUri(SocialNetworkServices serviceType, String userId) {
		return new Uri.Builder()
                .scheme(HTTPS)
                .authority(UrlHosts.from(serviceType).getHost())
				.appendPath(userId)
                .build();
	}
	
	/**
	 * SNSのユーザページのURIを取得する
	 * 
	 * @param serviceType SNSの種類
	 * @param userId サービスのユーザID 
	 * @return ユーザページURI
	 */
	public static final Uri getSocialPageUri(EventServices serviceType, String userId) {
		return new Uri.Builder()
                .scheme(HTTP)
                .authority(UrlHosts.from(serviceType).getHost())
				.appendPath(getSocialPagePath(serviceType))
                .appendPath(userId)
                .build();
	}

    /**
     * イベントに参加ページのURIを取得する
     *
     * @param serviceType サービスの種類
     * @param eventId イベント識別ID
     * @return 参加ページのURI
     */
    public static final Uri getParticipantionUri(EventServices serviceType, String eventId) {
        return new Uri.Builder()
                .scheme(HTTP)
                .authority(UrlHosts.from(serviceType).getHost())
                .appendEncodedPath(getParticipantionPagePath(serviceType, eventId))
                .build();
    }

    /**
     * イベントに参加ページのURIを取得する
     *
     * @param serviceType サービスの種類
     * @param eventId イベント識別ID
     * @return 参加ページのURI
     */
    public static final Uri getCancelUri(EventServices serviceType, String eventId) {
        return new Uri.Builder()
                .scheme(HTTP)
                .authority(UrlHosts.from(serviceType).getHost())
                .appendEncodedPath(getCancelPagePath(serviceType, eventId))
                .build();
    }

    /**
     * サービスのログインページのURIを取得する
     *
     * @param serviceType サービスの種類
     * @return ログインページのURI
     */
    public static final Uri getLoginUri(EventServices serviceType) {
        return new Uri.Builder()
                .scheme(HTTP)
                .authority(UrlHosts.from(serviceType).getHost())
                .appendEncodedPath(getLoginPagePath(serviceType))
                .build();
    }

	private static String getSocialPagePath(EventServices serviceType) {
        switch (serviceType) {
            case ATND:
               return "users";
            case ZUSAAR:
                return "user";
            case CONNPASS:
                return "user";
            default:
                return null;
        }
	}

    private static String getParticipantionPagePath(EventServices servicType, String eventId) {
        switch (servicType) {
            case ATND:
                return String.format("events/%s/entry/new", eventId);
            case ZUSAAR:
                return String.format("event/%s", eventId);
            case CONNPASS:
                return String.format("event/%s/join", eventId);
            default:
                return null;
        }
    }

    private static String getCancelPagePath(EventServices servicType, String eventId) {
        switch (servicType) {
            case ATND:
                return String.format("events/%s/entry/cancel", eventId);
            case ZUSAAR:
                return String.format("event/%s", eventId);
            case CONNPASS:
                return String.format("event/%s", eventId);
            default:
                return null;
        }
    }

    private static String getLoginPagePath(EventServices serviceType) {
        switch (serviceType) {
            case ATND:
                return "login";
            case ZUSAAR:
                return null;
            case CONNPASS:
                return null;
            default:
                return null;
        }
    }
}
