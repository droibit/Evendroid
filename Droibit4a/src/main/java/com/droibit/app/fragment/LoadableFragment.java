/**
 * 
 */
package com.droibit.app.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/**
 * ロード可能なフラグメント。<br>
 * コンテンツのロード中にプログレスダイアログを表示する。<br>
 * 動作は{@link android.app.ListFragment}と同じ。
 * 
 * @author kumagaishinya
 * 
 */
public abstract class LoadableFragment extends Fragment {
	static final int INTERNAL_PROGRESS_CONTAINER_ID = 0x00ff0002;
	static final int INTERNAL_MAIN_CONTAINER_ID = 0x00ff0003;

	final private Handler mHandler = new Handler();

	final private Runnable mRequestFocus = new Runnable() {
		public void run() {
			// mContentView.focusableViewAvailable(mContentView);
		}
	};

	View mContentView;
	View mProgressContainer;
	View mMainContainer;
	boolean mContentShown;

	public LoadableFragment() {
	}

	protected abstract View createContentView(Context context);

	/**
	 * Provide default implementation to return a simple list view. Subclasses
	 * can override to replace with their own layout. If doing so, the returned
	 * view hierarchy <em>must</em> have a ListView whose id is
	 * {@link android.R.id#list android.R.id.list} and can optionally have a
	 * sibling view id {@link android.R.id#empty android.R.id.empty} that is to
	 * be shown when the list is empty.
	 * 
	 * <p>
	 * If you are overriding this method with your own custom content, consider
	 * including the standard layout {@link android.R.layout#list_content} in
	 * your layout file, so that you continue to retain all of the standard
	 * behavior of ListFragment. In particular, this is currently the only way
	 * to have the built-in indeterminant progress state be shown.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		final Context context = getActivity();

		FrameLayout root = new FrameLayout(context);

		// ------------------------------------------------------------------

		LinearLayout pframe = new LinearLayout(context);
		pframe.setId(INTERNAL_PROGRESS_CONTAINER_ID);
		pframe.setOrientation(LinearLayout.VERTICAL);
		pframe.setVisibility(View.GONE);
		pframe.setGravity(Gravity.CENTER);

		ProgressBar progress = new ProgressBar(context, null,
				android.R.attr.progressBarStyleLarge);
		pframe.addView(progress, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));

		root.addView(pframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// ------------------------------------------------------------------

		FrameLayout mframe = new FrameLayout(context);
		mframe.setId(INTERNAL_MAIN_CONTAINER_ID);

		View cv = createContentView(getActivity());
		cv.setId(android.R.id.content);
		mframe.addView(cv, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		root.addView(mframe, new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		// ------------------------------------------------------------------

		root.setLayoutParams(new FrameLayout.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT));

		return root;
	}

	/**
	 * Attach to list view once the view hierarchy has been created.
	 */
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		ensureContent();
	}

	/**
	 * Detach from list view.
	 */
	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mRequestFocus);
		mContentView = null;
		mContentShown = false;
		super.onDestroyView();
	}

	/**
	 * Get the activity's content view widget.
	 */
	public View getContentView() {
		ensureContent();
		return mContentView;
	}

	/**
	 * Control whether the list is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown If true, the list view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 */
	public void setContentShown(boolean shown) {
		setContentShown(shown, true);
	}

	/**
	 * Like {@link #setContentShown(boolean)}, but no animation is used when
	 * transitioning from the previous state.
	 */
	public void setContentShownNoAnimation(boolean shown) {
		setContentShown(shown, false);
	}

	/**
	 * Control whether the list is being displayed. You can make it not
	 * displayed if you are waiting for the initial data to show in it. During
	 * this time an indeterminant progress indicator will be shown instead.
	 * 
	 * @param shown If true, the list view is shown; if false, the progress
	 *            indicator. The initial value is true.
	 * @param animate If true, an animation will be used to transition to the
	 *            new state.
	 */
	private void setContentShown(boolean shown, boolean animate) {
		ensureContent();
		if (mProgressContainer == null) {
			throw new IllegalStateException(
					"Can't be used with a custom content view");
		}
		if (mContentShown == shown) {
			return;
		}
		mContentShown = shown;
		if (shown) {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
				mMainContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
			} else {
				mProgressContainer.clearAnimation();
				mMainContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.GONE);
			mMainContainer.setVisibility(View.VISIBLE);
		} else {
			if (animate) {
				mProgressContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_in));
				mMainContainer.startAnimation(AnimationUtils.loadAnimation(
						getActivity(), android.R.anim.fade_out));
			} else {
				mProgressContainer.clearAnimation();
				mMainContainer.clearAnimation();
			}
			mProgressContainer.setVisibility(View.VISIBLE);
			mMainContainer.setVisibility(View.GONE);
		}
	}

	private void ensureContent() {
		if (mContentView != null) {
			return;
		}
		View root = getView();
		if (root == null) {
			throw new IllegalStateException("Content view not yet created");
		}

		mProgressContainer = root.findViewById(INTERNAL_PROGRESS_CONTAINER_ID);
		mMainContainer = root.findViewById(INTERNAL_MAIN_CONTAINER_ID);
		View rawContentView = mMainContainer.findViewById(android.R.id.content);
		if (rawContentView == null) {
            throw new RuntimeException(
                    "Your content must have a content view whose id attribute is " +
                    "'android.R.id.content'");
		}
		mContentView = rawContentView;
		mContentShown = true;
		
		mHandler.post(mRequestFocus);
	}
}
