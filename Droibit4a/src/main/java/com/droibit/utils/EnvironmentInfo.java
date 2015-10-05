package com.droibit.utils;

import static android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE;
import static android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK;
import static android.os.Environment.DIRECTORY_ALARMS;
import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOWNLOADS;
import static android.os.Environment.DIRECTORY_MOVIES;
import static android.os.Environment.DIRECTORY_MUSIC;
import static android.os.Environment.DIRECTORY_NOTIFICATIONS;
import static android.os.Environment.DIRECTORY_PICTURES;
import static android.os.Environment.DIRECTORY_PODCASTS;
import static android.os.Environment.DIRECTORY_RINGTONES;
import static android.os.Environment.MEDIA_MOUNTED;

import java.io.File;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;

import com.droibit.io.FileHelper;

/**
 * アプリケーションの環境や情報を取得するためのユーティリティクラス
 * 
 * @author Shinya Kumagai
 * 
 */
public final class EnvironmentInfo {

	@SuppressWarnings("unused")
	private static final String TAG = Environment.class.getSimpleName();

	/** 外部ストレージ(SDカード)のディレクトリ名 */
	private static final String EXTERNAL_SD = "external_sd";

	/**
	 * コンストラクタ
	 */
	private EnvironmentInfo() {
	}

	/**
	 * 外部ストレージの全容量を取得する
	 * 
	 * @return 外部ストレージの全容量
	 */
	public static long getTotalExternalStorageSpace() {
		return FileHelper.getUsableSpace(getExternalStorageDir());
	}

	/**
	 * 内部ストレージの空き容量を取得する
	 * 
	 * @return 内部ストレージの空き容量
	 */
	public static long getUsableExternalStorageSpace() {
		return FileHelper.getUsableSpace(getExternalStorageDir());
	}

	/**
	 * 内部ストレージの全容量を取得する
	 * 
	 * @return 内部ストレージの全容量
	 */
	public static long getTotalnternalStorageSpace() {
		return FileHelper.getTotalSpace(Environment.getDataDirectory());
	}

	/**
	 * 内部ストレージの空き全容量を取得する
	 * 
	 * @return 内部ストレージの空き容量
	 */
	public static long getUsableInternalStorageSpace() {
		return FileHelper.getUsableSpace(Environment.getDataDirectory());
	}

	/**
	 * 外部ストレージがマウントされているかどうか
	 * 
	 * @return trueならマウントされている、falseの場合はマウントされていない
	 */
	public static final boolean isExternalStorageMounted() {
		return Environment.getExternalStorageState().equals(MEDIA_MOUNTED);
	}

    /**
     * 内部ストレージのアプリケーションファイルの保存フォルダを取得する
     *
     * @param context コンテキスト
     * @return アプリケーションファイルの保存フォルダ
     */
    public static final File getInternalFilesDir(Context context) {
        return context.getFilesDir();
    }

    /**
     * 内部ストレージのアプリケーションキャッシュフォルダを取得する
     *
     * @param context コンテキスト
     * @return アプリケーションキャッシュフォルダ
     */
    public static final File getInternalCacheDir(Context context) {
        return context.getCacheDir();
    }

	/**
	 * 外部ストレージ(<i>ROM</i>)フォルダを取得する
	 * 
	 * @return 内部ストレージフォルダ
	 */
	public static final File getExternalStorageDir() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * 外部ストレージ(<i>ROM</i>)のパスを取得する
	 * 
	 * @return 内部ストレージのパス
	 */
	public static final String getExternalStoragePath() {
		return Environment.getExternalStorageDirectory().getAbsolutePath();
	}

	/**
	 * 外部ストレージ(SDカード)を取得する
	 * 
	 * @return 外部ストレージ(SDカード)
	 */
	public static final File getExternalSdCard() {
		return new File(getExternalStoragePath(), EXTERNAL_SD);
	}

	/**
	 * 外部ストレージ(SDカード)のパスを取得する。<br>
	 * SDカードが存在しない場合はnullを返す。
	 * 
	 * @return 外部ストレージ(SDカード)のパス
	 */
	public static final String getExternalSDCardPath() {
		final File sd = new File(getExternalStoragePath(), EXTERNAL_SD);
		if (sd.exists()) {
			return sd.getAbsolutePath();
		}
		return null;
	}

	/**
	 * 外部ストレージ(SDカード)が存在しているかどうか
	 * 
	 * @return trueなら存在する、falseなら存在しない
	 */
	public static final boolean existsExternalSDCard() {
		return new File(getExternalStoragePath(), EXTERNAL_SD).exists();
	}

	/**
	 * 外部ストレージのデータフォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Android/data/<i>packagename</i>/cache
	 * 
	 * @param context コンテキスト
	 * @return 外部ストレージのキャッシュ用フォルダのパス
	 */
	public static final File getExternalCacheDir(Context context) {
		return context.getExternalCacheDir();
	}

