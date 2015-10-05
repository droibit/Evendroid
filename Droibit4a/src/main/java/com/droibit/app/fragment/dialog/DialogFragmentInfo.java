package com.droibit.app.fragment.dialog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * {@link android.app.DialogFragment}を作成するためのリソースIDを格納するためのクラス。<br>
 * {@link #dialogKey}は外部から設定しない場合は-1が設定される。
 * 
 * @author kumagaishinya
 *
 */
public class DialogFragmentInfo implements Parcelable {

	/** {@link android.os.Bundle}のキー */
	public static final String ARG_DIALOG_INFO = "dialogInfo";

    /** 呼び出し元を識別するためのキー */
    public int dialogKey;
	
	/** ダイアログタイトルのリソースID */
	public int resTitle;
	
	/** ダイアログのコンテントのリソースID */
	public int resContent;

	/**
	 * 新しいインスタンスを作成する
	 */
	public DialogFragmentInfo() {
		this(-1, -1, -1);
	}
	
	/**
	 * 新しいインスタンスを作成する
	 * 
	 * @param resTitle ダイアログのタイトルのリソースID
	 * @param resContent ダイアログのコンテントのリソースID
	 */
	public DialogFragmentInfo(int resTitle, int resContent) {
		this(resTitle, resContent, -1);
	}
	
	/**
	 * 新しいインスタンスを作成する
	 *
     * @param dialogKey 呼び出し元を識別するためのキー
	 * @param resTitle ダイアログのタイトルのリソースID
	 * @param resContent ダイアログのコンテントのリソースID
	 */
	public DialogFragmentInfo(int dialogKey, int resTitle, int resContent) {
        this.dialogKey = dialogKey;
		this.resTitle = resTitle;
		this.resContent = resContent;
	}

	private DialogFragmentInfo(Parcel in) {
        dialogKey = in.readInt();
		resTitle = in.readInt();
		resContent = in.readInt();
	}
	
	/** {@inheritDoc} */
	@Override
	public int describeContents() {
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(dialogKey);
		dest.writeInt(resTitle);
		dest.writeInt(resContent);
	}
	
	/**
	 * Parcelオブジェクトを作成する
	 */
	public static final Creator<DialogFragmentInfo> CREATOR = new Creator<DialogFragmentInfo>() {
		/** {@inheritDoc} */
		@Override
		public DialogFragmentInfo createFromParcel(Parcel source) {
			return new DialogFragmentInfo(source);
		}

		/** {@inheritDoc} */
		@Override
		public DialogFragmentInfo[] newArray(int size) {
			return new DialogFragmentInfo[size];
		}
	};	
}
