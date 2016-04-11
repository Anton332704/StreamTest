package com.example.streamtest;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by User on 01.03.2016.
 */
public class SecondActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener{
    private MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    String streamPath = "";
    private static final String VIDEO_ADRESS = "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    private static final String STREAM_PATH = "stream_path";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seconds);
        streamPath = getIntent().getStringExtra(STREAM_PATH);
        vidSurface = (SurfaceView) findViewById(R.id.surfView);
        vidHolder = vidSurface.getHolder();
        vidHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            String rrr = streamPath;
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(streamPath);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepare();

            mediaPlayer.setLooping(true);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void buttonListener(View v)
    {
        switch (v.getId())
        {
            case R.id.buttonPause:
                if (mediaPlayer.isPlaying())
                    mediaPlayer.pause();
                break;
            case R.id.buttonPlay:
                if (!mediaPlayer.isPlaying())
                    mediaPlayer.start();
                break;
            case R.id.buttonStop:
                mediaPlayer.stop();
                break;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //mediaPlayer.start();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mediaPlayer.start();
    }
}
