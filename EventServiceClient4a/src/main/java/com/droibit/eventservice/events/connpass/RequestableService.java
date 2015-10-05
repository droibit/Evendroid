/**
 * 
 */
package com.droibit.eventservice.events.connpass;

import static com.droibit.eventservice.http.url.UrlHosts.CONNPASS;

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
	private static final GetRequest.UrlHostPath eventUrl = new GetRequest.UrlHostPath(CONNPASS.getApiHost(), "api/v1/event/");
		
	/** {@inheritDoc} */
	@Override
	public GetRequest.UrlHostPath getUrlHostPath(RequestContents contentType) {
		if (contentType == RequestContents.EVENT) {
			return eventUrl;
		}
		return null;
	}
}
