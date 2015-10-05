/**
 * 
 */
package com.droibit.eventservice.http;

import com.droibit.eventservice.http.url.ResponseFormats;
import com.droibit.eventservice.http.url.UrlParameter;

import android.net.Uri;

/**
 * イベント情報を取得するためのHTTPリクエストインターフェース
 *
 * @author kumagai
 *
 */
public interface IGetRequest {
	
	/**
	 * リクエストURLを取得する
	 * 
	 * @return リクエストURL
	 */
	Uri getUri();
	
	/**
	 * リクエストのURLパラメータを取得する
	 * 
	 * @return リクエストの{@link UrlParameter}
	 */
	public UrlParameter getParameter();
	
	/**
	 * レスポンス形式を取得する
	 * 
	 * @return レスポンス形式の列挙体
	 */
	public ResponseFormats getResponseFormat();
	
	/**
	 * {@link IGetRequest}を作成するためのビルダインターフェース
	 *
	 * @author kumagai
	 *
	 */
	public interface Builder {
		
		/**
		 * {@link IGetRequest}を作成する
		 * 
		 * @return {@link IGetRequest}オブジェクト
		 */
		public IGetRequest build();
	}
}
