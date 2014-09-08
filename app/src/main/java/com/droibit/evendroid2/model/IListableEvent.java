package com.droibit.evendroid2.model;

/**
 * イベントをリスト上に表示するためのインターフェース.
 *
 * @author kumagai
 * @since 2014/09/05
 */
public interface IListableEvent {
    /**
     * イベントの識別IDを取得する。
     *
     * @return イベントの識別ID
     */
    String getEventId();

    /**
     * イベントのタイトルを取得する。
     *
     * @return イベントのタイトル
     */
    String getTitle();

    /**
     * イベントの開催日付を取得する。
     *
     * @return イベントの開催日付
     */
    String getStartedAt();

    /**
     * イベントの開催場所を取得する。
     *
     * @return イベントの開催場所
     */
    String getAddress();
}
