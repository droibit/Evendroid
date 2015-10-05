package com.droibit.widget;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ListView;

import com.droibit.utils.Debug;
import com.droibit.utils.NullCheck;

/**
 * リストビューを復元するためユーティリティクラス。<br>
 * {@link Parcelable}インターフェースを実装しているので以下のメソッドが使用できる
 * <ul>
 * <li>{@link Bundle#putParcelable(String, Parcelable)}</li>
 * <li>{@link Bundle#getParcelable(String)}</li>
 * </ul>
 * 
 * @author kumagai
 * 
 */
public class ScrolledListHolder implements Parcelable {

	/** {@link Bundle}クラスにセットするときに使用するキー */
	public static final String KEY_SCROLL_HOLDER = "scrollHolder";

	/** 現在表示されているリストの先頭のインデックス */
	public int topPosition;

	/** 先頭のビューからのオフセット値(ピクセル値) */
	public int offsetY;

	/**
	 * コンストラクタ
	 */
	public ScrolledListHolder() {
		topPosition = offsetY = ListView.INVALID_POSITION;
	}

	/**
	 * コンストラクタ
	 * 
	 * @param listView 保存の対象となるリストビュー
	 */
	public ScrolledListHolder(AbsListView listView) {
		Debug.assertNotNull(listView);

		save(listView);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param in
	 */
	private ScrolledListHolder(Parcel in) {
		topPosition = in.readInt();
		offsetY = in.readInt();
	}

	/** {@inheritDoc} */
	@Override
	public int describeContents() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(topPosition);
		dest.writeInt(offsetY);
	}

	/**
	 * リストビューの現在の表示位置を保存する
	 * 
	 * @param listView 保存の対象となるリストビュー
	 */
	public void save(AbsListView listView) {
		Debug.assertNotNull(listView);

		topPosition = listView.getFirstVisiblePosition();

		final View child = listView.getChildAt(0);
		offsetY = NullCheck.isNotNull(child) ? child.getTop() : 0;
	}

	/**
	 * リストビューのスクロール位置を復元する
	 * 
	 * @param listView 復元対象となるリストビュー
	 */
	public void restore(AbsListView listView) {
		Debug.assertNotNull(listView);

		if (listView instanceof ListView) {
			((ListView) listView).setSelectionFromTop(topPosition, offsetY);
		} else if (listView instanceof GridView) {
			((GridView) listView).setSelection(topPosition);
		}
	}

	/**
	 * リストビューがスクロールしているか?<br>
	 * リストビューの位置を復元する時に使用する
	 * 
	 * @return スクロールしているかどうか?
	 */
	public boolean isScrolled() {
		return topPosition != ListView.INVALID_POSITION;
	}

	/**
	 * Parcelオブジェクトを作成する
	 */
	public static final Creator<ScrolledListHolder> CREATOR = new Creator<ScrolledListHolder>() {
		/** {@inheritDoc} */
		@Override
		public ScrolledListHolder createFromParcel(Parcel source) {
			return new ScrolledListHolder(source);
		}

		/** {@inheritDoc} */
		@Override
		public ScrolledListHolder[] newArray(int size) {
			return new ScrolledListHolder[size];
		}
	};
}
