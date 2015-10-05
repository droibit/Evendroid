package com.droibit.content;

import static android.content.Intent.ACTION_DELETE;
import static android.content.Intent.ACTION_SEND;
import static android.content.Intent.ACTION_VIEW;
import static android.content.Intent.EXTRA_EMAIL;
import static android.content.Intent.EXTRA_SUBJECT;
import static android.content.Intent.EXTRA_TEXT;

import java.util.List;
import java.util.Locale;

import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.provider.CalendarContract;
import android.text.TextUtils;
import android.util.Log;

import com.droibit.io.FileHelper;
import com.droibit.utils.Debug;
import com.droibit.utils.NullCheck;

/**
 * アクティビティ間でデータをシェアするためのユーティリティクラス
 * 
 * @author Shinya Kumagai
 * 
 */
public final class IntentHepler {

	private static final String TAG = IntentHepler.class.getSimpleName();

	public static final String MIME_IMAGE = "image/*";
	public static final String MIME_TEXT = "text/plain";
	public static final String MIME_EMAIL = "message/rfc822";
	public static final String MIME_CALENDAR = "vnd.android.cursor.item/event";
	public static final String SCHEMA_PACKAGE = "package";
	public static final String SCHEMA_GOOGLE_PLAY = "market://details?id=";
	public static final String SCHEMA_BROWSER_GOOGLE_PLAY = "http://play.google.com/store/apps/details?id=";
	public static final String SETTINGS_INSTALLED_APP_DETAILS = "com.android.settings/.applications.InstalledAppDetails";
	public static final String PACKAGE_GOOGLE_MAPS = "com.google.android.apps.maps";
	public static final String CLASS_GOOGLE_MAPS = "com.google.android.maps.MapsActivity";

	/**
	 * テキストをシェアする。<br>
	 * 共有アプリ一覧のダイアログのタイトルはデフォルトのものを使用する
	 * 
	 * @param context コンテキスト
	 * @param text 共有するテキスト
	 */
	public static final void shareText(Context context, String text) {
		final Intent intent = makeShareText(text);
		context.startActivity(intent);
	}

	/**
	 * テキスト共有用のインテントを作成する
	 * 
	 * @param text 共有するテキスト
	 * @return 共有用のインテント
	 */
	public static final Intent makeShareText(String text) {
		final Intent intent = new Intent(ACTION_SEND);
		intent.setType(MIME_TEXT);
		intent.putExtra(EXTRA_TEXT, text);
		return intent;
	}

	/**
	 * Indicates whether the specified action can be used as an intent. This
	 * method queries the package manager for installed packages that can
	 * respond to an intent with the specified action. If no suitable package is
	 * found, this method returns false.
	 * 
	 * @param context The application's environment.
	 * @param action The Intent action to check for availability.
	 * 
	 * @return True if an Intent with the specified action can be sent and
	 *         responded to, false otherwise.
	 */
	public static boolean istAvailableInten(Context context, String action) {
		final PackageManager pm = context.getPackageManager();
		final Intent intent = new Intent(action);
		final List<ResolveInfo> list = pm.queryIntentActivities(intent,
				PackageManager.MATCH_DEFAULT_ONLY);
		return list.size() > 0;
	}

	/**
	 * 
	 * @param context
	 * @param uniqueId
	 * @param intent
	 * @return
	 */
	public static PendingIntent getPendingIntentToLaunchActivity(
			Context context, int uniqueId, Intent intent) {
		return PendingIntent.getActivity(context, uniqueId, intent, 0);
	}

	/**
	 * 外部アプリを使用からメールを送信する。<br>
	 * メールの本文のみ必須になる。
	 * 
	 * @param context コンテキスト
	 * @param args メール情報
	 */
	public static final void sendMail(Context context, IntentMailArgs args) {
		Debug.assertNotNull(context);
		// メールの本文が存在しない場合は例外をスローする
		if (NullCheck.isNull(args.contents)) {
			throw new IllegalArgumentException("メールの本文が存在しません。");
		}

		// メールを送信するためのインテントを作成する
		final Intent mailIntent = new Intent(ACTION_SEND);

		// 住所が存在する場合はインテントにセットする
		if (NullCheck.isNotNull(args.address)) {
			mailIntent.putExtra(EXTRA_EMAIL, new String[] { args.address });
		}

		// タイトルが存在する場合はインテントにセットする
		if (!TextUtils.isEmpty(args.title)) {
			mailIntent.putExtra(EXTRA_SUBJECT, args.title);
		}
		mailIntent.putExtra(EXTRA_TEXT, args.contents);
		mailIntent.setType(MIME_EMAIL);

		// メール送信アプリを選択するインテントを発行する
		context.startActivity(mailIntent);
	}

	/**
	 * ブラウザを起動する
	 * 
	 * @param context コンテキスト
	 * @param uri ホームページのアドレス
	 */
	public static void launchBrowser(Context context, String uri) {
        if (uri.indexOf("://") != -1) {
            launchBrowser(context, Uri.parse(uri));
        }
	}

