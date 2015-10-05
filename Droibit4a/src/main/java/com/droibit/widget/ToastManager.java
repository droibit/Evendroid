package com.droibit.widget;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.droibit.utils.Debug;
import com.droibit.utils.NullCheck;

/**
 * 重複してトーストを表示しないように管理する
 * 
 * @author kumagai
 *
 */
public final class ToastManager {

	/** 表示する唯一のトースト */
	private static Toast toast;

	/**
	 * コンストラクタ
	 */
	private ToastManager() {
	}
	
	/**
	 *  短時間トーストを表示する
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *  
	 * @param context コンテキスト
	 * @param text 表示するテキスト
	 */
	public static final void showShort(Context context, CharSequence text) {
		show(makeShortToast(context, text));
	}
	
	/**
	 *  短時間トーストを表示する。
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *  
	 * @param context コンテキスト
	 * @param resId 文字列のリーソースID
	 */
	public static final void showShort(Context context, int resId) {
		show(makeShortToast(context, context.getString(resId)));
	}
	
	/**
	 *  長時間トーストを表示する
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *  
	 * @param context コンテキスト
	 * @param text 表示するテキスト
	 */
	public static final void showLong(Context context, CharSequence text) {
		show(makeLongToast(context, text));
	}
	
	/**
	 *  長時間トーストを表示する
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *  
	 * @param context コンテキスト
	 * @param resId 文字列のリーソースID
	 */
	public static final void showLong(Context context, int resId) {
		show(makeLongToast(context, context.getString(resId)));
	}
	
	/**
	 * トーストを表示する。
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *
	 * @param context コンテキスト
	 * @param text 表示する文字列
	 * @param duration 表示時間
	 * 
	 * @see Toast#LENGTH_LONG
	 * @see Toast#LENGTH_SHORT
	 */
	public static final void show(Context context, String text, int duration) {
		show(makeToast(context, text, duration));
	}
	
	/**
	 * トーストを表示する。
	 * 	<br>
	 *  以前表示していたトーストはキャンセルされる。
	 *  
	 * @param context コンテキスト
	 * @param resId 文字列のリソース
	 * @param duration 表示期間
	 * 
	 * @see Toast#LENGTH_LONG
	 * @see Toast#LENGTH_SHORT
	 */
	public static final void show(Context context, int resId, int duration) {
		show(makeToast(context, context.getText(resId), duration));
	}
	
	/**
	 * トーストを表示する。
	 * <br>
	 * 以前表示していたトーストはキャンセルされる。
	 * 
	 * @param toast 表示するトースト
	 */
	public static final void show(Toast toast) {
		setToast(toast);
		ToastManager.toast.show();
	}

	/**
	 * 以前のトーストはキャンセルして、新しく表示するトーストをセットする
	 * 
	 * @param toast 新しく表示するトースト
	 */
	public static final void setToast(Toast toast) {
		Debug.assertNotNull(toast);

        if (ToastManager.toast != null) {
            ToastManager.toast.cancel();
        }
		ToastManager.toast = toast;
	}
	
	/**
	 * 短時間表示するトーストを作成する
	 * 
	 * @param context コンテキスト
	 * @param text 表示するテキスト
	 * @return トースト
	 */
	public static final Toast makeShortToast(Context context, CharSequence text) {
		return makeToast(context, text, Toast.LENGTH_SHORT);
	}
	
	/**
	 * 長時間表示するトーストを作成する
	 * 
	 * @param context コンテキスト
	 * @param text 表示するテキスト
	 * @return トースト
	 */
	public static final Toast makeLongToast(Context context, CharSequence text) {
		return makeToast(context, text, Toast.LENGTH_LONG);
	}
	
	/**
	 * トーストを作成する
	 * 
	 * @param context コンテキスト
	 * @param text 文字列
	 * @param duration 表示期間
	 * @return トースト
	 */
	private static final Toast makeToast(Context context, CharSequence text, int duration) {
		if (TextUtils.isEmpty(text)) {
			throw new IllegalArgumentException();
		}
		return Toast.makeText(context, text, duration);
	}

	/**
	 * トーストを表示している場合は消す
	 */
	public static final void cancel() {
		if (NullCheck.isNotNull(toast)) {
			toast.cancel();
		}
		toast = null;
	}
}
