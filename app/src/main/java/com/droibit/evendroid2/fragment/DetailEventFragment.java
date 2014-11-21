package com.droibit.evendroid2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.droibit.app.fragment.LoadableFragment;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.contoller.DetailedContainerViewAdapter;
import com.droibit.evendroid2.model.DetailMenuAction;
import com.droibit.evendroid2.model.DetailedEvent;
import com.droibit.evendroid2.model.DetailedGroupBuilder;
import com.droibit.evendroid2.model.RefreshAction;
import com.droibit.evendroid2.view.OnTransitionListener;
import com.droibit.eventservice.EventServiceClient;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.network.Reachability;
import com.droibit.utils.Debug;
import com.droibit.widget.ToastManager;

/**
 * イベントの詳細情報を表示するためのフラグメント。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public class DetailEventFragment extends LoadableFragment
        implements DetailMenuAction.OnRefreshActionListener, OnTransitionListener {

    private static final String KEY_EVENT_ID = "event_id";
    private static final String KEY_SHOW_OWNER_EVENTS = "show_owner_events";
    private static final String KEY_GROPUED_EVENT_ITEMS = "grouped_event_items";

    private EventServiceClient mClient;
    private RecyclerView mRecyclerView;
    private DetailedContainerViewAdapter mAdapter;
    private DetailMenuAction mAction;
    private String mEventId;
    private boolean mShowOwnerEvents;
    private boolean mExistView;

    /** 検索のレスポンスを受け取るためのコールバック */
    private EventResponse.Callback mResponseCallback = new  EventResponse.Callback() {
        /** {@inheritDoc} */
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Debug.log(volleyError.getLocalizedMessage());

            if (mExistView) {
                mAction.onPostRefresh();
                setContentShown(true);
            }
            // エラーメッセージを表示する。
            ToastManager.showShort(getActivity(), R.string.toast_failed_to_fetch_event);
        }

        /** {@inheritDoc} */
        @Override
        public void onResponse(EventResponse listableEventResponse) {
            mAction.onPostRefresh();

            // ビューが存在しない場合は処理をスキップする
            if (!mExistView) {
                return;
            }

            // レスポンスにイベント情報が存在しない場合
            if (!listableEventResponse.existsEvent()) {
                // エラーメッセージを表示する。
                ToastManager.showShort(getActivity(), R.string.toast_failed_to_fetch_event);
                setContentShown(true);
                return;
            }

            final EventResponse.EventContainer[] responseEvents = listableEventResponse.events;
            final DetailedEvent event = new DetailedEvent(getActivity(), responseEvents[0].event);
            mAction.setDetailedEvent(event);

            final DetailedEvent.Group[] groups = DetailedGroupBuilder.build(getActivity(), event,
                    DetailEventFragment.this, mShowOwnerEvents);
            mAdapter.replace(groups);

            setContentShown(true);
        }
    };

    /**
     * 新しいインスタンスを作成する。
     *
     * @param eventId 詳細を表示するイベントの識別ID
     * @param showOwnerEvents 主催者イベントを表示するかどうか
     * @return 新しいインスタンスを作成する
     */
    public static final DetailEventFragment newInstance(String eventId, boolean showOwnerEvents) {
        final DetailEventFragment f = new DetailEventFragment();
        final Bundle args = new Bundle(2);
        args.putString(KEY_EVENT_ID, eventId);
        args.putBoolean(KEY_SHOW_OWNER_EVENTS, showOwnerEvents);
        f.setArguments(args);

        return f;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new EventServiceClient(getActivity());
        mEventId = getArguments().getString(KEY_EVENT_ID);
        mShowOwnerEvents = getArguments().getBoolean(KEY_SHOW_OWNER_EVENTS);

        mAction = new DetailMenuAction(getActivity(), mClient, mResponseCallback);
        mAction.setListener(this);
        mAction.setCondition(new RefreshAction.Condition(EventParameters.EVENT_ID, mEventId));

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAdapter = new DetailedContainerViewAdapter(getActivity());
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mExistView = true;
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAction.onRefresh();
    }

    /** {@inheritDoc} */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mExistView = false;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail, menu);

        // ブックマークのチェックの有無を変更する。
        mAction.toggleBookmarkCheck(menu.getItem(2), getArguments().getString(KEY_EVENT_ID));
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mAction.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onDetach() {
        super.onDetach();

        mClient.stop();
    }

    /** {@inheritDoc} */
    @Override
    protected View createContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_event_detail, null);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onPreRefresh() {
        // インターネットに接続されていない場合
        if (!Reachability.isConnected(getActivity())) {
            ToastManager.showLong(getActivity(), R.string.toast_failed_to_connect_network);
            return false;
        }

        // 検索中はインジケータを表示する
        setContentShown(false);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void onTransitionUsers(String eventId) {
        final UserListFragment f = UserListFragment.newInstance(eventId);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, f)
                .addToBackStack(null)
                .commit();
    }

    /** {@inheritDoc} */
    @Override
    public void onTransitionOwnerEvents(String owner) {
        final EventListFragment f = EventListFragment.newOwerEventListFragment(owner);
        getFragmentManager().beginTransaction()
                .replace(R.id.container, f)
                .addToBackStack(null)
                .commit();
    }
}
