package com.droibit.evendroid2.fragment.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import com.droibit.content.IntentHepler;
import com.droibit.evendroid2.model.ListableUser;
import com.droibit.eventservice.events.EventServices;
import com.droibit.eventservice.http.url.WebPage;
import com.droibit.eventservice.social.EventServiceAccount;
import com.droibit.eventservice.social.SocialAccount;
import com.droibit.eventservice.social.SocialNetworkServices;
import com.droibit.text.Strings;

import java.util.ArrayList;
import java.util.List;

/**
 * 参加者のアカウント情報を選択するためのダイアログ。
 *
 * @author kumagai
 * @since 2014/10/28
 */
public class AccountDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    private static final String KEY_USER = "user";
    private static final String TAG_DIALOG = "accountDialog";

    private List<SocialAccount> mUserAccounts;

    /**
     * 新しいインスタンスを作成する
     *
     * @param user アカウント情報の表示対象の参加者
     * @return 新しいインスタンス
     */
    public static final AccountDialogFragment newInstance(ListableUser user) {
        final Bundle args = new Bundle(1);
        args.putSerializable(KEY_USER, user);

        final AccountDialogFragment f = new AccountDialogFragment();
        f.setArguments(args);

        return f;
    }

    /** {@inheritDoc} */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ListableUser user = (ListableUser)getArguments().getSerializable(KEY_USER);

        mUserAccounts = makeSocialAccounts(user);
        final List<String> accountNames = new ArrayList<String>(mUserAccounts.size());
        for (SocialAccount account : mUserAccounts) {
            accountNames.add(account.getServiceName());
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setItems(accountNames.toArray(new String[0]), this);

        return builder.create();
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(DialogInterface dialog, int which) {
        final SocialAccount account = mUserAccounts.get(which);
        IntentHepler.launchBrowser(getActivity(), account.getUserUri());
    }

    /**
     * ダイアログを表示するためのヘルパーメソッド
     *
     * @param fm フラグメントマネージャ
     */
    public void show(FragmentManager fm) {
        show(fm, TAG_DIALOG);
    }

    private List<SocialAccount> makeSocialAccounts(ListableUser user) {
        final EventServiceAccount account = new EventServiceAccount(EventServices.ATND);
        account.setName(user.getName());
        account.setUserId(user.getUserID());

        if (!Strings.isEmptyOrWhitespace(user.getTwitterID())) {
            final Uri uri = WebPage.getSocialPageUri(SocialNetworkServices.TWITTER, user.getTwitterID());
            account.addSocialAccount(new SocialAccount(SocialNetworkServices.TWITTER.getName(), uri));
        }
        return account.getAllAcounts();
    }
}
