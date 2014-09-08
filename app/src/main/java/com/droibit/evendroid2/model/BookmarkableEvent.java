package com.droibit.evendroid2.model;

import android.support.annotation.NonNull;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.droibit.evendroid2.util.DateFormatter;
import com.droibit.text.Strings;

/**
 * ブックマークしたイベントモデル。 ActiveAndroidを使用してDBからマッピングする
 *
 * @author kumagai
 * @since 2014/09/03
 */
@Table(name = "bookmark")
public class BookmarkableEvent extends Model implements IListableEvent {

    /** イベントごとのユニークなID */
    @Column(name = "eventId", notNull = true)
    public String eventId;

    /** イベントのタイトル */
    @Column(name = "title", notNull = true)
    public String title;

    /** イベントの開始時間 */
    @Column(name = "startedAt")
    public String startedAt;

    /** イベントの場所 */
    @Column(name = "address")
    public String address;

    /**
     * コンストラクタ
     */
    public BookmarkableEvent() {
        eventId = Strings.EMPTY;
        title = Strings.EMPTY;
        startedAt = Strings.EMPTY;
        address = Strings.EMPTY;
    }

    /**
     * コンストラクタ
     *
     * @param event ブックマークに保存する情報
     */
    public BookmarkableEvent(@NonNull DetailedEvent event) {
        eventId = event.getId();
        title = event.getTitle();
        startedAt = DateFormatter.toShortDateString(event.getStartedAt());
        address = event.getAddress();
    }

    /**
     * ブックマークの値を更新する
     *
     * @param newBoomark 更新元のブックマーク
     */
    public void update(@NonNull BookmarkableEvent newBoomark) {
        title = newBoomark.title;
        startedAt = newBoomark.startedAt;
        address = newBoomark.address;
    }

    /** {@inheritDoc} */
    @Override
    public String getEventId() { return eventId; }

    /** {@inheritDoc} */
    @Override
    public String getTitle() {
        return title;
    }

    /** {@inheritDoc} */
    @Override
    public String getStartedAt() {
        return startedAt;
    }

    /** {@inheritDoc} */
    @Override
    public String getAddress() {
        return address;
    }
}
