package gemalto.com.gemaltouser;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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

import gemalto.com.gemaltodatalib.GenderCallBack;
import gemalto.com.gemaltodatalib.dataprocessing.RetrieveData;
import gemalto.com.gemaltodatalib.networking.response.genderquery.GetGenderQueryInfoResponse;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltodatalib.serviceimpl.PassGenderDataInterface;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener, GenderCallBack, PassGenderDataInterface {
    AppCompatActivity mActivityObj;
    List<UserResult> str;
    PassGenderDataInterface passGenderDataInterfaceObj;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mActivityObj = this;
        passGenderDataInterfaceObj = this;
        //str = (Call<GetGenderQueryInfoResponse>) new ArrayList<>();
        /*gemalto.com.gemaltodatalib.MainActivity mainActivity = new gemalto.com.gemaltodatalib.MainActivity();
        mainActivity.checkConnection();*/

        RetrieveData retrieveDataObj = new RetrieveData(mActivityObj);
       /* //List<UserResult> str =   retrieveDataObj.initiateGenderQuery("female");
        PassGenderDataInterface passGenderDataInterface = new PassGenderDataInterface() {
            @Override
            public void onReceivingDataFromlib(List<UserResult> list) {
                Log.d("tag","on receiving data from lib::"+list.get(0).getPhone());
            }
        };*/



        str = retrieveDataObj.initiateGenderQuery("female",this);
        //Log.d("tag","Size of s=="+s.toString());
try {
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
        @Override
        public void run() {
            // Do something after 5s = 5000ms
            // Log.d("tag","Size of s in app=="+str. get(0).getGender().toString()+"postal code=="+str.get(0).getLocation().getPostcode());
            try{
                Log.d("tag", "Size of s in app==" + str.toString());
            }
            catch (Exception e){

            }


        }
    }, 5000);
} catch (Exception e){
    Log.d("tag", "Exception:::"+e.toString());
}

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public List<UserResult> onSuccess(List<UserResult> list) {
        Log.d("TAG","UserList:::===="+list.toString());
        return null;
    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onReceivingDataFromlib(List<UserResult> list) {
        Log.d("tag","on receiving data from lib=========:"+list.get(0).getPhone());
    }
}
