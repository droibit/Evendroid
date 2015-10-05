/**
 * 
 */
package com.droibit.app.fragment.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.app.DialogFragment;
import android.app.FragmentManager;

import com.droibit.app.dialog.ChangeLogDialog;
import com.droibit.utils.EnvironmentInfo;

/**
 * アプリの変更履歴を表示するダイアログ
 * 
 * @author kumagaishinya
 * 
 */
public class ChangeLogDialogFragment extends DialogFragment {

	/** フラグメントマネージャに登録するタグ */
	private static final String TAG_DIALOG = "changeLogDialog";
	private static final String KEY_CHANGE_LOG = "changeLog";
	private static final String WHATS_NEW_LAST_SHOWN = "whats_new_last_shown";

	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param resChangeLog 変更履歴のリソース
	 * @return 新しいインスタンス
	 */
	public static final ChangeLogDialogFragment newInstance(int resChangeLog,
			int versionCode) {
		final ChangeLogDialogFragment df = new ChangeLogDialogFragment();
		final Bundle args = new Bundle(1);

		args.putInt(KEY_CHANGE_LOG, resChangeLog);
		df.setArguments(args);
		return df;
	}

	/** {@inheritDoc} */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(getActivity());
		final int versionCode = prefs.getInt(WHATS_NEW_LAST_SHOWN, 0);
		final int resChangeLog = getArguments().getInt(KEY_CHANGE_LOG);

		return new ChangeLogDialog(getActivity(), resChangeLog)
				.makeDialog(versionCode);
	}

	/**
	 * ダイアログを表示するためのヘルパーメソッド
	 * 
	 * @param fm フラグメントマネージャ
	 */
	public void show(FragmentManager fm) {
		show(fm, TAG_DIALOG);
	}

	/**
	 * アプリケーションの更新があったかどうか確認する。<br>
	 * ダイアログを作成する前にかならず確認する
	 * 
	 * @param context コンテキスト
	 * @return trueの場合更新あり、falseの場合更新なし
	 */
	public static boolean isUpdated(Context context) {
		// ToDo check if version is shown
		final SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		final int versionShown = prefs.getInt(WHATS_NEW_LAST_SHOWN, 0);
		final int oldVersion = EnvironmentInfo.getAppVersionCode(context);
		if (versionShown != oldVersion) {
			// Update last shown version
			prefs.edit().putInt(WHATS_NEW_LAST_SHOWN, versionShown).commit();
			return true;
		}
		return false;
	}
}
