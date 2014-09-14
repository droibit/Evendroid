package com.droibit.evendroid2;

import android.app.ActionBar;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;

import com.droibit.evendroid2.fragment.LoadableListFragment;
import com.droibit.evendroid2.fragment.util.NavContentFragmentFactory;
import com.droibit.evendroid2.fragment.NavigationDrawerFragment;

import static com.droibit.evendroid2.fragment.NavigationDrawerFragment.Navigations;
import static com.droibit.evendroid2.StartupActivity.KEY_FINISHED_START_UP;

/**
 * @author kumagai
 * @since 2014/09/03
 */
public class MainActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        LoadableListFragment.CallBacks {

    public static final String KEY_NAVIGATION_POSITION = "navigation_position";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        if (!pref.getBoolean(KEY_FINISHED_START_UP, false)) {
            final Intent intent = new Intent(this, StartupActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);
        getActionBar().setDisplayHomeAsUpEnabled(false);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    /** {@inheritDoc} */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    /** {@inheritDoc} */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the search content by replacing fragments
        final FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, NavContentFragmentFactory.create(position))
                .commit();
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onSelectEvent(String eventId) {
        final Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_EVENT_ID, eventId);
        startActivity(intent);
    }

    /** {@inheritDoc} */
    @Override
    public void onApplyActionBarTitle(Navigations navigation) {
        switch (navigation) {
            case SEARCH:
                mTitle = getString(R.string.navigation_title_search);
                break;
            case OWNER:
                mTitle = getString(R.string.navigation_title_owner);
                break;
            case PARTICIPATION:
                mTitle = getString(R.string.navigation_title_participation);
                break;
            case BOOKMARK:
                mTitle = getString(R.string.navigation_title_bookmark);
                break;
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDrawerOpen() {
        return mNavigationDrawerFragment.isDrawerOpen();
    }

    /**
     * アクションバーにタイトルをセットする
     */
    public void restoreActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }
}
