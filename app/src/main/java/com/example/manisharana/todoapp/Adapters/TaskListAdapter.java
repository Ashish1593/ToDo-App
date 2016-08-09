package com.example.manisharana.todoapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import java.util.List;

public class TaskListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int ITEM_VIEW_TYPE_REGULAR_WITH_SEPARATOR = 0;
    private static final int ITEM_VIEW_TYPE_REGULAR = 1;
    private final ArrayList<Task> tasks;
    private final Context context;


    public TaskListAdapter(Context context, ArrayList<Task> tasks) {
        this.tasks = tasks;
        this.context = context;
    }


    private void updateTaskStatusToDone(Task task)
    {
        new UpdateTask(this).execute(task);
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
        if(task != null) {
            if (position == 0) {
                populateSectionWithTaskView((SectionViewHolder) holder, task);
            } else if (!tasks.isEmpty() && position > 0) {
                Task prevObject = tasks.get(position - 1);
                Task currentObject = tasks.get(position);
                String friendlyPrevDayDate = Utility.getFriendlyDayString(prevObject.getDate());
                String friendlyCurDayDate = Utility.getFriendlyDayString(currentObject.getDate());
                if (friendlyPrevDayDate.equals(friendlyCurDayDate)) {
                    populateTaskView((TaskViewHolder)holder,task);
                } else {
                    populateSectionWithTaskView((SectionViewHolder)holder,task);
                }
            }
        }

    }

    private void populateTaskView(TaskViewHolder holder, Task task) {
        holder.titleView.setText(task.getTitle());
        holder.dateView.setText(Utility.getFormattedTime(task.getDate()));
        if (task.isStatus()) {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
        } else {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
            holder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    private void populateSectionWithTaskView(SectionViewHolder holder, Task task) {
        holder.dayView.setText(Utility.getFriendlyDayString(task.getDate()));
        holder.titleView.setText(task.getTitle());
        holder.dateView.setText(Utility.getFormattedTime(task.getDate()));
        if (task.isStatus()) {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
        } else {
            holder.taskStatusButton.setImageResource(R.drawable.ic_action_label);
            holder.titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    public void swap(ArrayList<Task> datas){
        tasks.clear();
        tasks.addAll(datas);
        notifyDataSetChanged();
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
        return  itemViewType;
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {


        private final ImageButton taskStatusButton;
        private final TextView titleView;
        private final TextView dateView;

        public TaskViewHolder(View itemView) {
            super(itemView);
            taskStatusButton = (ImageButton) itemView.findViewById(R.id.image_view_task_status);
            titleView = (TextView) itemView.findViewById(R.id.text_view_task_title);
            dateView = (TextView) itemView.findViewById(R.id.text_view_task_time);
        }


    }

    public class SectionViewHolder extends RecyclerView.ViewHolder {


        private final TextView dayView;
        private final ImageButton taskStatusButton;
        private final TextView titleView;
        private final TextView dateView;


        public SectionViewHolder(View itemView) {
            super(itemView);
            dayView = (TextView) itemView.findViewById(R.id.text_view_section_day);
            taskStatusButton = (ImageButton) itemView.findViewById(R.id.image_view_task_status);
            titleView = (TextView) itemView.findViewById(R.id.text_view_task_title);
            dateView = (TextView) itemView.findViewById(R.id.text_view_task_time);
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

    public class UpdateTask extends AsyncTask<Task, Void, Void> implements Callback {
        public final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        private final TaskListAdapter adapter;
        private OkHttpClient client;
        private Boolean success = false;

        public UpdateTask(TaskListAdapter taskListAdapter) {
            this.adapter = taskListAdapter;
        }


        @Override
        protected Void doInBackground(Task... tasks) {
            String json = getJsonString(tasks[0]);
            RequestBody body = RequestBody.create(JSON, json);

            client = new OkHttpClient();
            final HttpUrl url = HttpUrl.parse(context.getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("tasks")
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .put(body)
                    .build();
            client.newCall(request).enqueue(this);


            return null;
        }

        private String getJsonString(Task task) {
            JSONObject jsonTask = new JSONObject();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title", task.getTitle());
                jsonObject.put("date", task.getDate());
                jsonObject.put("status", task.isStatus());
                jsonObject.put("assgnByName", task.getAssignByName());
                jsonObject.put("assgnByPhon", task.getAssignByPhone());
                jsonObject.put("assgnToName", task.getAssignToName());
                jsonObject.put("assgnToPhon", task.getAssignToPhone());
                jsonObject.put("comments", "");
                jsonTask.put("id", task.getId());
                jsonTask.put("data", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonTask.toString();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (success) {
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("Error", "Error in updating task");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            success = true;
        }
    }


}
