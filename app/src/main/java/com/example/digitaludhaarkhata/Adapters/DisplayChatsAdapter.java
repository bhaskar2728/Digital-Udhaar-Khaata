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
import com.example.digitaludhaarkhata.Model.Chats;
import com.example.digitaludhaarkhata.Model.User;
import com.example.digitaludhaarkhata.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class DisplayChatsAdapter extends RecyclerView.Adapter<DisplayChatsAdapter.ViewHolder> {

    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;
    int MSG_TO;

    private Context context;
    private ArrayList<Chats> chatsArrayList;
    FirebaseUser firebaseUser;

    public DisplayChatsAdapter(Context context,ArrayList<Chats> arrayList){
        this.context = context;
        this.chatsArrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.right_custom_row, parent, false);
            return new ViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.left_custom_row, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chats chat = chatsArrayList.get(position);
        holder.amount.setText(context.getResources().getString(R.string.Rs)+" "+chat.getAmount());

        java.util.Date date=new java.util.Date();
        String date_str = date.toString().split(" ")[2] + " " + date.toString().split(" ")[1];

        if(chat.getDate().equals(date_str)){
            holder.date.setText("Today");
        }
        else{
            holder.date.setText(chat.getDate());
        }

            if(MSG_TO==1){
            holder.mode.setText("Paid");
        }
        else{
            holder.mode.setText("Received");
        }

    }

    @Override
    public int getItemCount() {
        return chatsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView amount;
        TextView mode;
        TextView date;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);
            amount = itemView.findViewById(R.id.amount);
            mode = itemView.findViewById(R.id.mode);
            date = itemView.findViewById(R.id.date);
        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if(chatsArrayList.get(position).getSender().equals(firebaseUser.getUid())){
            if(chatsArrayList.get(position).getFrom().equals(firebaseUser.getUid())){
                MSG_TO = 1;
            }
            else{
                MSG_TO = 0;
            }

            return MSG_RIGHT;
        }
        else {
            if(chatsArrayList.get(position).getFrom().equals(firebaseUser.getUid())){
                MSG_TO = 1;
            }
            else{
                MSG_TO = 0;
            }
            return MSG_LEFT;
        }
    }
}
