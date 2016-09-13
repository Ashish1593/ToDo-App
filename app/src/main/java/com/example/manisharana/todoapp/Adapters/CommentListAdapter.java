package com.example.manisharana.todoapp.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.manisharana.todoapp.Models.Comment;
import com.example.manisharana.todoapp.R;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter{

    private final Context context;
    private final ArrayList<Comment> comments;

    public CommentListAdapter(Context context, ArrayList<Comment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int i) {
        return comments.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Comment comment = comments.get(i);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if(comment.isSelf()){
            view = layoutInflater.inflate(R.layout.list_item_message_right,viewGroup,false);
        }else {
            view = layoutInflater.inflate(R.layout.list_item_message_left,viewGroup,false);
        }

        TextView lblFrom = (TextView) view.findViewById(R.id.lblMsgFrom);
        TextView txtMsg = (TextView) view.findViewById(R.id.txtMsg);

        txtMsg.setText(comment.getMessage());
        lblFrom.setText(comment.getFromName());

        return view;
    }
}
