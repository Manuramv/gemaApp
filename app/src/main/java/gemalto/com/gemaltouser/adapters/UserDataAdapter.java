package gemalto.com.gemaltouser.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import gemalto.com.gemaltouser.R;

/**
 * Created by Manuramv on 9/8/2018.
 */

public class UserDataAdapter extends RecyclerView.Adapter<UserDataAdapter.ViewHolder> {

    AppCompatActivity mAppCompatActivity;

    public UserDataAdapter(AppCompatActivity mAppCompatActivity) {
        this.mAppCompatActivity = mAppCompatActivity;
    }

    @NonNull
    @Override
    public UserDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.userdata_rview_adapter, parent, false);
        return new UserDataAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserDataAdapter.ViewHolder holder, int position) {
        holder.tvEmail.setText("email");
        holder.tvUsername.setText("usernma emanu");
        holder.tvGender.setText("gender  ");
    }

    @Override
    public int getItemCount() {
        return 5;
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
