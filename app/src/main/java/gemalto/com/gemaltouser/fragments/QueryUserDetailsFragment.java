package gemalto.com.gemaltouser.fragments;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.activities.CustomBaseActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class QueryUserDetailsFragment extends CustomBaseFragments {


    public QueryUserDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_query_user_details, container, false);
    }

}
