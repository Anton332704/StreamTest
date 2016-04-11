package com.example.streamtest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.TextView;

/**
 * Created by User on 01.03.2016.
 */
public class StreamActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {
    private static final String STREAM_NAME = "Stream";
    private static final String STREAM_PATH = "stream_path";
    private MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    private String streamPath = "";
    TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);
        textView = (TextView) findViewById(R.id.textViewSuperStream);

        String strName = getIntent().getStringExtra(STREAM_NAME);
        textView.setText(strName);

        streamPath = getIntent().getStringExtra(STREAM_PATH);

        vidSurface = (SurfaceView) findViewById(R.id.surfViewStream);
        vidHolder = vidSurface.getHolder();
        vidHolder.addCallback(this);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(streamPath);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepare();

            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
