package com.droibit.evendroid2.view;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.DetailedEvent;
import com.droibit.evendroid2.view.input.OnDetailedItemClickListener;

/**
 * イベントの各詳細項報を表示するためのビュー。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public class DetailedItemView extends RelativeLayout {

    private ImageView mIconView;
    private TextView mTitleView;

    /**
     * コンストラクタ
     *
     * @param context コンテキスト
     */
    public DetailedItemView(Context context) {
        super(context);

        inflate(context, R.layout.list_item_detail, this);

        mIconView = (ImageView) findViewById(R.id.image_icon);
        mTitleView = (TextView) findViewById(R.id.text_title);
    }

    /**
     * ビューのコンテンツを設定する。
     *
     * @param item 表示元の情報
     */
    public void setContent(@NonNull DetailedEvent.Item item) {
        setText(item.getTitle());
        setIconResouce(item.getIconResId());

//        final OnDetailedItemClickListener listener = item.getListener();
//        if (listener != null) {
//            this.setOnClickListener(new OnClickListener() {
//                public void onClick(View v) {
//                    listener.onClick();
//                }
//            });
//        }
    }

    /**
     * アイコン画像のリソースIDを設定する。
     *
     * @param resId アイコン画像のリソースID
     */
    public void setIconResouce(@DrawableRes int resId) {
        mIconView.setImageResource(resId);
    }

    /**
     * タイトルのテキストを設定する。
     *
     * @param text テキスト
     */
    public void setText(String text) {
        mTitleView.setText(text);
    }
}
