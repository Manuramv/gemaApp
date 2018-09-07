package gemalto.com.gemaltouser.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.adapters.UserDataAdapter;
import gemalto.com.gemaltouser.util.SimpleDividerItemDecoration;

public class UserDetailListActivity extends CustomBaseActivity {

    RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserDataAdapter userDataAdapterObj;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_list);
        setAppToolbar("Query User",R.drawable.ic_back_arrow);

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_userlist);

        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        userDataAdapterObj = new UserDataAdapter(this);
        mRecyclerView.setAdapter(userDataAdapterObj);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));


    }

}
