package com.droibit.text;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.regex.Pattern;

import android.text.TextUtils;

import com.droibit.utils.Debug;

/**
 * 文字列を操作するためのユーティリティクラス
 * 
 * @author kumagaishinya
 * 
 */
public final class Strings {

	/** 空文字 */
	public static final String EMPTY = "";

	/** 改行文字 */
	public static final String LINE_BREAK = "\n";
	
	/** 二重改行文字 */
	public static final String DOUBLE_LINE_BREAK = "\n\n";
	
	/** UTF-8形式 */
	public static final String UTF8 = "UTF-8";

	/** '1'の文字 */
	private static final String ONE = "1";
	
	/** '0'の文字 */
	private static final String ZERO = "0";

	private static MessageDigest digest = null;
	private static final Pattern MD5_PATTERN = Pattern.compile("[a-fA-F0-9]{32}");
	
	static {
		try {
			digest = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			// better never happens
		}
	}

	/**
	 * Returns a list joined together by the provided delimiter, for example,
	 * ["a", "b", "c"] could be joined into "a,b,c"
	 *
	 * @param things the things to join together
	 * @param delim the delimiter to use
	 * @return a string contained the things joined together
	 */
	public static final String join(List<?> things, String delim) {
		final StringBuilder builder = new StringBuilder();
		for (int i = 0, size = things.size(); i < size; i++) {
			if (i > 0) {
				builder.append(delim);
			}
			builder.append(things.get(i).toString());
		}
		return builder.toString();
	}
	
	/**
	 * 文字列の配列を結合する
	 * 
	 * @param src 文字列の配列
	 * @param separetor 区切り文字
	 * @return 結合した文字列
	 */
	public static String join(String[] src, String separetor) {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0, len = src.length; i < len; i++) {
			if (i > 0) {
				sb.append(separetor);
			}
			sb.append(src[i]);
		}
		return sb.toString();
	}

	/**
	 * 文字列をUTF-8形式の文字コードにエンコードする
	 * 
	 * @param src 変換元の文字列
	 * @return エンコードされた文字列
	 */
	public static final String encodeUTF8(String src) {
		Debug.assertTrue(!TextUtils.isEmpty(src));
		
		try {
			return URLEncoder.encode(src, UTF8);
		} catch (UnsupportedEncodingException e) {
			// utf-8 always available
		}
		return null;
	}

	/**
	 * UTF-8形式の文字コードの文字列をデコードする
	 * 
	 * @param src 変換元の文字列
	 * @return デコードされた文字列
	 */
	public static String decodeUTF8(String src) {
		Debug.assertTrue(!TextUtils.isEmpty(src));

		try {
			return URLDecoder.decode(src, UTF8);
		} catch (UnsupportedEncodingException e) {
			// utf-8 always available
		}
		return null;
	}

	/**
	 * 文字列のブール値と比較する
	 * 
	 * @param value 文字列のブール値(1:true, 0:false)
	 * @return trueの場合一致、falseの場合不一致
	 */
	public static boolean compareToBoolean(String value) {
		return ONE.equals(value);
	}

	/**
	 * 文字列のブール値に変換する
	 * 
	 * @param value 変換元のブール値
	 * @return 真の場合"1"、偽の場合は"0"の文字列
	 */
	public static String valueOf(boolean value) {
		return (value) ? ONE : ZERO;
	}

	/**
	 * Returns a 32 chararacter hexadecimal representation of an MD5 hash of the given String.
	 * 
	 * @param s the String to hash
	 * @return the md5 hash
	 */
	public static String md5(String s) {
		try {
			final byte[] bytes = digest.digest(s.getBytes("UTF-8"));
			StringBuilder b = new StringBuilder(32);
			for (byte aByte : bytes) {
				String hex = Integer.toHexString((int) aByte & 0xFF);
				if (hex.length() == 1) {
					b.append('0');
				}
				b.append(hex);
			}
			return b.toString();
		} catch (UnsupportedEncodingException e) {
			// utf-8 always available
		}
		return null;
	}
	
	/**
	 * Tests if the given string <i>might</i> already be a 32-char md5 string.
	 *
	 * @param s String to test
	 * @return <code>true</code> if the given String might be a md5 string
	 */
	public static boolean isMD5(String s) {
		return s.length() == 32 && MD5_PATTERN.matcher(s).matches();
	}
	
	/**
	 * 文字列が空、もしくは空白のみかどうか
	 * 
	 * @param text 対象テキスト
	 * @return tureの場合は空もしくは空白のみ, falseの場合は文字列
	 */
	public static boolean isEmptyOrWhitespace(String text) {
		return text == null || text.isEmpty() || text.trim().isEmpty(); 
	}
}
