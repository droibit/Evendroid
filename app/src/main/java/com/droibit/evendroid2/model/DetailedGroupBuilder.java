package com.droibit.evendroid2.model;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;

import com.droibit.content.IntentHepler;
import com.droibit.evendroid2.R;
import com.droibit.evendroid2.model.DetailedEvent.Item;
import com.droibit.evendroid2.model.DetailedEvent.Group;
import com.droibit.evendroid2.view.input.OnDetailedItemClickListener;
import com.droibit.evendroid2.view.input.OnTransitionListener;
import com.droibit.eventservice.social.EventServiceAccount;
import com.droibit.eventservice.social.SocialAccount;
import com.droibit.text.Strings;
import com.droibit.widget.ToastManager;

import java.util.List;

/**
 * フラグメントに表示するための{@link com.droibit.evendroid2.model.DetailedEvent.Group}を構築するためのクラス。
 *
 * @author kumagai
 * @since 2014/09/03.
 */
public final class DetailedGroupBuilder {

    /**
     * イベントの詳細情報から{@link com.droibit.evendroid2.model.DetailedEvent.Group}を構築する。
     *
     * @param context コンテキスト
     * @param event ソースのイベント情報
     * @param showOwnerEvents 主催者のイベント一覧を表示するかどうか
     * @param listener 画面遷移する際に呼ばれるイベントリスナー
     * @return グルーピングしたイベント情報群
     */
    public static final Group[] build(@NonNull final Context context, @NonNull final DetailedEvent event, @NonNull final OnTransitionListener listener, boolean showOwnerEvents) {
        // 概要セクション
        final Group summaryGroup = new Group(context.getString(R.string.event_detail_section_summary));
        // キャッチコピー
        if (!Strings.isEmptyOrWhitespace(event.getCatchcopy())) {
            final Item catchcopy = new Item(event.getCatchcopy(), R.drawable.ic_talk);
            catchcopy.setListener(new OnDetailedItemClickListener() {
                public void onClick() {
                    ToastManager.showLong(context, event.getCatchcopy());
                }
            });
            summaryGroup.add(catchcopy);
        }

        // スケジュール
        final Item schedule = new Item(event.getSchedule(), R.drawable.ic_clock);
        summaryGroup.add(schedule);

        // 会場
        final Item place = new Item(event.getPlace(), R.drawable.ic_marker);
        if (event.getCoordinate() != null) {
            place.setListener(new OnDetailedItemClickListener() {
                public void onClick() {
                    final PointF coord = event.getCoordinate();
                    IntentHepler.showOnGoogleMap(context, coord.y, coord.x);
                }
            });
        }
        summaryGroup.add(place);

        // Webページで見る
        final Item web = new Item(context.getString(R.string.event_detail_item_web_page), R.drawable.ic_globe);
        web.setListener(new OnDetailedItemClickListener() {
            public void onClick() {
                IntentHepler.launchBrowser(context, event.getEventUrl());
            }
        });
        summaryGroup.add(web);

        // 参加者セクション
        final Group usersGroup = new Group(context.getString(R.string.event_detail_section_users));
        final Item users = new Item(event.getUsers(), R.drawable.ic_users);
        final String eventId = event.getId();
        users.setListener(new OnDetailedItemClickListener() {
            public void onClick() {
                listener.onTransitionUsers(eventId);
            }
        });
        usersGroup.add(users);

        // 主催者セクション
        final EventServiceAccount ownerAccount = event.getAccount();
        final Group ownerGroup = new Group(ownerAccount.getName());

        // 主催者のイベントリスト
        if (showOwnerEvents) {
            final Item ownerEvents = new Item(context.getString(R.string.event_detail_item_owner_event), R.drawable.ic_menu);
            final String owenerName = ownerAccount.getName();
            ownerEvents.setListener(new OnDetailedItemClickListener() {
                public void onClick() {
                    listener.onTransitionOwnerEvents(owenerName);
                }
            });
            ownerGroup.add(ownerEvents);
        }

        // ユーザアカウントのリンク
        final List<SocialAccount> socialAccounts = ownerAccount.getAllAcounts();
        final SocialAccount atndAccount = socialAccounts.get(0);
        final Item atnd = new Item(atndAccount.getServiceName(), R.drawable.ic_user);
        atnd.setListener(new OnDetailedItemClickListener() {
            public void onClick() {
                IntentHepler.launchBrowser(context, atndAccount.getUserUri());
            }
        });
        ownerGroup.add(atnd);
        // Twitterアカウントが存在する場合
        if (socialAccounts.size() >= 2) {
            final SocialAccount twitterAccount = socialAccounts.get(1);
            final Item twitter = new Item(twitterAccount.getServiceName(), R.drawable.ic_twitter);
            twitter.setListener(new OnDetailedItemClickListener() {
                public void onClick() {
                    IntentHepler.launchBrowser(context, twitterAccount.getUserUri());
                }
            });
            ownerGroup.add(twitter);
        }

        // 関連ページ
        final Item url = new Item(event.getUrl(), R.drawable.ic_globe);
        url.setListener(new OnDetailedItemClickListener() {
            public void onClick() {
                // "---"の場合は起動メソッド内で弾いている。
                IntentHepler.launchBrowser(context, event.getUrl());
            }
        });
        ownerGroup.add(url);

        return new Group[] {summaryGroup, usersGroup, ownerGroup};
    }
}
