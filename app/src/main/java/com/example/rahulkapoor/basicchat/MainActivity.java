package com.example.rahulkapoor.basicchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;


public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private Button fab;
    private FirebaseListAdapter<ChatMessage> adapter;
    private ArrayList<String> list = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private ListView messageList;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the firebaseauth;
        init();

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);
        } else {

            Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
            //displayChat();
        }

        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        messageList = (ListView) findViewById(R.id.listView);
        messageList.setAdapter(listAdapter);

        reference = FirebaseDatabase.getInstance().getReference();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText input = (EditText) findViewById(R.id.input);
                //push message to firebase db;
//                Map<String, Object> map = new HashMap<>();
//                map.put(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail());
//                reference.push().setValue(map);
                ChatMessage chatMessage = new ChatMessage(input.getText().toString(), FirebaseAuth.getInstance().getCurrentUser().getEmail());//it will init the time;
                reference.push().setValue(chatMessage);
                //clear out message;
                input.setText("");
            }
        });

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull final DataSnapshot dataSnapshot) {

                Log.i("data", dataSnapshot.toString());
                //it is called first time and after that when data changes;
//                ChatMessage chatMessage = dataSnapshot.getValue(ChatMessage.class);
//                if (chatMessage != null) {
//                    Log.i("class_info", chatMessage.toString());
//                }

                if (dataSnapshot.getChildrenCount() > 0) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        // TODO: handle the post
                        Log.i("snapshot_data", postSnapshot.getValue().toString());
                        String msgUser = (String) postSnapshot.child("messageUser").getValue();
                        String msgText = (String) postSnapshot.child("messageText").getValue();
                        Date msgTime = new Date((long) postSnapshot.child("messageTime").getValue());
                        list.add(msgUser);
                        list.add(msgText);
                        list.add(msgTime.toString());
                        listAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onCancelled(@NonNull final DatabaseError databaseError) {

                Toast.makeText(MainActivity.this, "failed to read value", Toast.LENGTH_SHORT).show();
            }
        });


    }

    /**
     * init made;
     */
    private void init() {

        mAuth = FirebaseAuth.getInstance();
        fab = (Button) findViewById(R.id.fab);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Welcome...", Toast.LENGTH_SHORT).show();
                //displayChat();
            }
        } else {
            Toast.makeText(this, "unable to sign in, try again later...", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

//    /**
//     * display all chat messages;
//     */
//    private void displayChat() {
//
//        Query query = reference;
//        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
//                .setQuery(query, ChatMessage.class)
//                .setLayout(android.R.layout.simple_list_item_1)
//                .build();
//
//
//        adapter = new FirebaseListAdapter<ChatMessage>(options) {
//            @Override
//            protected void populateView(final View v, final ChatMessage model, final int position) {
//
//                TextView tvMessageText, tvMessageTime, tvUser;
//                //populate items in view;
//                tvMessageText = (TextView) v.findViewById(R.id.message_text);
//                tvMessageTime = (TextView) v.findViewById(R.id.message_time);
//                tvUser = (TextView) v.findViewById(R.id.message_user);
//
//                tvMessageText.setText(model.getMessageText());
//                tvUser.setText(model.getMessageUser());
//                tvMessageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));
//
//            }
//
//        };
//
//        Log.i("adapter", adapter.getCount() + "");
//        //set the filled adapter;
//        messageList.setAdapter(adapter);
//
//
//    }


}
