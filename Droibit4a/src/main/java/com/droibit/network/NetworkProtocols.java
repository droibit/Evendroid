package com.droibit.network;

/**
 * プロトコル一覧を定義する
 *
 * @author kumagai
 * @since 2014/03/22.
 */
public enum NetworkProtocols {
    HTTP("http"),
    HTTPS("https"),
    TCP("tcp"),
    FTP("ftp");

    private final String mName;

    private NetworkProtocols(String name) {
        mName = name;
    }

    public final String getName() {
        return mName;
    }
}
