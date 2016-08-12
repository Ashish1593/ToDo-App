package com.example.manisharana.todoapp.Sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ProgressDialog;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.manisharana.todoapp.Adapters.Utility;
import com.example.manisharana.todoapp.Data.UserEntry;
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

public class ToDoSyncAdapter extends AbstractThreadedSyncAdapter {
    private static final String LOG_TAG = ToDoSyncAdapter.class.getSimpleName();
    private OkHttpClient client;
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;



    public ToDoSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    @Override
    public void onPerformSync(Account account, Bundle bundle, String s, ContentProviderClient contentProviderClient, SyncResult syncResult) {
        Log.d(LOG_TAG, "onPerformSync Called.");

        client = new OkHttpClient();
        Utility mUtility = new Utility(getContext());

        final HttpUrl url = HttpUrl.parse(getContext().getString(R.string.sample_api_base_url)).newBuilder()
                .addPathSegment("api")
                .addPathSegment("users")
                .addEncodedPathSegment(mUtility.getFromPreferences("PhoneNumber"))
                .build();
        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Log.i("Error","Error in fetching contacts");
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    JSONArray jsonArray = new JSONArray(response.body().string());
                    ContentValues[] users = new ContentValues[jsonArray.length()];
                    for(int i = 0; i<jsonArray.length();i++){
                        ContentValues user = new ContentValues();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        String name = jsonObject.getString("name");
                        String phone = jsonObject.getString("phone");
                        String photoUri = "http://api.adorable.io/avatar/80/"+name;
                        user.put(UserEntry.COLUMN_NAME,name);
                        user.put(UserEntry.COLUMN_PHOTO_URI,photoUri);
                        user.put(UserEntry.COLUMN_PHONE_NUMBER,phone);
                        users[i]=user;
                    }

                    int insertedCount = getContext().getContentResolver().bulkInsert(UserEntry.CONTENT_URI, users);
                    Log.i(LOG_TAG,"Inserted contacts: "+insertedCount);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public static void syncImmediately(Context context) {
        ProgressDialog progressDialog = new Utility(context).getProgressDialog("Fetching Contacts");
        progressDialog.show();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context),
                context.getString(R.string.content_authority), bundle);
        progressDialog.dismiss();
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account(
                context.getString(R.string.app_name), context.getString(R.string.sync_account_type));

        if ( null == accountManager.getPassword(newAccount) ) {

            if (!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }
            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }


    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = context.getString(R.string.content_authority);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic(syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            ContentResolver.addPeriodicSync(account,
                    authority, new Bundle(), syncInterval);
        }
    }

    private static void onAccountCreated(Account newAccount, Context context) {
        ToDoSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(newAccount, context.getString(R.string.content_authority), true);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
