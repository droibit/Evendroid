package com.droibit.evendroid2.view;

/**
 * 詳細項目をクリック後、画面遷移する際に呼ばれるイベントリスナー。
 *
 * @author kumagai
 */
public interface OnTransitionListener {

    /**
     * 参加者一覧画面へ遷移する際に呼ばれる処理。
     *
     * @param eventId イベントの識別ID
     */
    void onTransitionUsers(String eventId);

    /**
     * 主催者イベント一覧画面へ遷移する際に呼ばれる処理。
     *
     * @param owner 主催者名
     */
    void onTransitionOwnerEvents(String owner);
}
