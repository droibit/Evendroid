package com.droibit.collection;

import java.io.Serializable;

/**
 * 浮動小数点値のペアクラス
 * 
 * @author kumagai
 * 
 */
public class FloatPair implements Cloneable, Serializable {

	/** 空のオブジェクト */
	public static final FloatPair EMPTY;
	
	/** シリアルID */
	private static final long serialVersionUID = 250958367236590463L;

	/** 値1 */
	public float first;

	/** 値2 */
	public float second;
	
	/**
	 * 静的コンストラクタ
	 */
	static {
		EMPTY = new FloatPair();
	}

	/**
	 * コンストラクタ
	 */
	public FloatPair() {
		this(0f, 0f);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param first 値1
	 * @param second 値2
	 */
	public FloatPair(float first, float second) {
		set(first, second);
	}

	/**
	 * 値を設定する
	 * 
	 * @param first 値1
	 * @param second 値2
	 */
	public void set(float first, float second) {
		this.first = first;
		this.second = second;
	}

	/** {@inheritDoc} */
	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}

		if (o instanceof FloatPair) {
			final FloatPair other = (FloatPair) o;
			return Float.floatToIntBits(first) == Float.floatToIntBits(other.first)
					&& Float.floatToIntBits(second) == Float.floatToIntBits(other.second);
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public int hashCode() {
		int result = 3;
		result = 31 * result + Float.floatToIntBits(first);
		result = 31 * result + Float.floatToIntBits(second);
		return result;
	}

	/** {@inheritDoc} */
	@Override
	public FloatPair clone() {
		try {
			return (FloatPair) super.clone();
		} catch (CloneNotSupportedException e) {
			// we do support clone.
			throw new AssertionError();
		}
	}
}
