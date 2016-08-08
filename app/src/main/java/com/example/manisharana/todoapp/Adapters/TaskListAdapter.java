package com.example.manisharana.todoapp.Adapters;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.os.AsyncTask;
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
            final Task task = getItem(position);

            if (task!= null) {
                final ImageButton taskStatusButton = (ImageButton) v.findViewById(R.id.image_view_task_status);
                final TextView titleView = (TextView) v.findViewById(R.id.text_view_task_title);
                TextView dateView = (TextView) v.findViewById(R.id.text_view_task_time);

                if(dateView != null){
                    dateView.setText(Utility.getFriendlyDayString(Long.valueOf(task.getDate())));
                }

                if (titleView != null) {
                    titleView.setText(task.getTitle());
                }


                if(task.isStatus()){
                    taskStatusButton.setImageResource(R.drawable.ic_action_label_outline);
                }else{
                    taskStatusButton.setImageResource(R.drawable.ic_action_label);
                    titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                }

                taskStatusButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        taskStatusButton.setImageResource(R.drawable.ic_action_label);
                        titleView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        task.setStatus(false);
                        updateTaskStatusToDone(task);
                    }
                });

            }
        return v;
    }




    private void updateTaskStatusToDone(Task task) {
        new UpdateTask(this).execute(task);
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

    public class UpdateTask extends AsyncTask<Task,Void,Void> implements Callback {
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
            final HttpUrl url = HttpUrl.parse(getContext().getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("tasks")
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(this);


            return null;
        }

        private String getJsonString(Task task) {
            JSONObject jsonTask = new JSONObject();
            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("title",task.getTitle());
                jsonObject.put("date",task.getDate());
                jsonObject.put("status",task.isStatus());
                jsonObject.put("assgnByName",task.getAssignByName());
                jsonObject.put("assgnByPhon",task.getAssignByPhone());
                jsonObject.put("assgnToName",task.getAssignToName());
                jsonObject.put("assgnToPhon",task.getAssignToPhone());
                jsonObject.put("comments","");
                jsonTask.put("id",task.getId());
                jsonTask.put("data",jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonTask.toString();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(success){
                adapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onFailure(Request request, IOException e) {
            Log.i("Error","Error in updating task");
        }

        @Override
        public void onResponse(Response response) throws IOException {
            success = true;
        }
    }



}
