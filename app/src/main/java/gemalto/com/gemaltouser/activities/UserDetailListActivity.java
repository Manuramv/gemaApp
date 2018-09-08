package gemalto.com.gemaltouser.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.adapters.UserDataAdapter;
import gemalto.com.gemaltouser.util.Employee;
import gemalto.com.gemaltouser.util.EmployeeOperations;
import gemalto.com.gemaltouser.util.SimpleDividerItemDecoration;

public class UserDetailListActivity extends CustomBaseActivity {

    RecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private UserDataAdapter userDataAdapterObj;
    ArrayList<UserResult> list;
    private EmployeeOperations employeeOps;
    private Employee newEmployee;
    public Button dbtes;
    private Employee newEmployee18;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_list);
        newEmployee = new Employee();
        newEmployee18 = new Employee();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if(getIntent().getExtras().getString("COMINGFROM").equalsIgnoreCase("view stored user")){
                setAppToolbar("View Stored User",R.drawable.ic_back_arrow);
                employeeOps = new EmployeeOperations(this);
                employeeOps.open();
                newEmployee.setEmpId(Long.parseLong("18"));
                newEmployee.setFirstname("Manu");
                newEmployee.setLastname("ramv");
                newEmployee.setEmail("manuram692@gmail.com");
                newEmployee.setDob("31/07/1988");
                employeeOps.addEmployee(newEmployee);


                newEmployee18.setEmpId(Long.parseLong("28"));
                newEmployee18.setFirstname("Sreelakshmi");
                newEmployee18.setLastname("manu");
                newEmployee18.setEmail("manuram692@gmail.com");
                newEmployee18.setDob("31/07/1988");
                employeeOps.addEmployee(newEmployee18);





            } else {
                setAppToolbar("View query User",R.drawable.ic_back_arrow);
                list = (ArrayList<UserResult>) getIntent().getExtras().get("myArrayListKey");
            }
           // String value = extras.getString("key");
            //The key argument here must match that used in the other activity

        }

        mRecyclerView = (RecyclerView)findViewById(R.id.rv_userlist);
        dbtes = (Button) findViewById(R.id.dbtest);

        dbtes.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                if(employeeOps.getAllEmployees()!=null && employeeOps.getAllEmployees().size()>0){
                    employeeOps.removeEmployee(employeeOps.getEmployee(Long.parseLong("18")));

                    Log.d("ttt","Size of database:::"+employeeOps.getAllEmployees().size());

                   ArrayList<Employee> emplSingle =  employeeOps.getAllEmployees();
                    Log.d("ttt","Size of database:::"+emplSingle.get(0).getFirstname());
                    Log.d("ttt","Size of database:::"+emplSingle.get(1).getFirstname());


                }
                else {
                    Log.d("ttt","empty");
                }

            }
        });
        linearLayoutManager = new LinearLayoutManager(this, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        userDataAdapterObj = new UserDataAdapter(this,list);
       /* mRecyclerView.setAdapter(userDataAdapterObj);
        mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(this));*/


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




    @Override
    protected void onPause() {
        super.onPause();
        employeeOps.close();

    }
}
