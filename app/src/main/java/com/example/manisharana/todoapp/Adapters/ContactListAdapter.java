package com.example.manisharana.todoapp.Adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.manisharana.todoapp.Fragments.ContactListFragment;
import com.example.manisharana.todoapp.R;

public class ContactListAdapter extends CursorAdapter {

    public ContactListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false);
        view.setTag(new ContactViewHolder(view));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ContactViewHolder itemViewHolder = (ContactViewHolder) view.getTag();

        String contactName = cursor.getString(ContactListFragment.COL_CONTACT_NAME);
        itemViewHolder.contactName.setText(contactName);

        String phoneNumber = cursor.getString(ContactListFragment.COL_CONTACT_NAME);
        itemViewHolder.phoneNumber.setText(phoneNumber);

    }

    public class ContactViewHolder {

        public final TextView contactName;
        public final TextView phoneNumber;

        public ContactViewHolder(View view) {
            contactName = (TextView) view.findViewById(R.id.text_view_contact_name);
            phoneNumber = (TextView) view.findViewById(R.id.text_view_contact_phone_number);
            //     userImage = (ImageView) view.findViewById(R.id.image_view_user_image);
        }

    }
}
