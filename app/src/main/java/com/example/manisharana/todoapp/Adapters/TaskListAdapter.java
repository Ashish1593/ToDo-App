package com.example.manisharana.todoapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Paint;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manisharana.todoapp.Data.TaskEntry;
import com.example.manisharana.todoapp.Fragments.TaskListFragment;
import com.example.manisharana.todoapp.R;

public class TaskListAdapter extends CursorAdapter {
    private String taskId;

    public TaskListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_task, parent, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);


        viewHolder.taskStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout parent = (LinearLayout) view.getParent();
                TextView textIdView = (TextView) parent.findViewById(R.id.text_view_task_id);
                taskId = textIdView.getText().toString();
                viewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
                viewHolder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                updateTaskStatusToDone(taskId);
            }
        });

        return view;
    }


    private void updateTaskStatusToDone(String taskId) {
        mContext.getContentResolver().update(TaskEntry.CONTENT_URI, getUpdateContentValues(), TaskEntry.TABLE_NAME + "." + TaskEntry._ID + " = ? ", new String[]{taskId});
    }

    private ContentValues getUpdateContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(TaskEntry.COLUMN_STATUS, "done");
        return contentValues;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        viewHolder.taskIdView.setText(String.valueOf(cursor.getInt(TaskListFragment.COL_TASK_ID)));

        long dateInMillis = cursor.getLong(TaskListFragment.COL_TASK_DATE);
        viewHolder.dayName.setText(Utility.getFriendlyDayString(context, dateInMillis));
        viewHolder.dateView.setText(Utility.getFormattedTime(dateInMillis));

        String title = cursor.getString(TaskListFragment.COL_TASK_TITLE);
        viewHolder.titleView.setText(title);


        if (cursor.getString(TaskListFragment.COL_TASK_STATUS).equals("done")) {
            viewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
            viewHolder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            viewHolder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
        }


    }

    public static class ViewHolder {
        //   public final ImageView userImage;
        public final TextView dateView;
        public final TextView titleView;
        private final TextView dayName;
        private final TextView taskIdView;
        private final ImageButton taskStatusButton;

        public ViewHolder(View view) {
            dayName = (TextView) view.findViewById(R.id.textView_day);
            taskIdView = (TextView) view.findViewById(R.id.text_view_task_id);
            taskStatusButton = (ImageButton) view.findViewById(R.id.image_view_task_status);
            titleView = (TextView) view.findViewById(R.id.text_view_task_title);
            dateView = (TextView) view.findViewById(R.id.text_view_task_time);
            //     userImage = (ImageView) view.findViewById(R.id.image_view_user_image);
        }
    }

    private abstract class ViewAnimationListener implements Animation.AnimationListener {

        private final View view;

        protected ViewAnimationListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationStart(Animation animation) {
            onAnimationStart(this.view, animation);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            onAnimationEnd(this.view, animation);
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        protected abstract void onAnimationStart(View view, Animation animation);

        protected abstract void onAnimationEnd(View view, Animation animation);
    }



}
