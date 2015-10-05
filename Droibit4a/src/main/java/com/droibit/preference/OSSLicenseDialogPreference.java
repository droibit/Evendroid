package com.droibit.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.droibit.R;
import com.droibit.text.OSSLicenseHTML;

/**
 * オープンソースのライセンスを表示するためのプレファレンスダイアログ
 *
 * @author kumagaishinya
 *
 */
public class OSSLicenseDialogPreference extends DialogPreference {

	private int resLicense;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public OSSLicenseDialogPreference(Context context) {
		this(context, null, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public OSSLicenseDialogPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public OSSLicenseDialogPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

        setLayoutResource(R.layout.preference_child);
		setDialogTitle(R.string.dialog_title_open_source_license);
		setDialogIcon(null);
		setPositiveButtonText(android.R.string.ok);
	}
	
	/** {@inheritDoc} */
	@Override
	protected View onCreateDialogView() {
		final WebView webView = new WebView(getContext());
		final OSSLicenseHTML html = new OSSLicenseHTML(getContext(), resLicense);
		webView.loadDataWithBaseURL(null, html.toString(), "text/html",
				"utf-8", null);
		//webView.setId(R.id.browser);
		return webView;
	}
	
	public void setLicense(int resLicense) {
		this.resLicense = resLicense;
	}
}
