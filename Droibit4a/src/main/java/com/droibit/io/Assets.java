package com.droibit.io;

import static com.droibit.io.FileHelper.closeStream;
import static com.droibit.io.FileHelper.copy;
import static com.droibit.io.FileHelper.getPathAppendFilename;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.droibit.utils.Debug;
import com.droibit.utils.EnvironmentInfo;

/**
 * ファイル群を管理するためのユーティリティクラス。<br>
 * <br>
 * ファイルを外部ストレージにコピーする場合はパーミッション
 * {@code <android:name="android.permission.WRITE_EXTERNAL_STORAGE">}
 * を有効にする必要がある。<br>
 * 外部ストレージのデータフォルダにコピーしたファイルは、アプリをアンインストールするときに自動的に削除される。<br>
 * 
 * @author kumagai
 * 
 */
public final class Assets {

	private static File DataDir = null;

	private Assets() {
	}
	
	/**
	 *  アセットファイルをオープンする
	 * 
	 * @param context コンテキスト
	 * @param fileName アセットファイル名（Assets直下）
	 * @return アセットファイルの入力ストリーム
	 * @throws IOException 入出力例外
	 */
	public static InputStream open(Context context, String fileName) throws IOException {
		Debug.assertTrue(!TextUtils.isEmpty(fileName));
		return context.getAssets().open(fileName);
	}

	/**
	 * アセットのサブフォルダ内のファイル名一覧を取得する
	 * 
	 * @param context コンテキスト
	 * @param dirname サブフォルダ名
	 * @return ファイルパスのリスト
	 */
	public static String[] getFileName(Context context, String dirname) {
		Debug.assertNotNull(context);
		Debug.assertTrue(!TextUtils.isEmpty(dirname));

		try {
			return context.getResources().getAssets().list(dirname);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * アッセトフォルダのサブフォルダ内の全ファイルをローカルストレージにコピーする。<br>
	 * <br>
	 * フォルダ階層をローカルのデータフォルダにそのままコピーすることはできない。<br>
	 * 保存先 : /data/data/<i>packagename</i>/files/<i>assetPath</i>
	 * 
	 * @param context コンテキスト
	 * @param assetPath サブフォルダ名
	 * @throws IOException 入出力エラーが発生したときにスローされる
	 * @throws FileNotFoundException ファイルが見つからない場合にスローされる
	 */
	@SuppressWarnings("deprecation")
	@SuppressLint("WorldWriteableFiles")
	public static void copyLocalStorage(Context context, String assetPath)
			throws IOException, FileNotFoundException {
		final AssetManager am = context.getAssets();
		final String[] assets = am.list(assetPath);

		for (int i = 0, len = assets.length; i < len; i++) {
			copyAssetFile(am.open(assets[i]), context.openFileOutput(assets[i],
					Context.MODE_WORLD_WRITEABLE));
		}
	}

	/**
	 * アセットフォルダのサブフォルダ内の全ファイルを外部ストレージにコピーする。<br>
	 * <br>
	 * 保存先 : /外部ストレージ/Android/data/<i>packagename</i>/files/<i>assetPath</i>
	 * 
	 * @param context コンテキスト
	 * @param assetPath アセット内のサブフォルダ名
	 * @throws IOException 入出力エラーが発生したときにスローされる
	 * @throws FileNotFoundException ファイルが見つからない場合にスローされる
	 */
	public static void copyExternalStorage(Context context, String assetPath)
			throws IOException, FileNotFoundException {
		DataDir = EnvironmentInfo.getExternalDataDir(context);
		copyExternalStorage(context.getAssets(), assetPath);
	}

	/**
	 * アセットフォルダのサブフォルダ内の全ファイルを外部ストレージにコピーする。<br>
	 * <br>
	 * フォルダ階層が存在する場合は再帰的にコピーする
	 * 
	 * @param am アセットマネージャ
	 * @param assetPath アセットファイルのパス
	 */
	private static void copyExternalStorage(AssetManager am,
			String assetPath) throws IOException, FileNotFoundException {
		final String[] assets = am.list(assetPath);
		// アセットファイルのコピー先のパス
		final File dstFile = new File(DataDir.toString(), assetPath);

		if (assets.length == 0) {
			// アセットファイルを外部ストレージにコピーする
			copyAssetFile(am.open(assetPath), new FileOutputStream(dstFile));
			return;
		}

		final File subDir = dstFile;
		if (!subDir.exists()) {
			subDir.mkdir();
		}
		// アセットフォルダ内のサブフォルダの場合は再帰的にコピーする
		for (int i = 0, len = assets.length; i < len; i++) {
			copyExternalStorage(am, getPathAppendFilename(assetPath, assets[i]));
		}
	}

	/**
	 * アセットファイルを端末にコピーする
	 * 
	 * @param input アセット入力ストリーム
	 * @param output ファイル出力ストリーム
	 * @throws IOException 入出力エラーが発生した場合にスローされる
	 */
	private static void copyAssetFile(InputStream input, OutputStream output)
			throws IOException {
		try {
			copy(input, output);
		} finally {
			closeStream(input, output);
		}
	}
}
