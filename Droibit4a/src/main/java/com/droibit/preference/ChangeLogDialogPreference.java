package com.droibit.preference;

import android.content.Context;
import android.preference.DialogPreference;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;

import com.droibit.R;
import com.droibit.text.ChangeLogHTML;

/**
 * アプリの変更点を表示するダイアログプレファレンスダイアログ
 * 
 * @author kumagaishinya
 * 
 */
public class ChangeLogDialogPreference extends DialogPreference {

	private int resChangeLog;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 */
	public ChangeLogDialogPreference(Context context) {
		this(context, null, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 */
	public ChangeLogDialogPreference(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param attrs 属性セット
	 * @param defStyle スタイル定義
	 */
	public ChangeLogDialogPreference(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

        setLayoutResource(R.layout.preference_child);
		setDialogTitle(R.string.dialog_title_changelog);
		setDialogIcon(null);
		setPositiveButtonText(android.R.string.ok);
	}

	/** {@inheritDoc} */
	@Override
	protected View onCreateDialogView() {
		final WebView webView = new WebView(getContext());
		final ChangeLogHTML html = new ChangeLogHTML(getContext(), resChangeLog);
		webView.loadDataWithBaseURL(null, html.toString(), "text/html",
				"utf-8", null);
		//webView.setId(R.id.browser);
		return webView;
	}
	
	public void setChangeLog(int resChangelog) {
		this.resChangeLog = resChangelog;
	}
}
