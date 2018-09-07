package gemalto.com.gemaltouser.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import gemalto.com.gemaltouser.R;

public class UserDetailsActivity extends CustomBaseActivity {

    private JSONObject json_object;
    TextView txtUserId,txtUsername,txtGender,txtAge,txtDob,txtEmail;

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
}
