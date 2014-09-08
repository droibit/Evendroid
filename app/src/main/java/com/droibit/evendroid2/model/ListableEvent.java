package com.droibit.evendroid2.model;

import android.support.annotation.NonNull;

import com.droibit.evendroid2.util.DateFormatter;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.text.Strings;

import java.util.Date;

/**
 * イベントをリスト上に表示する際の情報を保持するためのクラス。
 *
 * @author kumagai
 * @since 2014/09/02
 */
public class ListableEvent implements IListableEvent {

    private String mId;
    private String mTitle;
    private String mStartAt;
    private String mAddress;
    private boolean mAvairable;

    /**
     * コンストラクタ
     *
     * @param response イベント検索のレスポンス
     */
    public ListableEvent(@NonNull EventResponse.Event response) {
        mId = response.eventId;
        mTitle = response.title;
        mAddress = !Strings.isEmptyOrWhitespace(response.address) ? response.address : "---";
        mStartAt = DateFormatter.toShortDateString(response.startedAt);

        final boolean isOver = new Date(System.currentTimeMillis())
                                        .compareTo(DateFormatter.toDate(response.startedAt)) == 1;
        mAvairable = (response.accepted <= response.limit && !isOver);
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
        return mStartAt;
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
}