	/**
	 * 外部ストレージのデータフォルダを取得する。<br>
	 * <br>
	 * フォルダが存在しない場合は{@link File#mkdir()}メソッドを使用して作成する必要がある。<br>
	 * /外部ストレージ/Android/data/<i>packagename</i>/cache/<i>subDirname</i>
	 * 
	 * @param context コンテキスト
	 * @param dirname サブディレクトリ名
	 * @return 外部ストレージのキャッシュフォルダ
	 */
	public static final File getExternalCacheDir(Context context, String dirname) {
		return new File(context.getExternalCacheDir(), dirname);
	}

	/**
	 * 外部ストレージのデータフォルを取得する。<br>
	 * <br>
	 * /外部ストレージ/Android/data/<i>packagename</i>/files
	 * 
	 * @param context コンテキスト
	 * @return 外部ストレージのキャッシュフォルダのパス
	 */
	public static final File getExternalDataDir(Context context) {
		return context.getExternalFilesDir(null);
	}

	/**
	 * 外部ストレージのデータフォルダを取得する。<br>
	 * <br>
	 * フォルダが存在しない場合は{@link File#mkdir()}メソッドを使用して作成する必要がある。<br>
	 * /外部ストレージ/Android/data/<i>packagename</i>/files/<i>dirname</i>
	 * 
	 * @param context コンテキスト
	 * @param dirname サブディレクトリ名
	 * @return 外部ストレージのデータフォルダ
	 */
	public static final File getExternalDataDir(Context context, String dirname) {
		return new File(getExternalDataDir(context).toString(), dirname);
	}

	/**
	 * Check if external storage is built-in or removable.
	 * 
	 * @return True if external storage is removable (like an SD card), false
	 *         otherwise.
	 */
	@TargetApi(9)
	public static boolean isExternalStorageRemovable() {
		if (hasGingerbread()) {
			return Environment.isExternalStorageRemovable();
		}
		return true;
	}

	/**
	 * 外部ストレージのアラームフォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Alarms</i>
	 * 
	 * @return 外部ストレージのアラームフォルダ
	 */
	public static final File getExternalPublicAlermsDir() {
		return Environment.getExternalStoragePublicDirectory(DIRECTORY_ALARMS);
	}

	/**
	 * 外部ストレージのリングトーンフォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Alarms</i>
	 * 
	 * @return 外部ストレージのリングトーンフォルダ
	 */
	public static final File getExternalPublicRingtonesDir() {
		return Environment
				.getExternalStoragePublicDirectory(DIRECTORY_RINGTONES);
	}

	/**
	 * 外部ストレージの通知フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Notifications</i>
	 * 
	 * @return 外部ストレージの通知フォルダ
	 */
	public static final File getExternalPublicNotificationsDir() {
		return Environment
				.getExternalStoragePublicDirectory(DIRECTORY_NOTIFICATIONS);
	}

	/**
	 * 外部ストレージの画像フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Picturess</i>
	 * 
	 * @return 外部ストレージの写真フォルダ
	 */
	public static final File getExternalPublicPicturesDir() {
		return Environment
				.getExternalStoragePublicDirectory(DIRECTORY_PICTURES);
	}

	/**
	 * 外部ストレージの動画フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Movies</i>
	 * 
	 * @return 外部ストレージのアラームフォルダ
	 */
	public static final File getExternalPublicMoviesDir() {
		return Environment.getExternalStoragePublicDirectory(DIRECTORY_MOVIES);
	}

