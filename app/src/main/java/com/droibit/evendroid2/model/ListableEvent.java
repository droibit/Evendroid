package com.droibit.evendroid2.model;

import android.support.annotation.NonNull;

import com.droibit.evendroid2.util.DateFormatter;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.text.Strings;

import java.util.Date;

/**
 * イベントをリスト表示する情報を保持するクラス。
 *
 * @author kumagai
 */
public class ListableEvent implements IListableEvent, Comparable<ListableEvent> {

    private String mId;
    private String mTitle;
    private String mStartedAt;
    private String mAddress;
    private boolean mAvairable;

    private Date mStartedAtDate;

    /**
     * コンストラクタ
     *
     * @param response イベント検索のレスポンス
     */
    public ListableEvent(@NonNull EventResponse.Event response) {
        mId = response.eventId;
        mTitle = response.title;
        mAddress = !Strings.isEmptyOrWhitespace(response.address) ? response.address : "---";
        mStartedAt = DateFormatter.toShortDateString(response.startedAt);
        mStartedAtDate = DateFormatter.toDate(response.startedAt);

        final boolean isOver = new Date(System.currentTimeMillis())
                                        .compareTo(DateFormatter.toDate(response.startedAt)) == 1;
        mAvairable = (response.accepted <= response.limit || !isOver);
    }

    /** {@inheritDoc} */
    @Override
    public String getEventId() { return mId; }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return mTitle;
    }

    /** {@inheritDoc} */
    @Override
    public String getStartedAt() {
        return mStartedAt;
    }

    /** {@inheritDoc} */
    @Override
    public String getAddress() {
        return mAddress;
    }

    /**
     * イベントが有効かどうか取得する。
     * 定員オーバーもしくは開始日を超過している場合は無効。
     *
     * @return イベントが有効かどうか
     */
    public boolean isAvairable() {
        return mAvairable;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ListableEvent another) {
        if (another.mStartedAtDate == null) {
            return -1;
        }
        return another.mStartedAtDate.compareTo(mStartedAtDate);
    }
}
