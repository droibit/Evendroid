package com.droibit.app.fragment.dialog;

import static com.droibit.app.fragment.dialog.DialogFragmentInfo.ARG_DIALOG_INFO;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.app.DialogFragment;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.droibit.R;
import com.droibit.app.dialog.OkCancelDialogBuilder;

/**
 * ユーザにOK/キャンセルを入力するだけの簡単なダイアログフラグメント。<br>
 * <br>
 * このクラスは{@link DialogFragment}を継承している。<br>
 * 呼び出し元のアクティビティで{@link com.droibit.app.fragment.dialog.OkCancelDialogFragment.OnDialogAskListener}を実装する必要がある。<br>
 * インスタンスを作成する場合はファクトリメソッドの{@link #newInstance(DialogFragmentInfo)}を使用する。<br>
 * 
 * @author kumagai
 * 
 */
public class OkCancelDialogFragment extends DialogFragment {

    private static final String ARG_DISABLE_CANCEL = "disable_cancel";
	private static final String TAG_DIALOG = "askDialog";

	private OnDialogAskListener listener;
    private int dialogKey;

	/**
	 * 新しいインスタンスを作成するファクトリメソッド
	 *
	 * @param info ダイアログに表示する情報
	 * @return 新しいインスタンス
	 */
	public static OkCancelDialogFragment newInstance(DialogFragmentInfo info) {
        return newInstance(info, false);
	}

    /**
     * 新しいインスタンスを作成するファクトリメソッド
     *
     * @param info ダイアログに表示する情報
     * @param disableCancel キャンセルボタンを非表示にするかどうか
     * @return 新しいインスタンス
     */
    public static OkCancelDialogFragment newInstance(DialogFragmentInfo info, boolean disableCancel) {
        final OkCancelDialogFragment df = new OkCancelDialogFragment();
        final Bundle args = new Bundle(2);

        args.putParcelable(ARG_DIALOG_INFO, info);
        args.putBoolean(ARG_DISABLE_CANCEL, disableCancel);
        df.setArguments(args);

        return df;
    }

	/** {@inheritDoc} */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		if (getTargetFragment() instanceof OnDialogAskListener) {
			listener = (OnDialogAskListener) getTargetFragment();
		} else if (activity instanceof OnDialogAskListener) {
			listener = (OnDialogAskListener)activity;
		} else {
			throw new IllegalStateException();
		}
	}

	/** {@inheritDoc} */
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		final DialogFragmentInfo info = getArguments().getParcelable(ARG_DIALOG_INFO);
        final boolean disalbeCancel = getArguments().getBoolean(ARG_DISABLE_CANCEL);
		// ダイアログに表示するビューを作成する
		final View contentView = LayoutInflater.from(getActivity()).inflate(
				R.layout.simple_textview_medium, null, false);
		final TextView textView = (TextView) contentView.findViewById(R.id.simple_textview);
		textView.setText(info.resContent);

        dialogKey = info.dialogKey;
        final Runnable onCancel = new Runnable() {
            @Override
            public void run() {
                listener.onDialogClickCancel(dialogKey);
            }
        };

		final AlertDialog.Builder builder = new OkCancelDialogBuilder(getActivity(), new Runnable() {
			@Override
			public void run() {
				listener.onDialogClickOk(dialogKey);
			}
		}, !disalbeCancel ? onCancel : null).setView(contentView).setTitle(info.resTitle);

		return builder.create();
	}

	/**
	 * ダイアログを表示するためのヘルパーメソッド
	 * 
	 * @param fragment 呼び出し元のフラグメント
	 */
	public void show(Fragment fragment) {
		setTargetFragment(fragment, 0);
        show(fragment.getFragmentManager(), TAG_DIALOG);
	}

	/**
	 * ダイアログのボタンがクリックされたときに呼ばれるインベントリスナ
	 * 
	 * @author kumagai
	 *
	 */
	public interface OnDialogAskListener {
		
		/**
		 * "はい"ボタンがクリックされたときに呼ばれる処理 
		 */
		public void onDialogClickOk(int dialogKey);

		/**
		 * "いいえ"ボタンが押されたときに呼ばれる処理
		 */
		public void onDialogClickCancel(int dialogKey);
	}
}
