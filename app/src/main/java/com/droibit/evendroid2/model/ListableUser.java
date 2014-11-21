package com.droibit.evendroid2.model;

import android.support.annotation.NonNull;

import com.droibit.eventservice.events.atnd.UserResponse;

import java.io.Serializable;

/**
 * 参加者をリスト表示する情報を保持するクラス。
 *
 * @author kumagai
 * @since 2014/10/28
 */
public class ListableUser implements Comparable<ListableUser>, Serializable {

    /** シリアルID */
    private static final long serialVersionUID = 2987909589734849670L;

    private String userID;
    private String name;
    private String twitterID;
    private boolean participate;

    /**
     * コンストラクタ
     *
     * @param response 参加者検索のレスポンス
     */
    public ListableUser(@NonNull UserResponse.User response) {
        userID = String.valueOf(response.userID);
        name = response.nickname;
        twitterID = response.twitterID;
        participate = response.status == 1;
    }

    /** {@inheritDoc} */
    @Override
    public int compareTo(ListableUser another) {
        return name.compareTo(another.name);
    }

    /**
     * 参加者の識別IDを取得する
     *
     * @return 参加者の識別ID
     */
    public String getUserID() {
        return userID;
    }

    /**
     * 参加者名を取得する
     *
     * @return 参加者名
     */
    public String getName() {
        return name;
    }

    /**
     * 参加者のTwitterIDを取得する
     *
     * @return
     */
    public String getTwitterID() {
        return twitterID;
    }

    /**
     * イベントの参加状態を取得する
     *
     * @return trueの場合イベントに参加、falseの場合不参加
     */
    public boolean isParticipate() {
        return participate;
    }
}
