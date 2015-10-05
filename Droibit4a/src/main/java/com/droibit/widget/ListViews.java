package com.droibit.widget;

import java.util.ArrayList;
import java.util.List;

import android.util.SparseBooleanArray;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;

import com.droibit.utils.Debug;

/**
 * {@link AbsListView}から派生したリストビューを操作するためのユーティリティクラス。
 * 
 * @author kumagaishinya
 *
 */
public final class ListViews {

	private ListViews() {
	}
	
	/**
	 * リストビュー内のすべての項目をチェック済みにする
	 * 
	 * @param listView 操作対象のリストビュー
	 */
	public static void setItemsChecked(AbsListView listView) {
		for (int i= 0, count = listView.getAdapter().getCount(); i < count; i++) {
			listView.setItemChecked(i, true);
		}
	}
	
	/**
	 * リストビュー内のチェックされた項目をクリアする
	 * 
	 * @param listView 操作対象のリストビュー
	 */
	public static final void clearCheckedItems(AbsListView listView) {
		Debug.assertNotNull(listView);
		
		final ListAdapter adapter = listView.getAdapter();
		for (int i = 0, len = adapter.getCount(); i < len; i++) {
			listView.setItemChecked(i, false);
		}
		((BaseAdapter) adapter).notifyDataSetChanged();
	}
	
	/**
	 * リストビュー内のチェックされた項目を取得する
	 * 
	 * @param listView 確認対象のリスト
	 * @return チェックされた項目リスト
	 */
	@SuppressWarnings("unchecked")
	public static <T>List<T> getCheckedItems(AbsListView listView) {
		Debug.assertNotNull(listView);
		
		final ListAdapter adapter = listView.getAdapter();
		final SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
		final List<T> checkedItems = new ArrayList<T>(checkedPositions.size());
		
		for (int i = 0, count = adapter.getCount(); i < count; i++) {
			if (checkedPositions.get(i)) {
				checkedItems.add((T)adapter.getItem(i));
			}
		}
		return checkedItems;
	}
}
