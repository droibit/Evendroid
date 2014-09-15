package com.droibit.evendroid2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.droibit.evendroid2.fragment.StartupFragment;

/**
 * 初回起動時にアカウントを設定するためのアクティビティ。
 *
 * @author kumagai
 * @since 2014/09/12
 */
public class StartupActivity extends Activity {

    public static final String KEY_FINISHED_START_UP = "finished_start_up";

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new StartupFragment())
                .commit();
    }
}
