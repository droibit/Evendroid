package com.droibit.evendroid2.view.input;

import com.droibit.evendroid2.model.IListableEvent;

/**
 * イベントのリストがクリックされた時に呼ばれるイベントリスナー。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public interface OnListItemClickListener {

    /**
     * イベント項目がクリックされた時に呼ばれる。
     *
     * @param event クリックされたイベント
     */
    public void onItemClick(IListableEvent event);

    /**
     * イベント項目が長押しされた時に呼ばれる。
     *
     * @param event 長押しされたイベント
     */
    public void onItemLongClick(IListableEvent event);
}
