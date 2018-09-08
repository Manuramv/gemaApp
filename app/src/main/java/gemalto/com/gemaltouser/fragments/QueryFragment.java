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
      /*  selectedGender = paths[position];
        etSeed.setText("");
        etMultiplUser.setText("");*/
       // etMultiplUser.setText("");

        selectedGender = paths[position];
        GemAppConstants.isGenderSelected = true;
        GemAppConstants.isUserIDSelected = false;
        GemAppConstants.isMultiUserSelected = true;

        Log.d("tag","Selected item::"+selectedGender);


       /* switch (position){
            case 1:
                selectedGender = "female";
                etSeed.setText("");
                etMultiplUser.setText("");
                break;

            case 2:
                selectedGender = "male";
                etSeed.setText("");
                etMultiplUser.setText("");
                break;


        }*/

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

    private void proceedWithDataQuery() {
         {
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
        } /*catch (Exception e){
            Log.d("Tag","Exception happened"+e);
        }*/
    }

    @Override
    public void onReceivingDataFromlib(GetGenderQueryInfoResponse list) {
        Log.d("tag","on receiving data from lib in query fragment=========:"+list.getResults(). get(0).getPhone());
        commonUtilities.removeBusyIndicator(mActivityObj);
        //navigateToFragment(userDetailsFragment, true);
         JSONObject obj = generateJsonObjToDisplay(list);
        navigateToUserDetail(obj, "comingFromGender");
    }


    @Override
    public void onReceivingUserIdDataFromlib(GetGenderQueryInfoResponse list) {
        commonUtilities.removeBusyIndicator(mActivityObj);
        Log.d("tag","on receiving data from lib in query fragment=========:"+list.getResults(). get(0).getPhone());
        JSONObject obj = generateJsonObjToDisplay(list);

        navigateToUserDetail(obj,"comingFromSeedId");
    }

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

    private JSONObject generateJsonObj(List<UserResult> list) {
        JSONObject obj = new JSONObject();
        try {
            //obj.put("id", list.get(0).getValue());
            obj.put("name", list.get(0).getName().getFirst()+" "+ list.get(0).getName().getLast());
            obj.put("gender", list.get(0).getGender().toString());
            obj.put("age", list.get(0).getDob().getAge().toString());
            obj.put("dob", list.get(0).getDob().getDate().toString());
            obj.put("email", list.get(0).getEmail().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return obj;
    }

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


    public void navigateToFragment(android.support.v4.app.Fragment frag, boolean addtostack) {

        FragmentTransaction ft = mActivityObj.getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentcontainer, frag); // f1_container is your FrameLayout container
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        if (addtostack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void navigateToUserDetail(JSONObject obj, String entryPoint){
        Intent intent = new Intent(mActivityObj, UserDetailsActivity.class);
        intent.putExtra("userdata",obj.toString());
        intent.putExtra("comingfrom",entryPoint);
        startActivity(intent);
    }



}
