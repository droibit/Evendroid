package com.droibit.eventservice;

import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IMappable;

/**
 * イベント支援サービスからイベントを取得するためのクライアントインターフェース
 *
 * @author kumagai
 */
public interface IEventServiceClient {

	/**
	 * イベント支援サービスへリクエストを送る
	 * @param <T> レスポンスの型
	 * 
	 * @param request HTTPリクエスト
     * @return レスポンスからパースしたオブジェト
	 */
	<T extends IMappable> void load(EventRequest<T> request);
	
	/**
	 * リクエストをキャンセルする
	 */
	void stop();
}
