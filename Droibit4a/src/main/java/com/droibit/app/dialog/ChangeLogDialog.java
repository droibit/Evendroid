/*
 * (c) 2012 Martin van Zuilekom (http://martin.cubeactive.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.droibit.app.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.webkit.WebView;

import com.droibit.R;
import com.droibit.text.ChangeLogHTML;
import com.droibit.utils.NullCheck;

/**
 * Class to show a change log dialog
 */
public class ChangeLogDialog {
	private static final String TAG = "ChangeLogDialog";

	protected ChangeLogHTML changeLog;
	protected Context context;
	protected DialogInterface.OnDismissListener onDismissListener;

	public ChangeLogDialog(Context context, int resChangeLog) {
		this.changeLog = new ChangeLogHTML(context, resChangeLog);
	}

	protected Context getContext() {
		return context;
	}

	// Get the current app version
	private String getAppVersion() {
		String versionName = "";
		try {
			final PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(context.getPackageName(), 0);
			versionName = packageInfo.versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage(), e);
		}
		return versionName;
	}

	// Parse a date string from the xml and format it using the local date
	// format
	@SuppressLint("SimpleDateFormat")
	private String parseDate(final String dateString) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		try {
			final Date parsedDate = dateFormat.parse(dateString);
			return DateFormat.getDateFormat(getContext()).format(parsedDate);
		} catch (ParseException ignored) {
			return dateString;
		}
	}

	public void setStyle(String style) {
		changeLog.setStyle(style);
	}

	public ChangeLogDialog setOnDismissListener(
			DialogInterface.OnDismissListener onDismissListener) {
		this.onDismissListener = onDismissListener;
		return this;
	}

	/** Returns change log in HTML format */
	public String getHTML() {
		// TODO: Remove duplicate code with the method show()
		// Get resources
		final String packageName = context.getPackageName();
		final Resources resources;
		try {
			resources = context.getPackageManager().getResourcesForApplication(
					packageName);
		} catch (NameNotFoundException ignored) {
			return "";
		}

		// Create HTML change log
		return changeLog.getHtmlChangeLog(resources, 0);
	}

	// Call to show the change log dialog
	public void show() {
		show(0);
	}

	public Dialog makeDialog(final int version) {
		// Get resources
		final String packageName = context.getPackageName();
		final Resources resources;
		try {
			resources = context.getPackageManager().getResourcesForApplication(
					packageName);
		} catch (NameNotFoundException e) {
			return null;
		}

		// Get dialog title
		String title = resources.getString(R.string.dialog_title_changelog);
		title = String.format("%s v%s", title, getAppVersion());

		// Create html change log
		final String htmlChangelog = changeLog.getHtmlChangeLog(resources,
				version);
		// Get button strings
		final String closeString = resources.getString(R.string.close);

		// Check for empty change log
		if (TextUtils.isEmpty(htmlChangelog)) {
			// It seems like there is nothing to show, just bail out.
			return null;
		}

		// Create web view and load html
		final WebView webView = new WebView(context);
		webView.loadDataWithBaseURL(null, htmlChangelog, "text/html", "utf-8",
				null);
		final AlertDialog.Builder builder = new AlertDialog.Builder(context)
				.setTitle(title).setView(webView)
				.setPositiveButton(closeString, new Dialog.OnClickListener() {
					public void onClick(final DialogInterface dialogInterface,
							final int i) {
						dialogInterface.dismiss();
					}
				}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						dialog.dismiss();
					}
				});
		final AlertDialog dialog = builder.create();
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(final DialogInterface dialog) {
				if (NullCheck.isNotNull(onDismissListener)) {
					onDismissListener.onDismiss(dialog);
				}
			}
		});
		return dialog;
	}

	protected void show(final int version) {
		final Dialog dialog = makeDialog(version);
		if (NullCheck.isNotNull(dialog)) {
			dialog.show();
		}
	}
}
