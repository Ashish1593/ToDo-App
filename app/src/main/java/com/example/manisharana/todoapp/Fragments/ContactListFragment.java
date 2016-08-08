package com.example.manisharana.todoapp.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.manisharana.todoapp.Adapters.ContactListAdapter;
import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Models.User;
import com.example.manisharana.todoapp.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.HttpUrl;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ContactListFragment extends Fragment {

    private ContactListAdapter contactListAdapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_contact_list, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.list_view_contact_list);
        contactListAdapter = new ContactListAdapter(getActivity(),R.layout.list_item_contact);
        listView.setAdapter(contactListAdapter);
        GetAllUsersTask getAllUsersTask = new GetAllUsersTask(getActivity());
        getAllUsersTask.execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                User user = (User) adapterView.getItemAtPosition(i);
                Intent intent = new Intent();
                intent.putExtra("UserName", user.getName());
                intent.putExtra("UserPhone", user.getPhoneNumber());
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });
        return rootView;
    }



    public class GetAllUsersTask extends AsyncTask<Void, Void, ArrayList<User>> {

        private final Context context;
        private OkHttpClient client;

        public GetAllUsersTask(Context context) {
            this.context = context;
        }

        @Override
        protected ArrayList<User> doInBackground(Void... args) {
            client = new OkHttpClient();
            final ArrayList<User> users = new ArrayList<>();
            final HttpUrl url = HttpUrl.parse(getString(R.string.sample_api_base_url)).newBuilder()
                    .addPathSegment("api")
                    .addPathSegment("users")
                    .addEncodedPathSegment(Utility.getFromPreferences(context,"PhoneNumber"))
                    .build();
            final Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Request request, IOException e) {
                    Log.i("Errrrrooooorr","Error in fetching contacts");
                }

                @Override
                public void onResponse(Response response) throws IOException {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        for(int i = 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String name = jsonObject.getString("name");
                            String phone = jsonObject.getString("phone");
                            User user = new User(name, phone);
                            users.add(user);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
            return users;
        }

        @Override
        protected void onPostExecute(ArrayList<User> users) {
            super.onPostExecute(users);
            try {
                Thread.sleep(5000,0);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            contactListAdapter.addAll(users);

        }
    }


}
