package com.droibit.evendroid2.contoller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.DetailedEvent;
import com.linearlistview.LinearListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@link DetailedEvent.Group}の情報をビューに表示するためのアダプタ。
 *
 * @author kumagai
 * @since 2014/09/06.
 */
public class DetailedContainerViewAdapter extends RecyclerView.Adapter<DetailedContainerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<DetailedEvent.Group> mGroups;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public DetailedContainerViewAdapter(@NonNull Context context) {
        mContext = context;
        mGroups = new ArrayList<DetailedEvent.Group>(4);
    }

    /** {@inheritDoc} */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(mContext);

        final View view = inflater.inflate(R.layout.recycler_container_detail, parent, false);
        return new ViewHolder(view);
    }

    /** {@inheritDoc} */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        viewHolder.update(mGroups.get(position));
    }

    /** {@inheritDoc} */
    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    /**
     * 詳細項目のグルーピングリストを取得する。
     *
     * @return グルーピングしたリスト
     */
    public List<DetailedEvent.Group> getGroups() {
        return mGroups;
    }

    /**
     * 表示する項目を全て置き換える。
     *
     * @param groups 表示する項目
     */
    public void replace(DetailedEvent.Group[] groups) {
        mGroups.clear();
        mGroups.addAll(Arrays.asList(groups));
        notifyDataSetChanged();
    }

    /**
     * 表示する項目が存在しているかどうか
     *
     * @return trueの場合存在し、falseの場合存在しない
     */
    public boolean isEmpty() {
        return mGroups.isEmpty();
    }

    /**
     * イベントをリスト表示するためのビュー。
     */
    class ViewHolder extends RecyclerView.ViewHolder implements LinearListView.OnItemClickListener {

        private final TextView mHeaderView;
        private final LinearListView mListView;
        private final DetailedItemViewAdapter mAdapter;

        ViewHolder(View itemView) {
            super(itemView);

            mAdapter = new DetailedItemViewAdapter(mContext);

            mHeaderView = (TextView) itemView.findViewById(R.id.header);
            mListView = (LinearListView) itemView.findViewById(android.R.id.list);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(this);
        }

        /** {@inheritDoc} */
        @Override
        public void onItemClick(LinearListView parent, View view, int position, long id) {
            final DetailedEvent.Group group = mGroups.get(getPosition());
            final DetailedEvent.Item item = group.getItems().get(position);
            if (item.getListener() != null) {
                item.getListener().onClick();
            }
        }

        void update(DetailedEvent.Group group) {
            mHeaderView.setText(group.getTitle());

            mAdapter.clear();
            mAdapter.addAll(group.getItems());
        }
    }
}
