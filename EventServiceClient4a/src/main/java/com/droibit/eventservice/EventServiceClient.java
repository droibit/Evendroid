/**
 * 
 */
package com.droibit.eventservice;

import android.content.Context;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IMappable;

/**
 * イベント支援サービスのクライアント
 *
 * @author kumagai
 *
 */
public class EventServiceClient implements IEventServiceClient {
	
	/** ログに出力するためのタグ */
	private static final String TAG = EventServiceClient.class.getSimpleName();

	/** リクエストキュー */
	private RequestQueue mRequestQueue;

	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param context コンテキスト
	 */
	public EventServiceClient(Context context) {
		mRequestQueue = Volley.newRequestQueue(context);
	}

	/** {@inheritDoc} */
	@Override
	public <T extends IMappable> void load(EventRequest<T> request) {
		Log.d(TAG, String.format("Request Uri : %s", request.getUrl()));
		mRequestQueue.add(request);
		mRequestQueue.start();
	}
	
	/** {@inheritDoc} */
	@Override
	public void stop() {
		mRequestQueue.stop();
	}
}
