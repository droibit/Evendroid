package com.droibit.evendroid2.fragment.nav;

import android.app.Fragment;

import com.droibit.evendroid2.fragment.BookmarkListFragment;
import com.droibit.evendroid2.fragment.EventListFragment;
import com.droibit.evendroid2.fragment.SearchListFragment;

import static com.droibit.evendroid2.fragment.EventListFragment.EVENT_TYPE_OWNER;
import static com.droibit.evendroid2.fragment.EventListFragment.EVENT_TYPE_PARTICIPATION;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;

/**
 * ナビゲーションに応じたフラグメントを作成するためのファクトリ。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public final class NavContentFragmentFactory {

    /**
     * ナビゲーションに応じたフラグメントを作成する。
     *
     * @param position ナビゲーションのセクション番号
     * @return 表示するフラグメント
     */
    public static final Fragment create(int position) {
        switch (Navigations.from(position)) {
            case SEARCH:
                return SearchListFragment.newInstance();
            case OWNER:
                return EventListFragment.newInstance(EVENT_TYPE_OWNER);
            case PARTICIPATION:
                return EventListFragment.newInstance(EVENT_TYPE_PARTICIPATION);
            case BOOKMARK:
                return BookmarkListFragment.newInstance();
            default:
                throw new IllegalArgumentException();
        }
    }
}
