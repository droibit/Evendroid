package com.droibit.content;


import java.io.Serializable;

import com.droibit.text.Strings;

/**
 * 外部アプリでメールを送信するための情報。
 * 
 * @author kumagai
 *
 */
public class IntentMailArgs implements Serializable {

	/** シリアルID */
	private static final long serialVersionUID = -375051489380768380L;

    /** タイトル */
    public String title;

	/** 送信先アドレス */
	public String address;

    /** メール本文 */
    public String contents;

    /**
     * 新しいインスタンスを作成する
     */
    public IntentMailArgs() {
        title = Strings.EMPTY;
        address = Strings.EMPTY;
        contents = Strings.EMPTY;
    }
}
