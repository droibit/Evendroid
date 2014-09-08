package com.droibit.evendroid2.view.input;

/**
 * ナビゲーション項目がクリックされた時に呼ばれるイベントリスナー
 *
 * @author kumagai
 * @since 2014/09/05.
 */
public interface OnNavItemClickListener {

    /**
     * ナビゲーション項目がクリックされた時に呼ばれる処理。
     *
     * @param position ナビゲーションのインデックス
     */
    void onItemClick(int position);
}
