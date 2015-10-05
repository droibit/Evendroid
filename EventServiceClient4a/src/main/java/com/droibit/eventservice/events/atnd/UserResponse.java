/**
 * 
 */
package com.droibit.eventservice.events.atnd;

import com.android.volley.Response;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.IMappable;
import com.droibit.eventservice.http.ResponseCallback;
import com.google.gson.annotations.SerializedName;

/**
 * ATNDのユーザ情報
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
	@SerializedName("events")
	public EventContainer[] events;

    /**
     * レスポンスにイベント情報が存在するかどうか
     *
     * @return trueの場合存在する、falseの場合存在しない。
     */
    public boolean existsEvent() {
        return events != null && events.length > 0;
    }

    /**
     * イベント情報を格納するコンテナ
     */
    public static class EventContainer implements IMappable {

        /** シリアルID */
        private static final long serialVersionUID = -2247913777966431919L;

        /** イベント情報 */
        @SerializedName("event")
        public Event event;
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
		public UserContainer[] users;

        /**
         * レスポンスにユーザ情報が存在するかどうか
         *
         * @return trueの場合存在する、falseの場合存在しない。
         */
        public boolean existsUser() {
            return users != null && users.length > 0;
        }
	}

    /**
     * 参加者情報を格納するコンテナ
     */
    public static class UserContainer implements IMappable {

        /** シリアルID */
        private static final long serialVersionUID = -8761625158396275767L;

        /** 参加者情報 */
        @SerializedName("user")
        public User user;
    }

    /**
     * 参加者の情報を格納するクラス
     *
     * @author kumagai
     *
     */
    public static class User implements IMappable {

        /** シリアルID */
        private static final long serialVersionUID = -3816147172729857876L;

        /** 参加者ユーザのID */
        @SerializedName("user_id")
        public int userID;

        /** 参加者のニックネーム */
        @SerializedName("nickname")
        public String nickname;

        /** 参加者のTwitter ID */
        @SerializedName("twitter_id")
        public String twitterID;

        /** 参加者のステータス */
        @SerializedName("status")
        public int status;
    }
	
	/**
	 * {@link UserResponse}を受け取るためのリクエストを作成する
	 * 
	 * @param getRequest HTTP GETリクエスト
	 * @param callback レスポンスを受け取るためのコールバック
	 * @return {@link  EventRequest}オブジェクト
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
