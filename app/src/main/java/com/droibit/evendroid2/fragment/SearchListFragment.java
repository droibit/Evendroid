package com.droibit.evendroid2.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.android.volley.VolleyError;
import com.droibit.evendroid2.MainActivity;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.ListableEvent;
import com.droibit.evendroid2.model.SearchAction;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.network.Reachability;
import com.droibit.utils.Debug;
import com.droibit.widget.ToastManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.droibit.evendroid2.MainActivity.KEY_NAVIGATION_POSITION;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;
import static com.droibit.evendroid2.contoller.ListableEventViewAdapter.KEY_SHOW_AVAILABLE_ICON;

/**
 * イベントを検索するためのフラグメント。
 *
 * @author kumagai
 * @since 2014/09/02
 */
public class SearchListFragment extends LoadableListFragment
        implements SearchAction.OnSearchActionListener {

    private SearchAction mSearchAction;
    private MenuItem mSearchMenuItem;

    /** 検索のレスポンスを受け取るためのコールバック */
    private EventResponse.Callback mResponseCallback = new  EventResponse.Callback() {

        /** {@inheritDoc} */
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Debug.log(volleyError.getLocalizedMessage());

            mSearchAction.onPostSearch();
            // エラーが発生した場合はコンテンツを非表示にする
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            setContentShown(true);
            // エラーメッセージを表示する。
            ToastManager.showShort(getActivity(), R.string.toast_failed_to_search_event);
        }

        /** {@inheritDoc} */
        @Override
        public void onResponse(EventResponse listableEventResponse) {
            mSearchAction.onPostSearch();
            mAdapter.clear();

            // レスポンスにイベント情報が存在しない場合
            if (!listableEventResponse.existsEvent()) {
                // リストにはイベント情報を何も表示しない。
                mAdapter.notifyDataSetChanged();
                setContentShown(true);
                ToastManager.showShort(getActivity(), R.string.toast_not_find_results);
                return;
            }

            // レスポンスからリスト表示するイベント群を作成する。
            final EventResponse.EventContainer[] responseEvents = listableEventResponse.events;
            final List<ListableEvent> events = new ArrayList<ListableEvent>(responseEvents.length);
            for (EventResponse.EventContainer eventContainer : responseEvents) {
                events.add(new ListableEvent(eventContainer.event));
            }
            // キーワード検索の場合ソートされていないので日付順にソートする。
            Collections.sort(events);
            mAdapter.addAll(events);
            mAdapter.notifyDataSetChanged();

            // スクロールの位置を先頭に戻す
            mRecyclerView.scrollToPosition(0);

            setContentShown(true);
        }
    };

    /**
     * 新しい{@link SearchListFragment}を作成する
     *
     * @return 新しいフラグメントのインスタンス
     */
    public static final SearchListFragment newInstance() {
        final SearchListFragment f = new SearchListFragment();

        final Bundle args = new Bundle(2);
        args.putBoolean(KEY_SHOW_AVAILABLE_ICON, true);
        args.putSerializable(KEY_NAVIGATION_POSITION, Navigations.SEARCH);
        f.setArguments(args);

        return f;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSearchAction = new SearchAction(getActivity(), mClient, mResponseCallback);
        mSearchAction.setSearchListener(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderView.setText(R.string.event_search_list_header);
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // ナビゲーションが表示されているときはメニューを上書きしないようにする。
        if (mCallbacks.isDrawerOpen()) {
            return;
        }

        inflater.inflate(R.menu.search, menu);
        mSearchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) mSearchMenuItem.getActionView();
        mSearchAction.setSearchView(searchView, mSearchMenuItem);
    }

    /** {@inheritDoc} */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onPreSearch(String query) {
        // インターネットに接続されていない場合
        if (!Reachability.isConnected(getActivity())) {
            ToastManager.showLong(getActivity(), R.string.toast_failed_to_connect_network);
            return false;
        }

        // 検索中はインジケータを表示する
        setContentShown(false);
        mHeaderView.setText(query);
        return true;
    }
}
