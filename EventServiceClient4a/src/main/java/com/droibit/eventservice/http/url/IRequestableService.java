package com.droibit.eventservice.events;

import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.url.RequestContents;

/**
 * GET リクエストする際のURL+Path部を取得するためのインタフェース。<br/>
 * 対応していないリクエストの場合はnullが返る。
 *
 * @author kumagai
 *
 */
public interface IRequestableService {

	/**
	 * コンテンツの種類に応じた{@link com.droibit.eventservice.http.GetRequest.UrlHostPath}を取得する
	 * 
	 * @param contentType コンテンツの種類
	 * @return 検索に応じた{@link GetRequest.UrlPart}
	 */
	GetRequest.UrlHostPath getUrlHostPath(RequestContents contentType);
}
