/**
 * 
 */
package com.droibit.eventservice.events;

import com.android.volley.Response;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.IMappable;
import com.droibit.eventservice.http.ResponseCallback;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

/**
 * 全イベント支援サービスの共通イベント情報のレスポンス<br>
 * 用途として、イベントの詳細情報ではなく、複数の情報をリスト
 * 表示する際に使用する。
 *
 * @author kumagai
 *
 */
public class SimpleEventResponse implements IMappable {

	/** シリアルID */
	private static final long serialVersionUID = -6731091782103554581L;

	/** 含まれる検索結果の件数 */
	@SerializedName("results_returned")
	public String resultsReturned;

	/** 検索の開始位置 */
	@SerializedName("results_start")
	public String resultsStart;
	
	/** イベント情報 */
	@SerializedName("events")
 	public Event[] events;

    /** Zusaarのイベント情報 */
    @SerializedName("event")
    public Event[] event;

    /**
     * イベント情報の配列を取得する。<br>
     * Zusaarのパラメータが"event"と他と異なるため、メンバ変数ではなく
     * このメソッドからイベント情報を取得する。
     *
     * @return イベント情報の配列
     */
    public Event[] getEvents() {
        return events != null ? events : event;
    }
	
	/**
	 * レスポンスにイベント情報が存在するかどうか
	 * 
	 * @return trueの場合存在する、falseの場合存在しない。
	 */
	public boolean existsEvent() {
        return (events != null && events.length > 0) ||
               (event != null && event.length > 0);
	}
	
	/**
	 * イベント情報を格納するクラス。
	 * 
	 * @author kumagai
	 * 
	 */
	public static class Event implements IMappable {
		
		/** シリアルID */
		private static final long serialVersionUID = 4550632747963575192L;

		/** イベントのID */
		@SerializedName("event_id")
		public String eventId;

		/** イベントのタイトル */
		@SerializedName("title")
		public String title;

		/** イベントのキャッチコピー */
		@SerializedName("catch")
		public String catchCopy;

		/** イベントのURL */
		@SerializedName("event_url")
		public String eventUrl;
		
		/** イベントの開始日時 */
		@SerializedName("started_at")
		public String startedAt;

		/** イベントの終了日時 */
		@SerializedName("ended_at")
		public String endedAt;
		
		/** 定員 */
		@SerializedName("limit")
		public int limit;

		/** 開催場所 */
		@SerializedName("address")
		public String address;

		/** 開催会場 */
		@SerializedName("place")
		public String place;
		
		/** 参加者 */
		@SerializedName("accepted")
		public int accepted;
	}
	
	/**
	 * {@link EventResponse}を受け取るためのリクエストを作成する
	 * 
	 * @param getRequest HTTP GETリクエスト
	 * @param callback レスポンスを受け取るためのコールバック
	 * @return {@link  EventRequest}オブジェクト
	 */
	public static EventRequest<SimpleEventResponse> createRequest(IGetRequest getRequest, Callback callback) {
		return new EventRequest<SimpleEventResponse>(getRequest, callback);
	}

	/**
	 * {@link EventResponse}を受け取るためのイベントリスナ
	 * 
	 * @author kumagai
	 * 
	 */
	public interface Listener extends Response.Listener<SimpleEventResponse> {
	}

	/**
	 * レスポンスを呼び出し元に返すためのクラス
	 * 
	 * @author kumagai
	 * 
	 */
	public static abstract class Callback extends ResponseCallback<SimpleEventResponse> {

		/**
		 * 新しいインスタンスを作成する
		 */
		public Callback() {
			super(SimpleEventResponse.class);
		}
	}
}
