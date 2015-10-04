package com.droibit.evendroid2.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.BookmarkableEvent;
import com.droibit.evendroid2.view.OnEventItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link BookmarkableEvent}クラスの情報をビューに設定するためのアダプタ。
 * ActionModeを使用したかったため、クラスを分けている。
 *
 * @author kumagai
 * @since 2014/09/02
 */
public class BookmarkableViewAdapter extends RecyclerView.Adapter<BookmarkableViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<BookmarkableEvent> mEvents;
    private final SparseBooleanArray mSelectedItems;
    private final OnEventItemClickListener mListener;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param listener クリックイベントリスナ
     */
    public BookmarkableViewAdapter(@NonNull Context context, @NonNull OnEventItemClickListener listener) {
        mContext = context;
        mEvents = new ArrayList<BookmarkableEvent>();
        mSelectedItems = new SparseBooleanArray();
        mListener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.recycler_item_event, parent, false);
        return new ViewHolder(view);
    }

    /** {@inheritDoc} */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.update(mEvents.get(position));
    }

    /** {@inheritDoc} */
    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    /**
     * ブックマーク情報を全て削除する。
     */
    public void clear() {
        mEvents.clear();
    }

    /**
     * リストの項目が空かどうか。
     *
     * @return trueの場合は空、falseの場合存在する
     */
    public boolean isEmpty() {
        return mEvents.isEmpty();
    }

    /**
     * リストの項目数を取得する。
     *
     * @return リストの項目数
     */
    public int count() {
        return mEvents.size();
    }

    /**
     * ブックマーク情報を削除する。
     *
     * @param event ブックマーク情報
     * @return  削除した項目のインデックス
     */
    public void remove(BookmarkableEvent event) {
        final BookmarkableEvent bookmark = (BookmarkableEvent) event;
        final int position = mEvents.indexOf(bookmark);
        if (position != -1) {
            mEvents.remove(position);
            notifyItemRemoved(position);
        }
    }

    /**
     * 複数のブックマークを削除する。
     *
     * @param events 削除対象のブックマーク
     */
    public void removeAll(List<BookmarkableEvent> events) {
        mEvents.removeAll(events);
        notifyDataSetChanged();
    }

    /**
     * 複数のブックマークを追加する。
     *
     * @param events 追加対象のブックマーク
     */
    public void addAll(@NonNull List<BookmarkableEvent> events) {
        mEvents.addAll(events);
        notifyDataSetChanged();
    }

    /**
     * ブックマークを置き換える。
     *
     * @param events 対象のブックマーク
     */
    public void replace(@NonNull List<BookmarkableEvent> events) {
        mEvents.clear();
        addAll(events);
    }

    /**
     * ブックマークしたイベントリストを取得する。
     *
     * @return ブックマークしたイベントのリスト
     */
    @NonNull
    public List<BookmarkableEvent> getEvents() {
        return mEvents;
    }

    /**
     * イベントをリスト表示するためのビュー。
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final TextView titleView;
        final TextView startAtView;
        final TextView addressView;

        ViewHolder(View itemView) {
            super(itemView);

            titleView = (TextView) itemView.findViewById(R.id.text_title);
            startAtView = (TextView) itemView.findViewById(R.id.text_startat);
            addressView = (TextView) itemView.findViewById(R.id.text_address);

            //final View containerView = itemView.findViewById(R.id.container);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        /** {@inheritDoc} */
        @Override
        public void onClick(View v) {
            mListener.onItemClick(mEvents.get(getPosition()));
        }

        /** {@inheritDoc} */
        @Override
        public boolean onLongClick(View v) {
            mListener.onItemLongClick(mEvents.get(getPosition()));
            return true;
        }

        void update(BookmarkableEvent event) {
            titleView.setText(event.getTitle());
            startAtView.setText(event.getStartedAt());
            addressView.setText(event.getAddress());
        }
    }
}
