package com.droibit.view.actionmode;

import static com.droibit.utils.NullCheck.isNotNull;
import static com.droibit.utils.NullCheck.isNull;
import android.annotation.TargetApi;
import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Checkable;
import android.widget.ListView;

import com.droibit.utils.Debug;

/**
 * {@link ActionMode}と{@link AbsListView#CHOICE_MODE_SINGLE}を組み合わせて使用する。<br>
 * <br>
 * このクラスは{@link ActionMode.Callback}を実装している。<br>
 * <br>
 * コンストラクタで渡されるListViewをロングクリックするとアクションモードが開始する。<br>
 * また、現在のベージョンでは最初にロングクリックされたアイテムは選択されない。<br>
 * <br>
 * リストビューのアイテムとなるビューは{@link Checkable}インターフェースを実装している必要がある。<br>
 * 実装していない場合はメニューをクリックしても
 * {@link #onActionItemClicked(ActionMode, MenuItem, Checkable)}メソッドは呼ばれない。
 * 
 * @author kumagaishinya
 * 
 */
@TargetApi(11)
public class SingleChoiceActionMode implements ActionMode.Callback,
		OnItemLongClickListener, OnItemClickListener {

	/** コンテキスト */
	private final Activity activity;

	/** リストビュー */
	private final ListView listView;

	/** メニューのリソースID */
	private final int menuResId;

	/** アクションモード */
	private ActionMode actionMode;

	/** 現在選択されているビュー */
	private Checkable checkedView;

	/** 選択時に呼ばれるイベントリスナ */
	private SingleChoiceModeListener choiceModeListener;

	/** アクションモード開始前に設定されていたイベントリスナ */
	private OnItemClickListener clickListenerHolder;

	/**
	 * コンストラクタ
	 * 
	 * @param activity アクティビティ
	 * @param listView 操作対象となるリストビュー
	 * @param menuResId メニューのリソースID
	 */
	public SingleChoiceActionMode(Activity activity, ListView listView, int menuResId) {
		Debug.assertNotNull(activity);
		Debug.assertNotNull(listView);

		this.activity = activity;
		this.listView = listView;
		this.menuResId = menuResId;
		this.actionMode = null;
		this.choiceModeListener = new NullSingleChoiceModeListener();
		this.clickListenerHolder = null;

		// ロングクリック時にアクションモードが始まるように設定する
		listView.setOnItemLongClickListener(this);
	}

	/** {@inheritDoc} */
	@Override
	public boolean onCreateActionMode(ActionMode mode, Menu menu) {
		activity.getMenuInflater().inflate(menuResId, menu);
		return true;
	}

	/** {@inheritDoc} */
	@Override
	public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
		if (choiceModeListener.onClickedActionItem(mode, item, checkedView)) {
			mode.finish();
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void onDestroyActionMode(ActionMode mode) {
		// クリックされているビューが存在する場合はチェックを解除しておく
		if (isNotNull(checkedView)) {
			checkedView.setChecked(false);
			checkedView = null;
		}

		// 選択モードを解除する
		listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
		actionMode = null;

		// リストビューをクリックしたときの処理を以前の状態に戻す
		restoreListViewItemClickListener();
	}

	/** {@inheritDoc} */
	@TargetApi(11)
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position,
			long id) {
		// アクションモードが開始されていない場合
		if (isNull(actionMode)) {
			actionMode = activity.startActionMode(this);
			listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
			changeListViewItemClickListener();
			return true;
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (view instanceof Checkable) {
			setCheckedView((Checkable) view);
		}
		choiceModeListener.onCheckedListViewItem(actionMode, position);
	}

	/**
	 * アクションモード時のイベントリスナを取得する
	 * 
	 * @return アクションモード時のイベントリスナ
	 */
	public SingleChoiceModeListener getSingleChoiceModeListener() {
		return choiceModeListener;
	}

	/**
	 * アクションモード時のイベントリスナを設定する
	 * 
	 * @param listener アクションモード時のイベントリスナ
	 */
	public void setSingleChoiceModeListener(SingleChoiceModeListener listener) {
		Debug.assertNotNull(listener);
		choiceModeListener = listener;
	}

	/**
	 * アクションモードを取得する
	 * 
	 * @return アクションモード
	 */
	public ActionMode getActionMode() {
		return actionMode;
	}

	/**
	 * 現在選択されているビューを取得する
	 * 
	 * @return 現在選択されているビュー
	 */
	public Checkable getCheckedView() {
		return checkedView;
	}

	/**
	 * アクションモードが開始されているかどうか
	 * 
	 * @return 戻り値が真の場合は開始している
	 */
	public boolean isStarted() {
		return isNotNull(actionMode);
	}

	/**
	 * リストビューをクリックしたときに呼ばれるイベントリスナを変更する。<br>
	 * 元々設定されているイベントリスナは保存される。
	 */
	private void changeListViewItemClickListener() {
		clickListenerHolder = listView.getOnItemClickListener();
		listView.setOnItemClickListener(this);
	}

	/**
	 * リストビューに元々設定してあったクリックしたときのイベントリスナを復元する
	 */
	private void restoreListViewItemClickListener() {
		listView.setOnItemClickListener(clickListenerHolder);
		clickListenerHolder = null;
	}

	/**
	 * 選択されたビューを保持する
	 * 
	 * @param checkableView 選択されたビュー
	 */
	private void setCheckedView(Checkable checkedView) {
		this.checkedView = checkedView;
	}

	/**
	 * アクションアイテムをクリックした時に呼ばれるイベントリスナ
	 * 
	 * @author kumagaishinya
	 * 
	 */
	public interface SingleChoiceModeListener {
		/**
		 * アクションアイテムをクリックした時に呼ばれる処理。<br>
		 * {@link Checkable}インターフェースを実装したビューの場合このメソッドは呼ばれる。
		 * 
		 * @param mode 現在のアクションモード
		 * @param item クリックされたメニューアイテム
		 * @param checkedItemView 選択されているアイテムビュー
		 * @return アクションモードを終了するかどうか
		 */
		public boolean onClickedActionItem(ActionMode mode, MenuItem item,
				Checkable checkedItemView);

		/**
		 * リストビューのアイテムがクリックされた時に呼ばれる処理
		 * 
		 * @param mode 現在のアクションモード
		 * @param position 選択されたアイテムの位置
		 */
		public void onCheckedListViewItem(ActionMode mode, int position);
	}

	/**
	 * シングルチョイスモード時に各アイテムをクリックした時に呼ばれるイベントリスナ。
	 * 
	 * @author kumagaishinya
	 * 
	 */
	private class NullSingleChoiceModeListener implements SingleChoiceModeListener {

		/** {@inheritDoc} */
		@Override
		public boolean onClickedActionItem(ActionMode mode, MenuItem item,
				Checkable checkedView) {
			return false;
		}

		/** {@inheritDoc} */
		@Override
		public void onCheckedListViewItem(ActionMode mode, int position) {
		}
	}
}
