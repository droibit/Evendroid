package com.droibit.evendroid2.model;

import android.content.Context;
import android.graphics.PointF;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.util.DateFormatter;
import com.droibit.evendroid2.view.OnDetailedItemClickListener;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.url.WebPage;
import com.droibit.eventservice.social.EventServiceAccount;
import com.droibit.eventservice.social.SocialAccount;
import com.droibit.eventservice.social.SocialNetworkServices;
import com.droibit.text.Strings;
import com.droibit.utils.Debug;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * @author kumagai
 * @since 2014/09/03.
 */
public class DetailedEvent {

    private String mId;
    private String mTitle;
    private String mCatchcopy;
    private Date mStartedAt;
    private Date mEndedAt;
    private String mSchedule;
    private String mPlace;
    private String mAddress;
    private PointF mCoordinate;
    private String mUsers;
    private EventServiceAccount mAccount;
    private String mEventUrl;
    private String mUrl;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param response レスポンスのイベント情報
     */
    public DetailedEvent(@NonNull Context context, @NonNull EventResponse.Event response) {
        Debug.assertNotNull(response);

        mId = response.eventId;
        mTitle = response.title;
        mCatchcopy = response.catchCopy;
        mStartedAt = DateFormatter.toDate(response.startedAt);
        mEndedAt =  DateFormatter.toDate(response.endedAt);
        mSchedule = makeSchedule(mStartedAt, mEndedAt);
        mAccount = makeServiceAccount(response);
        mUrl = response.eventUrl;
        mAddress = !Strings.isEmptyOrWhitespace(response.address) ? response.address : "---";
        mEventUrl = response.eventUrl;

        // イベントの開催場所が設定されている場合
        if (!Strings.isEmptyOrWhitespace(response.place)) {
            mPlace = response.place;
            mCoordinate = new PointF(Float.valueOf(response.longtitude),
                                     Float.valueOf(response.latitude));
        } else {
            mPlace = "---";
            mCoordinate = null;
        }

        // イベントの定員が設定されている場合
        if (response.accepted != 0) {
            mUsers = context.getString(R.string.event_detail_participants,
                    response.accepted, response.limit);
        } else {
            mUsers = context.getString(R.string.event_detail_no_participants);
        }

        // 関連URLが設定されている場合
        if (!Strings.isEmptyOrWhitespace(response.url)) {
            mUrl = response.url;
        } else {
            mUrl = "---";
        }
    }

    /**
     * イベントのタイトルを取得する。
     *
     * @return イベントのタイトル
     */
    public String getTitle() {
        return mTitle;
    }

    /**
     * イベントの識別IDを取得する。
     *
     * @return イベントの識別ID
     */
    public String getId() {
        return mId;
    }

    /**
     * イベントの主催者情報を取得する。
     *
     * @return イベントの主催者情報
     */
    public EventServiceAccount getAccount() {
        return mAccount;
    }

    /**
     * イベントのキャッチコピーを取得する。
     *
     * @return イベントのキャッチコピー
     */
    public String getCatchcopy() { return mCatchcopy; }

    /**
     * イベントの開始時刻を取得する。
     *
     * @return イベントの開始時刻
     */
    public Date getStartedAt() {
        return mStartedAt;
    }

    /**
     * イベントの終了時刻を取得する。
     *
     * @return イベントの終了時刻
     */
    public Date getEndedAt() {
        return mEndedAt;
    }

    /**
     * イベントのスケジュールを取得する。
     *
     * @return イベントのスケジュール
     */
    public String getSchedule() {
        return mSchedule;
    }

    /**
     * イベントの住所を取得する。
     *
     * @return イベントの住所
     */
    public String getAddress() {
        return mAddress;
    }

    /**
     * イベントの開催場所を取得する。
     *
     * @return イベントの開催場所
     */
    public String getPlace() {
        return mPlace;
    }

    /**
     * イベントの開催場所の座標を取得する。
     *
     * @return 開催場所の座標
     */
    public PointF getCoordinate() {
        return mCoordinate;
    }

    /**
     * イベントの参加者情報を取得する。
     *
     * @return イベントの参加者情報
     */
    public String getUsers() {
        return mUsers;
    }

    /**
     * イベントページのURLを取得する。
     *
     * @return イベントページ
     */
    public String getEventUrl() { return mEventUrl; }

