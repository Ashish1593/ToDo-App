package com.example.manisharana.todoapp.Adapters;

import com.example.manisharana.todoapp.Models.Task;

import org.json.JSONException;
import org.json.JSONObject;

public class TaskUtility {

    public static  String getTaskJson(Task task) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("title", task.getTitle());
            jsonObject.put("date", task.getDate());
            jsonObject.put("assgnByName", task.getAssignByName());
            jsonObject.put("assgnByPhon", task.getAssignByPhone());
            jsonObject.put("assgnToName", task.getAssignToName());
            jsonObject.put("assgnToPhon", task.getAssignToPhone());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject.toString();
    }

    public static String getUpdatedTaskJson(Task task) {
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

}