	/**
	 * ブラウザを起動する
	 * 
	 * @param context コンテキスト
	 * @param uri ホームページのアドレス
	 */
	public static void launchBrowser(Context context, Uri uri) {
		Debug.assertNotNull(context);
		Debug.assertNotNull(uri);

		final Intent browserIntent = new Intent(ACTION_VIEW, uri);
		context.startActivity(browserIntent);
	}

	/**
	 * アプリケーションをアンインストールする
	 * 
	 * @param context コンテキスト
	 * @param packageName アンインストールアプリのパッケージ名
	 */
	public static void uninstallApp(Context context, String packageName) {
		Debug.assertNotNull(context);
		final Uri uri = Uri.fromParts(SCHEMA_PACKAGE, packageName, null);
		final Intent intent = new Intent(ACTION_DELETE, uri);
		context.startActivity(intent);
	}

	/**
	 * アプリケーションの詳細情報を表示する
	 * 
	 * @param context コンテキスト
	 * @param packageName アプリのパッケージ名
	 */
	public static void showAppDetail(Context context, String packageName) {
		Debug.assertNotNull(context);

		final Intent intent = new Intent();
		intent.setData(FileHelper.getPackageUri(packageName));

		final ComponentName component = ComponentName
				.unflattenFromString(SETTINGS_INSTALLED_APP_DETAILS);
		intent.setComponent(component);

		context.startActivity(intent);
	}

	/**
	 * Google Playでアプリケーションの詳細ページを開く
	 * 
	 * @param context コンテキスト
	 * @param appName アプリケーション名
	 */
	public static void sendGooglePlay(Context context, String appName) {
		try {
			context.startActivity(new Intent(ACTION_VIEW, Uri
					.parse(SCHEMA_GOOGLE_PLAY + appName)));
		} catch (ActivityNotFoundException e) {
			context.startActivity(new Intent(ACTION_VIEW, Uri
					.parse(SCHEMA_BROWSER_GOOGLE_PLAY + appName)));
		}
	}

	/**
	 * アプリのGooglePlayのURIを作成する
	 * 
	 * @param appName アプリ名
	 * @return アプリのURI
	 */
	public static Uri makeGooglePlayUri(String appName) {
		return Uri.parse(SCHEMA_GOOGLE_PLAY + appName);
	}

	/**
	 * アプリのブラウザ用GooglePlayのURIを作成する
	 * 
	 * @param appName アプリ名
	 * @return アプリのURI
	 */
	public static Uri makeBrowserGooglePlayUri(String appName) {
		return Uri.parse(SCHEMA_BROWSER_GOOGLE_PLAY + appName);
	}

	/**
	 * Google Calendarに予定を追加する。<br>
	 * 引数が空ではない値のみ予定で表示される。<br>
	 * 開始、終了時間はエポックタイムを指定する。
	 * 
	 * @param context コンテキスト
	 * @param args カレンダー登録情報
	 */
	public static final void sendGoogleCalendar(Context context, IntentCalendarObject args) {
		final Intent intent = new Intent(Intent.ACTION_EDIT)
				.setType(MIME_CALENDAR);

		if (TextUtils.isEmpty(args.title)
				|| TextUtils.isEmpty(args.description)) {
			throw new IllegalArgumentException("カレンダーのタイトルもしくは詳細が存在しません。");
		}
		intent.putExtra(CalendarContract.Events.TITLE, args.title)
				.putExtra(CalendarContract.Events.DESCRIPTION, args.description)
				.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
						args.beginTimeMills);

		// 住所が存在する場合
		if (!TextUtils.isEmpty(args.location)) {
			intent.putExtra(CalendarContract.Events.EVENT_LOCATION,
					args.location);
		}

		// 終了時間が存在する場合
		if (args.endTimeMills > 0L) {
			intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
					args.endTimeMills);
		}
		context.startActivity(intent);
	}

	/**
	 * 指定された座標をGoogleMapで表示する
	 * 
	 * @param context コンテキスト
	 * @param latitude 緯度
	 * @param longitude 経度
	 */
	public static void showOnGoogleMap(Context context, float latitude, float longitude) {
		try {
			// We don't use "geo:latitude,longitude" because it only centers
			// the MapView to the specified location, but we need a marker
			// for further operations (routing to/from).
			// The q=(lat, lng) syntax is suggested by geo-team.
			final String uri = formatLatitudeLongitude(
					"http://maps.google.com/maps?f=q&q=(%f,%f)", latitude,
					longitude);
			final ComponentName compName = new ComponentName(
					PACKAGE_GOOGLE_MAPS, CLASS_GOOGLE_MAPS);
			final Intent mapsIntent = new Intent(ACTION_VIEW, Uri.parse(uri))
					.setComponent(compName);
			context.startActivity(mapsIntent);
		} catch (ActivityNotFoundException e) {
			// Use the "geo intent" if no GMM is installed
			Log.e(TAG, "GMM activity not found!", e);
			final String url = formatLatitudeLongitude("geo:%f,%f", latitude,
					longitude);
			final Intent mapsIntent = new Intent(ACTION_VIEW, Uri.parse(url));
			context.startActivity(mapsIntent);
		}
	}

	private static String formatLatitudeLongitude(String format,
			double latitude, double longitude) {
		// We need to specify the locale otherwise it may go wrong in some language
		return String.format(Locale.JAPANESE, format, latitude, longitude);
	}
}
