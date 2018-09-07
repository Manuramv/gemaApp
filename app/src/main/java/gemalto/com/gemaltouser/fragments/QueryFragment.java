package gemalto.com.gemaltouser.fragments;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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

import java.util.List;

import gemalto.com.gemaltodatalib.dataprocessing.RetrieveData;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltodatalib.serviceimpl.PassGenderDataInterface;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.activities.UserDetailsActivity;
import gemalto.com.gemaltouser.util.CommonUtilities;
import gemalto.com.gemaltouser.util.CustomWatcher;
import gemalto.com.gemaltouser.util.UserDetailDto;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends CustomBaseFragments implements AdapterView.OnItemSelectedListener, View.OnClickListener, PassGenderDataInterface {
    private Spinner spinner;
    private static final String[] paths = {"Select","Female","Male"};
    private View mFragmentView;
    private EditText etSeed,etMultiplUser;
    private Button btnquery;
    private AppCompatActivity mActivityObj;
    PassGenderDataInterface passGenderDataInterfaceObj;
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
        commonUtilities = new CommonUtilities();
        userDetailsFragment = new QueryUserDetailsFragment();

        spinner = (Spinner)mFragmentView.findViewById(R.id.gender_dropdown);
        etSeed = (EditText) mFragmentView.findViewById(R.id.et_seed);
        etMultiplUser = (EditText) mFragmentView.findViewById(R.id.et_multiuser);
        btnquery = (Button) mFragmentView.findViewById(R.id.btn_query);
        btnquery.setOnClickListener(this);

        etSeed.addTextChangedListener(new CustomWatcher(etSeed,etMultiplUser,spinner));
        etMultiplUser.addTextChangedListener(new CustomWatcher(etMultiplUser,etSeed,spinner));

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
        switch (position){
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


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

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
        RetrieveData retrieveDataObj = new RetrieveData(mActivityObj);
        retrieveDataObj.initiateGenderQuery(selectedGender,passGenderDataInterfaceObj);
        commonUtilities.showBusyIndicator(mActivityObj);
    }

    @Override
    public void onReceivingDataFromlib(List<UserResult> list) {
        Log.d("tag","on receiving data from lib in query fragment=========:"+list.get(0).getPhone());
        commonUtilities.removeBusyIndicator(mActivityObj);
        //navigateToFragment(userDetailsFragment, true);
        JSONObject obj = new JSONObject();
        try {
            obj.put("id", list.get(0).getId().getValue().toString());
            obj.put("name", list.get(0).getName().getFirst()+" "+ list.get(0).getName().getLast());
            obj.put("gender", list.get(0).getGender().toString());
            obj.put("age", list.get(0).getDob().getAge().toString());
            obj.put("dob", list.get(0).getDob().getDate().toString());
            obj.put("email", list.get(0).getEmail().toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        navigateToUserDetail(obj);
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

    public void navigateToUserDetail(JSONObject obj){
        Intent intent = new Intent(mActivityObj, UserDetailsActivity.class);
        intent.putExtra("userdata",obj.toString());
        startActivity(intent);
    }





}
