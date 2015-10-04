package com.droibit.evendroid2.model;

import android.app.Activity;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.provider.SearchRecentSuggestions;
import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ImageSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.SettingsActivity;
import com.droibit.evendroid2.provider.RecentSuggestionsProvider;
import com.droibit.eventservice.IEventServiceClient;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.EventRequest;
import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.eventservice.http.url.IParameterKey;
import com.droibit.eventservice.http.url.RequestContents;
import com.droibit.eventservice.http.url.UrlParameter;
import com.droibit.text.Strings;
import com.droibit.utils.Debug;
import com.droibit.utils.SystemServices;

import static android.app.SearchManager.SUGGEST_COLUMN_TEXT_1;

/**
 * {@link SearchView}を使用してイベント検索を行うためのクラス。
 *
 * @author kumagai
 */
public class SearchAction implements SearchView.OnQueryTextListener,
        SearchView.OnSuggestionListener, View.OnFocusChangeListener {

    private final Context mContext;
    private final IEventServiceClient mClient;
    private SearchView mSearchView;
    private MenuItem mSearchMenuItem;
    private EventRequest<EventResponse> mRequest;
    private EventResponse.Callback mResponseCallback;
    private OnSearchActionListener mSearchListener;
    private SearchRecentSuggestions mSuggestions;
    private boolean mIsSearching;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param client イベント検索のクライアント
     * @param responseCallback 検索のレスポンスを受け取るためのコールバック
     */
    public SearchAction(@NonNull Context context, @NonNull IEventServiceClient client, @NonNull EventResponse.Callback responseCallback) {
        Debug.assertNotNull(context);
        Debug.assertNotNull(client);
        Debug.assertNotNull(responseCallback);

        mContext = context;
        mClient = client;
        mResponseCallback = responseCallback;
        mRequest = null;
        mIsSearching = false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onQueryTextSubmit(String query) {
        // 検索中もしくはクエリが空の場合は処理を終える
        if (TextUtils.isEmpty(query) || mIsSearching) {
            return false;
        }
        mRequest = buildEventRequest(query);

        // 検索を実行する前にサーチビューを閉じておく
        mSearchView.setQuery(Strings.EMPTY, false);
        mSearchView.clearFocus();

        if (mSearchListener.onPreSearch(query)) {
            mIsSearching = true;
            mSuggestions.saveRecentQuery(query, null);
            mClient.load(mRequest);
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onQueryTextChange(String s) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onSuggestionSelect(int position) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onSuggestionClick(int position) {
        final Cursor cursor = (Cursor) mSearchView.getSuggestionsAdapter().getItem(position);
        final int columnIndex = cursor.getColumnIndex(SUGGEST_COLUMN_TEXT_1);
        onQueryTextSubmit(cursor.getString(columnIndex));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            mSearchView.onActionViewCollapsed();
            mSearchMenuItem.collapseActionView();
        }
    }

    /**
     * イベントの検索後に呼ばれる処理
     */
    public void onPostSearch() {
        mIsSearching = false;
    }

    /**
     * イベント検索リクエストを取得する
     *
     * @return イベント検索リクエスト
     */
    public EventRequest<EventResponse> getRequest() {
        return mRequest;
    }

    /**
     * {@link android.support.v7.widget.SearchView}を保持する
     *
     * @param searchView 保持する{@link SearchView}クラスのオブジェクト
     * @param searchMenuItem 保持する{@link MenuItem}クラスのオブジェクト
     */
    public void setSearchView(@NonNull SearchView searchView, @NonNull MenuItem searchMenuItem) {
        mSearchMenuItem = searchMenuItem;
        mSearchView = searchView;
        //mSearchView.setQueryHint(mContext.getString(R.string.search_query_hint_keyword));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(this);

        // サジェストを表示するための設定をする。
        final SearchableInfo searchableInfo = SystemServices.getSearchManager(mContext)
                .getSearchableInfo(((Activity) mContext).getComponentName());
        mSearchView.setSearchableInfo(searchableInfo);
        mSuggestions = RecentSuggestionsProvider.newRecentSuggestions(mContext);

        // アクションバーのアイコンを変更する。
        // ※ アクションバーの色を変更しているためSearchViewのアイコンが合わない。
        final Resources res = searchView.getResources();
        final int searchButtonId = res.getIdentifier("android:id/search_button", null, null);
        final ImageView searchButton = (ImageView) mSearchView.findViewById(searchButtonId);
        searchButton.setImageResource(R.drawable.ic_action_search);
        // 「×」ボタンのアイコンを変更する。
        final int closeButtonId = res.getIdentifier("android:id/search_close_btn", null, null);
        final ImageView closeButton = (ImageView) mSearchView.findViewById(closeButtonId);
        closeButton.setImageResource(R.drawable.ic_action_cancel);
        // 検索の下線の部分の画像を変更する。
        final int searchPlateId = res.getIdentifier("android:id/search_plate", null, null);
        final LinearLayout searchPlate = (LinearLayout) mSearchView.findViewById(searchPlateId);
        searchPlate.setBackgroundResource(R.drawable.edit_frame_holo);
        // ヒント左のアイコンを変更する。
        final int searchTextId = res.getIdentifier("android:id/search_src_text", null, null);
        final AutoCompleteTextView searchText = (AutoCompleteTextView) searchPlate.findViewById(searchTextId);
        final SpannableStringBuilder ssb = new SpannableStringBuilder("   ");
        ssb.append(mContext.getString(R.string.hint_search_query_keyword));
        final Drawable searchIcon = res.getDrawable(R.drawable.ic_action_search_hint);
        final int textSize = (int) (searchText.getTextSize() * 1.25);
        searchIcon.setBounds(0, 0, textSize, textSize);
        ssb.setSpan(new ImageSpan(searchIcon), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        searchText.setHint(ssb);
    }

    /**
     * イベント検索のリスナーを保持する
     *
     * @param searchListener イベント検索のリスナー
     */
    public void setSearchListener(@NonNull OnSearchActionListener searchListener) {
        mSearchListener = searchListener;
    }

    private EventRequest<EventResponse> buildEventRequest(String keyword) {
        Debug.assertNotNull(mResponseCallback, "検索後のコールバックを設定してください。");

        final GetRequest.Builder builder = new GetRequest.Builder()
                .append(EventServices.ATND, RequestContents.EVENT)
                .append(EventParameters.KEYWORD, keyword)
                .append(EventParameters.COUNT, String.valueOf(SettingsActivity.getLoadCount(mContext)));
        return EventResponse.createRequest(builder.build(), mResponseCallback);
    }

    private String getRequestedKeyword(IGetRequest request) {
        final UrlParameter params = request.getParameter();
        if (params.containsKey(EventParameters.KEYWORD)) {
            return params.get(EventParameters.KEYWORD);
        }
        return params.get(EventParameters.KEYWORD_OR);
    }

    private IParameterKey getKeywordKey(boolean isAndSearch) {
        return isAndSearch ? EventParameters.KEYWORD : EventParameters.KEYWORD_OR;
    }

    /**
     * イベントの検索の際に呼ばれるリスナー
     *
     * @author kumagai
     * @since 2014/03/10
     */
    public interface OnSearchActionListener {

        /**
         * イベントの検索前に呼ばれる処理。<br>
         * 検索前にインジケータを表示する際に使用する。
         *
         * @param keyword　検索キーワード
         * @return 検索を開始するかどうか。
         */
        boolean onPreSearch(String keyword);
    }
}
