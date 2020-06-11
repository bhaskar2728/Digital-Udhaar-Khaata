package com.example.digitaludhaarkhata.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.digitaludhaarkhata.ChatActivity;
import com.example.digitaludhaarkhata.Model.User;
import com.example.digitaludhaarkhata.R;

import java.util.ArrayList;

public class DisplayUsersAdapter extends RecyclerView.Adapter<DisplayUsersAdapter.ViewHolder> {

    private Context context;
    private ArrayList<User> userArrayList;
    public DisplayUsersAdapter(Context context,ArrayList<User> arrayList){
        this.context = context;
        this.userArrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.custom_row_user,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final User user = userArrayList.get(position);

        holder.username.setText(user.getUsername());
        holder.email.setText(user.getEmail());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("userid",user.getID());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView username;
        TextView email;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            username = itemView.findViewById(R.id.username);
            email = itemView.findViewById(R.id.email);
        }
    }
}
