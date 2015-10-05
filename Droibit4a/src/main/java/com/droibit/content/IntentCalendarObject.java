package com.droibit.content;

import java.io.Serializable;

import android.text.TextUtils;

import com.droibit.text.Strings;
import com.droibit.utils.Debug;


/**
 * Googleカレンダーにスケジュールを登録するための情報
 * 
 * @author kumagaishinya
 * 
 */
public class IntentCalendarObject implements Serializable {

	/** シリアルID */
	private static final long serialVersionUID = 2674687456090247717L;

    /** タイトル */
    public String title;

    /** 詳細情報 */
    public String description;

	/** 開催場所 */
	public String location;

	/** 開始時間 */
	public long beginTimeMills;

	/** 終了時間 */
	public long endTimeMills;

	/**
	 * 新しいインスタンスを作成する
	 */
	private IntentCalendarObject() {
        title = Strings.EMPTY;
        description = Strings.EMPTY;
		location = Strings.EMPTY;
		beginTimeMills = 0L;
		endTimeMills = 0L;
	}

	/**
	 * {@link IntentCalendarObject}オブジェクトを作成するためのビルダ
	 * 
	 * @author kumagaishinya
	 * 
	 */
	public static final class Builder {

		/** {@link IntentCalendarObject}オブジェクト */
		private final IntentCalendarObject event;

		/**
		 * コンストラクタ
		 */
		public Builder() {
			event = new IntentCalendarObject();
		}

		/**
		 * タイトルを設定する
		 * 
		 * @param title タイトル
		 * @return 自身のインスタンス
		 */
		public Builder setTitle(String title) {
            Debug.assertTrue(!TextUtils.isEmpty(title));
			event.title = title;
			return this;
		}

		/**
		 * 詳細情報を設定する
		 * 
		 * @param description 詳細情報
		 * @return 自身のインスタンス
		 */
		public Builder setDescription(String description) {
            Debug.assertTrue(!TextUtils.isEmpty(description));
            event.description = description;
			return this;
		}

		/**
		 * 開催場所を設定する
		 * 
		 * @param location 開催場所
		 * @return 自身のインスタンス
		 */
		public Builder setLocation(String location) {
			event.location = location;
			return this;
		}

		/**
		 * 開始時間を設定する
		 * 
		 * @param beginTimeMills 開始時間(ミリ秒)
		 * @return 自身のインスタンス
		 */
		public Builder setBeginTimeMills(long beginTimeMills) {
			event.beginTimeMills = beginTimeMills;
			return this;
		}

		/**
		 * 終了時間を設定する
		 * 
		 * @param endTimeMills 終了時間(ミリ秒)
		 * @return 自身のインスタンス
		 */
		public Builder setEndTimeMills(long endTimeMills) {
			event.endTimeMills = endTimeMills;
			return this;
		}

        /**
         * {@link IntentCalendarObject}オブジェクトを作成する
         *
         * @return {@link IntentCalendarObject}オブジェクト
         */
		public IntentCalendarObject build() {
			return event;
		}
	}
}
