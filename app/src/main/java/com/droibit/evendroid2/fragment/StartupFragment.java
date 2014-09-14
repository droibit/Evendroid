package com.droibit.evendroid2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.droibit.app.fragment.dialog.DialogFramgentInfo;
import com.droibit.app.fragment.dialog.OkCancelDialogFragment;
import com.droibit.content.IntentHepler;
import com.droibit.evendroid2.MainActivity;
import com.droibit.evendroid2.R;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.http.url.WebPage;

import static com.droibit.evendroid2.StartupActivity.KEY_FINISHED_START_UP;

/**
 * 初回起動時にアカウントを設定するためのフラグメント。
 *
 * @author kumagai
 * @since 2014/09/11.
 */
public class StartupFragment extends Fragment
        implements View.OnClickListener, OkCancelDialogFragment.OnDialogAskListener {

    private static final int DIALOG_KEY_ENTER_OK = 1;
    private static final int DIALOG_KEY_CREATE_ACCOUNT = DIALOG_KEY_ENTER_OK+1;

    private EditText mTextAccountName;

    /** {@inheritDoc} */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View containerView = inflater.inflate(R.layout.fragment_startup, container, false);
        mTextAccountName = (EditText) containerView.findViewById(R.id.edit_account_name);
        containerView.findViewById(R.id.button_ok).setOnClickListener(this);
        containerView.findViewById(R.id.button_create_account).setOnClickListener(this);
        return containerView;
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button_ok) {
            onClickOk(v);
        } else {
            onLaunchBrowser(v);
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onDialogClickOk(int dialogKey) {
        if (dialogKey == DIALOG_KEY_ENTER_OK) {
            launchMainActivity();
        } else if (dialogKey == DIALOG_KEY_CREATE_ACCOUNT) {
            // アカウントの作成の場合
            IntentHepler.launchBrowser(getActivity(), WebPage.getLoginUri(EventServices.ATND));
        }
    }

    /** {@inheritDoc} */
    @Override
    public void onDialogClickCancel(int dialogKey) {
    }

    /**
     * "OK"ボタンがクリックされた時に呼ばれる処理
     *
     * @param view
     */
    public void onClickOk(View view) {
        final DialogFramgentInfo info;
        final boolean disableCancel;
        // アカウントが入力されている場合
        if (!TextUtils.isEmpty(mTextAccountName.getText())) {
            info = new DialogFramgentInfo(DIALOG_KEY_ENTER_OK, R.string.startup_dialog_title_account,
                    R.string.startup_dialog_content_guide_account);
            disableCancel = true;
        } else {
            info = new DialogFramgentInfo(DIALOG_KEY_ENTER_OK, R.string.startup_dialog_title_account,
                    R.string.startup_dialog_content_warning_account);
            disableCancel = false;
        }
        OkCancelDialogFragment.newInstance(info, disableCancel).show(this);
    }

    /**
     * アカウント作成ボタンがクリックされた時に呼ばれる処理
     *
     * @param view
     */
    public void onLaunchBrowser(View view) {
        final DialogFramgentInfo info = new DialogFramgentInfo(DIALOG_KEY_CREATE_ACCOUNT,
                R.string.startup_dialog_title_create_account,
                R.string.startup_dialog_content_create_account);
        OkCancelDialogFragment.newInstance(info, false).show(this);
    }

    private void launchMainActivity() {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pref.edit()
                .putBoolean(KEY_FINISHED_START_UP, true)
                .putString(getString(R.string.pref_key_atnd_user_name), mTextAccountName.getText().toString())
                .commit();

        // ログインが終わったのでメイン画面を表示する。
        startActivity(new Intent(getActivity(), MainActivity.class));
        getActivity().finish();
    }
}
