package com.droibit.evendroid2.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.droibit.content.IntentCalendarObject;
import com.droibit.content.IntentHepler;
import com.droibit.evendroid2.R;
import com.droibit.eventservice.IEventServiceClient;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.widget.ToastManager;

import java.util.Calendar;

/**
 * @author kumagai
 */
public class DetailMenuAction extends RefreshAction {

    private DetailedEvent mEvent;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param client サービスのクライアント
     * @param responseCallback 検索結果を受け取るためのコールバック
     */
    public DetailMenuAction(@NonNull Context context, @NonNull IEventServiceClient client, @NonNull EventResponse.Callback responseCallback) {
        super(context, client, responseCallback);
    }

    /**
     *イベントの詳細情報を保持する。
     *
     * @param event イベントの詳細情報
     */
    @NonNull
    public void setDetailedEvent(@NonNull DetailedEvent event) {
        mEvent = event;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // イベントの詳細情報が取得できていない場合は処理を行わない。
        if (mEvent == null) {
            return true;
        }

        switch (item.getItemId()) {
            case R.id.action_schedule:
                onRegistCalendar();
                return true;
            case R.id.action_share:
                onShareEvent();
                return true;
            case R.id.action_bookmark:
                onToggleBookmark(item);
                return true;
            case R.id.action_refresh:
                onRefresh();
                return true;
        }
        return false;
    }

    /**
     * メニュー作成後ブックマークの有無をビューに反映させる。
     *
     * @param item メニュー項目
     * @param eventId イベントの識別ID
     */
    public void toggleBookmarkCheck(MenuItem item, String eventId) {
        item.setChecked(DatabaseManager.existsBookmark(eventId));
    }

    private void onRegistCalendar() {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(mEvent.getStartedAt());

        final IntentCalendarObject.Builder builder = new IntentCalendarObject.Builder()
                .setTitle(mEvent.getTitle())
                .setDescription(mEvent.getEventUrl())
                .setBeginTimeMills(calendar.getTimeInMillis())
                .setLocation(mEvent.getPlace());

        // イベントの終了日が設定されている場合
        if (mEvent.getEndedAt() != null) {
            calendar.setTime(mEvent.getEndedAt());
            builder.setEndTimeMills(calendar.getTimeInMillis());
        } else {
            builder.setEndTimeMills(0L);
        }
        IntentHepler.sendGoogleCalendar(mContext, builder.build());
    }

    private void onShareEvent() {
        final String text = String.format("%s\n%s", mEvent.getTitle(), mEvent.getEventUrl());
        IntentHepler.shareText(mContext, text);
    }

    private void onToggleBookmark(MenuItem item) {
        if (!item.isChecked()) {
            final BookmarkableEvent bookmark = new BookmarkableEvent(mEvent);
            bookmark.save();
            item.setChecked(true);
            ToastManager.showShort(mContext, R.string.toast_successfull_added_bookmark);
            return;
        }

        // ブックマークしたイベントを削除する。
        final BookmarkableEvent bookmark = DatabaseManager.selectBookmark(mEvent.getId());
        if (bookmark != null) {
            bookmark.delete();
            item.setChecked(false);
            ToastManager.showShort(mContext, R.string.toast_successfull_remove_bookmark);
        } else {
            ToastManager.showShort(mContext, R.string.toast_failed_to_remove_bookmark);
        }
    }
}
