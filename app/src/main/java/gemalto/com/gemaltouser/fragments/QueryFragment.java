package gemalto.com.gemaltouser.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Parcelable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import gemalto.com.gemaltodatalib.callbackinterface.PassMultipleUserIdInterface;
import gemalto.com.gemaltodatalib.callbackinterface.PassUserIdInterface;
import gemalto.com.gemaltodatalib.dataprocessing.RetrieveData;
import gemalto.com.gemaltodatalib.dataprocessing.RetrieveMultipleUserData;
import gemalto.com.gemaltodatalib.dataprocessing.RetrieveUserIdData;
import gemalto.com.gemaltodatalib.networking.response.genderquery.GetGenderQueryInfoResponse;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltodatalib.serviceimpl.PassGenderDataInterface;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.activities.UserDetailListActivity;
import gemalto.com.gemaltouser.activities.UserDetailsActivity;
import gemalto.com.gemaltouser.util.CommonUtilities;
import gemalto.com.gemaltouser.util.CustomWatcher;
import gemalto.com.gemaltouser.util.GemAppConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends CustomBaseFragments implements AdapterView.OnItemSelectedListener, View.OnClickListener, PassGenderDataInterface, PassUserIdInterface, PassMultipleUserIdInterface {
    private Spinner spinner;
    private static final String[] paths = {"Select","Female","Male"};
    private View mFragmentView;
    private EditText etSeed,etMultiplUser;
    private Button btnquery;
    private AppCompatActivity mActivityObj;
    PassGenderDataInterface passGenderDataInterfaceObj;
    private PassUserIdInterface passUserIDInterfaceObj;
    private PassMultipleUserIdInterface passMultipleUserIdInterfaceObj;
    CommonUtilities commonUtilities;
    private String selectedGender;
    private QueryUserDetailsFragment userDetailsFragment;


    public QueryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mFragmentView = inflater.inflate(R.layout.fragment_query, container, false);
         mActivityObj = (AppCompatActivity) this.getActivity();
         passGenderDataInterfaceObj = this;
        passUserIDInterfaceObj = this;
        passMultipleUserIdInterfaceObj = this;
        commonUtilities = new CommonUtilities();
        userDetailsFragment = new QueryUserDetailsFragment();

        spinner = (Spinner)mFragmentView.findViewById(R.id.gender_dropdown);
        etSeed = (EditText) mFragmentView.findViewById(R.id.et_seed);
        etMultiplUser = (EditText) mFragmentView.findViewById(R.id.et_multiuser);
        btnquery = (Button) mFragmentView.findViewById(R.id.btn_query);
        btnquery.setOnClickListener(this);

        etSeed.addTextChangedListener(new CustomWatcher(etSeed,etMultiplUser,spinner, "editingUserIdField"));
        etMultiplUser.addTextChangedListener(new CustomWatcher(etMultiplUser,etSeed,spinner, "editingMultipleuser"));

        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,paths){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
               if(position == 0){
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                }
                else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return mFragmentView;
    }



    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        selectedGender = paths[position];
        GemAppConstants.isGenderSelected = true;
        GemAppConstants.isUserIDSelected = false;
        GemAppConstants.isMultiUserSelected = true;

        Log.d("tag","Selected item::"+selectedGender);

        }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        GemAppConstants.isGenderSelected = false;

    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.btn_query:
                proceedWithDataQuery();
             break;
             default:
                 break;
        }
    }
    /**
     * This method triggers when user clicking the query button, it will call the correct API of the library based on the user query criteria.
     * There are 3 different APIs for gender,seed/user and multiple user to fetch data from library.
     */
    private void proceedWithDataQuery() {
         try{
            if (GemAppConstants.isGenderSelected) {
                RetrieveData retrieveDataObj = new RetrieveData(mActivityObj);
                retrieveDataObj.initiateGenderQuery(selectedGender.toLowerCase(), passGenderDataInterfaceObj);
                commonUtilities.showBusyIndicator(mActivityObj);
            } else if (GemAppConstants.isUserIDSelected) {

                RetrieveUserIdData retrieveUserIdDataObj = new RetrieveUserIdData(mActivityObj);
                retrieveUserIdDataObj.initiateUserIDQuery(etSeed.getText().toString().toLowerCase(), passUserIDInterfaceObj);
                commonUtilities.showBusyIndicator(mActivityObj);
            } else {
                RetrieveMultipleUserData retrieveMultipleUserDataObj = new RetrieveMultipleUserData(mActivityObj);
                retrieveMultipleUserDataObj.initiateMultipleUserQuery(etMultiplUser.getText().toString(), passMultipleUserIdInterfaceObj);
                commonUtilities.showBusyIndicator(mActivityObj);
            }
        } catch (Exception e){
             Toast toast=Toast.makeText(mActivityObj,"Something went wrong could be no matching data, please try again later",Toast.LENGTH_SHORT);
             toast.show();
        }
    }

    /**
     * This method returns the data from the library if the user is selected gender option
     * @param list this parameter contains the data based on the Gender query.
     */
    @Override
    public void onReceivingDataFromlib(GetGenderQueryInfoResponse list) {
        commonUtilities.removeBusyIndicator(mActivityObj);
        Log.d("tag","on receiving data from lib in query fragment=========:"+list.getResults(). get(0).getPhone());
        //navigateToFragment(userDetailsFragment, true);
         JSONObject obj = generateJsonObjToDisplay(list);
        navigateToUserDetail(obj, "comingFromGender");
    }

    /**
     * This method returns the data from the library if the user is selected userid/seed option
     * @param list this parameter contains the data based on the UserId/seed query.
     */
    @Override
    public void onReceivingUserIdDataFromlib(GetGenderQueryInfoResponse list) {
        commonUtilities.removeBusyIndicator(mActivityObj);
        Log.d("tag","on receiving data from lib in query fragment=========:"+list.getResults(). get(0).getPhone());
        JSONObject obj = generateJsonObjToDisplay(list);

        navigateToUserDetail(obj,"comingFromSeedId");
    }

    /**
     * This method returns the data from the library if the user is selected multiple user count option
     * @param list this parameter contains the data based on the Multiple user query.
     */
    @Override
    public void onReceivingMultipleUserDataFromlib(GetGenderQueryInfoResponse list) {
        commonUtilities.removeBusyIndicator(mActivityObj);
        Log.d("TAG","Multiple user call back in fragment::"+list.getInfo().getSeed());
        Intent intent = new Intent(mActivityObj, UserDetailListActivity.class);
        //intent.putSerializableExtra("songs", songs);
        // Bundle bundle = new Bundle();
        intent.putExtra("COMINGFROM","query user");
        intent.putExtra("myArrayListKey",  list);
        //intent.putExtra("myBundle", bundle);
        startActivity(intent);
    }

    /**
     * This method parse the response whatever receiving from the Library and parse it into the json and returns to show in UI.
     * @param list Data to convert in to json for the ease of use.
     */
    private JSONObject generateJsonObjToDisplay(GetGenderQueryInfoResponse list) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", list.getInfo().getSeed());
            obj.put("name", list.getResults().get(0).getName().getFirst()+" "+ list.getResults().get(0).getName().getLast());
            obj.put("gender", list.getResults().get(0).getGender().toString());
            obj.put("age", list.getResults().get(0).getDob().getAge().toString());
            obj.put("dob", list.getResults().get(0).getDob().getDate().toString());
            obj.put("email", list.getResults().get(0).getEmail().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }



    /**
     * This method takes the user to the next screen to show the deatils of user.
     * @param obj Json object to show the data in the screen.
     *  @param entryPoint Parameter tells the next screen from which method I am coming from.
     */
    public void navigateToUserDetail(JSONObject obj, String entryPoint){
        Intent intent = new Intent(mActivityObj, UserDetailsActivity.class);
        intent.putExtra("userdata",obj.toString());
        intent.putExtra("comingfrom",entryPoint);
        startActivity(intent);
    }



}
