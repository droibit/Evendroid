package com.droibit.app;

import java.util.List;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

import com.droibit.utils.Debug;

/**
 * スマートフォンとタブレットに対応したプレファレンス画面。<br>
 * <br>
 * このアクティビティを使用する場合は必ず{@link EXTRA_PREF_RES_ID}を使用してIntentに
 * プレファレンスヘッダのリソースIDをセットする必要がある。<br>
 * 
 * @author kumagai
 *
 */
@TargetApi(11)
public class SimplePreferenceActivity extends PreferenceActivity {
	
	/** インテントからヘッダIDを取得するためのキー */
	public static final String EXTRA_PREF_RES_ID = "com.droibit.preference.extra.RES_ID";
	
	/** 設定画面のヘッダID */
	private int headerId;

	/** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// プレファレンスヘッダのリソースiDを取得する
		headerId = getIntent().getIntExtra(EXTRA_PREF_RES_ID, -1);
		Debug.assertTrue(headerId != -1);
		
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	
	/** {@inheritDoc} */
	@Override
	public void onBuildHeaders(List<Header> target) {
		loadHeadersFromResource(headerId, target);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (getFragmentManager().getBackStackEntryCount() > 0) {
				getFragmentManager().popBackStack();
			} else {
				finish();
			}
			return true;
		}
		return false;
	}
}
