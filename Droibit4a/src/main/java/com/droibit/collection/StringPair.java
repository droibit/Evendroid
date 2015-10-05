package com.droibit.collection;

import java.io.Serializable;


/**
 * 文字列のキーと値のペアクラス
 * 
 * @author kumagaishinya
 * 
 */
public class StringPair extends KeyValuePair<String, String> implements Serializable {

	/** シリアルID */
	private static final long serialVersionUID = 3283058333167394535L;

	/**
	 * コンストラクタ
	 */
	public StringPair() {
	}

	/**
	 * コンストラクタ
	 * 
	 * @param key キー値
	 * @param value  キーに対応する値
	 */
	public StringPair(String key, String value) {
		super(key, value);
	}
}
