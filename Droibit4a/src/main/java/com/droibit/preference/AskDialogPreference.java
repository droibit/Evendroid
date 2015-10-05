/**
 * 
 */
package com.droibit.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.preference.Preference;
import android.util.AttributeSet;

import com.droibit.R;

/**
 * ユーザに尋ねるだけのシンプルなダイアログプレファレンス
 * 
 * @author kumagaishinya
 * 
 */
public class AskDialogPreference extends DialogPreference {

	/** ボタンのクリックリスナ */
	private OnDialogPreferenceClickListener listener;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public AskDialogPreference(Context context) {
		this(context, null, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public AskDialogPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public AskDialogPreference(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		listener = new NullClickListener();

        setLayoutResource(R.layout.preference_child);
		setPositiveButtonText(android.R.string.ok);
		setNegativeButtonText(android.R.string.cancel);
		setDialogIcon(null);
	}

	/** {@inheritDoc} */
	@Override
	protected void onDialogClosed(boolean positiveResult) {
		super.onDialogClosed(positiveResult);

		listener.onDialogPreferenceClick(this, positiveResult);
	}

    /**
     * ダイアログのクリックリスナーを取得する。
     *
     * @return クリックリスナー
     */
	public OnDialogPreferenceClickListener getOnDialogPreferenceClickListener() {
		return listener;
	}

    /**
     * ダイアログのクリックリスナーを保持する。
     *
     * @param listener クリックリスナー
     */
	public void setOnDialogPreferenceClickListener(
			OnDialogPreferenceClickListener listener) {
		this.listener = listener;
	}

	private static final class NullClickListener implements
			OnDialogPreferenceClickListener {
		/** {@inheritDoc} */
		@Override
		public void onDialogPreferenceClick(Preference preference, boolean positiveResult) {
		}
	}
}
