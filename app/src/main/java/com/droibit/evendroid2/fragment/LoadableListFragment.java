package com.droibit.evendroid2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.droibit.app.fragment.LoadableFragment;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.contoller.ListableEventViewAdapter;
import com.droibit.evendroid2.model.IListableEvent;
import com.droibit.evendroid2.view.OnListItemClickListener;
import com.droibit.eventservice.EventServiceClient;
import com.droibit.widget.ToastManager;

import static com.droibit.evendroid2.MainActivity.KEY_NAVIGATION_POSITION;
import static com.droibit.evendroid2.contoller.ListableEventViewAdapter.KEY_SHOW_AVAILABLE_ICON;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoadableListFragment.CallBacks} interface
 * to handle interaction events.
 */
public class LoadableListFragment extends LoadableFragment implements OnListItemClickListener {

    protected RecyclerView mRecyclerView;
    protected ListableEventViewAdapter mAdapter;
    protected EventServiceClient mClient;
    protected TextView mHeaderView;
    protected CallBacks mCallbacks;
    protected boolean mExistsView;

    public LoadableListFragment() {
        // Required empty public constructor
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

        final boolean showIcon = getArguments().getBoolean(KEY_SHOW_AVAILABLE_ICON);
        mAdapter = new ListableEventViewAdapter(getActivity(), this, showIcon);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);

        mExistsView = true;
    }

    /** {@inheritDoc} */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (CallBacks) activity;

            // アクションバーのタイトルをナビゲーションに合わせる。
            final Navigations index = (Navigations) getArguments().getSerializable(KEY_NAVIGATION_POSITION);
            mCallbacks.onApplyActionBarTitle(index);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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
        mCallbacks = null;

        // フラグメントがリプレイスされた場合にリクエストを止めるようにする。
        mClient.stop();
    }

    /** {@inheritDoc} */
    @Override
    public void onItemClick(IListableEvent event) {
        mCallbacks.onSelectEvent(event.getEventId());
    }

    /** {@inheritDoc} */
    @Override
    public void onItemLongClick(IListableEvent event) {
        ToastManager.showLong(getActivity(), event.getTitle());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface CallBacks {

        /**
         * リストのイベントが選択された時に呼ばれる処理
         *
         * @param eventId 選択されたイベントの識別ID
         */
        void onSelectEvent(String eventId);

        /**
         * フラグメントがアタッチされた際にアクションバー名を変更する
         *
         * @param navigation
         */
        void onApplyActionBarTitle(Navigations navigation);

        /**
         * ナビゲーションが表示されているかどうか
         *
         * @return trueの場合表示されており、falseの場合表示されていない
         */
        boolean isDrawerOpen();
    }
}
