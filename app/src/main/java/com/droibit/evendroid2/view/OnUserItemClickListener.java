package com.droibit.evendroid2.view;

import com.droibit.evendroid2.model.ListableUser;

/**
 * リスト内の参加者がクリックされた時に呼ばれるイベントリスナー。
 *
 * @author kumagai
 * @since 2014/10/28
 */
public interface OnUserItemClickListener {

    /**
     * 参加者項目がクリックされた時に呼ばれる。
     *
     * @param user クリックされた参加者
     */
    void onClickItem(ListableUser user);
}
