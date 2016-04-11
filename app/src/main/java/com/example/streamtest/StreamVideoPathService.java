package com.example.streamtest;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import helppocket.StreamInfoItem;
import VolleySingleton.VolSingleton;

/**
 * Created by User on 02.03.2016.
 */
public class StreamVideoPathService extends IntentService{
    public final static String urlTranslations = "https://bet-1x.com/LiveFeed/VideoGames";
    public final static String urlServer = "https://bet-1x.com/cinema";
    public final static int socketTimeout = 20000;

    private String serverIpFromBytes = "";
    private Context context;
    private ArrayList<StreamInfoItem> streamInfoItemList;
    private int count = 0;

    public StreamVideoPathService(String name) {
        super(name);
        context = getApplicationContext();
    }

    public StreamVideoPathService()
    {
        super("1");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        getListStreamInfo();
    }

    private void getListStreamInfo()
    {
        JsonArrayRequest jsonArrayRequestStreamInfo = new JsonArrayRequest(Request.Method.GET, urlTranslations, jsonArrayListener, errorListener);
        jsonArrayRequestStreamInfo.setRetryPolicy(new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolSingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonArrayRequestStreamInfo);
    }

    private ArrayList<StreamInfoItem> getParsedList(JSONArray jsonArray)
    {
        ArrayList<StreamInfoItem> infoItems = new ArrayList<StreamInfoItem>();
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String champName = jsonObject.getString("Champ");
                Boolean finish = jsonObject.getBoolean("Finish");
                String firstOpponent = jsonObject.getString("Opp1");
                String secondOpponent = jsonObject.getString("Opp2");
                int sportId = jsonObject.getInt("SportId");
                int top = jsonObject.getInt("Top");
                String videoId = jsonObject.getString("VideoId");

                StreamInfoItem streamInfoItem = new StreamInfoItem(champName, finish, firstOpponent, secondOpponent, sportId, top, videoId);
                infoItems.add(streamInfoItem);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return infoItems;
    }

    private Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            streamInfoItemList = getParsedList(response);
            final Intent mainIntent = new Intent(MainActivity.BROADCAST_ACTION);
            mainIntent.putExtra(MainActivity.STREAM_INFO_SERIALIZABLE, streamInfoItemList);
            count++;
            mainIntent.putExtra(MainActivity.COUNT, count);
            mainIntent.putExtra(MainActivity.STATUS_BROADCAST, MainActivity.STATUS_LIST);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run()
                {
                try {
                    HttpURLConnection conn = (HttpURLConnection)new URL(urlServer).openConnection();
                    conn.connect();
                    InputStreamReader reader = new InputStreamReader(conn.getInputStream());
                    BufferedReader bufferedReader = new BufferedReader(reader);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine())!=null) {
                        sb.append(line);
                    }
                    String result = sb.toString();
                    result = result.replace(" ", "");
                    boolean value = Pattern.compile("^https?://.*$").matcher(result).matches();
                    if (value) {
                        result = result.replace("https://", "").replace("http://", "").replace(" ", "");
                    }
                    String UTF8_BOM = "\uFEFF";//cuz we get bytes row, not string
                    //mainIntent.putExtra(MainActivity.SERVER_IP_DATA, "123");
                    mainIntent.putExtra(MainActivity.SERVER_IP_DATA, result.replace(UTF8_BOM, ""));
                    }
                    catch (IOException ioe)
                    {
                        ioe.printStackTrace();
                    }
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("SERVICE", "IS ALIVE!");
            sendBroadcast(mainIntent);
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


    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("SERVICE", "STOPPPPED!");
    }

}
