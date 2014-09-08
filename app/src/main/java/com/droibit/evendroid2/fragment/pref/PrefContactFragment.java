package com.droibit.evendroid2.fragment.pref;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;

import com.droibit.content.IntentHepler;
import com.droibit.content.IntentMailArgs;
import com.droibit.evendroid2.R;
import com.droibit.eventservice.http.url.WebPage;
import com.droibit.eventservice.social.SocialNetworkServices;

/**
 * 作成者とコンタクトを取るためのフラグメント。
 * 
 * @author kumagai
 * 
 */
public class PrefContactFragment extends PreferenceFragment implements
		OnPreferenceClickListener {

	private Preference mPrefRequest;
	private Preference mPrefContactTwitter;
    private Preference mPrefContactGitHub;

	/** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.pref_contact);

		mPrefRequest = findPreference(getString(R.string.pref_key_request_apps));
		mPrefRequest.setOnPreferenceClickListener(this);

		mPrefContactTwitter = findPreference(getString(R.string.pref_key_contact_twitter));
		mPrefContactTwitter.setOnPreferenceClickListener(this);

        mPrefContactGitHub = findPreference(getString(R.string.pref_key_contact_github));
        mPrefContactGitHub.setOnPreferenceClickListener(this);
    }

	/** {@inheritDoc} */
	@Override
	public boolean onPreferenceClick(Preference preference) {
		if (preference == mPrefRequest) {
			// 意見・要望のメールを送信する
			final IntentMailArgs args = new IntentMailArgs();
		    args.address = getString(R.string.mail_address);
			args.title = getString(R.string.mail_subject_request_apps);
			IntentHepler.sendMail(getActivity(), args);
			return true;
		} else if (preference == mPrefContactGitHub) {
			// GitHubのリポジトリページを表示する。
//			final String googlePlusId = getString(R.string.google_plus_id);
//			IntentHepler.launchBrowser(getActivity(),
//					SnsHelper.uriForGooglePlusUser(googlePlusId));
			return true;
		} else if (preference == mPrefContactTwitter) {
			// Twitterのプロフィールを表示する
			final String twitterId = getString(R.string.twitter_id);
			IntentHepler.launchBrowser(getActivity(), WebPage.getSocialPageUri(SocialNetworkServices.TWITTER, twitterId));
			return true;
		}
		return false;
	}
}
