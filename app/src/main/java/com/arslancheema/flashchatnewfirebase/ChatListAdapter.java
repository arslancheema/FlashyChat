package com.arslancheema.flashchatnewfirebase;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
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

    public ChatListAdapter (Activity activity, DatabaseReference reference, String name){
        mActivity = activity;
        mDatabaseReference = reference.child("messages");
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

    // Listview would ask for number of items
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public InstantMessage getItem(int position) {
        return null;
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

        String author = message.getAuthor();
        holder.author.setText(author);

        String msg = message.getMessage();
        holder.body.setText(msg);

        return convertView;
    }
}
