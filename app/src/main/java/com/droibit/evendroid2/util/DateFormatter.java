package com.droibit.evendroid2.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.droibit.text.Strings;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * レスポンスの日付を整形するためのクラス。
 *
 * @author kumagai
 */
public final class DateFormatter {

    private static final DateFormat SRC_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ");
    private static final DateFormat DEST_SHORT_DATE_FORMAT = new SimpleDateFormat("yy/MM/dd");
    private static final DateFormat DEST_LONG_DATE_FORMAT = new SimpleDateFormat("yy/MM/dd(E) HH:mm");
    private static final DateFormat DEST_LONG_DATE_WITHOUT_YEAR_FORMART = new SimpleDateFormat("MM/dd(E) HH:mm");
    private static final DateFormat DEST_TIME_FORMAT = new SimpleDateFormat("HH:mm");

    /**
     * 短いフォーマットの日付文字列(yy/MM/dd)に変換する。
     *
     * @param dateString 日付文字列
     * @return 短いフォーマットの日付文字列
     */
    public static final String toShortDateString(@Nullable String dateString) {
        if (Strings.isEmptyOrWhitespace(dateString)) {
            return Strings.EMPTY;
        }

        Date srcDate = null;
        try {
            srcDate = SRC_DATE_FORMAT.parse(dateString);
            return DEST_SHORT_DATE_FORMAT.format(srcDate);
        } catch (ParseException e) {
        }
        return Strings.EMPTY;
    }

    /**
     * 短いフォーマットの日付文字列(yy/MM/dd)に変換する。
     *
     * @param date 日付
     * @return 短いフォーマットの日付文字列
     */
    public static final String toShortDateString(@NonNull Date date) {
        if (date == null) {
            return Strings.EMPTY;
        }
        return DEST_SHORT_DATE_FORMAT.format(date);
    }

    /**
     * 長いフォーマットの日付文字列(yy/MM/dd HH:mm)に変換する。
     *
     * @param date 対象の日付
     * @return 長いフォーマットの日付文字列
     */
    public static final String toLongDateString(@Nullable Date date) {
        if (date == null) {
            return Strings.EMPTY;
        }
        return DEST_LONG_DATE_FORMAT.format(date);
    }

    /**
     * 年を除いた長いフォーマットの日付文字列(MM/dd HH:mm)に変換する。
     *
     * @param date 対象の日付
     * @return 長いフォーマットの日付文字列
     */
    public static final String toLongDateWithoutYerString(@Nullable Date date) {
        if (date == null) {
            return Strings.EMPTY;
        }
        return DEST_LONG_DATE_WITHOUT_YEAR_FORMART.format(date);
    }

    /**
     * 日付を時間(HH:mm)のみのフォーマットに変換する。
     *
     * @param date 対象の日付
     * @return 時間のみの日付文字列
     */
    public static final String toTimeString(@Nullable Date date) {
        if (date == null) {
            return Strings.EMPTY;
        }
        return DEST_TIME_FORMAT.format(date);
    }

    /**
     * 日付文字列から{@link Date}オブジェクトに変換する。
     *
     * @param dateString 日付文字列
     * @return {@link Date}オブジェクト
     */
    @Nullable
    public static final Date toDate(@Nullable String dateString) {
        if (Strings.isEmptyOrWhitespace(dateString)) {
            return null;
        }

        try {
            return SRC_DATE_FORMAT.parse(dateString);
        } catch (ParseException e) {
        }
        return null;
    }
}
