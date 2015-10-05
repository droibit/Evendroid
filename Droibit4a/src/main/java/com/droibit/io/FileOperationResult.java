package com.droibit.io;

/**
 * ファイル操作の結果を表す列挙体
 * 
 * @author kumagai
 * 
 */
public enum FileOperationResult {
	EMPTY_FILENAME, 
	CHANGE_FILE_EXTENSION,
	EXISTS_FILE,
	SUCCESS, 
	FAILED,
}
