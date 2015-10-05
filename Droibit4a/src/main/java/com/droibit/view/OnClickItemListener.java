package com.droibit.view;

/**
 * リスト内の項目がクリックされたときに呼ばれるイベントリスナ
 * 
 * @author kumagai
 */
public interface OnClickItemListener {

	/**
	 * リスト内の項目がクリックされたときに呼ばれる処理
	 * 
	 * @param position 項目の位置
	 * @return クリックの結果
	 */
	public boolean onClickItem(int position);
}