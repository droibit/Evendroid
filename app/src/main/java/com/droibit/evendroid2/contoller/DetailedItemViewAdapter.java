package com.droibit.evendroid2.contoller;

import android.app.ActionBar;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.view.ViewGroup.LayoutParams;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.DetailedEvent;
import com.droibit.evendroid2.model.DetailedEvent.Item;
import com.droibit.evendroid2.view.DetailedItemView;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link DetailedEvent.Item}の情報をビューに表示するためのアダプタ。
 *
 * @author kumagai
 * @since 2014/09/06.
 */
public class DetailedItemViewAdapter extends BaseAdapter {

    private final Context mContext;
    private final List<DetailedEvent.Item> mItems;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public DetailedItemViewAdapter(@NonNull Context context) {
        mContext = context;
        mItems = new ArrayList<Item>();
    }

    /** {@inheritDoc} */
    @Override
    public int getCount() {
        return mItems.size();
    }

    /** {@inheritDoc} */
    @Override
    public Object getItem(int position) {
        return mItems.get(position);
    }

    /** {@inheritDoc} */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = createItemView();
        }
        final TextView titleView = (TextView) convertView.findViewById(R.id.text_title);
        final ImageView iconView = (ImageView) convertView.findViewById(R.id.image_icon);

        final DetailedEvent.Item item = (Item) getItem(position);
        titleView.setText(item.getTitle());
        iconView.setImageResource(item.getIconResId());

        return convertView;
    }

    /**
     * 詳細項目を全て削除する。
     */
    public void clear() {
        mItems.clear();
    }

    /**
     * 詳細項目を追加する。
     *
     * @param items 詳細項目のリスト。
     */
    public void addAll(@NonNull List<Item> items) {
        mItems.addAll(items);
        notifyDataSetChanged();
    }

    private View createItemView() {
        final View convertView = View.inflate(mContext, R.layout.list_item_detail, null);

        final int height = (int) mContext.getResources().getDimension(R.dimen.material_list_header_height);
        final LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height);
        convertView.setLayoutParams(params);
        convertView.setClickable(true);


        return convertView;
    }
}
