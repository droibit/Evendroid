package com.droibit.collection;

import java.io.Serializable;

/**
 * 整数値のペア
 * 
 * @author Shinya Kumagai
 * 
 */
public final class IntPair implements Cloneable, Serializable {

	/** 空のオブジェクト */
	public static final IntPair EMPTY;
	
	/** シリアルID */
	private static final long serialVersionUID = -341769151115501628L;

	/** 値1 */
	public int first;

	/** 値2 */
	public int second;
	
	/**
	 * 静的コンストラクタ
	 */
	static {
		EMPTY = new IntPair();
	}

	/**
	 * コンストラクタ
	 */
	public IntPair() {
		this(0, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param first 値1
	 * @param second 値2
	 */
	public IntPair(int first, int second) {
		set(first, second);
	}

	/**
	 * 値を設定する
	 * 
	 * @param first 値1
	 * @param second 値2
	 */
	public void set(int first, int second) {
		this.first = first;
		this.second = second;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		return first + (second << 8);
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}

		if (o instanceof IntPair) {
			final IntPair other = (IntPair) o;
			return (first == other.first) && (second == other.second);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public IntPair clone() {
		try {
			return (IntPair) super.clone();
		} catch (CloneNotSupportedException e) {
			// we do support clone.
			throw new AssertionError();
		}
	}
}