package com.droibit.utils;

/**
 * インスタンスがnullかどうか調べるためのユーティリティクラス<br>
 * <br>
 * nullが許容されない場合は{@link Debug#assertNotNull(Object)}メソッドを使用する
 * 
 * @author kumagai
 *
 */
public final class NullCheck {
	
	private NullCheck() {}

	/**
	 * インスタンスがnullかどうか
	 * 
	 * @param target チェック対象のインスタンス
	 * @return trueの場合はインスタンスがnull、falseの場合はnullではない
	 */
	public static final boolean isNull(Object target) {
		return target == null;
	}
	
	/**
	 * インスタンスがnullではないかどうか
	 * 
	 * @param target チェック対象のインスタンス
	 * @return trueの場合はインスタンスがnullではない、falseの場合はnull
	 */
	public static final boolean isNotNull(Object target) {
		 return target != null;
	}
}
