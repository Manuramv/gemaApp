package gemalto.com.gemaltouser.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.BuildConfig;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.fragments.QueryFragment;
import gemalto.com.gemaltouser.fragments.SettingsFragment;
import gemalto.com.gemaltouser.fragments.StoredUserFragment;
/**
 * This class {@link MainActivity} contains main logic.Navigaton drawer,fragments switching and all.
 * This class extends the {@link CustomBaseActivity} to get the global features of the application.
 */
public class MainActivity extends CustomBaseActivity  implements NavigationView.OnNavigationItemSelectedListener {
    AppCompatActivity mActivityObj;
    private QueryFragment queryFragment;
    private StoredUserFragment storedUserFragment;
    private SettingsFragment settingsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivityObj = this;
        queryFragment = new QueryFragment();
        storedUserFragment = new StoredUserFragment();
        settingsFragment = new SettingsFragment();
        navigateToFragment(queryFragment,false);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    /**
     * Ondevice backclick this methoid execute.
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * This method takes the user to particular screen based on user selection.
     * @param item Selected item from the navigation drawer.
     */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_setting) {
           // navigateToFragment(settingsFragment,false);
            startActivity(new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
        } else if (id == R.id.nav_queryuser) {
            navigateToFragment(queryFragment,false);

        } else if (id == R.id.nav_view_stored_user) {
            //navigateToFragment(storedUserFragment,false);
            Intent intent = new Intent(this,UserDetailListActivity.class);
            intent.putExtra("COMINGFROM","view stored user");
            startActivity(intent);
        } else if (id == R.id.nav_exit) {
            //System.exit(0);
            android.os.Process.killProcess(android.os.Process.myPid());
            //System.exit(1);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This method helps to replace the fragment.
     * @param frag Name of the fragment to replace.
     * @param addtostack Parameter to check whether needs to keep in stack or not.
     */
    public void navigateToFragment(Fragment frag, boolean addtostack) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentcontainer, frag); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addtostack) {
            ft.addToBackStack(null);
        }

        invalidateOptionsMenu();
        ft.commit();
    }

}
