package com.droibit.evendroid2.contoller;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.droibit.evendroid2.R;

/**
 * ナビゲーション項目をビューに設定するためのアダプタ。
 *
 * @author kumagai
 * @since 2014/09/05.
 */
public class NavigationViewAdapter extends ArrayAdapter<NavigationViewAdapter.Item> {

    /**
     * コンストラクタ
     *
     * @param context コンストラクタ
     */
    public NavigationViewAdapter(Context context) {
        super(context, R.layout.list_item_navigation);

        final Item[] items = new Item[] {
            new Item(R.string.navigation_title_search, R.drawable.ic_search_uncheck),
            new Item(R.string.navigation_title_owner, R.drawable.ic_owner_uncheck),
            new Item(R.string.navigation_title_participation, R.drawable.ic_tear_off_calendar_uncheck),
            new Item(R.string.navigation_title_bookmark, R.drawable.ic_star_uncheck),
        };
        addAll(items);
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(getContext(), R.layout.list_item_navigation, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.update(getItem(position));
        return convertView;
    }

    /**
     * ビューに表示する情報を保持するためのクラス。
     */
    public static class Item {
        int titleRes;
        int iconRes;

        /**
         * コンストラクタ
         *
         * @param titleRes タイトル文字のリソースID
         * @param iconRes アイコンのリソースID
         */
        public Item(@StringRes int titleRes, @DrawableRes int iconRes) {
            this.titleRes = titleRes;
            this.iconRes = iconRes;
        }
    }

    /**
     * ビューを再利用するために保持するクラス。
     */
    static class ViewHolder {

        TextView titleView;
        ImageView iconView;

        public ViewHolder(View itemView) {
            this.titleView = (TextView) itemView.findViewById(R.id.text_title);
            this.iconView = (ImageView) itemView.findViewById(R.id.image_icon);
        }

        public void update(Item item) {
            titleView.setText(item.titleRes);
            iconView.setImageResource(item.iconRes);
        }
    }
}
