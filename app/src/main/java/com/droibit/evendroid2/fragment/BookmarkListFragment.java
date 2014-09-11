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
import com.droibit.evendroid2.model.BookmarkableEvent;
import com.droibit.evendroid2.model.DatabaseManager;
import com.droibit.evendroid2.model.IListableEvent;
import com.droibit.evendroid2.contoller.BookmarkableViewAdapter;
import com.droibit.evendroid2.view.OnListItemClickListener;
import com.droibit.utils.Debug;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

import java.util.List;

import static com.droibit.evendroid2.MainActivity.KEY_NAVIGATION_POSITION;
import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;
import static com.droibit.evendroid2.fragment.LoadableListFragment.CallBacks;

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

        loadBookmarks();

        setRetainInstance(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onResume() {
        super.onResume();
    }

    /** {@inheritDoc} */
    @Override
    public void onPause() {
        super.onPause();
    }



    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
    public void onClickOk() {
        final BookmarkableEvent bookmark = (BookmarkableEvent) mSelectedBookmark;
        bookmark.delete();
        mAdapter.remove(mSelectedBookmark);
        mSelectedBookmark = null;

        setListHeaderContent(mAdapter.count());
    }

    /** {@inheritDoc} */
    @Override
    public void onClickCancel() {
        mSelectedBookmark = null;
    }

    private void loadBookmarks() {
        new AndroidDeferredManager()
        .when(new DeferredAsyncTask<Void, Void, List<BookmarkableEvent>>() {
            @Override
            protected List<BookmarkableEvent> doInBackgroundSafe(Void... params) throws Exception {
                return DatabaseManager.selectAllBookmarks();
            }
        }).done(new DoneCallback<List<BookmarkableEvent>>() {
            @Override
            public void onDone(List<BookmarkableEvent> result) {
                if (result != null && !result.isEmpty()) {
                    mAdapter.clear();
                    mAdapter.addAll(result);
                    setListHeaderContent(result.size());
                } else {
                    setListHeaderContent(0);
                }
            }
        }).fail(new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Debug.log(result.getMessage());
                //ToastManager.showShort(getActivity(), R.string.failed);
            }
        });
    }

    private void setListHeaderContent(int bookmarkCount) {
        if (bookmarkCount == 0) {
            mHeaderView.setText(R.string.event_bookmark_list_empty_header);
        } else {
            mHeaderView.setText(getString(R.string.event_bookmark_list_header, bookmarkCount));
        }
    }
}
