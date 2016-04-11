package com.example.streamtest;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.net.URI;
import java.util.concurrent.TimeUnit;

/**
 * Created by User on 10.03.2016.
 */
public class MediaPlayerActivity extends Activity implements SurfaceHolder.Callback, MediaPlayer.OnPreparedListener {

    private MediaPlayer mediaPlayer;
    private SurfaceHolder vidHolder;
    private SurfaceView vidSurface;
    String vidAddress = "";
    private static final String STREAM_PATH = "stream_path";
    private static final int STATE_IDLE = 0;
    private ProgressDialog progressDialog;
    public static final String url3 = "http://tvsawpdvr-lh.akamaihd.net/i/stch02wp_1@119660/master.m3u8";
    public static final String urlString = "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_player);
        if(mediaPlayer != null)
        {
            mediaPlayer.release();
        }
        vidAddress = getIntent().getStringExtra(STREAM_PATH);
        progressDialog = new ProgressDialog(MediaPlayerActivity.this);

        vidSurface = (SurfaceView) findViewById(R.id.surfView);

        vidHolder = vidSurface.getHolder();
        vidHolder.addCallback(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        try {
            Uri uri = Uri.parse(vidAddress);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDisplay(vidHolder);
            mediaPlayer.setDataSource(MediaPlayerActivity.this, uri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.prepareAsync();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        } catch (IllegalStateException el) {
            progressDialog.setTitle("Невозможно воспроизвести");
            progressDialog.setMessage("Воспроизведение невозможно");
            progressDialog.show();
            el.printStackTrace();
        } catch (Exception e) {
            progressDialog.setTitle("Невозможно воспроизвести");
            progressDialog.setMessage("Воспроизведение невозможно");
            progressDialog.show();
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        vidSurface.setMinimumWidth(mp.getVideoWidth());
        vidSurface.setMinimumHeight(mp.getVideoHeight());

        mediaPlayer.start();
    }

    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;

            vidHolder.removeCallback(this);

            AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
            am.abandonAudioFocus(null);
        }
        finish();
        super.onBackPressed();
    }
}
