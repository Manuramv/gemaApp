package gemalto.com.gemaltouser.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

import gemalto.com.gemaltodatalib.networking.response.genderquery.GetGenderQueryInfoResponse;
import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.R;
import gemalto.com.gemaltouser.activities.UserDetailsActivity;

import static gemalto.com.gemaltouser.util.CommonUtilities.generateJsonObj;

/**
 * Created by Manuramv on 9/8/2018.
 */

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    private final GetGenderQueryInfoResponse responseObj;
    AppCompatActivity mAppCompatActivity;
    ArrayList<UserResult> dataList;

    public UserDataAdapter(AppCompatActivity mAppCompatActivity, GetGenderQueryInfoResponse list) {
        this.mAppCompatActivity = mAppCompatActivity;
        this.dataList = list.getResults();
        this.responseObj = list;
    }

    @NonNull
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdata_rview_adapter, parent, false);
        return new UserDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAdapter.ViewHolder holder, final int position) {
        if(dataList.get(position).getEmail().toString()!=null && !TextUtils.isEmpty(dataList.get(position).getEmail().toString())) {
            holder.tvEmail.setText(dataList.get(position).getEmail());
        }
        if(dataList.get(position).getName()!=null && dataList.get(position).getName().getFirst()!=null && dataList.get(position).getName().getLast()!=null){
            holder.tvUsername.setText(dataList.get(position).getName().getFirst()+" "+dataList.get(position).getName().getLast());
        }
        if(dataList.get(position).getGender().toString()!=null && !TextUtils.isEmpty(dataList.get(position).getGender().toString())){
            holder.tvGender.setText(dataList.get(position).getGender().toString());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDetailPage(responseObj,position);
            }
        });

    }




    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvUsername,tvGender,tvEmail;


        public ViewHolder(View view) {
            super(view);
            tvUsername = (TextView) view.findViewById(R.id.username);
            tvGender = (TextView) view.findViewById(R.id.gender);
            tvEmail = (TextView) view.findViewById(R.id.email);


        }

    }

    /**
     * This method takes the user to the detail screen.
     * @param dataList list of data
     *  @param  position Item position which is selected by the user from the list.
     */
    private void showDetailPage(GetGenderQueryInfoResponse dataList, int position) {
        //navigateToFragment(userDetailsFragment, true);
        JSONObject obj = generateJsonObj(dataList,position);
        Intent intent = new Intent(mAppCompatActivity, UserDetailsActivity.class);
        intent.putExtra("userdata",obj.toString());
        intent.putExtra("comingfrom","fromuserList");
        mAppCompatActivity.startActivity(intent);
    }
}
