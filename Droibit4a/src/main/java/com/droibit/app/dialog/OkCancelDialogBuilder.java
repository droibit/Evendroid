package com.droibit.app.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/**
 * OK/キャンセルを入力するためのダイアログを作成するためのビルダ
 * 
 * @author kumagaishinya
 * 
 */
public class OkCancelDialogBuilder extends AlertDialog.Builder {

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param ok "OK"ボタンをクリックされた時に実行する処理
	 * @param cancel "キャンセル"ボンタンをクリックされた時に実行される処理
	 */
	public OkCancelDialogBuilder(Context context, final Runnable ok, final Runnable cancel) {
		super(context);

		setPositiveButton(android.R.string.ok, new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				ok.run();
			}
		});

        if (cancel != null) {
            setNegativeButton(android.R.string.cancel, new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    cancel.run();
                }
            });
        }
	}
}
