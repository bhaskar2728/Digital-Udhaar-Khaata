package com.example.digitaludhaarkhata;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

public class AfterLogin extends AppCompatActivity {

    Button btnNewChat;
    FirebaseAuth mAuth;
    TextView username;
    DatabaseReference reference;
    FirebaseUser firebaseUser;
    RecyclerView chatList;
    Toolbar toolbar;
    DisplayUsersAdapter usersAdapter;
    ArrayList<User> userArrayList;
    ArrayList<String> users;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_login);
        Firebase.setAndroidContext(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading");
        progressDialog.show();


        btnNewChat = findViewById(R.id.btnNewChat);

        chatList = findViewById(R.id.chatList);
        chatList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatList.setHasFixedSize(true);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        username = findViewById(R.id.username);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        btnNewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AfterLogin.this, Users.class));
            }
        });
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                username.setText(user.getUsername());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        users = new ArrayList<>();
        DisplayUsers();
    }

    private void DisplayUsers() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Transactions");

        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                progressDialog.show();
                users.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chats chats = snapshot.getValue(Chats.class);

                    if (!users.contains(chats.getSender()) && !users.contains(chats.getReceiver())) {

                        if (chats.getSender().equals(firebaseUser.getUid())) {
                            users.add(chats.getReceiver());
                        } else if (chats.getReceiver().equals(firebaseUser.getUid())) {
                            users.add(chats.getSender());
                        }
                    }
                }
                for (String id : users) {
                    Log.i("UserID", id);
                }
                DisplayUsers2();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void DisplayUsers2() {

        userArrayList = new ArrayList<>();
        DatabaseReference reference2;

        reference2 = FirebaseDatabase.getInstance().getReference("Users");

        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userArrayList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.getValue(User.class);
                    for (String id : users) { //all those users chatting with current user
                        if (user.getID().equals(id)) {
                            if (userArrayList.size() != 0) {
                                int flag = 0;
                                for (User user1 : userArrayList) { //check if user there in list
                                    if (!user1.getID().equals(user.getID())) {
//                                        flag = 1;
                                        Log.d("id", user1.getID());
                                        userArrayList.add(user);
                                        break;
                                    }

                                }
//                                if (flag == 1)
//                                    userArrayList.add(user);
                            } else {
                                userArrayList.add(user);
                            }
                        }
                    }

                }

                usersAdapter = new DisplayUsersAdapter(AfterLogin.this, userArrayList);
                chatList.setAdapter(usersAdapter);
                progressDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.logout:
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setIcon(R.drawable.dialog_logout_icon);
                builder.setTitle("Logout");
                builder.setMessage("Are you sure you want to Logout?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(AfterLogin.this, MainActivity.class));
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
                return true;
        }
        return false;
    }
}
