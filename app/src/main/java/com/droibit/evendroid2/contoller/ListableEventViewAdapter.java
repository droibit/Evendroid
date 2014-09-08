package com.droibit.evendroid2.contoller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.ListableEvent;
import com.droibit.evendroid2.view.input.OnListItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ListableEvent}クラスの情報をビューに設定するためのアダプタ。
 * 
 * @author kumagai 
 * @since 2014/09/02
 */
public class ListableEventViewAdapter extends RecyclerView.Adapter<ListableEventViewAdapter.ViewHolder> {

    public static final String KEY_SHOW_AVAILABLE_ICON = "show_available_icon";

    private static final int VIEW_TYPE_ICONIFIED_TEXT = 0;
    private static final int VIEW_TYPE_TEXT = 1;

    private final Context mContext;
    private final List<ListableEvent> mEvents;
    private final OnListItemClickListener mListener;
    private final boolean mShowAvailableIcon;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param listener クリックイベントリスナ
     * @param showAvailableIcon リストにアイコンを表示するかどうか
     */
    public ListableEventViewAdapter(@NonNull Context context, @NonNull OnListItemClickListener listener, boolean showAvailableIcon) {
        mContext = context;
        mEvents = new ArrayList<ListableEvent>();
        mListener = listener;
        mShowAvailableIcon = showAvailableIcon;
    }

    /** {@inheritDoc} */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);

        final View view;
        if (viewType == VIEW_TYPE_ICONIFIED_TEXT) {
            view = inflater.inflate(R.layout.recycler_item_iconified_list, parent, false);
        } else {
            view = inflater.inflate(R.layout.recycler_item_list, parent, false);
        }
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

    /** {@inheritDoc} */
    @Override
    public int getItemViewType(int position) {
        return mShowAvailableIcon ? VIEW_TYPE_ICONIFIED_TEXT : VIEW_TYPE_TEXT;
    }

    /**
     * 新しい項目を追加する。
     *
     * @param item 新しいイベント情報
     */
    public void add(@NonNull ListableEvent item) {
        mEvents.add(item);
    }


    /**
     * イベント情報を全て削除する。
     */
    public void clear() {
        mEvents.clear();
    }

    /**
     * イベントのリストを取得する。
     *
     * @return イベントのリスト
     */
    public List<ListableEvent> getEvents() {
        return mEvents;
    }

    /**
     * イベントをリスト表示するためのビュー。
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        final ImageView iconView;
        final TextView titleView;
        final TextView startAtView;
        final TextView addressView;

        ViewHolder(View itemView) {
            super(itemView);

            iconView = (ImageView) itemView.findViewById(R.id.image_available);
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

        void update(ListableEvent event) {
            if (iconView != null) {
                iconView.setImageResource(event.isAvairable() ? R.drawable.ic_available_event :
                                                                R.drawable.ic_unavailable_event);
            }
            titleView.setText(event.getTitle());
            startAtView.setText(event.getStartedAt());
            addressView.setText(event.getAddress());
        }
    }
}
