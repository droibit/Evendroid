package com.droibit.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.droibit.utils.SystemServices;

/**
 * ネットワークの接続状況を確認するためのユーティリティクラス
 *
 * @author kumagai
 */
public final class Reachability {

    /**
     * モバイルネットワーク、WiFi問わずインターネットに接続されているかどうか確認する
     *
     * @param context コンテキスト
     * @return trueの場合接続されている、falseの場合接続されていない
     */
    public static boolean isConnected(Context context) {
        final ConnectivityManager cm = SystemServices.getConnectivityManager(context);
        final NetworkInfo info = cm.getActiveNetworkInfo();

        if (info != null) {
            return info.isConnected();
        }
        return false;
    }
}
