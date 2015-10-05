/*
 * Copyright (c) 2011 Droibit
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
 * * Neither the name of the project nor the names of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.droibit.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.droibit.utils.Debug;
import com.droibit.utils.EnvironmentInfo;

/**
 * assetフォルダ内のSQLデータベースをコピーする。<br>
 * 作成されるデータベースはアセット名と同じになる。
 * 
 * @author Shinya Kumagai
 * 
 */
public class AssetSQLiteOpenHelper extends SQLiteOpenHelper {

	/** データベースバージョン */
	private static final int DATABASE_VERSION = 1;

	/** データベース名 */
	private final String dbName;

	/** デフォルトのデータベースのパス */
	private String dbPath;

	/** コンテキスト */
	private Context context;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param assetName アセットDB名
	 */
	public AssetSQLiteOpenHelper(Context context, String assetName) {
		this(context, assetName, DATABASE_VERSION);
	}

	/**
	 * コンストラクタ。<br>
	 * アセット名をそのままデータベース名として使用する。
	 * 
	 * @param context コンテキスト
	 * @param assetName アセット名
	 * @param version データベースのバージョン
	 */
	public AssetSQLiteOpenHelper(Context context, String assetName, int version) {
		super(context, assetName, null, version);
		Debug.assertNotNull(context);
		Debug.assertTrue(TextUtils.isEmpty(assetName));
		
		this.dbName = assetName;
		this.context = context;
		this.dbPath = EnvironmentInfo.getInternalFilesDir(context).getAbsolutePath();
	}

	/**
	 * データベースが作成されていない場合に呼ばれる
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		copyDatabaseFromAssets();
	}

	/**
	 * データベースのバージョンが上がった時に呼ばれる
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	/**
	 * assetフォルダのデータベースをコピーする
	 */
	private void copyDatabaseFromAssets() {
		InputStream input = null;
		OutputStream output = null;

		try {
			input = Assets.open(context, dbName);
			output = new FileOutputStream(FileHelper.getPathAppendFilename(dbPath, dbName));

			// データベースをコピーする
			FileHelper.copy(input, output);
			output.flush();

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			FileHelper.closeStream(input, output);
		}
	}
}
