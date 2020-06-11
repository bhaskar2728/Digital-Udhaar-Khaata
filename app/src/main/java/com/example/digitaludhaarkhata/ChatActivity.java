package com.example.digitaludhaarkhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.digitaludhaarkhata.Adapters.DisplayChatsAdapter;
import com.example.digitaludhaarkhata.Adapters.DisplayUsersAdapter;
import com.example.digitaludhaarkhata.Model.Chats;
import com.example.digitaludhaarkhata.Model.User;
import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatActivity extends AppCompatActivity {
    Intent intent;

    int sum;
    Toolbar toolbar;
    TextView username, txtAmountType;
    ImageView imgAmountType;
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    Button btnTakePayment,btnGiveCredit;
    LinearLayout takePayment,giveCredit;
    Dialog dialog_payment,dialog_credit;
    EditText amountPayment,amountCredit;
    ArrayList<Chats> chatsArrayList;
    TextView Total;
    RecyclerView messagesView;
    DisplayChatsAdapter chatsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Firebase.setAndroidContext(this);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        intent = getIntent();
        final String userid = intent.getStringExtra("userid");


        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

        txtAmountType = findViewById(R.id.txtAmountType);
        imgAmountType = findViewById(R.id.imgAmountType);

        username  = findViewById(R.id.username);
        takePayment = findViewById(R.id.takePayment);
        giveCredit = findViewById(R.id.giveCredit);
        Total = findViewById(R.id.Total);

        messagesView = findViewById(R.id.messagesView);
        messagesView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        messagesView.setLayoutManager(linearLayoutManager);

        takePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_payment.show();
            }
        });
        giveCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog_credit.show();
            }
        });


        dialog_credit = new Dialog(this);
        dialog_credit.setContentView(R.layout.custom_dialog_give_credit);

        btnGiveCredit = dialog_credit.findViewById(R.id.btnGiveCredit);
        amountCredit = dialog_credit.findViewById(R.id.amountCredit);

        btnGiveCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sender = firebaseUser.getUid();
                String from = firebaseUser.getUid();
                String receiver = userid;
                String to = userid;
                String amount = amountCredit.getText().toString();
                if(!TextUtils.isEmpty(amount)) {
                    Transaction(sender, receiver, to, from, amount);
                }
                else{
                    Toast.makeText(ChatActivity.this, "Please fill the amount", Toast.LENGTH_SHORT).show();
                }

            }
        });


        dialog_payment = new Dialog(this);
        dialog_payment.setContentView(R.layout.custom_dialog_take_payment);

        btnTakePayment = dialog_payment.findViewById(R.id.btnTakePayment);
        amountPayment = dialog_payment.findViewById(R.id.amountPayment);

        btnTakePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sender = firebaseUser.getUid();
                String from = userid;
                String receiver = userid;
                String to = firebaseUser.getUid();
                String amount = amountPayment.getText().toString();
                if(!TextUtils.isEmpty(amount)) {
                    Transaction(sender, receiver, to, from, amount);
                }
                else{
                    Toast.makeText(ChatActivity.this, "Please fill the amount", Toast.LENGTH_SHORT).show();
                }
            }
        });





        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());

                readMessage(firebaseUser.getUid(),user.getID());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    public void Transaction(String sender,String receiver,String to,String from,String amount){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("from",from);
        hashMap.put("to",to);
        hashMap.put("amount",amount);

        java.util.Date date=new java.util.Date();
        String date_str = date.toString().split(" ")[2] + " " + date.toString().split(" ")[1];

        hashMap.put("date",date_str);

        reference.child("Transactions").push().setValue(hashMap);
        dialog_payment.dismiss();
        dialog_credit.dismiss();

    }
    public void readMessage(final String myid, final String userid){

        chatsArrayList = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("Transactions");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatsArrayList.clear();
                sum = 0;
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){

                    Chats chats = snapshot.getValue(Chats.class);
                    if((chats.getSender().equals(myid) && chats.getReceiver().equals(userid))||(chats.getSender().equals(userid) && chats.getReceiver().equals(myid))){
                        chatsArrayList.add(chats);
                        if(chats.getFrom().equals(myid)){
                            sum-=Integer.parseInt(chats.getAmount());
                        }
                        else{
                            sum+=Integer.parseInt(chats.getAmount());
                        }

                    }

                }

                if (sum>0){
                    imgAmountType.setImageResource(R.drawable.ic_down_arrow);
                    txtAmountType.setText("Due");
                    txtAmountType.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                    imgAmountType.setColorFilter(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_red_dark), PorterDuff.Mode.SRC_IN);

                } else if (sum<0){
                    imgAmountType.setImageResource(R.drawable.ic_up_arrow);
                    txtAmountType.setText("Advance");
                    txtAmountType.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
                    imgAmountType.setColorFilter(ContextCompat.getColor(getApplicationContext(),android.R.color.holo_green_dark), PorterDuff.Mode.SRC_IN);
                } else {
                    imgAmountType.setImageResource(0);
                    txtAmountType.setText("Balanced");
                    txtAmountType.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
                }

                Total.setText(String.valueOf(Math.abs(sum)));
                chatsAdapter = new DisplayChatsAdapter(getApplicationContext(),chatsArrayList);
                messagesView.setAdapter(chatsAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
