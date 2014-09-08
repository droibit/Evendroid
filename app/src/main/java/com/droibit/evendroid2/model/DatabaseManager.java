package com.droibit.evendroid2.model;

import android.database.sqlite.SQLiteException;
import android.support.annotation.NonNull;

import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.util.List;

/**
 * SQLiteを操作し、データを取得するためのクラス。
 *
 * @author kumagai
 * @since 2014/09/03
 */
public class DatabaseManager {

    private static final DatabaseManager INSTANCE = new DatabaseManager();

    private DatabaseManager() {
    }

    /**
     * 指定された識別IDのブックマークを取得する。
     *
     * @param eventId イベントの識別ID
     * @return ブックマーク
     */
    public static BookmarkableEvent selectBookmark(String eventId) {
        return new Select().from(BookmarkableEvent.class)
                .where("eventId = ?", eventId)
                .executeSingle();
    }

    /**
     * 全てのブックマークを取得する。
     *
     * @return ブックマーク
     */
    public static List<BookmarkableEvent> selectAllBookmarks() {
        return new Select().from(BookmarkableEvent.class)
                .execute();
    }

    /**
     * ブックマーク数をカウントする
     *
     * @return カウント数
     */
    public static int countBookmarks() {
        return new Select().from(BookmarkableEvent.class)
                .orderBy("startedAt ASC")
                .count();
    }

    /**
     * 指定されたイベントがブックマークされているかどうか
     *
     * @param eventId イベントの内ID
     * @return 存在する場合はtrue、存在しない場合はfalseが返る
     */
    public static boolean existsBookmark(@NonNull String eventId) {
        try {
            return new Select().from(BookmarkableEvent.class)
                    .where("eventId = ?", eventId)
                    .exists();
        } catch (SQLiteException e) {
            return false;
        }
    }

    /**
     * ブックマークを全て削除する
     */
    public static void clearBookmarks() {
        new Delete().from(BookmarkableEvent.class).execute();
    }

    /**
     * シングルトンインスタンスを取得する。
     *
     * @return シングルトンインスタンス
     */
    public static final DatabaseManager getInstance() {
        return INSTANCE;
    }
}
