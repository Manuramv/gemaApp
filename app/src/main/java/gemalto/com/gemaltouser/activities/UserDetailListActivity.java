package gemalto.com.gemaltouser.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.adapters.UserDataAdapter;
import gemalto.com.gemaltouser.util.SimpleDividerItemDecoration;

public class UserDetailListActivity extends CustomBaseActivity {

    RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserDataAdapter userDataAdapterObj;
    ArrayList<UserResult> list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_list);
        setAppToolbar("Query User",R.drawable.ic_back_arrow);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           // String value = extras.getString("key");
            //The key argument here must match that used in the other activity
            list = (ArrayList<UserResult>) getIntent().getExtras().get("myArrayListKey");
        }

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_userlist);

        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        userDataAdapterObj = new UserDataAdapter(this,list);
        mRecyclerView.setAdapter(userDataAdapterObj);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
