package gemalto.com.gemaltouser.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
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

import java.util.List;

import gemalto.com.gemaltodatalib.dataprocessing.RetrieveData;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltodatalib.serviceimpl.PassGenderDataInterface;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.util.CommonUtilities;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends CustomBaseFragments implements AdapterView.OnItemSelectedListener, View.OnClickListener, PassGenderDataInterface {
    private Spinner spinner;
    private static final String[] paths = {"Female","Male"};
    private View mFragmentView;
    private EditText etSeed,etMultiplUser;
    private Button btnquery;
    private AppCompatActivity mActivityObj;
    PassGenderDataInterface passGenderDataInterfaceObj;
    CommonUtilities commonUtilities;
    private String selectedGender;

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

        spinner = (Spinner)mFragmentView.findViewById(R.id.gender_dropdown);
        etSeed = (EditText) mFragmentView.findViewById(R.id.et_seed);
        etMultiplUser = (EditText) mFragmentView.findViewById(R.id.et_multiuser);
        btnquery = (Button) mFragmentView.findViewById(R.id.btn_query);
        btnquery.setOnClickListener(this);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,paths);

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
                break;

            case 2:
                selectedGender = "male";
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
    }
}
