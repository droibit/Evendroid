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
 * Zusaar のユーザ情報のレスポンス
 *
 * @author kumagai
 *
 */
public class UserResponse implements IMappable {

	/** シリアルID */
	private static final long serialVersionUID = -4609590214608039502L;

	/** 検索の開始位置 */
	@SerializedName("results_start")
	public int resultsStart;

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
	 * イベント情報を格納するクラス
	 * 
	 * @author kumagai
	 *
	 */
	public static class Event implements IMappable {

		/** シリアルID */
		private static final long serialVersionUID = -2445493675380749502L;
		
		/** イベントのID */
		@SerializedName("event_id")
		public String eventId;

		/** イベントのタイトル */
		@SerializedName("title")
		public String title;
		
		/** Zusaar上のURL */
		@SerializedName("event_url")
		public String eventUrl;
		
		/** 定員 */
		@SerializedName("limit")
		public int limit;
		
		/** 参加者 */
		@SerializedName("accepted")
		public int accepted;
		
		/** 補欠者 */
		@SerializedName("waiting")
		public int waiting;
		
		/** 参加ユーザ */
		@SerializedName("users")
		public User[] users;

        /**
         * レスポンスにユーザ情報が存在するかどうか
         *
         * @return trueの場合存在する、falseの場合存在しない。
         */
        public boolean existsUser() {
            return users != null && users.length > 0;
        }
	
		/**
		 * 参加ユーザの情報を格納するクラス
		 * 
		 * @author kumagai
		 *
		 */
		public static class User implements IMappable {

			/** シリアルID */
			private static final long serialVersionUID = 4359328969496476159L;

			/** 参加者ユーザのID */
			@SerializedName("user_id")
			public int userId;
			
			/** 参加者のニックネーム */
			@SerializedName("nickname")
			public String nickname;
			
			/** 参加者のプロフィールURL */
			@SerializedName("profile_url")
			public String profileUrl;
		
			/** 参加者のステータス */
			@SerializedName("status")
			public String status;
		}
	}
	
	/**
	 * {@link UserResponse}を受け取るためのリクエストを作成する
	 * 
	 * @param getRequest HTTP GETリクエスト
	 * @param callback レスポンスを受け取るためのコールバック
	 * @return {@link  EventServiceRequest}オブジェクト
	 */
	public static EventRequest<UserResponse> createRequest(IGetRequest getRequest, Callback callback) {
		return new EventRequest<UserResponse>(getRequest, callback);
	}
	
	/**
	 * {@link UserResponse}を受け取るためのイベントリスナ
	 * 
	 * @author kumagai
	 * 
	 */
	public interface Listener extends Response.Listener<UserResponse> {
	}
	
	/**
	 * レスポンスを呼び出し元に返すためのクラス
	 *
	 * @author kumagai
	 *
	 */
	public static abstract class Callback extends ResponseCallback<UserResponse> {

		/**
		 * 新しいインスタンスを作成する
		 */
		public Callback() {
			super(UserResponse.class);
		}
	}
}
