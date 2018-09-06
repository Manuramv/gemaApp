package gemalto.com.gemaltouser.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import gemalto.com.gemaltouser.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryFragment extends CustomBaseFragments implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    private Spinner spinner;
    private static final String[] paths = {"Female","Male"};
    private View mFragmentView;
    private EditText etSeed,etMultiplUser;
    private Button btnquery;

    public QueryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         mFragmentView = inflater.inflate(R.layout.fragment_query, container, false);

        spinner = (Spinner)mFragmentView.findViewById(R.id.gender_dropdown);
        etSeed = (EditText) mFragmentView.findViewById(R.id.et_seed);
        etMultiplUser = (EditText) mFragmentView.findViewById(R.id.et_multiuser);
        btnquery = (Button) mFragmentView.findViewById(R.id.btn_query);
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        return mFragmentView;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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
    }
}
