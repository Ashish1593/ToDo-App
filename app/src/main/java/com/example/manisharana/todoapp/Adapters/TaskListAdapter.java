package com.example.manisharana.todoapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;

import java.util.List;

public class TaskListAdapter extends ArrayAdapter<Task>{


    public TaskListAdapter(Context context, int layoutResourceId) {
        super(context,layoutResourceId);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(getContext());
                v = vi.inflate(R.layout.list_item_task, parent,false);
            }
            Task task = getItem(position);

            if (task!= null) {
                final ImageButton taskStatusButton = (ImageButton) v.findViewById(R.id.image_view_task_status);
                TextView taskIdView = (TextView) v.findViewById(R.id.text_view_task_id);
                final TextView titleView = (TextView) v.findViewById(R.id.text_view_task_title);
                TextView dateView = (TextView) v.findViewById(R.id.text_view_task_time);


//                if (taskIdView != null) {
//                    taskIdView.setText(String.valueOf(task.getId()));
//                }

                if(dateView != null){
                    dateView.setText(task.getDate());
                }

                if (titleView != null) {
                    titleView.setText(task.getTitle());
                }


                if(!task.isStatus()){
                    taskStatusButton.setImageResource(R.drawable.ic_action_label);
                    titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }else{
                    taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
                }

                taskStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        LinearLayout parent = (LinearLayout) view.getParent();
                        TextView textIdView = (TextView) parent.findViewById(R.id.text_view_task_id);
                        String taskId = textIdView.getText().toString();
                        taskStatusButton.setImageResource(R.drawable.ic_action_label);
                        titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        updateTaskStatusToDone(taskId);
                    }
                });

            }
        return v;
    }




    private void updateTaskStatusToDone(String taskId) {
      //  mContext.getContentResolver().update(TaskEntry.CONTENT_URI, getUpdateContentValues(), TaskEntry.TABLE_NAME + "." + TaskEntry._ID + " = ? ", new String[]{taskId});
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
