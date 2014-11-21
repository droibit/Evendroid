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
import com.droibit.evendroid2.model.ListableUser;
import com.droibit.evendroid2.view.OnUserItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link ListableUser}クラスの情報をビューに設定するためのアダプタ。
 *
 * @author kumagai
 * @since 2014/10/28
 */
public class ListableUserViewAdapter extends RecyclerView.Adapter<ListableUserViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<ListableUser> mUsers;
    private final OnUserItemClickListener mListener;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     * @param listener クリックイベントリスナ
     */
    public ListableUserViewAdapter(@NonNull Context context, @NonNull OnUserItemClickListener listener) {
        mContext = context;
        mUsers = new ArrayList<ListableUser>();
        mListener = listener;
    }

    /** {@inheritDoc} */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View itemView = LayoutInflater.from(mContext).inflate(R.layout.recycler_item_iconified_user, parent, false);
        return new ViewHolder(itemView);
    }

    /** {@inheritDoc} */
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        final ListableUser user = mUsers.get(position);

        if (user.isParticipate()) {
            viewHolder.iconView.setImageResource(R.drawable.ic_user);
        } else {
            viewHolder.iconView.setImageResource(R.drawable.ic_user_bad);
        }
        viewHolder.titleView.setText(user.getName());
    }

    /** {@inheritDoc} */
    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    /**
     * 参加者情報を全て削除する。
     */
    public void clear() {
        mUsers.clear();
        notifyDataSetChanged();
    }

    /**
     * 参加者情報を全て新規追加する。
     *
     * @param users 参加者情報
     */
    public void addAll(List<ListableUser> users) {
        mUsers.addAll(users);
        notifyDataSetChanged();
    }

    /**
     * 参加者情報をリスト表示するためのビュー。
     */
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final ImageView iconView;
        final TextView titleView;

        ViewHolder(View itemView) {
            super(itemView);

            iconView = (ImageView) itemView.findViewById(R.id.image_icon);
            titleView = (TextView) itemView.findViewById(R.id.text_title);

            itemView.setOnClickListener(this);
        }

        /** {@inheritDoc} */
        @Override
        public void onClick(View v) {
            mListener.onClickItem(mUsers.get(getPosition()));
        }
    }
}
