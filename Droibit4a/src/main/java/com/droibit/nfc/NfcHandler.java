package com.droibit.nfc;

import static android.provider.Settings.ACTION_NFCSHARING_SETTINGS;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.NfcAdapter.CreateNdefMessageCallback;
import android.nfc.NfcAdapter.OnNdefPushCompleteCallback;
import android.nfc.NfcEvent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.droibit.utils.Debug;
import com.droibit.utils.EnvironmentInfo;
import com.droibit.utils.NullCheck;

/**
 * NFC通信をするためのユーティリティクラス
 * 
 * @author kumagai
 */
@TargetApi(14)
public class NfcHandler implements CreateNdefMessageCallback, OnNdefPushCompleteCallback {

	/**
	 * NFC通信のイベントが発生した場合に呼ばれるイベントリスナ
	 * 
	 * @author kumagai
	 */
	public interface OnNfcEventListener {
		
		/**
		 * NFCで送信するデータを作成するときに呼ばれる
		 * 
		 * @param event NFCインベント
		 * @return NFCで送信するデータ
		 */
		public NdefMessage onCreateNdefMessage(NfcEvent event);
		
		/**
		 * NFCでデータの送信に成功した場合に呼ばれる処理
		 */
		public void onNdefPushComplete();
	}
	
	private static final int MSG_SEND = 1;

	private final OnNfcEventListener listener;

	@SuppressLint("HandlerLeak")
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_SEND:
				listener.onNdefPushComplete();
				break;
			}
		}
	};

	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param listener NFC通信のイベントリスナ
	 */
	public NfcHandler(OnNfcEventListener listener) {
		Debug.assertNotNull(listener);
		
		this.listener = listener;
	}
	

	/** {@inheritDoc} */
	@Override
	public NdefMessage createNdefMessage(NfcEvent event) {
		return listener.onCreateNdefMessage(event);
	}

	/** {@inheritDoc} */
	@Override
	public void onNdefPushComplete(NfcEvent event) {
		handler.obtainMessage(MSG_SEND).sendToTarget();
	}
	
	/**
	 * 端末がNFCに対応しているかどうか
	 * 
	 * @param context コンテキスト
	 * @return trueの場合対応している、falseの場合対応していない
	 */
	public static final boolean isEnableNfc(Context context) {
		return EnvironmentInfo.isSupportedNFC() && EnvironmentInfo.hasNFCFeature(context);
	}

	/**
	 * NFC通信のイベントリスナを登録する。
	 * 
	 * @param activity アクティビティ
	 * @param handler NFCハンドラ
	 */
	public static final void register(Activity activity, NfcHandler handler) {
		Debug.assertNotNull(activity);
		
		final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(activity);
		Debug.assertNotNull(adapter);

		adapter.setNdefPushMessageCallback(handler, activity);
		adapter.setOnNdefPushCompleteCallback(handler, activity);
	}

	/**
	 * NFC通信のイベントリスナの登録を解除する
	 * 
	 * @param activity アクティビティ
	 */
	public static final void unregister(Activity activity) {
		register(activity, null);
	}

	/**
	 * TAG検出時のインテントかどうか調べる
	 * 
	 * @param intent インテント
	 * @return trueの場合TAG検出時のインテント、falseの場合はインテントではない
	 */
	public static final boolean isDiscoveredTag(Intent intent) {
		if (NullCheck.isNull(intent) || TextUtils.isEmpty(intent.getAction())) {
			return false;
		}
		return intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED);
	}
	
	/**
	 * 
	 * @param intent
	 * @return
	 */
	public static final boolean isDiscoveredNdef(Intent intent) {
		if (NullCheck.isNull(intent) || TextUtils.isEmpty(intent.getAction())) {
			return false;
		}
		return intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED);
	}
	
	/**
	 * NFC通信の切り替えの設定画面を表示する
	 * 
	 * @param context コンテキスト
	 */
	public static final void onLaunchNfcSharingSettings(Context context) {
		Debug.assertNotNull(context);
		context.startActivity(new Intent(ACTION_NFCSHARING_SETTINGS));
	}
}
