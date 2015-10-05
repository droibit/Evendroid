package com.droibit.eventservice.events;

/**
 * イベントの参加状態の列挙体
 *
 * @author kumagai
 * @since 2014/03/20.
 */
public enum EventStatus {
    PARTICIPATION, CANCEL;

    /**
     * イベントの参加状態を取得する
     *
     * @param status 参加状態を表す整数値
     * @return イベントの参加状態
     */
    public static final EventStatus from(int status) {
        if (status == 1) {
            return PARTICIPATION;
        } else if (status == 0) {
            return CANCEL;
        }
        throw new IllegalArgumentException("0:キャンセル、1:参加のみ対応しています");
    }
}
