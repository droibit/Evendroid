/**
 * 
 */
package com.droibit.eventservice.http;


/**
 * レスポンスの文字列をクラスに変換するためのパーサインターフェース
 * @param <T> パースするクラスの型
 *
 * @author kumagai
 *
 */
interface IResponseParser<T extends IMappable> {

	/**
	 * レスポンスの文字列からクラスを作成する
	 * 
	 * @param clazz パースするクラス
	 * @param contents 文字列のレスポンス
	 * @return パースしたオブジェクト
	 */
	public T parse(Class<T> clazz, String contents); 
}
