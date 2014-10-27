package com.droibit.evendroid2.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.droibit.app.fragment.LoadableFragment;
import com.droibit.app.fragment.dialog.DialogFramgentInfo;
import com.droibit.app.fragment.dialog.OkCancelDialogFragment;
import com.droibit.evendroid2.MainActivity;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.contoller.BookmarkableViewAdapter;
import com.droibit.evendroid2.model.BookmarkableEvent;
import com.droibit.evendroid2.model.DatabaseManager;
import com.droibit.evendroid2.model.IListableEvent;
import com.droibit.evendroid2.view.OnListItemClickListener;

import org.jdeferred.DoneCallback;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.droibit.evendroid2.MainActivity.KEY_NAVIGATION_POSITION;
import static com.droibit.evendroid2.fragment.LoadableListFragment.CallBacks;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;

/**
 * ブックマークした情報をリスト表示するためのクラス。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public class BookmarkListFragment extends LoadableFragment
        implements OnListItemClickListener, OkCancelDialogFragment.OnDialogAskListener {

    private RecyclerView mRecyclerView;
    private TextView mHeaderView;
    private BookmarkableViewAdapter mAdapter;
    private CallBacks mCallbacks;
    private IListableEvent mSelectedBookmark;

    /**
     * 新しい{@link BookmarkListFragment}を作成する
     *
     * @return 新しいフラグメントのインスタンス
     */
    public static final BookmarkListFragment newInstance() {
        final BookmarkListFragment f = new BookmarkListFragment();

        final Bundle args = new Bundle(1);
        args.putSerializable(KEY_NAVIGATION_POSITION, Navigations.BOOKMARK);
        f.setArguments(args);

        return f;
    }

    /** {@inheritDoc} */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (CallBacks) activity;

            // アクションバーのタイトルをナビゲーションに合わせる。
            final MainActivity mainActivity = (MainActivity) activity;
            mCallbacks.onApplyActionBarTitle(Navigations.BOOKMARK);
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        setRetainInstance(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mHeaderView = (TextView) view.findViewById(R.id.recycler_header);
        setListHeaderContent(0);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler);
        mAdapter = new BookmarkableViewAdapter(getActivity(), this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /** {@inheritDoc} */
    @Override
    public void onResume() {
        super.onResume();

        loadBookmarks();
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // ナビゲーションが表示されているときはメニューを上書きしないようにする。
        if (mCallbacks.isDrawerOpen()) {
            return;
        }
        inflater.inflate(R.menu.global, menu);
    }

    /** {@inheritDoc} */
    @Override
    protected View createContentView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.fragment_event_list, null);
    }

    /** {@inheritDoc} */
    @Override
    public void onItemClick(IListableEvent event) {
        mCallbacks.onSelectEvent(event.getEventId());
    }

    /** {@inheritDoc} */
    @Override
    public void onItemLongClick(IListableEvent event) {
        mSelectedBookmark = event;

        final DialogFramgentInfo info = new DialogFramgentInfo(R.string.dialog_titile_remove_bookmark_event,
                                                               R.string.dialog_content_remove_bookmark_event);
        final OkCancelDialogFragment f = OkCancelDialogFragment.newInstance(info);
        f.show(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onDialogClickOk(int dialogKey) {
        final BookmarkableEvent event = (BookmarkableEvent) mSelectedBookmark;
        event.delete();

        // 選択したブックマークを削除する。
        mAdapter.remove(event);
        mSelectedBookmark = null;

        setListHeaderContent(mAdapter.count());
    }

    /** {@inheritDoc} */
    @Override
    public void onDialogClickCancel(int dialogKey) {
        mSelectedBookmark = null;
    }

    private void loadBookmarks() {
        if (mAdapter.isEmpty()) {
            setContentShown(false);
        }

        new AndroidDeferredManager()
        .when(new DeferredAsyncTask<Void, Void, List<BookmarkableEvent>>() {
            @Override
            protected List<BookmarkableEvent> doInBackgroundSafe(Void... params) throws Exception {
                return DatabaseManager.selectAllBookmarks();
            }
        }).done(new DoneCallback<List<BookmarkableEvent>>() {
            @Override
            public void onDone(List<BookmarkableEvent> result) {
                onLoadBookmarkedEvents(result);
            }
        });
    }

    private void onLoadBookmarkedEvents(final List<BookmarkableEvent> events) {
        if (mAdapter.isEmpty()) {
            // ブックマークしたイベントが何もない場合
            if ((events == null || events.isEmpty())) {
                setListHeaderContent(0);
            } else {
                // ブックマークが何もなかった場合
                mAdapter.addAll(events);
                setListHeaderContent(events.size());
            }
            setContentShown(true);
            return;
        }

        final List<BookmarkableEvent> newEvents = new ArrayList<BookmarkableEvent>(events);
        final List<BookmarkableEvent> oldEvents = new ArrayList<BookmarkableEvent>(mAdapter.getEvents());
        if (newEvents.size() != oldEvents.size()) {
            // ブックマーク数が増減した場合はリストの内容を入れ替える。
            mAdapter.replace(newEvents);
        } else {
            // ブックマーク数が同じ場合は項目が変わっている場合のみリストを入れ替える。
            Collections.sort(newEvents);
            Collections.sort(oldEvents);
            for (int i = 0, size = newEvents.size(); i < size; i++) {
                if (!newEvents.get(i).equals(oldEvents.get(i))) {
                    mAdapter.replace(newEvents);
                    break;
                }
            }
        }
        setListHeaderContent(mAdapter.count());

        //setContentShown(true);
    }

    private void setListHeaderContent(int bookmarkCount) {
        if (bookmarkCount == 0) {
            mHeaderView.setText(R.string.event_bookmark_list_empty_header);
        } else {
            mHeaderView.setText(getString(R.string.event_bookmark_list_header, bookmarkCount));
        }
    }
}
