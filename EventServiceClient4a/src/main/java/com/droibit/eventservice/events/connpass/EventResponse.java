/**
 * 
 */
package com.droibit.eventservice.events.connpass;

import com.android.volley.Response;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.IMappable;
import com.droibit.eventservice.http.ResponseCallback;
import com.google.gson.annotations.SerializedName;

/**
 * connpass イベント情報のレスポンス
 *
 * @author kumagai
 *
 */
public class EventResponse implements IMappable {

	/** シリアルID */
	private static final long serialVersionUID = -9056123980466438668L;

	/** 含まれる検索結果の件数 */
	@SerializedName("results_returned")
	public String resultsReturned;

	/** 検索結果の総件数 */
	@SerializedName("results_available")
	public String resultsAvailable;
	
	/** 検索の開始位置 */
	@SerializedName("results_start")
	public String resultsStart;
	
	/** イベント情報 */
	@SerializedName("event")
	public Event[] events;

    /**
     * レスポンスにイベント情報が存在するかどうか
     *
     * @return trueの場合存在する、falseの場合存在しない。
     */
    public boolean existsEvent() {
        return events != null && events.length > 0;
    }

	
	/**
	 * イベント情報を格納するクラス。
	 * 
	 * @author kumagai
	 * 
	 */
	public static class Event implements IMappable {

		/** シリアルID */
		private static final long serialVersionUID = 3211556448546676806L;
		
		/** イベントのID */
		@SerializedName("event_id")
		public String eventId;

		/** イベントのタイトル */
		@SerializedName("title")
		public String title;

		/** イベントのキャッチコピー */
		@SerializedName("catch")
		public String catchCopy;
		
		/** イベントの概要 */
		@SerializedName("description")
		public String description;

		/** connpassのURL */
		@SerializedName("event_url")
		public String eventUrl;
		
		/** ハッシュタグ */
		@SerializedName("hash_tag")
		public String hashtag;
		
		/** イベントの開始日時 */
		@SerializedName("started_at")
		public String startedAt;

		/** イベントの終了日時 */
		@SerializedName("ended_at")
		public String endedAt;
		
		/** 定員 */
		@SerializedName("limit")
		public int limit;		
		
		/** イベント参加タイプ */
		@SerializedName("event_type")
		public String event_type;
		
		/** 開催場所 */
		@SerializedName("address")
		public String address;

		/** 開催会場 */
		@SerializedName("place")
		public String place;
		
		/** 開催会場の緯度 */
		@SerializedName("lat")
		public float latitude;

		/** 開催会場の経度 */
		@SerializedName("lon")
		public float longtitude;
		
		/** 主催者のID */
		@SerializedName("owner_id")
		public String ownerId;
		
		/** 主催者のニックネーム */
		@SerializedName("owner_nickname")
		public String ownerNickname;

		/** 管理者の表示名 */
		@SerializedName("owner_display_name")
		public String ownerDsplayName;
		
		/** 参加者 */
		@SerializedName("accepted")
		public int accepted;

		/** 補欠者 */
		@SerializedName("waiting")
		public String waiting;
		
		/** 更新日時 */
		@SerializedName("updated_at")
		public String updatedAt;
		
		/** シリーズ */
		@SerializedName("series")
		public Series series;
		
		/**
		 * シリーズ情報を格納するクラス。
		 * 
		 * @author kumagai
		 * 
		 */
		public static class Series implements IMappable {

			/** シリアルID */
			private static final long serialVersionUID = -8262905805600059223L;
			
			/** シリーズID */
			@SerializedName("id")
			public int id;
			
			/** シリーズタイトル */
			@SerializedName("title")
			public String title;
			
			/** シリーズのconnpass.com 上のURL */
			@SerializedName("url")
			public String url;
		}		
	}
	
	/**
	 * {@link EventResponse}を受け取るためのリクエストを作成する
	 * 
	 * @param getRequest HTTP GETリクエスト
	 * @param callback レスポンスを受け取るためのコールバック
	 * @return {@link  EventServiceRequest}オブジェクト
	 */
	public static EventRequest<EventResponse> createRequest(IGetRequest getRequest, Callback callback) {
		return new EventRequest<EventResponse>(getRequest, callback);
	}
	
	/**
	 * {@link EventResponse}を受け取るためのイベントリスナ
	 *
	 * @author kumagai
	 *
	 */
	public interface Listener extends Response.Listener<EventResponse> {
	}
	
	/**
	 * レスポンスを呼び出し元に返すためのクラス
	 *
	 * @author kumagai
	 *
	 */
	public static abstract class Callback extends ResponseCallback<EventResponse> {

		/**
		 * 新しいインスタンスを作成する
		 */
		public Callback() {
			super(EventResponse.class);
		}
	}
}
