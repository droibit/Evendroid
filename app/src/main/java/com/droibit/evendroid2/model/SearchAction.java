package com.droibit.evendroid2.model;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.SettingsActivity;
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

/**
 * {@link SearchView}を使用してイベント検索を行うためのクラス。
 *
 * @author kumagai
 * @since 2014/09/02
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
        return false;
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
        mSearchView.setQueryHint(mContext.getString(R.string.search_query_hint_keyword));
        mSearchView.setOnQueryTextListener(this);
        mSearchView.setOnSuggestionListener(this);
        mSearchView.setOnQueryTextFocusChangeListener(this);

        // アクションバーのアイコンを変更する。
        // ※ アクションバーの色を変更しているためSearchViewのアイコンが合わない。
        final Resources res = searchView.getResources();
        final int searchButtonId = res.getIdentifier("android:id/search_button", null, null);
        final ImageView v = (ImageView) mSearchView.findViewById(searchButtonId);
        v.setImageResource(R.drawable.ic_action_search);
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
