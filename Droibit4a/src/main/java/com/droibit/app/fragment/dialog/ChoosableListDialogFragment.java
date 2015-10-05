package com.droibit.app.fragment.dialog;

import static com.droibit.app.fragment.dialog.DialogFragmentInfo.ARG_DIALOG_INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;

import com.droibit.view.OnClickItemListener;

/**
 * リストからアイテムを選択するダイアログフラグメント。<br>
 * 選択された位置は保存されるが永続化はしていない。
 * 
 * @author kumagaishinya
 * 
 */
public class ChoosableListDialogFragment extends DialogFragment implements
		DialogInterface.OnClickListener {

	private static final String TAG_DIALOG = "listDialog";
	private static final String KEY_POSITION = "position";
	private static final String KEY_LIST_TYPE = "listType";

	private OnClickItemListener listener;

	/**
	 * アイテムタイプの新しいインスタンスを作成するファクトリメソッド
	 * 
	 * @param info ダイアログ表示のための情報
	 * @param position リストの初期選択位置
	 * @return 新しいインスタンス
	 */
	public static ChoosableListDialogFragment newInstanceItems(DialogFragmentInfo info, int position) {
		return newInstance(info, Lists.ITEMS, position);
	}
	
	/**
	 * シングルチョイスタイプの新しいインスタンスを作成するファクトリメソッド
	 * 
	 * @param info ダイアログ表示のための情報
	 * @param position リストの初期選択位置
	 * @return 新しいインスタンス
	 */
	public static ChoosableListDialogFragment newInstanceChoise(DialogFragmentInfo info, int position) {
		return newInstance(info, Lists.SINLE_CHOISE_ITEMS, position);
	}
	
	private static ChoosableListDialogFragment newInstance(DialogFragmentInfo info, Lists listType, int position) {
		final ChoosableListDialogFragment df = new ChoosableListDialogFragment();

		final Bundle args = new Bundle();
		args.putParcelable(ARG_DIALOG_INFO, info);
		args.putInt(KEY_POSITION, position);
		args.putInt(KEY_LIST_TYPE, listType.getIndex());
		df.setArguments(args);

		return df;
	}
	
	/** {@inheritDoc} */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (getTargetFragment() instanceof OnClickItemListener) {
			listener = (OnClickItemListener) getTargetFragment();
		} else if (activity instanceof OnClickItemListener) {
			listener = (OnClickItemListener) activity;
		} else {
			throw new IllegalStateException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final DialogFragmentInfo info = getArguments().getParcelable(
				ARG_DIALOG_INFO);
		final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if (info.resTitle != -1) {
			builder.setTitle(info.resTitle);
		}
		
		switch (Lists.from(getArguments().getInt(KEY_LIST_TYPE))) {
		case ITEMS:
			builder.setItems(info.resContent, this);
			break;
		case SINLE_CHOISE_ITEMS:
			builder.setSingleChoiceItems(info.resContent, getArguments()
					.getInt(KEY_POSITION), this);
			break;
		default:
			break;
		}
		return builder.create();
	}

	/** {@inheritDoc} */
	@Override
	public void onClick(DialogInterface dialog, int which) {
		if (listener.onClickItem(which)) {
			dismiss();
		}
		getArguments().putInt(KEY_POSITION, which);
	}
	
	/**
	 * ダイアログを表示するためのヘルパーメソッド
	 * 
	 * @param fm フラグメントマネージャ
	 */
	public void show(FragmentManager fm) {
		show(fm, TAG_DIALOG);
	}
	
	/**
	 * ダイアログを表示するためのヘルパーメソッド<br>
	 * 引数のフラグメントは{@link #setTargetFragment(Fragment, int)}で保持される。
	 * 
	 * @param targetFragment 表示元のフラグメント
	 */
	public void show(Fragment targetFragment) {
		setTargetFragment(targetFragment, 0);
		show(targetFragment.getFragmentManager(), TAG_DIALOG);
	}
	
	/**
	 * リストの種類を定義する列挙体
	 * 
	 * @author kumagai
	 */
	private enum Lists {
		ITEMS(0), SINLE_CHOISE_ITEMS(1), UNKINOWN(-1);
		
		private Lists(int index) {
			this.index = index;
		}
		private final int index;
		
		public int getIndex() {
			return index;
		}
		
		/**
		 * 整数値から{@link Lists}列挙体を作成する
		 * 
		 * @param index 列挙体に対応する整数値
		 * @return 対応する{@link Lists}列挙体
		 */
		public static Lists from(int index) {
			switch (index) {
			case 0:
				return ITEMS;
			case 1:
				return SINLE_CHOISE_ITEMS;
			case -1:
				return UNKINOWN;
			default:
				throw new IllegalArgumentException();
			}
		}
	}
}
