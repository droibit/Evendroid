package com.droibit.collection;

/**
 * キーと値のペアクラス
 * 
 * @author kumagaishinya
 *
 */
public class KeyValuePair<K, V> implements Cloneable {

	/** キー */
	public K key;

	/** 値 */
	public V value;

	/**
	 * コンストラクタ
	 */
	public KeyValuePair() {
	}

	/**
	 * コンストラクタ
	 * 
	 * @param key キー値
	 * @param value  キーに対応する値
	 */
	public KeyValuePair(K key, V value) {
		set(key, value);
	}

	/**
	 * キーと値を設定する
	 * 
	 * @param key キー値
	 * @param value  キーに対応する値
	 */
	public void set(K key, V value) {
		this.key = key;
		this.value = value;
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (!(o instanceof KeyValuePair)) {
			return false;
		}

		final KeyValuePair<K, V> other;
		try {
			other = (KeyValuePair<K, V>) o;
		} catch (ClassCastException e) {
			return false;
		}
		return key.equals(other.key) && value.equals(other.value);
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int result = 17;
		result = 31 * result + key.hashCode();
		result = 31 * result + value.hashCode();
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return String.format("dialogKey: %s, value: %s", key.toString(), value.toString());
	}

	/** {@inheritDoc} */
	@SuppressWarnings("unchecked")
	@Override
	public KeyValuePair<K, V> clone() {
		try {
			return (KeyValuePair<K, V>) super.clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError(e.getMessage());
		}
	}

	/**
	 * 新しいペアを作成するためのコンビニエンスメソッド
	 * 
	 * @return ペアオブジェクト
	 */
	public static final <K, V> KeyValuePair<K, V> newPair() {
		return new KeyValuePair<K, V>();
	}
}
