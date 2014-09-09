package com.droibit.evendroid2.fragment.pref;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.text.TextUtils;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.SettingsActivity;
import com.droibit.evendroid2.model.DatabaseManager;
import com.droibit.preference.AskDialogPreference;
import com.droibit.preference.OnDialogPreferenceClickListener;
import com.droibit.text.Strings;

/**
 * イベント支援サイト毎の設定するフラグメント
 * 
 * @author kumagai
 * 
 */
public class PrefEventFragment extends PreferenceFragment implements
		OnPreferenceChangeListener, OnDialogPreferenceClickListener {

	private Preference mPrefsUserName;
    private Preference mPrefLoadCount;
    private Preference mPrefClearBookmark;


    /** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_event);

        // ユーザ名の入力
        mPrefsUserName = findPreference(getString(R.string.pref_key_atnd_user_name));
        mPrefsUserName.setOnPreferenceChangeListener(this);
		// ユーザ名が入力されている場合はサマリに表示する
		final String userName = SettingsActivity.getUserName(getActivity());
		if (!TextUtils.isEmpty(userName)) {
            mPrefsUserName.setSummary(userName);
		}

        // イベントのロード数
        mPrefLoadCount = findPreference(getString(R.string.pref_key_laoding_count));
        mPrefLoadCount.setOnPreferenceChangeListener(this);
        final int loadCount = SettingsActivity.getLoadCount(getActivity());
        mPrefLoadCount.setSummary(getString(
                R.string.pref_general_loading_summary,
                String.valueOf(loadCount)));

        mPrefClearBookmark = findPreference(getString(R.string.pref_key_clear_bookmark));
        ((AskDialogPreference) mPrefClearBookmark)
                .setOnDialogPreferenceClickListener(this);

        final int count = DatabaseManager.countBookmarks();
        mPrefClearBookmark.setSummary(getString(
                R.string.pref_general_clear_bookmark_summary, count));
        if (count == 0) {
            mPrefClearBookmark.setEnabled(false);
        }
	}

	/** {@inheritDoc} */
	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mPrefsUserName) {
            final String inputedValue = newValue.toString();
			if (!TextUtils.isEmpty(inputedValue)) {
				preference.setSummary(getString(R.string.pref_atnd_summary,
						inputedValue));
				return true;
			}
            // 空文字が入力された場合はユーザ名をクリアする。
            ((EditTextPreference) preference).setText(Strings.EMPTY);
			preference.setSummary("---");
			return false;
		} else if (preference == mPrefLoadCount) {
            preference.setSummary(getString(
                    R.string.pref_general_loading_summary,
                    newValue.toString()));
            // 設定にロード数を保存する。
            ((ListPreference) preference).setValue(newValue.toString());
            return true;
        }
		return false;
	}

    /** {@inheritDoc} */
    @Override
    public void onDialogPreferenceClick(boolean positiveResult) {
        if (positiveResult) {
            DatabaseManager.clearBookmarks();
            mPrefClearBookmark.setSummary(getString(
                    R.string.pref_general_clear_bookmark_summary, 0));
            mPrefClearBookmark.setEnabled(false);
        }
    }
}
