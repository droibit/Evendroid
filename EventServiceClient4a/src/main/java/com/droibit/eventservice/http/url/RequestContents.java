/**
 * 
 */
package com.droibit.eventservice.http.url;

import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.events.connpass.RequestableService;
import com.droibit.eventservice.http.GetRequest;

/**
 * リクエストする情報の種類を定義する列挙体
 *
 * @author kumagai
 */
public enum RequestContents {
	EVENT, USER, CATEGORY, NONE;
	
	/**
	 * リクエストに対応する{@link com.droibit.eventservice.http.GetRequest.UrlHostPath}を取得する
	 * 
	 * @param serviceType イベント支援サービスの種類
	 * @return リクエストに対応する{@link GetRequest.UrlHostPath}
	 */
	public final GetRequest.UrlHostPath getHostPath(EventServices serviceType) {
		switch (serviceType) {
		case ATND:
			return new com.droibit.eventservice.events.atnd.RequestableService().getUrlHostPath(this);
		case ZUSAAR:
			return new com.droibit.eventservice.events.zusaar.RequestableService().getUrlHostPath(this);
		case CONNPASS:
			return new RequestableService().getUrlHostPath(this);
		default:
			throw new IllegalArgumentException("対応していないイベント支援サービスです");
		}
	}
}
