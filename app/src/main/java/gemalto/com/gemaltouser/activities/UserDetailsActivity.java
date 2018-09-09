package gemalto.com.gemaltouser.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.util.CommonUtilities;


/**
 * @class UserDetailsActivity This activity show the specific user details.
 *
 */
public class UserDetailsActivity extends CustomBaseActivity {

    private JSONObject json_object;
    TextView txtUserId,txtUsername,txtGender,txtAge,txtDob,txtEmail;
    private Button btnStoreDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        setAppToolbar("Query User",R.drawable.ic_back_arrow);
        txtUserId = (TextView)findViewById(R.id.txt_userID);
        txtUsername = (TextView)findViewById(R.id.txt_name);
        txtGender = (TextView)findViewById(R.id.txt_gender);
        txtAge = (TextView)findViewById(R.id.txt_age);
        txtDob = (TextView)findViewById(R.id.txt_dob);
        txtEmail = (TextView)findViewById(R.id.txt_email);
        btnStoreDelete = (Button) findViewById(R.id.btn_store_delete);

        try {
            json_object = new JSONObject(getIntent().getStringExtra("userdata"));

            Log.e("TAG", "Example Item: " + json_object.getString("name"));
            txtUserId.setText(json_object.getString("id"));
            txtUsername.setText(json_object.getString("name"));
            txtGender.setText(json_object.getString("gender"));
            txtAge.setText(json_object.getString("age"));
            txtDob.setText(json_object.getString("dob"));
            txtEmail.setText(json_object.getString("email"));

        } catch (JSONException e) {
            e.printStackTrace();
        }
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
    protected void onDestroy() {
        super.onDestroy();
        CommonUtilities.removeBusyIndicator(this);
        finish();
    }
}
