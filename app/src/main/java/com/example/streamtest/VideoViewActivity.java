package com.example.streamtest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageItemInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

/**
 * Created by User on 01.03.2016.
 */
public class VideoViewActivity extends Activity{
    private static final String STREAM_PATH = "stream_path";
    private static final String STREAM_NAME = "Stream";

    private static final String VIDEO_ADRESS = "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    VideoView vidView;
    MediaController mediaController;
    String vidAddress = "";
    String vidIp = "";
    Uri uri;
    private ProgressDialog pDialog;
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);
        pDialog = new ProgressDialog(VideoViewActivity.this);
        pDialog.setTitle("Загрузка");
        pDialog.setMessage("Загрузка стрима" + "...");
        pDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                dialog.cancel();
                VideoViewActivity.this.finish();
            }
        });
        pDialog.show();
        vidAddress = getIntent().getStringExtra(STREAM_PATH);


        //frameLayout = (FrameLayout) findViewById(R.id.mainFrame);

        if(vidView != null)
        {
            vidView = null;
        }
        vidView = (VideoView)findViewById(R.id.myVideoStream);

       // String uri = "http://web.videostream.dn.ua/hls-live/xmlive/_definst_/" + vidAddress +  "/" + vidAddress + ".m3u8";
        uri = new Uri.Builder()
                .scheme("http")
                .authority(vidIp)
                .path(String.format("/hls-live/xmlive/_definst_/%s/%s.m3u8", vidAddress, vidAddress))
                .build();
        playVideoStream(vidAddress);
    }

    @Override
    public void onBackPressed() {
        vidView.suspend();
        vidView.pause();
        vidView.stopPlayback();
        AudioManager am = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        am.abandonAudioFocus(null);
        vidView.destroyDrawingCache();
        finish();
        super.onBackPressed();
    }

    private void playVideoStream(String urlStream)
    {

        Uri vidUri = Uri.parse(urlStream);
        mediaController = new MediaController(VideoViewActivity.this, false)
        {
            @Override
            public boolean onTrackballEvent(MotionEvent ev) {
                return super.onTrackballEvent(ev);
            }

            @Override
            public void setOnClickListener(OnClickListener l) {
                Toast.makeText(VideoViewActivity.this, "www+++www", Toast.LENGTH_LONG).show();
                super.setOnClickListener(l);
            }
        };
        mediaController.setMediaPlayer(vidView);
        mediaController.setAnchorView(vidView);

        mediaController.setVisibility(View.VISIBLE);
        vidView.setMediaController(mediaController);
        vidView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(VideoViewActivity.this, "Стрим завершен", Toast.LENGTH_LONG).show();
            }
        });
        vidView.requestFocus();
        vidView.setVideoURI(vidUri);
        vidView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        vidView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                //vidView.setZOrderOnTop(true);
                vidView.start();
                pDialog.dismiss();
            }
        });
    }
}
