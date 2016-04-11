package com.example.streamtest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import helppocket.SportInfoItem;
import VolleySingleton.VolSingleton;

/**
 * Created by User on 08.03.2016.
 */
public class SportInfoService extends IntentService {

    public final static String urlTypeOfSport = "https://bet-1x.com/MobileOpen/Mobile_sports?lng=ru";
    private Context context;
    private static int socketTimeout = 15000;

    public SportInfoService(String name) {
        super(name);
    }

    public SportInfoService() {
        super("123");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        getSportInfo();
    }

    private void getSportInfo()
    {
        JsonObjectRequest jsonObjectRequestSportInfo = new JsonObjectRequest(Request.Method.GET, urlTypeOfSport, jsonObjectSportInfoListener, errorListener);
        //jsonObjectRequestSportInfo.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequestSportInfo);
    }

    private Response.Listener<JSONObject> jsonObjectSportInfoListener = new Response.Listener<JSONObject>() {
        @Override
        public void onResponse(JSONObject response) {
            try {
                boolean success = response.getBoolean("Success");
                if(success)
                {
                    ArrayList<SportInfoItem> sportInfoItems = new ArrayList<SportInfoItem>();
                    JSONArray jsonArray = response.getJSONArray("Data");
                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONArray jsonArraySportInfo = jsonArray.getJSONArray(i);
                        int sportId = jsonArraySportInfo.getInt(0);
                        String sportName = jsonArraySportInfo.getString(1);

                        SportInfoItem sportInfoItem = new SportInfoItem(sportId, sportName, "", "");
                        sportInfoItems.add(sportInfoItem);
                    }
                    Intent intent = new Intent(MainActivity.BROADCAST_ACTION_SPORT_INFO);
                    intent.putExtra(MainActivity.SPORT_INFO_SERIALIZABLE, sportInfoItems);
                    sendBroadcast(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            String str = null;
            str = error.getMessage();
            Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        }
    };
}
