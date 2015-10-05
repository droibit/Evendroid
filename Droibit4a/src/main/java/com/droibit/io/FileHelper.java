package com.droibit.io;

import static com.droibit.utils.NullCheck.isNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.nio.channels.FileChannel;
import java.util.Date;

import android.annotation.TargetApi;
import android.content.Context;
import android.net.Uri;
import android.os.StatFs;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;

import com.droibit.utils.EnvironmentInfo;

/**
 * ファイル操作のユーティリティクラス
 * 
 * @author kumagai
 * 
 */
public final class FileHelper {

	/** ドット */
	public static final String DOT = ".";

	private static final String PATH_FORMAT = "%s/%s";
	private static final int DEFAULT_BUFFER_SIZE = 1024;


	/**
	 * uriからファイル名を取得する
	 * 
	 * @param uri 取得対象のuri
	 * @return ファイル名が存在する場合はそれを、存在しない場合はnullが戻る
	 */
	public static final String getFilename(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return null;
		}

		final int s = uri.lastIndexOf(File.separator);
		if (s >= 0) {
			return uri.substring(s + 1);
		}
		return null;
	}

	/**
	 * uriから拡張子を除いたファイル名を取得する
	 * 
	 * @param uri 取得対象のuri
	 * @return ファイル名が存在する場合はそれを、存在しない場合はnullが戻る
	 */
	public static final String getFilenameWithoutExtenstion(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return null;
		}

		final int s = uri.lastIndexOf(File.separator);
		final int d = uri.lastIndexOf(DOT);

		if (s > 0 && d > 0) {
			return uri.substring(s + 1, d);
		}
		return null;
	}

	/**
	 * uriからファイル名を除いたパスを取得する。
	 * 
	 * @param file 
	 * @return ファイルが存在しない場合はnullが戻る。<br>
	 * 				 ファイルがディレクトリの場合はファイルが、通常のファイルの場合は親ディレクトリのパスが戻る。
	 */
	public static final File getPathWithoutFilename(File file) {
		if (isNotNull(file)) {
			return null;
		}

		if (file.isDirectory()) {
			// no file to be split off. Return everything
			return file;
		}
		// Construct path without file name.
		String pathwithoutname = file.getAbsolutePath().substring(0,
				file.getAbsolutePath().length() - file.getName().length());
		if (pathwithoutname.endsWith(File.separator)) {
			pathwithoutname = pathwithoutname.substring(0,
					pathwithoutname.length() - 1);
		}
		return new File(pathwithoutname);
	}

	/**
	 * uriからファイルの拡張子を取得する
	 * 
	 * @param uri 取得対象のuri
	 * @return ファイルが存在する場合はドットを含めた拡張子、存在しない場合はnullが戻る
	 */
	public static final String getExtension(String uri) {
		if (TextUtils.isEmpty(uri)) {
			return null;
		}

		final int dot = uri.lastIndexOf(DOT);
		if (dot >= 0) {
			return uri.substring(dot);
		}
		return null;
	}
	
	/**
	 * パッケージのURIを取得する
	 * 
	 * @param packageName パッケージ名
	 * @return パッケージのURI
	 */
	public static Uri getPackageUri(String packageName) {
		return Uri.fromParts("package", packageName, null);
	}

	/**
	 * uriをファイルオブジェクトに変換する
	 * 
	 * @param uri 変換対象のuri
	 * @return uriが存在する場合はファイルオブジェクト、存在しない場合はnullが戻る
	 */
	public static final File toFile(Uri uri) {
		if (isNotNull(uri)) {
			return new File(uri.getPath());
		}
		return null;
	}

	/**
	 * パスとファイル名からファイルオブジェクトを作成しする
	 * 
	 * @param targetDirPath ファイルを作成する対象となるディレクトリ
	 * @param filename ファイル名
	 * @return 作成されたファイル
	 */
	public static final File getFile(String targetDirPath, String filename) {
		if (targetDirPath.endsWith(File.separator)) {
			return new File(targetDirPath + filename);
		}
		return new File(targetDirPath, filename);
	}

	/**
	 * パスとファイル名からファイルオブジェクトを作成しする
	 * 
	 * @param targetDir ファイルを作成する対象となるディレクトリ
	 * @param filename ファイル名
	 * @return 作成されたファイル
	 */
	public static final File getFile(File targetDir, String filename) {
		return getFile(targetDir.getAbsolutePath(), filename);
	}

	/**
	 * 子ファイルの親ディレクトリにファイルを作成する
	 * 
	 * @param childFile 親ディレクトリを取得する対象となるファイル
	 * @param filename ファイル名
	 * @return 作成されたファイル
	 */
	public static final File getFileAppendParentDir(File childFile, String filename) {
		return getFile(childFile.getParent(), filename);
	}

	/**
	 * 親ディレクトリにファイル名を追加したパスを取得する。<br>
	 * 
	 * @param parentPath 親ディレクトリのパス
	 * @param filename 追加するファイル名
	 * @return ファイルパス
	 */
	public static final String getPathAppendFilename(String parentPath,
			String filename) {
		if (parentPath.lastIndexOf(File.separator) == 0) {
			return parentPath + filename;
		}
		return String.format(PATH_FORMAT, parentPath, filename);
	}
	
	/**
	 * 対象ディレクトリの全容量を取得する
	 * 
	 * @param dir 対象ディレクトリ
	 * @return ディレクトリの全容量
	 */
	public static long getTotalSpace(File dir) {
		final StatFs fs = new StatFs(dir.getPath());
		return fs.getBlockSizeLong() * fs.getBlockCountLong(); 
	}

	/**
	 * 対象ディレクトリの空き容量を取得する
	 *
	 * @param dir 対象ディレクトリ
	 * @return ディレクトリの空き容量
	 */
	@TargetApi(9)
	public static long getUsableSpace(File dir) {
		if (EnvironmentInfo.hasGingerbread()) {
			return dir.getUsableSpace();
		}
		final StatFs fs = new StatFs(dir.getPath());
		return fs.getBlockSizeLong() * (long) fs.getAvailableBlocksLong();
	}

	/**
	 * ファイルサイズを整形して出力する
	 * 
	 * @param context コンテキスト
	 * @param sizeInBytes ファイルサイズ
	 * @return 整形した結果
	 */
	public static final String formatSize(Context context, long sizeInBytes) {
		return Formatter.formatFileSize(context, sizeInBytes);
	}

	/**
	 * ファイルの更新日を整形して出力する
	 * 
	 * @param context コンテキスト
	 * @param dateTime ファイルの更新日
	 * @return 整形した結果
	 */
	public static String formatDate(Context context, long dateTime) {
		return formatDate(context, new Date(dateTime));
	}
	
	/**
	 * ファイルの更新日を整形して出力する
	 * 
	 * @param context コンテキスト
	 * @param date ファイルの更新日
	 * @return 整形した結果
	 */
	public static String formatDate(Context context, Date date) {
		return DateFormat.getDateFormat(context).format(date);
	}

	/**
	 * ディレクトリ内に存在するファイル数を計算する
	 * 
	 * @param dir 計算対象のディレクトリ
	 * @return 通常のファイルの場合は常に1、ディレクトリの場合はカウントした結果が戻る
	 */
	public static final int calculateFileCountInDir(File dir) {
		return calculateFileCountInDir(dir, 0);
	}

	/**
	 * ディレクトリ内に存在するファイル数を計算する。<br>
	 * <br>
	 * ディレクトリ内に階層が存在する場合は再帰的に処理する。
	 * 
	 * @param file
	 * @param fileCount
	 * @return
	 */
	private static final int calculateFileCountInDir(File file, int fileCount) {
		if (!file.isDirectory()) {
			return 1;
		}

		final File[] files = file.listFiles();
		if (isNotNull(files) && files.length > 1) {
			for (int i = 0, len = files.length; i < len; i++) {
				fileCount = calculateFileCountInDir(files[i], fileCount);
			}
		}
		return fileCount;
	}

	/**
	 * ファイルのサイズを計算する
	 * 
	 * @param file 計算対象のファイル
	 * @return ファイルサイズ
	 */
	public static final long calculateFileSize(File file) {
		return calculateFileSize(file, 0L);
	}

	/**
	 * ファイルのサイズを計算する
	 * <br>
	 * ディレクトリ内に階層が存在する場合は再帰的に処理する。
	 *
	 * @param file 計算対象のファイル
	 * @param fileSize ファイルサイズ
	 * @return ファイルサイズ
	 */
	private static long calculateFileSize(File file, long fileSize) {
		if (file.isFile()) {
			return fileSize + file.length();
		}

		final File[] files = file.listFiles();
		if (isNotNull(files) && files.length > 1) {
			for (int i = 0, len = files.length; i < len; i++) {
				fileSize = calculateFileSize(files[i], fileSize);
			}
		}
		return fileSize;
	}
	
	/**
	 * 入力ストリームの後始末をする
	 * 
	 * @param input 入力ストリーム
	 */
	public static void closeStream(InputStream input) {
		closeStream(input, null);
	}
	
	/**
	 * 出力ストリームの後始末をする
	 * <br>
	 * ストリームを閉じるだけなので{@link OutputStream#flush()}メソッドは呼ばない。
	 * 
	 * @param output 出力ストリーム
	 */
	public static void closeStream(OutputStream output) {
		closeStream(null, output);
	}

	/**
	 * 各ストリームの後始末をする。<br>
	 * <br>
	 * ストリームを閉じるだけなので{@link OutputStream#flush()}メソッドは呼ばない。
	 * 
	 * @param input 入力ストリーム
	 * @param output 出力ストリーム
	 */
	public static void closeStream(InputStream input, OutputStream output) {
		try {
			if (isNotNull(input)) {
				input.close();
			}
			if (isNotNull(output)) {
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 各ストリームの後始末をする
	 * 
	 * @param input 入力チャネル
	 * @param output 出力チャネル
	 */
	public static void closeChanel(FileChannel input, FileChannel output) {
		try {
			if (isNotNull(input)) {
				input.close();
			}
			if (isNotNull(output)) {
				output.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * リーダの後始末をする
	 * 
	 * @param reader 対象のリーダ
	 */
	public static void closeReader(Reader reader) {
		try {
			if (isNotNull(reader)) {
				reader.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ファイルを高速にコピーする。<br>
	 * 
	 * @param src コピー元のファイル
	 * @param dst コピー先のファイル
	 * @throws FileNotFoundException ファイルが存在しない場合にスローされる
	 */
	public static final void copy(File src, File dst) throws FileNotFoundException,
			IOException {
		fastCopy(new FileInputStream(src), new FileOutputStream(dst));
	}

	/**
	 * ファイルをコピーする。<br>
	 * 
	 * @param input コピー元の入力ストリーム
	 * @param output  コピー先の出力ストリーム
	 * @throws IOException 入出力の例外
	 */
	public static final void copy(InputStream input, OutputStream output)
			throws IOException {
		final byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int byteRead;
		while ((byteRead = input.read(buffer)) > -1) {
			output.write(buffer, 0, byteRead);
		}
		output.flush();
	}

	/**
	 * ファイルを高速でコピーする
	 * 
	 * @param input 入力ファイルストリーム
	 * @param output 出力ファイルストリーム
	 * @throws IOException 
	 */
	public static void fastCopy(FileInputStream input, FileOutputStream output)
			throws IOException {
		FileChannel inChannel = null;
		FileChannel outChannel = null;

		try {
			inChannel = input.getChannel();
			outChannel = output.getChannel();

			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			closeChanel(inChannel, outChannel);
		}
	}

	/**
	 * ファイルをコピーする
	 * 
	 * @param srcDir コピー元のファイルのディレクトリ
	 * @param srcFilename コピー元のファイル名
	 * @param dstDir コピー先のファイルのディレクトリ
	 * @param dstFilename コピー先のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult copyFile(File srcDir,
			String srcFilename, File dstDir, String dstFilename, boolean force) {

		return copyFile(getFile(srcDir, srcFilename), getFile(dstDir, dstFilename),
				force);
	}

	/**
	 * ファイルをコピーする
	 * 
	 * @param srcDirpath コピー元のファイルのディレクトリ
	 * @param srcFilename コピー元のファイル名
	 * @param dstDirpath コピー先のファイルのディレクトリ
	 * @param dstFilename コピー先のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult copyFile(String srcDirpath,
			String srcFilename, String dstDirpath, String dstFilename, boolean force) {

		return copyFile(getFile(srcDirpath, srcFilename),
				getFile(dstDirpath, dstFilename), force);
	}

	/**
	 * ファイルをコピーする
	 * 
	 * @param srcFilepath コピー元のファイルパス
	 * @param dstFilePath コピー先のファイルパス
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult copyFile(String srcFilepath,
			String dstFilepath, boolean force) {
		return copyFile(new File(srcFilepath), new File(dstFilepath), force);
	}

	/**
	 * ファイルをコピーする
	 * 
	 * @param srcFile コピー元のファイル
	 * @param dstFile コピー先のファイル
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult copyFile(File srcFile, File dstFile,
			boolean force) {
		if (!dstFile.exists() && dstFile.exists()) {
			return FileOperationResult.EXISTS_FILE;
		}

		try {
			copy(srcFile, dstFile);
			return FileOperationResult.SUCCESS;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return FileOperationResult.FAILED;
	}

	/**
	 * ファイルをリネームする
	 * 
	 * @param oldFilepath リネーム対象のファイルパス
	 * @param newFilename 変更後のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult renameFile(String oldFilepath,
			String newFilename, boolean force) {
		return renameFile(new File(oldFilepath), newFilename, force);
	}

	/**
	 * ファイルをリネームする
	 * 
	 * @param oldfile リネーム対象のファイル
	 * @param newFilename 変更後のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult renameFile(File oldfile,
			String newFilename, boolean force) {
		// 新しいファイル名が空の場合
		if (TextUtils.isEmpty(newFilename)) {
			return FileOperationResult.EMPTY_FILENAME;
		}

		// ファイルの拡張子を変えようとしている場合。
		// ただし、強制的にリネームする場合は無視する
		if (!force && !oldfile.isDirectory()
				&& getExtension(newFilename).equals(oldfile.getName())) {
			return FileOperationResult.CHANGE_FILE_EXTENSION;
		}

		// リネームしたファイルを作成する
		final File newfile = getFileAppendParentDir(oldfile, newFilename);
		// ファイルが既に存在している場合。
		// ただし、強制的にリネームする場合は無視する
		if (!force && newfile.exists()) {
			return FileOperationResult.EXISTS_FILE;
		}

		// 対象のファイルをリネームする
		if (oldfile.renameTo(newfile)) {
			return FileOperationResult.SUCCESS;
		}
		return FileOperationResult.FAILED;
	}

	/**
	 * ファイルを移動させる
	 * 
	 * @param srcDirPath 移動元のファイルのディレクトリのパス
	 * @param srcFilename コピー元のファイル名
	 * @param srcDirPath 移動先のファイルのディレクトリのパス
	 * @param dstFilename コピー先のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult moveFile(String srcDirPath,
			String srcFilename, String dstDirpath, String dstFilename, boolean force) {
		return moveFile(new File(srcDirPath), srcFilename, new File(dstDirpath),
				dstFilename, force);
	}

	/**
	 * ファイルを移動させる
	 * 
	 * @param srcDir 移動元のファイルのディレクトリ
	 * @param srcFilename コピー元のファイル名
	 * @param dstDir 移動先のファイルのディレクトリ
	 * @param dstFilename コピー先のファイル名
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult moveFile(File srcDir,
			String srcFilename, File dstDir, String dstFilename, boolean force) {
		return moveFile(getFile(srcDir, srcFilename), getFile(dstDir, dstFilename),
				force);
	}

	/**
	 * ファイルを移動する
	 * 
	 * @param srcFilepath 移動元のファイルパス
	 * @param dstFilePath 移動先のファイルパス
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult moveFile(String srcFilepath,
			String dstFilepath, boolean force) {
		return moveFile(new File(srcFilepath), new File(dstFilepath), force);
	}

	/**
	 * ファイルを移動する
	 * 
	 * @param srcFilepath 移動元のファイル
	 * @param dstFilePath 移動先のファイル
	 * @param force 強制的に処理を続けるかどうか
	 * @return ファイル操作の結果
	 */
	public static final FileOperationResult moveFile(File srcFile, File dstFile,
			boolean force) {
		if (!force && dstFile.exists()) {
			return FileOperationResult.EXISTS_FILE;
		}

		if (srcFile.renameTo(dstFile)) {
			return FileOperationResult.SUCCESS;
		}
		return FileOperationResult.FAILED;
	}

	/**
	 * ファイルを削除する
	 * 
	 * @param file 削除対象のファイル
	 * @return trueの場合は削除成功、falseの場合は削除失敗
	 */
	public static final boolean deleteFile(File file) {
		if (file.isDirectory()) {
			final File[] children = file.listFiles();
			// ディレクトリに階層が存在する場合は再帰的に削除していく
			if (children != null && children.length > 1) {
				for (int i = 0, len = children.length; i < len; i++) {
					if (!deleteFile(children[i])) {
						return false;
					}
				}
			}
		}
		return file.delete();
	}
}
