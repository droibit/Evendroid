package com.droibit.evendroid2.contoller;

import android.content.Context;
import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * 直前の検索結果をサジェストとして表示するためのコンテンツプロバイダ。
 *
 * @author kumagai
 * @since 2014/09/10.
 */
public class RecentSuggestionsProvider extends SearchRecentSuggestionsProvider {

    public static final String AUTHORITY = RecentSuggestionsProvider.class.getName();
    public static final int MODE = DATABASE_MODE_QUERIES;

    /**
     * コンストラクタ
     */
    public RecentSuggestionsProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

    /**
     * 新しい{@link SearchRecentSuggestions}オブジェクトを作成する。
     *
     * @param context コンテキスト
     * @return 新しい{@link SearchRecentSuggestions}オブジェクト
     */
    public static SearchRecentSuggestions newRecentSuggestions(Context context) {
        return new SearchRecentSuggestions(context, AUTHORITY, MODE);
    }
}
