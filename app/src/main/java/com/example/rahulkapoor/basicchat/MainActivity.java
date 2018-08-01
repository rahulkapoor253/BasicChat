package com.example.rahulkapoor.basicchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;


public class MainActivity extends AppCompatActivity {

    private static final int SIGN_IN_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private FloatingActionButton fab;
    private FirebaseListAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //init the firebaseauth;
        init();


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                EditText input = (EditText) findViewById(R.id.input);
                //push message to firebase db;
                FirebaseDatabase.getInstance().getReference().push().setValue(new ChatMessage(input.getText().toString(), mAuth.getCurrentUser().getEmail()));
                //clear out message;
                input.setText("");
            }
        });

        if (mAuth.getCurrentUser() == null) {

            startActivityForResult(AuthUI.getInstance().createSignInIntentBuilder().build(), SIGN_IN_REQUEST_CODE);

        } else {


        }

    }

    /**
     * init made;
     */
    private void init() {

        mAuth = FirebaseAuth.getInstance();
        fab = (FloatingActionButton) findViewById(R.id.fab);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SIGN_IN_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Welcome...", Toast.LENGTH_SHORT).show();
                displayChat();
            }
        }
    }

    /**
     * display all chat messages;
     */
    private void displayChat() {
        ListView messageList = (ListView) findViewById(R.id.listView);
        Query query = FirebaseDatabase.getInstance().getReference();
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(query, ChatMessage.class)
                .build();

        adapter = new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(final View v, final ChatMessage model, final int position) {

                TextView tvMessageText, tvMessageTime, tvUser;
                //populate items in view;
                tvMessageText = (TextView) findViewById(R.id.message_text);
                tvMessageTime = (TextView) findViewById(R.id.message_time);
                tvUser = (TextView) findViewById(R.id.message_user);

                tvMessageText.setText(model.getMessageText());
                tvUser.setText(model.getMessageUser());
                tvMessageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getMessageTime()));

            }

        };

        //set the filled adapter;
        messageList.setAdapter(adapter);

    }

}
