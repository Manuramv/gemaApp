package gemalto.com.gemaltouser.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import gemalto.com.gemaltodatalib.callbackinterface.PassMultipleUserIdInterface;
import gemalto.com.gemaltodatalib.dataprocessing.RetrieveMultipleUserData;
import gemalto.com.gemaltodatalib.networking.response.genderquery.GetGenderQueryInfoResponse;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.adapters.UserDataAdapter;
import gemalto.com.gemaltouser.util.CommonUtilities;
import gemalto.com.gemaltouser.util.SimpleDividerItemDecoration;

public class UserDetailListActivity extends CustomBaseActivity implements PassMultipleUserIdInterface {

    RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserDataAdapter userDataAdapterObj;
    GetGenderQueryInfoResponse list;
    private AppCompatActivity mActivityObj;
    private PassMultipleUserIdInterface passMultipleUserIdInterfaceObj;
    CommonUtilities commonUtilities;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_list);
        setAppToolbar("Query User",R.drawable.ic_back_arrow);
        mActivityObj = this;
        passMultipleUserIdInterfaceObj = this;
        commonUtilities = new CommonUtilities();

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_userlist);
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(extras.getString("COMINGFROM").equalsIgnoreCase("query user")){
                setAppToolbar("Query User",R.drawable.ic_back_arrow);
                list = (GetGenderQueryInfoResponse) getIntent().getExtras().get("myArrayListKey");
                showlistVIew(list);
            } else  if(extras.getString("COMINGFROM").equalsIgnoreCase("view stored user")){
                setAppToolbar("View Stored User",R.drawable.ic_back_arrow);
                fetchDataFromDb();

            }


        }




    }

    private void showlistVIew(GetGenderQueryInfoResponse list) {
        userDataAdapterObj = new UserDataAdapter(this, list);
        mRecyclerView.setAdapter(userDataAdapterObj);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));
    }

    private void fetchDataFromDb() {
        RetrieveMultipleUserData retrieveMultipleUserDataObj = new RetrieveMultipleUserData(mActivityObj);
        retrieveMultipleUserDataObj.initiateMultipleUserQuery("ALL",passMultipleUserIdInterfaceObj);
       // commonUtilities.showBusyIndicator(mActivityObj);
    }



    @Override
    public void onReceivingMultipleUserDataFromlib(GetGenderQueryInfoResponse getGenderQueryInfoResponse) {
       // commonUtilities.removeBusyIndicator(mActivityObj);
        showlistVIew(getGenderQueryInfoResponse);

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
