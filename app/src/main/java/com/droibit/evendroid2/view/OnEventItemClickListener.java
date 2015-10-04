package com.droibit.evendroid2.view;

import com.droibit.evendroid2.model.IListableEvent;

/**
 * リスト内のイベントがクリックされた時に呼ばれるイベントリスナー。
 *
 * @author kumagai
 */
public interface OnEventItemClickListener {

    /**
     * イベント項目がクリックされた時に呼ばれる。
     *
     * @param event クリックされたイベント
     */
    void onItemClick(IListableEvent event);

    /**
     * イベント項目が長押しされた時に呼ばれる。
     *
     * @param event 長押しされたイベント
     */
    void onItemLongClick(IListableEvent event);
}
