package com.example.manisharana.todoapp.Adapters;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manisharana.todoapp.Fragments.ContactListFragment;
import com.example.manisharana.todoapp.R;
import com.squareup.picasso.Picasso;

public class ContactListAdapter extends CursorAdapter {

    public ContactListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor,flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.contact_list_item_left,viewGroup,false);
        view.setTag(new ViewHolder(view));
        return view;

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder vH = (ViewHolder) view.getTag();
        vH.contactNameView.setText(cursor.getString(ContactListFragment.COL_CONTACT_NAME));
        vH.phoneNumberView.setText(cursor.getString(ContactListFragment.COL_CONTACT_PHONE_NUMBER));
        String url = cursor.getString(ContactListFragment.COL_CONTACT_PHOTO);
        Picasso.with(mContext).load(url).into(vH.photoView);
    }

    public class ViewHolder{

        private final TextView contactNameView;
        private final TextView phoneNumberView;
        private final ImageView photoView;

        public ViewHolder(View view){
            contactNameView = (TextView) view.findViewById(R.id.text_view_contact_name);
            phoneNumberView = (TextView) view.findViewById(R.id.text_view_contact_phone_number);
            photoView = (ImageView) view.findViewById(R.id.image_view_contact_photo);

        }
    }

}
