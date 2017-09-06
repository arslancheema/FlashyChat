package com.arslancheema.flashchatnewfirebase;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainChatActivity extends AppCompatActivity {

    // TODO: Add member variables here:
    private String mDisplayName;
    private ListView mChatListView;
    private EditText mInputText;
    private ImageButton mSendButton;
    private DatabaseReference mDatabaseReference;
    private ChatListAdapter mChatListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_chat);

        setupDisplayName();

        // For reading / writing to the Firebase Db.
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        // Link the Views in the layout to the Java code
        mInputText = (EditText) findViewById(R.id.messageInput);
        mSendButton = (ImageButton) findViewById(R.id.sendButton);
        mChatListView = (ListView) findViewById(R.id.chat_list_view);

        // To handle event when enter is pressed
       mInputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
           @Override
           public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
               sendMessage();
               return  true;
           }
       });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });


    }

    private void setupDisplayName(){
        SharedPreferences preferences = getSharedPreferences(RegisterActivity.CHAT_PREFS,MODE_PRIVATE);
        mDisplayName = preferences.getString(RegisterActivity.DISPLAY_NAME_KEY,null);
        if (mDisplayName == null){
            mDisplayName = "Anonymous";
        }
    }

    private void sendMessage() {
        String input = mInputText.getText().toString();
        if (!input.equals("")){
            InstantMessage instantMessage = new InstantMessage(input,mDisplayName);
            mDatabaseReference.child("messages").push().setValue(instantMessage);
            mInputText.setText("");
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatListAdapter = new ChatListAdapter(this,mDatabaseReference,mDisplayName);
        mChatListView.setAdapter(mChatListAdapter);
    }

    @Override
    public void onStop() {
        super.onStop();
        mChatListAdapter.cleanUp();

    }

}
