package com.example.manisharana.todoapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.manisharana.todoapp.Activities.TaskActivity;
import com.example.manisharana.todoapp.Activities.TaskCommentListActivity;
import com.example.manisharana.todoapp.AsyncTasks.UpdateTask;
import com.example.manisharana.todoapp.Models.Task;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_REGULAR_WITH_SEPARATOR = 0;
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;
    private final ArrayList<Task> tasks;
    private final Context context;


    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
    }

    @Override
    public int getItemCount() {
        if (tasks.isEmpty())
            return 0;
        return tasks.size();
    }

    @Override
    public int getItemViewType(int position) {
        super.getItemViewType(position);
        int itemViewType = -1;

        if (position == 0) {
            itemViewType = ITEM_VIEW_TYPE_REGULAR_WITH_SEPARATOR;
        } else if (!tasks.isEmpty() && position > 0) {
            Task prevObject = tasks.get(position - 1);
            Task currentObject = tasks.get(position);
            String friendlyPrevDayDate = Utility.getFriendlyDayString(prevObject.getDate());
            String friendlyCurDayDate = Utility.getFriendlyDayString(currentObject.getDate());
            if (friendlyPrevDayDate.equals(friendlyCurDayDate)) {
                itemViewType = ITEM_VIEW_TYPE_REGULAR;
            } else {
                itemViewType = ITEM_VIEW_TYPE_REGULAR_WITH_SEPARATOR;
            }
        }
        return itemViewType;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        switch (viewType) {
            case ITEM_VIEW_TYPE_REGULAR_WITH_SEPARATOR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.section_view, parent, false);
                return new SectionViewHolder(view);
            case ITEM_VIEW_TYPE_REGULAR:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_task, parent, false);
                return new TaskViewHolder(view);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Task task = tasks.get(position);
        if (task != null) {
            if (position == 0) {
                populateSectionWithTaskView((SectionViewHolder) holder, task);
            } else if (!tasks.isEmpty() && position > 0) {
                Task prevObject = tasks.get(position - 1);
                Task currentObject = tasks.get(position);
                String friendlyPrevDayDate = Utility.getFriendlyDayString(prevObject.getDate());
                String friendlyCurDayDate = Utility.getFriendlyDayString(currentObject.getDate());
                if (friendlyPrevDayDate.equals(friendlyCurDayDate)) {
                    populateTaskView((TaskViewHolder) holder, task);
                } else {
                    populateSectionWithTaskView((SectionViewHolder) holder, task);
                }
            }
        }

    }

    private void setOnClickListenerOnView(LinearLayout rootView, final Task task) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntentWithData(task);
                context.startActivity(intent);
            }
        });
    }

    private Intent getIntentWithData(Task task) {
        Intent intent = new Intent(context, TaskCommentListActivity.class);
        intent.putExtra("Task", task);
        return intent;
    }

    private void populateTaskView(TaskViewHolder holder, Task task) {
        holder.titleView.setText(task.getTitle());
        holder.dateView.setText(Utility.getFormattedTime(task.getDate()));
        if (task.isStatus()) {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
        } else {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
            //    holder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        setOnClickListenerOnButton(holder.taskStatusButton, task);
        setOnClickListenerOnView(holder.rootView, task);

    }

    private void populateSectionWithTaskView(SectionViewHolder holder, Task task) {
        holder.dayView.setText(Utility.getFriendlyDayString(task.getDate()));
        holder.titleView.setText(task.getTitle());
        holder.dateView.setText(Utility.getFormattedTime(task.getDate()));
        if (task.isStatus()) {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
        } else {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
            //    holder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
        setOnClickListenerOnButton(holder.taskStatusButton, task);
        setOnClickListenerOnView(holder.rootView, task);
    }

    private void setOnClickListenerOnButton(final ImageButton taskStatusButton, final Task task) {
        taskStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                task.setStatus(false);
                updateTaskStatusToDone(task);
                taskStatusButton.setImageResource(R.drawable.ic_action_label);
            }
        });
    }

    private void updateTaskStatusToDone(Task task) {
        new UpdateTask(context).execute(task);
    }

    public void swap(ArrayList<Task> datas) {
        tasks.clear();
        tasks.addAll(datas);
        notifyDataSetChanged();
    }


    public class TaskViewHolder extends RecyclerView.ViewHolder {

        private final ImageButton taskStatusButton;
        private final TextView titleView;
        private final TextView dateView;
        private final LinearLayout rootView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskStatusButton = (ImageButton) itemView.findViewById(R.id.image_view_task_status);
            titleView = (TextView) itemView.findViewById(R.id.text_view_task_title);
            dateView = (TextView) itemView.findViewById(R.id.text_view_task_time);
            rootView = (LinearLayout) itemView.findViewById(R.id.section_view_list_item_task);
        }

    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView dayView;
        private final ImageButton taskStatusButton;
        private final TextView titleView;
        private final TextView dateView;
        private final LinearLayout rootView;

        public SectionViewHolder(View itemView) {
            super(itemView);
            dayView = (TextView) itemView.findViewById(R.id.text_view_section_day);
            taskStatusButton = (ImageButton) itemView.findViewById(R.id.image_view_task_status);
            titleView = (TextView) itemView.findViewById(R.id.text_view_task_title);
            dateView = (TextView) itemView.findViewById(R.id.text_view_task_time);
            rootView = (LinearLayout) itemView.findViewById(R.id.section_view_list_item_task);
        }

    }

}
