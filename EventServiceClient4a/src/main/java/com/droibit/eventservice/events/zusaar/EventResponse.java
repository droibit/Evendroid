/**
 * 
 */
package com.droibit.eventservice.events.zusaar;

import com.android.volley.Response;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.IMappable;
import com.droibit.eventservice.http.ResponseCallback;
import com.google.gson.annotations.SerializedName;

/**
 * Zusaar イベント情報のレスポンス
 *
 * @author kumagai
 *
 */
public class EventResponse implements IMappable {

	/** シリアルID */
	private static final long serialVersionUID = -1961910099960382037L;
	
	/** 含まれる検索結果の件数 */
	@SerializedName("results_returned")
	public String resultsReturned;

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
		private static final long serialVersionUID = -244227821872960909L;

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

		/** ATNDのURL */
		@SerializedName("event_url")
		public String eventUrl;

		/** イベントの開始日時 */
		@SerializedName("started_at")
		public String startedAt;

		/** イベントの終了日時 */
		@SerializedName("ended_at")
		public String endedAt;
		
		/** 無料/有料イベント */
		@SerializedName("pay_type")
		public String payType;
		
		/** 参考URL */
		@SerializedName("url")
		public String url;

		/** 定員 */
		@SerializedName("limit")
		public int limit;

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

		/** 主催者のプロフィールURL */
		@SerializedName("owner_profile_url")
		public String ownerProfileUrl;

		/** 主催者のID */
		@SerializedName("owner_id")
		public String ownerId;
		
		/** 主催者のニックネーム */
		@SerializedName("owner_nickname")
		public String ownerNickname;
		
		/** 参加者 */
		@SerializedName("accepted")
		public int accepted;

		/** 補欠者 */
		@SerializedName("waiting")
		public String waiting;
		
		/** 更新日時 */
		@SerializedName("updated_at")
		public String updatedAt;
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
