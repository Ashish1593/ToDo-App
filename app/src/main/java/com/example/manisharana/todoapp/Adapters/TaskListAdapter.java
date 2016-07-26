package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.manisharana.todoapp.Fragments.TaskListFragment;
import com.example.manisharana.todoapp.R;

public class TaskListAdapter extends CursorAdapter {
    public TaskListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        long dateInMillis = cursor.getLong(TaskListFragment.COL_TASK_DATE);
        viewHolder.dateView.setText(Utility.getFormattedDateAndTime(dateInMillis));

        String title = cursor.getString(TaskListFragment.COL_TASK_TITLE);
        viewHolder.titleView.setText(title);

//        String tagColor = cursor.getString(TaskListFragment.COL_TAG_COLOR);
//        viewHolder.tagView.setBackgroundColor(Color.parseColor(tagColor));

    }

    public static class ViewHolder {
     //   public final ImageView userImage;
        public final TextView dateView;
        public final TextView titleView;
        public final ImageView tagView;
       // public final TextView removeButton;

        public ViewHolder(View view) {
            tagView = (ImageView) view.findViewById(R.id.image_view_tag_color);
            titleView = (TextView) view.findViewById(R.id.text_view_task_title);
            dateView = (TextView) view.findViewById(R.id.text_view_task_time);
       //     userImage = (ImageView) view.findViewById(R.id.image_view_user_image);
       //     removeButton = (Button) view.findViewById(R.id.button_delete_task);
        }
    }
}
