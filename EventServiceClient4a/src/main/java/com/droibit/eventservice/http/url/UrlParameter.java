package com.droibit.eventservice.http.url;

import java.util.EventObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import static com.droibit.eventservice.http.url.EventParameters.*;

/**
 * GET リクエストのURLパラメータ。<br>
 * URLパラメータのキーワードには"KEYWORD"と"KEYWORD_OR"の2種類がある。
 * 本来ならどちらか一つを使用するが、両方指定することができるようになっている。
 * 使用の際は必ずどちらか1つを指定する。
 *
 * @author kumagai
 *
 */
public class UrlParameter extends Observable {

	/** 検索開始地点のデフォルト値 */
	private static final String DEFAULT_START_POSITION = "0";

	/** レスポンスのフォーマットのデフォルト値 */
	private static final String DEFAULT_FORMAT = "json";
	
	/** URLパラメータ */
	private final Map<IParameterKey, String> mParams;
		
	/**
	 * 新しいインスタンスを作成する
	 */
	public UrlParameter() {
		mParams = new HashMap<IParameterKey, String>();
		
		initialize();
	}
	
	/**
	 * URLパラメータのキーに対する値を取得する
	 * 
	 * @param key URLパラメータのキー
	 * @return キーに対応する値
	 */
	public String get(IParameterKey key) {
		if (mParams.containsKey(key)) {
			return mParams.get(key);
		}
		return null;
	}
		
	/**
	 * URLパラメータを追加する
	 * 
	 * @param key URLパラメータのキー値
	 * @param value URLパラメータ値
	 */
	public void put(IParameterKey key, String value) {
		mParams.put(key, value);
		
		// URLパラメータが変更されたことを通知する
		if (countObservers() > 0) {
			setChanged();
			notifyObservers(new EventObject(this));
		}
	}
	
	/**
	 * URLパラメータを削除する
	 * 
	 * @param key URLパラメータのキー値
	 */
	public void remove(IParameterKey key) {
		if (mParams.containsKey(key)) {
			mParams.remove(key);
		}
		
		// URLパラメータが変更されたことを通知する
		if (countObservers() > 0) {
			setChanged();
			notifyObservers(new EventObject(this));
		}
	}
	
	/**
	 * URLパラメータをクリアする
	 */
	public void clear() {
		mParams.clear();
		
		// URLパラメータが変更されたことを通知する
		if (countObservers() > 0) {
			setChanged();
			notifyObservers(new EventObject(this));
		}
		initialize();		
	}

    /**
     * URLパラメータを含むかどうか確認する
     *
     * @param key 対象のURLパラメータ
     * @return trueの場合パラメータに含む、falseの場合含まない
     */
    public boolean containsKey(IParameterKey key) {
        return mParams.containsKey(key);
    }
	
	/**
	 * URLパラメータが空かどうか
	 * 
	 * @return {@code true}の場合は空、{@code false}の場合はそうではない
	 */
	public boolean isEmpty() {
		return mParams.isEmpty();
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		// URLパラメータを連結する
		for (IParameterKey key : mParams.keySet()) {
			sb.append(key.getKey()).append("=").append(mParams.get(key)).append("&");
		}
		// 末尾の「&」は不要なので削除する
		return sb.substring(0, sb.length() - 1);
	}
	
	private void initialize() {
		mParams.put(START, DEFAULT_START_POSITION);
		mParams.put(FORMAT, DEFAULT_FORMAT);
	}
}
