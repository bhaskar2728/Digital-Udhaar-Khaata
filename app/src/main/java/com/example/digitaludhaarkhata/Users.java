package com.example.digitaludhaarkhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.provider.ContactsContract;

import com.example.digitaludhaarkhata.Adapters.DisplayUsersAdapter;
import com.example.digitaludhaarkhata.Model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Users extends AppCompatActivity {
    RecyclerView usersView;
    DisplayUsersAdapter displayUsersAdapter;
    ArrayList<User> userArrayList;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);


        usersView = findViewById(R.id.usersView);
        usersView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        usersView.setHasFixedSize(true);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");

        userArrayList = new ArrayList<>();
        DisplayUsers();

    }

    private void DisplayUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.show();
                userArrayList.clear();
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    User user = snapshot.getValue(User.class);
                    if(!user.getID().equals(firebaseUser.getUid())){
                        userArrayList.add(user);
                    }
                }
                displayUsersAdapter = new DisplayUsersAdapter(Users.this,userArrayList);
                usersView.setAdapter(displayUsersAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