    /**
     * 関連ページのURLを取得する。
     *
     * @return 関連ページのURL
     */
    public String getUrl() { return mUrl; }


    private String makeSchedule(Date startedAt, Date endedAt) {
        if (startedAt == null) {
            return "---";
        } else if (endedAt == null) {
            return DateFormatter.toLongDateString(startedAt);
        }

        final Calendar toDate = Calendar.getInstance(Locale.JAPAN);
        final Calendar fromDate = Calendar.getInstance(Locale.JAPAN);
        toDate.setTime(startedAt);
        fromDate.setTime(endedAt);

        final String started = DateFormatter.toLongDateString(startedAt);
        final String ended;
        // イベントの日程が一日で完結する場合
        if (toDate.get(YEAR) == fromDate.get(YEAR)
                && toDate.get(MONTH) == fromDate.get(MONTH)
                && toDate.get(DATE) == fromDate.get(DATE)) {
            // 時間のみ表示するようにする
            ended = DateFormatter.toTimeString(endedAt);
        } else if (toDate.get(YEAR) == fromDate.get(YEAR)) {
            // 同じ年の場合
            ended = DateFormatter.toLongDateWithoutYerString(endedAt);
        } else {
            ended = DateFormatter.toLongDateString(endedAt);
        }
        return String.format("%s ~ %s", started, ended);
    }

    private EventServiceAccount makeServiceAccount(EventResponse.Event response) {
        final EventServiceAccount account = new EventServiceAccount(EventServices.ATND);
        account.setName(response.ownerNickname);
        account.setUserId(response.ownerId);

        // Twitterアカウントが存在する場合
        if (!Strings.isEmptyOrWhitespace(response.ownerTwitterId)) {
            final Uri uri = WebPage.getSocialPageUri(SocialNetworkServices.TWITTER, response.ownerTwitterId);
            account.addSocialAccount(new SocialAccount(SocialNetworkServices.TWITTER.getName(), uri));
        }
        return account;
    }

    /**
     * イベントの詳細情報をグルーピングするためのクラス。
     */
    public static class Group {

        private String mTitle;
        private List<Item> mItems;

        /**
         * コンテキスト
         *
         * @param title セクション名
         */
        public Group(@NonNull String title) {
            Debug.assertTrue(!TextUtils.isEmpty(title));

            mTitle = title;
            mItems = new ArrayList<Item>();
        }

        /**
         * 詳細情報の項目を追加する。
         *
         * @param item 詳細情報の項目
         */
        public void add(Item item) {
            Debug.assertNotNull(item);
            mItems.add(item);
        }

        /**
         * 詳細情報の項目を全て削除する。
         */
        public void clear() {
            mItems.clear();
        }

        /**
         * セクション名を取得する。
         *
         * @return セクション名
         */
        public String getTitle() {
            return mTitle;
        }

        /**
         * 詳細情報の項目リストを取得する。
         *
         * @return 詳細情報の項目リスト
         */
        public List<Item> getItems() {
            return mItems;
        }
    }

    /**
     * イベントの各詳細情報を保持するためのクラス。
     */
    public static class Item {

        private String mTitle;
        private int mIconResId;
        private OnDetailedItemClickListener mListener;

        /**
         * コンストラクタ
         *
         * @param title     詳細情報の項目名
         * @param iconResId 対応するアイコンのリソースID
         */
        public Item(@NonNull String title, @DrawableRes int iconResId) {
            Debug.assertTrue(!TextUtils.isEmpty(title));

            mTitle = title;
            mIconResId = iconResId;
            mListener = null;
        }

        /**
         * 詳細情報の項目名を取得する。
         *
         * @return 詳細情報の項目名
         */
        public String getTitle() {
            return mTitle;
        }

        /**
         * アイコンのリソースIDを取得する。
         *
         * @return アイコンのリソースID
         */
        public int getIconResId() {
            return mIconResId;
        }

        /**
         * クリックリスナーを取得する。
         *
         * @return クリックリスナー
         */
        public OnDetailedItemClickListener getListener() {
            return mListener;
        }

        /**
         * クリックリスナーを設定する。
         *
         * @param listener クリックリスナー
         */
        public void setListener(@NonNull OnDetailedItemClickListener listener) {
            Debug.assertNotNull(listener);
            this.mListener = listener;
        }
    }
}
