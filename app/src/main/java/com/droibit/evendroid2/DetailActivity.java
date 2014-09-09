package com.droibit.evendroid2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.droibit.evendroid2.fragment.DetailEventFragment;
import com.droibit.evendroid2.fragment.LoadableListFragment;
import com.droibit.evendroid2.fragment.NavigationDrawerFragment;

/**
 * イベントの詳細情報を表示するためのアクティビティ。
 *
 * @author kumagai
 * @since 2014/09/03
 */
public class DetailActivity extends Activity implements LoadableListFragment.CallBacks {

    public static final String EXTRA_EVENT_ID = "event_id";

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a
            // fragment transaction.
            final DetailEventFragment fragment = DetailEventFragment.newInstance(
                    getIntent().getStringExtra(EXTRA_EVENT_ID), true);
            getFragmentManager()
                    .beginTransaction()
                    //.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                    .replace(R.id.container, fragment)
                    .commit();
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        final int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onSelectEvent(String eventId) {
        // 主催者のイベント一覧が選択された時によばれる。
        final DetailEventFragment fragment = DetailEventFragment.newInstance(eventId, false);
        getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    /** {@inheritDoc} */
    @Override
    public void onApplyActionBarTitle(NavigationDrawerFragment.Navigations navigation) {
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDrawerOpen() {
        return false;
    }
}
