package com.droibit.evendroid2.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.VolleyError;
import com.droibit.evendroid2.MainActivity;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.SettingsActivity;
import com.droibit.evendroid2.model.ListableEvent;
import com.droibit.evendroid2.model.RefreshAction;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.eventservice.http.url.IParameterKey;
import com.droibit.network.Reachability;
import com.droibit.utils.Debug;
import com.droibit.widget.ToastManager;

import static com.droibit.evendroid2.MainActivity.KEY_NAVIGATION_POSITION;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;
import static com.droibit.evendroid2.contoller.ListableEventViewAdapter.KEY_SHOW_AVAILABLE_ICON;

/**
 * 主催/参加イベントをリスト表示するための
 *
 * @author kumagai
 * @since 2014/09/02
 */
public class EventListFragment extends LoadableListFragment
        implements RefreshAction.OnRefreshActionListener {

    public static final int EVENT_TYPE_OWNER = 0;
    public static final int EVENT_TYPE_PARTICIPATION = 1;

    private static final String KEY_EVENT_TYPE = "event_type";
    private static final String KEY_EXTERNAL_USER_NAME = "external_user_name";

    private RefreshAction mAction;
    private int mEventType;

    /** 検索のレスポンスを受け取るためのコールバック */
    private EventResponse.Callback mResponseCallback = new  EventResponse.Callback() {

        /** {@inheritDoc} */
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Debug.log(volleyError.getLocalizedMessage());
            mAction.onPostRefresh();

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
            mAction.onPostRefresh();
            mAdapter.clear();

            // レスポンスにイベント情報が存在しない場合
            if (!listableEventResponse.existsEvent()) {
                mAdapter.notifyDataSetChanged();
                setListHeaderContent(0);
                setContentShown(true);
                return;
            }

            final EventResponse.EventContainer[] responseEvents = listableEventResponse.events;
            for (EventResponse.EventContainer eventContainer : responseEvents) {
                mAdapter.add(new ListableEvent(eventContainer.event));
            }
            mAdapter.notifyDataSetChanged();

            setListHeaderContent(mAdapter.getEvents().size());
            setContentShown(true);
        }
    };

    /**
     * 新しい{@link EventListFragment}を作成する
     *
     * @param eventType 表示するイベントの種類
     * @return 新しいフラグメントのインスタンス
     */
    public static final EventListFragment newInstance(int eventType) {
        final EventListFragment f = new EventListFragment();

        final Bundle args = new Bundle(3);
        args.putBoolean(KEY_SHOW_AVAILABLE_ICON, false);
        args.putInt(KEY_EVENT_TYPE, eventType);
        args.putSerializable(KEY_NAVIGATION_POSITION,
            (eventType == EVENT_TYPE_OWNER) ? Navigations.OWNER : Navigations.PARTICIPATION);
        f.setArguments(args);

        return f;
    }

    /**
     * 新しい{@link EventListFragment}を作成する。<br>
     * イベントの種類は主催者のイベントで外部から指定する
     *
     * @param owner 主催者名
     * @return 新しいフラグメントのインスタンス
     */
    public static final EventListFragment newOwerEventListFragment(String owner) {
        final EventListFragment f = new EventListFragment();

        final Bundle args = new Bundle(4);
        args.putBoolean(KEY_SHOW_AVAILABLE_ICON, false);
        args.putInt(KEY_EVENT_TYPE, EVENT_TYPE_OWNER);
        args.putSerializable(KEY_NAVIGATION_POSITION,  Navigations.OWNER);
        args.putString(KEY_EXTERNAL_USER_NAME, owner);

        f.setArguments(args);

        return f;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mEventType = getArguments().getInt(KEY_EVENT_TYPE);
        mAction = new RefreshAction(getActivity(), mClient, mResponseCallback);
        mAction.setListener(this);

        // 主催/参加イベントを切り替えて検索する。
        final IParameterKey key = getConditionParameterKey();

        // 外部から主催者名が設定されている場合
        if (getArguments().containsKey(KEY_EXTERNAL_USER_NAME)) {
            final String user = getArguments().getString(KEY_EXTERNAL_USER_NAME);
            mAction.setCondition(new RefreshAction.Condition(key, user));
            return;
        }

        final String user = SettingsActivity.getUserName(getActivity());
        if (!TextUtils.isEmpty(user)) {
            mAction.setCondition(new RefreshAction.Condition(key, user));
        } else {
            ToastManager.showShort(getActivity(), R.string.toast_not_exist_username);
        }

    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setListHeaderContent(0);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAction.onRefresh();
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // ナビゲーションが表示されているときはメニューを上書きしないようにする。
        if (mCallbacks.isDrawerOpen()) {
            return;
        }
        inflater.inflate(R.menu.list, menu);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return mAction.onOptionsItemSelected(item);
    }


    /** {@inheritDoc} */
    @Override
    public boolean onPreRefresh() {
        // インターネットに接続されていない場合
        if (!Reachability.isConnected(getActivity())) {
            ToastManager.showLong(getActivity(), R.string.toast_failed_to_connect_network);
            return false;
        }

        // ユーザ名が設定されていない場合（※ 外部の主催者が設定されている場合は除く）
        final String user = SettingsActivity.getUserName(getActivity());
        if (TextUtils.isEmpty(user) && !getArguments().containsKey(KEY_EXTERNAL_USER_NAME)) {
            ToastManager.showShort(getActivity(), R.string.toast_not_exist_username);
            return false;
        }

        // 設定画面から戻ってきた後更新された時のために確認する
        // TODO: 設定するユーザ名を切り分けるかどうか？
        if (!mAction.hasCondition()) {
            final IParameterKey key = getConditionParameterKey();
            mAction.setCondition(new RefreshAction.Condition(key, user));
        }

        // 検索中はインジケータを表示する
        setContentShown(false);
        return true;
    }

    private void setListHeaderContent(int eventCount) {
        if (eventCount == 0) {
            if (mEventType == EVENT_TYPE_OWNER) {
                mHeaderView.setText(R.string.event_owner_list_empty_header);
            } else {
                mHeaderView.setText(R.string.event_participation_list_empty_header);
            }
            return;
        }

        final String text;
        if (mEventType == EVENT_TYPE_OWNER) {
            text = getString(R.string.event_owner_list_header, eventCount);
        } else {
            text = getString(R.string.event_participation_list_header, eventCount);
        }
        mHeaderView.setText(text);
    }

    private IParameterKey getConditionParameterKey() {
        return (mEventType == EVENT_TYPE_OWNER)
                ? EventParameters.OWNER_NICKNAME : EventParameters.NICKNAME;
    }
}
