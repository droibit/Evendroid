/**
 * 
 */
package com.droibit.eventservice.http;

import com.droibit.eventservice.http.url.ResponseFormats;
import com.google.gson.Gson;

/**
 *
 *
 * @author kumagai
 *
 */
final class ResponseParser {
	
	/**
	 * 
	 * @param type
	 * @return
	 */
	public static final <T extends IMappable> IResponseParser<T> from(ResponseFormats format) {
		switch (format) {
		case JSON:
			return new JsonResponseParser<T>();
		case XML:
			return new XmlResponseParser<T>();
		case JSONP:
			throw new UnsupportedOperationException("サポートされていないフォーマットです");
		}
		throw new IllegalArgumentException("サポートされていないフォーマットです");
	}

	/**
	 * JSON形式のレスポンスの文字列をクラスに変換するためのパーサ
	 * @param <T> パースするクラスの型
	 *
	 * @author kumagai
	 *
	 */
	public static final class JsonResponseParser<T extends IMappable> implements IResponseParser<T> {

	    private static final Gson gson;
	    
	    /**
	     * スタティックコンストラクタ
	     */
	    static {
	    		gson = new Gson();
	    }
		
		/** {@inheritDoc} */
		@Override
		public T parse(Class<T> clazz, String contents) {
			return gson.fromJson(contents, clazz);
		}
	}
	
	/**
	 * XML形式のレスポンスの文字列をクラスに変換するためのパーサ
	 * @param <T> パースするクラスの型
	 *
	 * @author kumagai
	 *
	 */	
	public static final class XmlResponseParser<T extends IMappable> implements IResponseParser<T> {
		
		/** {@inheritDoc} */
		@Override
		public T parse(Class<T> clazz, String contents) {
			return null;
		}		
	}
}
