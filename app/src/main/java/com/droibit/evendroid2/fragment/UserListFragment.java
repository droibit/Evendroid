package com.droibit.evendroid2.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.droibit.app.fragment.LoadableFragment;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.contoller.ListableUserViewAdapter;
import com.droibit.evendroid2.fragment.dialog.AccountDialogFragment;
import com.droibit.evendroid2.model.ListableEvent;
import com.droibit.evendroid2.model.ListableUser;
import com.droibit.evendroid2.view.OnUserItemClickListener;
import com.droibit.eventservice.EventServiceClient;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.events.atnd.UserResponse;
import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.url.EventParameters;
import com.droibit.eventservice.http.url.RequestContents;
import com.droibit.utils.Debug;
import com.droibit.widget.ToastManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * イベントの参加者を表示する目のフラグメント。
 *
 * @author kumagai
 * @since 2014/10/28
 */
public class UserListFragment extends LoadableFragment implements OnUserItemClickListener {

    private static final String KEY_EVENT_ID = "eventid";

    private RecyclerView mRecyclerView;
    private TextView mHeaderView;
    private ListableUserViewAdapter mAdapter;
    private EventServiceClient mClient;
    private boolean mExistsView;

    private UserResponse.Callback mResponseCallback = new UserResponse.Callback() {
        /** {@inheritDoc} */
        @Override
        public void onErrorResponse(VolleyError error) {
            Debug.log(error.getLocalizedMessage());

            if (mExistsView) {
                mAdapter.clear();
                setContentShownNoAnimation(true);
            }
            // エラーメッセージを表示する。
            ToastManager.showShort(getActivity(), R.string.toast_failed_to_fetch_user);
        }

        /** {@inheritDoc} */
        @Override
        public void onResponse(UserResponse response) {
            // ビューが破棄されている場合は処理をスキップする。
            if (!mExistsView) {
                return;
            }

            // イベントもしくは参加者情報が存在しない場合
            if (!response.existsEvent() || !response.events[0].event.existsUser()) {
                mAdapter.clear();
                setListHeaderContent(0);
                setContentShown(true);
                return;
            }

            final UserResponse.Event responseEvent = response.events[0].event;
            final List<ListableUser> users = new ArrayList<ListableUser>(responseEvent.users.length);
            for (UserResponse.UserContainer container : responseEvent.users) {
                users.add(new ListableUser(container.user));
            }
            Collections.sort(users);
            mAdapter.addAll(users);
            setListHeaderContent(mAdapter.getItemCount());
            setContentShown(true);
        }
    };

    /**
     * 新しいインスタンスを作成する
     *
     * @param eventID 参加者を取得するイベントの識別ID
     * @return 新しいインスタンス
     */
    public static final UserListFragment newInstance(String eventID) {
        final Bundle args = new Bundle(1);
        args.putString(KEY_EVENT_ID, eventID);

        final UserListFragment f = new UserListFragment();
        f.setArguments(args);
        return f;
    }

    /** {@inheritDoc} */
    @Override
    protected View createContentView(Context context) {
        final View contentView = LayoutInflater.from(context).inflate(
                R.layout.fragment_event_list, null);
        mHeaderView = (TextView) contentView.findViewById(R.id.recycler_header);
        mRecyclerView = (RecyclerView) contentView.findViewById(R.id.recycler);

        return contentView;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mClient = new EventServiceClient(getActivity());

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mAdapter = new ListableUserViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mExistsView = true;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.global, menu);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final String eventID = getArguments().getString(KEY_EVENT_ID);
        final IGetRequest getRequest = new GetRequest.Builder()
                .append(EventServices.ATND, RequestContents.USER)
                .append(EventParameters.EVENT_ID, eventID)
                .build();
        mClient.load(UserResponse.createRequest(getRequest, mResponseCallback));
        setContentShown(false);
    }

    /** {@inheritDoc} */
    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mExistsView = false;
    }

    /** {@inheritDoc} */
    @Override
    public void onDetach() {
        super.onDetach();
        // フラグメントがリプレイスされた場合にリクエストを止めるようにする。
        mClient.stop();
    }

    /** {@inheritDoc} */
    @Override
    public void onClickItem(ListableUser user) {
        AccountDialogFragment.newInstance(user)
                .show(getFragmentManager());
    }

    private void setListHeaderContent(int eventCount) {
        if (eventCount == 0) {
            mHeaderView.setText(R.string.event_user_list_empty_header);
            return;
        }

        final String text = getString(R.string.event_user_list_header, eventCount);
        mHeaderView.setText(text);
    }
}
