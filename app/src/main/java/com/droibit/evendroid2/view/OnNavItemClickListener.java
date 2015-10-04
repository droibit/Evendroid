package com.droibit.evendroid2.view;

/**
 * ナビゲーション項目がクリックされた時に呼ばれるイベントリスナー
 *
 * @author kumagai
 */
public interface OnNavItemClickListener {

    /**
     * ナビゲーション項目がクリックされた時に呼ばれる処理。
     *
     * @param position ナビゲーションのインデックス
     */
    void onItemClick(int position);
}
