package com.arslancheema.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by Cheema on 2017/09/05.
 */

public class ChatListAdapter extends BaseAdapter {

    private Activity mActivity;
    private DatabaseReference mDatabaseReference;
    private String mDisplayName ;

    // Data type used by Firebase to pass the data to the app
    private ArrayList<DataSnapshot> mDataSnapshots ;

    private ChildEventListener mChildEventListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            mDataSnapshots.add(dataSnapshot);
            notifyDataSetChanged();
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };

    public ChatListAdapter (Activity activity, DatabaseReference reference, String name){
        mActivity = activity;
        mDatabaseReference = reference.child("messages");
        mDatabaseReference.addChildEventListener(mChildEventListener);
        mDisplayName = name;
        mDataSnapshots = new ArrayList<>();
    }



    // Individual Layout in Chat. Little package for individual row
    static class ViewHolder {
        TextView author;
        TextView body;
        // For styling messages row programmatically
        LinearLayout.LayoutParams params ;
    }

    // Listview would ask for number of items at first
    @Override
    public int getCount() {
        return mDataSnapshots.size();
    }

    @Override
    public InstantMessage getItem(int position) {
        // get relevant data out of DataSnapShot List.
        DataSnapshot dataSnapshot = mDataSnapshots.get(position);
        return dataSnapshot.getValue(InstantMessage.class);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    // getView() is called for each item in the list you pass to your adapter.
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // if convertview is null then we have to create from scratch
        if (convertView == null){
            // Create view from xml layout file
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_msg_row,parent,false);

            // linking the fields of ViewHolder to the views
            final ViewHolder holder = new ViewHolder();
            holder.author = (TextView) convertView.findViewById(R.id.author);
            holder.body = (TextView) convertView.findViewById(R.id.message);
            holder.params = (LinearLayout.LayoutParams) holder.author.getLayoutParams();
            // temporarily store in convertview to avoid recalling
            convertView.setTag(holder);
        }

        // If the view is recycled
        final InstantMessage message = getItem(position);
        final ViewHolder holder = (ViewHolder) convertView.getTag();

        boolean isMe = message.getAuthor().equals(mDisplayName);
        setChatRowAppearance(isMe,holder);

        String author = message.getAuthor();
        holder.author.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        return convertView;
    }

    private void setChatRowAppearance (Boolean isMe, ViewHolder holder){

        if (isMe){
            holder.params.gravity = Gravity.END;
            holder.author.setTextColor(Color.GREEN);
            holder.body.setBackgroundResource(R.drawable.bubble2);
        } else {
            holder.params.gravity = Gravity.START;
            holder.author.setTextColor(Color.BLUE);
            holder.body.setBackgroundResource(R.drawable.bubble1);
        }

        holder.author.setLayoutParams(holder.params);
        holder.body.setLayoutParams(holder.params);

    }

    public void cleanUp(){
        mDatabaseReference.removeEventListener(mChildEventListener);
    }
}
