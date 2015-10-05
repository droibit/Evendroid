/**
 * 
 */
package com.droibit.eventservice.events.atnd;

import static com.droibit.eventservice.http.url.UrlHosts.ATND;

import com.droibit.eventservice.events.IRequestableService;
import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.url.RequestContents;

/**
 * GET リクエストする際のURL+Path部を取得するためのクラス
 *
 * @author kumagai
 *
 */
public class RequestableService implements IRequestableService {
	
	/** イベント情報のリクエストURL */
	private static final GetRequest.UrlHostPath eventUrl = new GetRequest.UrlHostPath(ATND.getApiHost(), "events/");
	
	/** 参加者情報のリクエストURL */
	private static final GetRequest.UrlHostPath userUrl = new GetRequest.UrlHostPath(ATND.getApiHost(), "events/users/");

	/** {@inheritDoc} */
	@Override
	public GetRequest.UrlHostPath getUrlHostPath(RequestContents contentType) {
		switch (contentType) {
		case EVENT:
			return eventUrl;
		case USER:
			return userUrl;
		default:
			return null;
		}
	}
}
