package com.example.streamtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import helppocket.DbHelper;
import helppocket.ItemMapping;
import helppocket.SportInfoItem;
import helppocket.StreamInfoItem;

public class MainActivity extends Activity {


    private static final String STREAM_NAME = "Stream";
    private static final String STREAM_PATH = "stream_path";
    private ProgressDialog pDialog;

    public static final String BROADCAST_ACTION = "com.example.streamtest";
    public static final String BROADCAST_ACTION_SPORT_INFO = "com.example.streamtest.sportinfo";

    public static final String STATUS_BROADCAST = "update_textview";
    public static final String STREAM_INFO_SERIALIZABLE= "serializableExtra";
    public static final String STATUS_LIST = "list";
    public static final String STATUS_TEXT = "text";
    public static final String COUNT = "count_of_timer";
    public static final String SERVER_IP_DATA = "server_ip";
    public static final String SPORT_INFO_SERIALIZABLE = "serializableExtraSport";

    private boolean isFirstStart = true;
    private List<String> champsList;
    String SERVER_IP = "";

    ArrayList<StreamInfoItem> streamLastInfoItemsList;

    Intent intentService;

    Context context;
    List<StreamInfoItem> listStreamInfo;
    ListView listViewStream;
    StreamAdapter streamAdapter;

    TimerTask timerTask;
    Timer timer;

    IntentFilter intentFilter;
    IntentFilter intentSportInfoFilter;

    DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        listViewStream = (ListView) findViewById(R.id.listViewStream);
        pDialog = new ProgressDialog(context);
        pDialog.show();

        LinkedList<String> linkedList = new LinkedList<String>();
        linkedList.add("first");
        linkedList.add("firsdsd32t");
        linkedList.add("firsdst");
        linkedList.add("first155");




        Intent intentSportInfo = new Intent(MainActivity.this, SportInfoService.class);
        startService(intentSportInfo);

        dbHelper = new DbHelper(context, 1);

        intentService = new Intent(this, StreamVideoPathService.class);

