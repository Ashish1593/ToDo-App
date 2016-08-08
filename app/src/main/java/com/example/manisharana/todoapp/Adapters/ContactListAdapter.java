package com.example.manisharana.todoapp.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.manisharana.todoapp.Models.User;
import com.example.manisharana.todoapp.R;

import java.util.ArrayList;

public class ContactListAdapter extends ArrayAdapter<User>{

    public ContactListAdapter(Context context, int resource, ArrayList<User> users) {
        super(context, resource,users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inf = LayoutInflater.from(getContext());
            view = inf.inflate(R.layout.list_item_contact, parent, false);
        }
        User user = getItem(position);
        if(user != null){
            TextView userNameView = (TextView) view.findViewById(R.id.text_view_contact_name);
            TextView phoneNumberView = (TextView) view.findViewById(R.id.text_view_contact_phone_number);

            if(userNameView != null)
                userNameView.setText(user.getName());

            if(phoneNumberView != null)
                phoneNumberView.setText(user.getPhoneNumber());

        }
        return view;
    }

}
