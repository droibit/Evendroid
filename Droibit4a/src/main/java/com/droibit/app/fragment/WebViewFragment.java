/**
 * 
 */
package com.droibit.app.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

/**
 * A fragment that displays a WebView.
 * <p>
 * The WebView is automically paused or resumed when the Fragment is paused or
 * resumed.
 * 
 * @author kumagaishinya
 * 
 */
public class WebViewFragment extends Fragment {

	private WebView mWebView;
	private boolean mIsWebViewAvailable;

	public WebViewFragment() {
	}

	/**
	 * Called to instantiate the view. Creates and returns the WebView.
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (mWebView != null) {
			mWebView.destroy();
		}
		mWebView = new WebView(getActivity());
        mWebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
                    mWebView.goBack();
                    return true;
                }
                return false;
            }
        });
		mIsWebViewAvailable = true;
		return mWebView;
	}

	/**
	 * Called when the fragment is visible to the user and actively running.
	 * Resumes the WebView.
	 */
	@Override
	public void onPause() {
		super.onPause();
		mWebView.onPause();
	}

	/**
	 * Called when the fragment is no longer resumed. Pauses the WebView.
	 */
	@Override
	public void onResume() {
		mWebView.onResume();
		super.onResume();
	}

	/**
	 * Called when the WebView has been detached from the fragment. The WebView
	 * is no longer available after this time.
	 */
	@Override
	public void onDestroyView() {
		mIsWebViewAvailable = false;
		super.onDestroyView();
	}

	/**
	 * Called when the fragment is no longer in use. Destroys the internal state
	 * of the WebView.
	 */
	@Override
	public void onDestroy() {
		if (mWebView != null) {
			mWebView.destroy();
			mWebView = null;
		}
		super.onDestroy();
	}

	/**
	 * Gets the WebView.
	 */
	public WebView getWebView() {
		return mIsWebViewAvailable ? mWebView : null;
	}
}