        listViewStream.setOnItemClickListener(onListClickListener);
    }

    synchronized private DbHelper getHelper()
    {
        return dbHelper;
    }

    AdapterView.OnItemClickListener onListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String videoId = listStreamInfo.get(position).getVideoId();
            String urlStream = "http://" + SERVER_IP + "/hls-live/xmlive/_definst_/" + videoId + "/" + videoId + ".m3u8";
            TextView txt = (TextView) view.findViewById(R.id.textViewStream);
            String str = txt.getText().toString();
            Intent intent = new Intent(context, VideoViewActivity.class);
            //intent.putExtra(STREAM_NAME, SERVER_IP);
            intent.putExtra(STREAM_PATH, urlStream);
            startActivity(intent);
        }
    };

    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, final Intent intent) {
            String str = intent.getStringExtra(STATUS_BROADCAST);
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    SERVER_IP = intent.getStringExtra(SERVER_IP_DATA);
                    ArrayList<StreamInfoItem> streamInfoItemsList;
                    streamInfoItemsList = (ArrayList<StreamInfoItem>) intent.getSerializableExtra(STREAM_INFO_SERIALIZABLE);
                    listStreamInfo = streamInfoItemsList;
                    boolean same = true;
                    if(streamLastInfoItemsList == null) {
                        same = false;
                    }
                    else if(streamLastInfoItemsList.size() != streamInfoItemsList.size()){
                        same = false;
                    }
                    else if(streamInfoItemsList.size() == streamLastInfoItemsList.size())
                    {
                        Collections.sort(streamInfoItemsList, new Comparator<StreamInfoItem>() {
                            @Override
                            public int compare(StreamInfoItem lhs, StreamInfoItem rhs) {
                                if(lhs.getTop()<rhs.getTop()) return -1;
                                if(lhs.getTop()>rhs.getTop()) return 1;
                                return 0;
                            }
                        });
                        for(int i = 0; i < streamInfoItemsList.size(); i++)
                        {
                            same = streamInfoItemsList.get(i).equals(streamLastInfoItemsList.get(i));
                            if(same == false) break;
                        }
                    }
                    if(!same)
                    {
                        champsList = new ArrayList<String>();
                        for (int i = 0; i < streamInfoItemsList.size(); i++) {
                            champsList.add(streamInfoItemsList.get(i).getName());
                        }
                        streamLastInfoItemsList = streamInfoItemsList;
                        updateListView(streamInfoItemsList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (pDialog != null) {
                                    pDialog.dismiss();
                                }
                            }
                        });
                    }
                    if(isFirstStart == true)
                    {
                        isFirstStart = false;
                    }
                }
            });



            if (intent.getStringExtra(STATUS_BROADCAST).equals(STATUS_LIST)) {
                thread.start();
            }

        }
    };

    BroadcastReceiver receiverSportInfo = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    ArrayList<SportInfoItem> sportInfoItems;
                    sportInfoItems = (ArrayList<SportInfoItem>) intent.getSerializableExtra(SPORT_INFO_SERIALIZABLE);
                    DbHelper dbHelper = getHelper();
                    SQLiteDatabase database = dbHelper.getWritableDatabase();
                    Cursor c = database.rawQuery("SELECT * FROM " + DbHelper.TABLE, null);
                    if(c.getCount() == 0)
                    {
                        for(int i = 0; i < sportInfoItems.size(); i++)
                        {
                            ContentValues cv = new ContentValues();
                            cv.put(DbHelper.ID, sportInfoItems.get(i).getSportId());
                            cv.put(DbHelper.NAME, sportInfoItems.get(i).getSportName());
                            cv.put(DbHelper.MODEL, sportInfoItems.get(i).getSportModel());
                            cv.put(DbHelper.TEAM, sportInfoItems.get(i).getTeam());
                            database.insert(DbHelper.TABLE, null, cv);
                        }
                    }
                    else if(c.getCount() > 0)
                    {
                        c.moveToFirst();
                        for(int y = 0; y < c.getCount(); y++)
                        {
                            //boolean b = c.moveToPosition(y - 1);
                            //int id1 = c.getColumnIndex(DbHelper.ID);
                            int id = c.getInt(c.getColumnIndex(DbHelper.ID));
                            ContentValues cv = new ContentValues();
                            cv.put(DbHelper.ID, sportInfoItems.get(y).getSportId());
                            cv.put(DbHelper.NAME, sportInfoItems.get(y).getSportName());
                            cv.put(DbHelper.MODEL, sportInfoItems.get(y).getSportModel());
                            cv.put(DbHelper.TEAM, sportInfoItems.get(y).getTeam());
                            database.update(DbHelper.TABLE, cv, DbHelper.ID + "=" + id, null);
                            c.moveToNext();
                        }
                    }
                }
            });
            thread.start();
        }
    };

    private void updateListView(final ArrayList<StreamInfoItem> items)
    {
            ArrayList<ItemMapping> itemMappings = new ArrayList<ItemMapping>();
            Cursor c = ((getHelper().getWritableDatabase())).query(DbHelper.TABLE, null,null,null,null,null,null);

            for(StreamInfoItem item : items)
            {
                for(int i = 0; i < c.getCount(); i++)
                {
                    c.moveToPosition(i);
                    String id = c.getString(c.getColumnIndex(DbHelper.ID));
                    String name = c.getString(c.getColumnIndex(DbHelper.NAME));
                    if(item.getSportId() == Integer.parseInt(id))
                    {
                        ItemMapping itemMapping = new ItemMapping(name, item.getName());
                        itemMappings.add(itemMapping);
                        break;
                    }
                }
            }
            streamAdapter = new StreamAdapter(itemMappings, context);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listViewStream.setAdapter(streamAdapter);

            }
        });
//
    }


    @Override
    protected void onResume() {
        intentFilter = new IntentFilter(BROADCAST_ACTION);
        intentSportInfoFilter = new IntentFilter(BROADCAST_ACTION_SPORT_INFO);
        registerReceiver(receiver, intentFilter);
        registerReceiver(receiverSportInfo, intentSportInfoFilter);
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                startService(intentService);
            }
        };
        timer.schedule(timerTask, 0, 5000);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(receiver);
        unregisterReceiver(receiverSportInfo);
        timer.cancel();
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("SERVICE", "Stop AActivity");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
