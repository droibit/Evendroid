package com.droibit.evendroid2.model;

import android.support.annotation.NonNull;
import android.view.MenuItem;

import com.droibit.evendroid2.R;
import com.droibit.eventservice.IEventServiceClient;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.events.atnd.EventResponse;
import com.droibit.eventservice.http.GetRequest;
import com.droibit.eventservice.http.IGetRequest;
import com.droibit.eventservice.http.url.IParameterKey;
import com.droibit.eventservice.http.url.RequestContents;

/**
 * メニューの更新処理を実行するためのクラス。
 *
 * @author kumagai
 * @since 2014/09/04.
 */
public class RefreshAction {

    protected Condition mCondition;

    private final IEventServiceClient mClient;
    private final EventResponse.Callback mResponseCallback;
    private OnRefreshActionListener mListener;
    private boolean mIsRefreshing;

    /**
     * コンストラクタ
     *
     * @param client サービスのクライアント
     * @param responseCallback 検索結果を受け取るためのコールバック
     */
      public RefreshAction(@NonNull IEventServiceClient client, @NonNull EventResponse.Callback responseCallback) {
          mClient = client;
          mResponseCallback = responseCallback;
      }

    /**
     * アクションバーのメニューが選択さた時に呼ばれる。
     *
     * @param item 選択されたメニュー
     * @return 処理を続けるかどうか
     */
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            onRefresh();
        }
        return false;
    }

    /**
     * イベントの詳細情報を取得する。
     */
    public void onRefresh() {
        if (mIsRefreshing) {
            return;
        }

        if (mListener.onPreRefresh()) {
            final IGetRequest getRequest = new GetRequest.Builder()
                    .append(EventServices.ATND, RequestContents.EVENT)
                    .append(mCondition.key, mCondition.value)
                    .build();
            mClient.load(EventResponse.createRequest(getRequest, mResponseCallback));
            mIsRefreshing = true;
        }
    }

    /**
     * 更新後に呼ばれる処理
     */
    public void onPostRefresh() {
        mIsRefreshing = false;
    }

    /**
     * 更新条件の情報を保持する。
     *
     * @param condition 更新条件
     */
    public void setCondition(@NonNull Condition condition) {
        mCondition = condition;
    }

    /**
     * 更新条件を保持しているかどうか
     *
     * @return trueの場合保持し、falseの場合保持していない
     */
    public boolean hasCondition() {
        return mCondition != null;
    }

    /**
     * 更新の際に呼ばれるリスナーを保持する。
     *
     * @param mListener イベントリスナー
     */
    public void setListener(@NonNull OnRefreshActionListener mListener) {
        this.mListener = mListener;
    }

    /**
     * イベントを更新するための条件を保持するためのクラス。
     */
    public static class Condition {
        public IParameterKey key;
        public String value;

        /**
         * コンストラクタ
         */
        public Condition(@NonNull IParameterKey key, @NonNull String value) {
            this.key = key;
            this.value = value;
        }
    }

    /**
     * イベントの更新の際に呼ばれるリスナー。
     *
     * @author kumagai
     * @since 2014/09/04
     */
    public interface OnRefreshActionListener {

        /**
         * イベントの更新前に呼ばれる処理。<br>
         * 検索前にインジケータを表示する際に使用する。
         *
         * @return 更新を開始するかどうか。
         */
        boolean onPreRefresh();
    }
}
