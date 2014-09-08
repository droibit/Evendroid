package com.droibit.evendroid2.fragment.pref;

import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.droibit.content.IntentHepler;
import com.droibit.content.IntentMailArgs;
import com.droibit.evendroid2.R;
import com.droibit.preference.ChangeLogDialogPreference;
import com.droibit.preference.OSSLicenseDialogPreference;
import com.droibit.utils.EnvironmentInfo;

/**
 * アプリ情報を表示するためのフラグメント
 * 
 * @author kumagai
 * 
 */
public class PrefAppFragment extends PreferenceFragment implements
		OnPreferenceClickListener {

	private Preference mPrefShareApp;

	/** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_app);

		final Preference prefBuildVersion = findPreference(getString(R.string.pref_key_build_version));
		prefBuildVersion.setSummary(EnvironmentInfo
				.getVersionName(getActivity()));

		final ChangeLogDialogPreference prefChangelog =
                (ChangeLogDialogPreference) findPreference(getString(R.string.pref_key_change_log));
		prefChangelog.setChangeLog(R.xml.changelog);

		final OSSLicenseDialogPreference prefLicense =
                (OSSLicenseDialogPreference) findPreference(getString(R.string.pref_key_open_souce_license));
		prefLicense.setLicense(R.xml.osslicense);
		
		mPrefShareApp = findPreference(getString(R.string.pref_key_share_app));
		mPrefShareApp.setOnPreferenceClickListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mPrefShareApp) {
			// ブラウザ用のGooglePlayのアプリページをメールで送信できるようにする
			final Uri appUri = IntentHepler
					.makeBrowserGooglePlayUri(getActivity().getPackageName());
			final IntentMailArgs args = new IntentMailArgs();
            args.address = getString(R.string.mail_address);
            args.title = getString(R.string.mail_share_title);
            args.contents = getString(R.string.mail_share_detail, appUri.toString());
			IntentHepler.sendMail(getActivity(), args);
		}
		return false;
	}
}
