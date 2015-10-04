package com.droibit.evendroid2.fragment.pref;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.provider.SearchRecentSuggestions;
import android.provider.SyncStateContract.Columns;
import android.text.TextUtils;

import com.droibit.evendroid2.R;
import com.droibit.evendroid2.SettingsActivity;
import com.droibit.evendroid2.provider.RecentSuggestionsProvider;
import com.droibit.evendroid2.model.DatabaseManager;
import com.droibit.preference.AskDialogPreference;
import com.droibit.preference.OnDialogPreferenceClickListener;
import com.droibit.text.Strings;
import com.droibit.utils.Debug;

import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.android.AndroidDeferredManager;
import org.jdeferred.android.DeferredAsyncTask;

import static com.droibit.evendroid2.provider.RecentSuggestionsProvider.SEARCH_URI;

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
    private Preference mPrefClearSuggest;


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

        // ブックマークのクリア
        mPrefClearBookmark = findPreference(getString(R.string.pref_key_clear_bookmark));
        ((AskDialogPreference) mPrefClearBookmark)
                .setOnDialogPreferenceClickListener(this);
        mPrefClearBookmark.setSummary(getString(
                R.string.pref_general_clear_bookmark_summary, 0));
        mPrefClearBookmark.setEnabled(false);
        // ブックマーク数のカウントは非同期で行う。
        countBookmarkedEvents();

        // 履歴のクリア
        mPrefClearSuggest = findPreference(getString(R.string.pref_key_clear_suggest));
        mPrefClearSuggest.setEnabled(false);
        mPrefClearSuggest.setOnPreferenceChangeListener(this);
        ((AskDialogPreference) mPrefClearSuggest)
                .setOnDialogPreferenceClickListener(this);
        // 検索履歴の読み込みは非同期で行う。
        loadSearchHistory();
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
    public void onDialogPreferenceClick(Preference preference, boolean positiveResult) {
        if (!positiveResult) {
            return;
        }

        if (preference == mPrefClearBookmark) {
            DatabaseManager.clearBookmarks();
            mPrefClearBookmark.setSummary(getString(
                    R.string.pref_general_clear_bookmark_summary, 0));
            mPrefClearBookmark.setEnabled(false);
        } else if (preference == mPrefClearSuggest) {
            final SearchRecentSuggestions suggestions = RecentSuggestionsProvider.newRecentSuggestions(getActivity());
            suggestions.clearHistory();
            mPrefClearSuggest.setEnabled(false);
        }
    }

    private void countBookmarkedEvents() {
        new AndroidDeferredManager().when(new DeferredAsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackgroundSafe(Void... voids) throws Exception {
                return DatabaseManager.countBookmarks();
            }
        }).done(new DoneCallback<Integer>() {
            @Override
            public void onDone(Integer result) {
                // ブックマークが存在する場合のみ削除できるようにする。
                if (result > 0) {
                    mPrefClearBookmark.setEnabled(true);
                    mPrefClearBookmark.setSummary(getString(
                            R.string.pref_general_clear_bookmark_summary, result));
                }
            }
        }).fail(new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Debug.log(result.getLocalizedMessage());
            }
        });
    }

    private void loadSearchHistory() {
        new AndroidDeferredManager().when(new DeferredAsyncTask<Void, Void, Integer>() {
            @Override
            protected Integer doInBackgroundSafe(Void... voids) throws Exception {
                final ContentResolver resolver = getActivity().getContentResolver();
                // 検索履歴が存在するかどうか確認する。
                final Cursor c = resolver.query(SEARCH_URI, new String[]{Columns._ID}, "", new String[]{""}, null, null);
                return c.getCount();
            }
        }).done(new DoneCallback<Integer>() {
            @Override
            public void onDone(Integer result) {
                // 検索履歴が存在する場合のみ削除できるようにする。
                if (result > 0) {
                    mPrefClearSuggest.setEnabled(true);
                }
            }
        }).fail(new FailCallback<Throwable>() {
            @Override
            public void onFail(Throwable result) {
                Debug.log(result.getLocalizedMessage());
            }
        });
    }
}
