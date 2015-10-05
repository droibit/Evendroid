package com.droibit.utils;

import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.app.UiModeManager;
import android.app.admin.DevicePolicyManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.hardware.SensorManager;
import android.hardware.usb.UsbManager;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.nfc.NfcManager;
import android.os.DropBoxManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.os.storage.StorageManager;
import android.service.wallpaper.WallpaperService;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.view.inputmethod.InputMethodManager;
import android.view.textservice.TextServicesManager;

/**
 * システムサービスを取得するためのユーティリティクラス
 * 
 * @author kumagai
 *
 */
public final class SystemServices {

	public static final PowerManager getPowerManager(Context context) {
		return (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	}

	public static final WindowManager getWindowManager(Context context) {
		return (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	}

	public static final AccountManager getAccountManager(Context context) {
		return (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
	}

	public static final ActivityManager getActivityManager(Context context) {
		return (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
	}

	public static final NotificationManager getNotificationManagerr(Context context) {
		return (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
	}

	public static final AccessibilityManager getAccessibilityManager(Context context) {
		return (AccessibilityManager) context
				.getSystemService(Context.ACCESSIBILITY_SERVICE);
	}

	public static final LocationManager getLocationManager(Context context) {
		return (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
	}

	public static final SearchManager getSearchManager(Context context) {
		return (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
	}

	public static final SensorManager getSensorManager(Context context) {
		return (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	}

	public static final StorageManager getStorageManager(Context context) {
		return (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
	}

	public static final WallpaperService getWALLPAPER_SERVICE(Context context) {
		return (WallpaperService) context.getSystemService(Context.WALLPAPER_SERVICE);
	}

	public static final Vibrator getVibrator(Context context) {
		return (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
	}

	public static final ConnectivityManager getConnectivityManager(Context context) {
		return (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}

	public static final WifiManager getWifiManager(Context context) {
		return (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
	}

	@TargetApi(14)
	public static final WifiP2pManager getWifiP2pManager(Context context) {
		return (WifiP2pManager) context.getSystemService(Context.WIFI_P2P_SERVICE);
	}

	public static final AudioManager getAudioManager(Context context) {
		return (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
	}

	@TargetApi(11)
	public static final ClipboardManager getClipboardManager(Context context) {
		return (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
	}

	public static final InputMethodManager getInputMethodManager(Context context) {
		return (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
	}

	@TargetApi(14)
	public static final TextServicesManager getTextServicesManager(Context context) {
		return (TextServicesManager) context
				.getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
	}

	public static final DropBoxManager getDropBoxManager(Context context) {
		return (DropBoxManager) context.getSystemService(Context.DROPBOX_SERVICE);
	}

	public static final DevicePolicyManager getDevicePolicyManager(Context context) {
		return (DevicePolicyManager) context
				.getSystemService(Context.DEVICE_POLICY_SERVICE);
	}

	public static final UiModeManager getUiModeManager(Context context) {
		return (UiModeManager) context.getSystemService(Context.UI_MODE_SERVICE);
	}

	public static final DownloadManager getDownloadManager(Context context) {
		return (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
	}

	@TargetApi(10)
	public static final NfcManager getNfcManager(Context context) {
		return (NfcManager) context.getSystemService(Context.NFC_SERVICE);
	}

	@TargetApi(12)
	public static final UsbManager getUsbManager(Context context) {
		return (UsbManager) context.getSystemService(Context.USB_SERVICE);
	}

	public static final KeyguardManager getKeyguardManager(Context context) {
		return (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
	}
}
