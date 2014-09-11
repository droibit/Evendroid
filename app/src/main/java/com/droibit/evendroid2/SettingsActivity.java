package com.droibit.evendroid2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.droibit.evendroid2.fragment.pref.PrefAppFragment;
import com.droibit.evendroid2.fragment.pref.PrefContactFragment;
import com.droibit.evendroid2.fragment.pref.PrefEventFragment;
import com.droibit.text.Strings;

import java.util.List;

/**
 * A {@link PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class SettingsActivity extends PreferenceActivity {

    private static final int DEFAULT_LOADING_COUNT = 15;

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    /** {@inheritDoc} */
    @Override
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isValidFragment(String fragmentName) {
        if (PrefEventFragment.class.getName().equals(fragmentName) ||
            PrefAppFragment.class.getName().equals(fragmentName)   ||
            PrefContactFragment.class.getName().equals(fragmentName)) {
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
//            if (getFragmentManager().getBackStackEntryCount() > 0) {
//                getFragmentManager().popBackStack();
//            } else {
//                finish();
//            }
            finish();
            return true;
        }
        return false;
    }

    /**
     * 一度にイベントをロードする数を取得する
     *
     * @param context コンテキスト
     * @return イベントのロード数
     */
    public static final int getLoadCount(Context context) {
        final SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        final String countString = sharedPrefs.getString(
                context.getString(R.string.pref_key_laoding_count), Strings.EMPTY);
        if (!TextUtils.isEmpty(countString)) {
            return Integer.valueOf(countString);
        }
        return DEFAULT_LOADING_COUNT;
    }

    /**
     * ユーザ名を取得する
     *
     * @param context コンテキスト
     * @return ユーザ名
     */
    public static final String getUserName(Context context) {
        final SharedPreferences sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        return sharedPrefs.getString(
                context.getString(R.string.pref_key_atnd_user_name),
                Strings.EMPTY);
    }
}