	/**
	 * 外部ストレージの音楽フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Music</i>
	 * 
	 * @return 外部ストレージの音楽フォルダ
	 */
	public static final File getExternalPublicMusicDir() {
		return Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC);
	}

	/**
	 * 外部ストレージの音楽フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Podcasts</i>
	 * 
	 * @return 外部ストレージのポッドキャストフォルダ
	 */
	public static final File getExternalPodcastsDir() {
		return Environment
				.getExternalStoragePublicDirectory(DIRECTORY_PODCASTS);
	}

	/**
	 * 外部ストレージの写真フォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/DCIM</i>
	 * 
	 * @return 外部ストレージの写真フォルダ
	 */
	public static final File getExternalDCIMDir() {
		return Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
	}

	/**
	 * 外部ストレージのダウンロードフォルダを取得する。<br>
	 * <br>
	 * /外部ストレージ/Downloads</i>
	 * 
	 * @return 外部ストレージのダウンロードフォルダ
	 */
	public static final File getExternalDownloadsDir() {
		return Environment
				.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS);
	}

	/**
	 * 端末がスマートフォンかどうか
	 * 
	 * @param context コンテキスト
	 * @return trueならスマートフォン、falseならタブレット
	 */
	public static final boolean isSmartPhone(Context context) {
		return !isTablet(context);
	}

	/**
	 * 端末がタブレットかどうか
	 * 
	 * @param context コンテキスト
	 * @return trueならタブレット、falseならスマートフォン
	 */
	public static final boolean isTablet(Context context) {
		final int screenLayout = context.getResources().getConfiguration().screenLayout;
		return (screenLayout & SCREENLAYOUT_SIZE_MASK) >= SCREENLAYOUT_SIZE_LARGE;
	}

	/**
	 * OSのバージョンがEclair(2.1)以上かどうか
	 * 
	 * @return trueなら2.1以上、falseなら2.1未満
	 */
	public static boolean hasEclair() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ECLAIR;
	}

	/**
	 * OSのバージョンがFroyo(2.2)以上かどうか
	 * 
	 * @return trueなら2.2以上、falseなら2.2未満
	 */
	public static final boolean hasFroyo() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO;
	}

	/**
	 * OSのバージョンがGingerbread(2.3)以上かどうか
	 * 
	 * @return trueなら2.3以上、falseなら2.3未満
	 */
	public static boolean hasGingerbread() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * OSのバージョンがHoneyComb(3.0)以上かどうか
	 * 
	 * @return trueなら3.0以上、falseなら3.0未満
	 */
	public static final boolean hasHoneycomb() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
	}

	/**
	 * OSのバージョンがHoneyComb(3.1)以上かどうか
	 * 
	 * @return trueなら3.1以上、falseなら3.1未満
	 */
	public static boolean hasHoneycombMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1;
	}

	/**
	 * OSのバージョンがIceCreamSandwich(4.0)以上かどうか
	 * 
	 * @return trueなら4.0以上、falseなら4.0未満
	 */
	public static final boolean hasIceCreamSandwich() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH;
	}

	/**
	 * OSのバージョンがJellyBean(4.1)以上かどうか
	 * 
	 * @return trueなら4.1以上、falseなら4.1未満
	 */
	public static boolean hasJellyBean() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
	}

	/**
	 * OSのバージョンがJellyBean(4.2)以上かどうか
	 * 
	 * @return trueなら4.2以上、falseなら4.2未満
	 */
	public static boolean hasJellyBeanMR1() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1;
	}
	
	/**
	 * OSのバージョンがJellyBean(4.3)以上かどうか
	 * 
	 * @return trueなら4.3以上、falseなら4.3未満
	 */
	public static boolean hasJellyBeanMR2() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2;
	}
	
	/**
	 * OSのバージョンがKitKat(4.4)以上かどうか
	 * 
	 * @return trueなら4.4以上、falseなら4.4未満
	 */
	public static boolean hasKitKat() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
	}

    /**
     * OSのバージョンがLollipop(5.0)以上かどうか
     *
     * @return trueなら5.0以上、falseなら5.0未満
     */
    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

	/**
	 * アプリケーションのバージョン名を取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションのバージョン名
	 */
	public static final String getVersionName(Context context) {
		final PackageManager pm = context.getPackageManager();

		final PackageInfo packageInfo;
		try {
			packageInfo = pm.getPackageInfo(context.getPackageName(),
					PackageManager.GET_ACTIVITIES);
			return packageInfo.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * アプリケーションのバージョンコードを取得する
	 * 
	 * @param context コンテキスト
	 * @return バージョンコード
	 */
	public static int getAppVersionCode(Context context) {
		try {
			final String packageName = context.getPackageName();
			final PackageInfo packageInfo = context.getPackageManager()
					.getPackageInfo(packageName, 0);
			return packageInfo.versionCode;
		} catch (NameNotFoundException ignored) {
			return 0;
		}
	}

	/**
	 * アプリケーションが初回起動か調べる。<br>
	 * <br>
	 * 初回起動時にプレファレンスファイルへlaunchedという値を書き込んでおき、 以後はlaunchedという値が存在するかどうかで判断する。<br>
	 * 書き込む対象のプレファレンス名は<i>"shared_pref"</i>
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションが初回起動かどうか
	 */
	public static final boolean isFirstRun(Context context) {
		final SharedPreferences pref = PreferenceManager
				.getDefaultSharedPreferences(context);

		// launchedという値があるか調べて、存在しない場合はを書き込む
		if (!pref.getBoolean("launched", false)) {
			pref.edit().putBoolean("launched", true).apply();
			return true;
		}
		return false;
	}

	/**
	 * アプリケーションのデフォルトのアイコンを取得する
	 * 
	 * @param context コンテキスト
	 * @return デフォルトのアイコン
	 */
	public static final Drawable getDefaultAppIcon(Context context) {
		return context.getResources().getDrawable(
				android.R.drawable.sym_def_app_icon);
	}

	/**
	 * NFCをサポートしているバージョンかどうかどうか。<br>
	 * <br>
	 * Android2.3以上のOSの場合NFCをサポートしている。
	 * 
	 * @return trueの場合サポートしている、falseの場合はサポートしていない
	 */
	public static final boolean isSupportedNFC() {
		return Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD;
	}

	/**
	 * ハードウェアがNFCを実装しているかどうか調べる
	 * 
	 * @param context コンテキスト
	 * @return trueなら実装している、falseなら実装していない
	 */
	public static final boolean hasNFCFeature(Context context) {
		return context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_NFC);
	}
}
