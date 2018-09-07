package gemalto.com.gemaltouser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import gemalto.com.gemaltodatalib.networking.response.genderquery.UserResult;
import gemalto.com.gemaltouser.R;

/**
 * Created by Manuramv on 9/8/2018.
 */

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    AppCompatActivity mAppCompatActivity;
    ArrayList<UserResult> dataList;

    public UserDataAdapter(AppCompatActivity mAppCompatActivity, ArrayList<UserResult> list) {
        this.mAppCompatActivity = mAppCompatActivity;
        this.dataList = list;
    }

    @NonNull
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdata_rview_adapter, parent, false);
        return new UserDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAdapter.ViewHolder holder, int position) {
        if(dataList.get(position).getEmail().toString()!=null && !TextUtils.isEmpty(dataList.get(position).getEmail().toString())) {
            holder.tvEmail.setText(dataList.get(position).getEmail());
        }
        if(dataList.get(position).getName()!=null && dataList.get(position).getName().getFirst()!=null && dataList.get(position).getName().getLast()!=null){
            holder.tvUsername.setText(dataList.get(position).getName().getFirst()+" "+dataList.get(position).getName().getLast());
        }
        if(dataList.get(position).getGender().toString()!=null && !TextUtils.isEmpty(dataList.get(position).getGender().toString())){
            holder.tvGender.setText(dataList.get(position).getGender().toString());
        }

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
}
