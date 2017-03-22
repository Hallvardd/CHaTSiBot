package com.example.taphan.core1;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Charles on 21.03.2017.
 */

public class MessageAdapter extends ArrayAdapter<String> {

    Context messageContext;
    ArrayList<String> messageList;

    public MessageAdapter(Context context, ArrayList<String> messages) {
        super(context,0,messages);
        messageList = messages;
        messageContext = context;
    }

    @Override
    public int getCount() {
        return messageList.size();
    }

    @Override
    public String getItem(int position) {
        return messageList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MessageViewHolder holder;

        // if there is not already a view created for an item in the Message list.

        if (convertView == null){
            LayoutInflater messageInflater = (LayoutInflater) messageContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

            // get the current text
            String text = getItem(position);

            // create a view out of our `leftl` file
            convertView = messageInflater.inflate(R.layout.left, null);

            // create a MessageViewHolder
            holder = new MessageViewHolder();

            // set the holder's properties to elements in `leftl`
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.iconL);
            holder.arrowView = (ImageView) convertView.findViewById(R.id.arrowL);
            holder.bodyView = (TextView) convertView.findViewById(R.id.botMsg);

            holder.bodyView.setText(text);
            // assign the holder to the view we will return
            convertView.setTag(holder);
        } else {

            // otherwise fetch an already-created view holder
            holder = (MessageViewHolder) convertView.getTag();
        }

        holder.bodyView.setText("");
        return convertView;

    }

    private static class MessageViewHolder {
        public ImageView thumbnailImageView;
        public ImageView arrowView;
        public TextView bodyView;
    }
}