package com.example.streamtest;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.exoplayer.ExoPlayer;



/**
 * Created by User on 11.03.2016.
 */
public class VitamioActivity extends Activity {
    public static final String urlString = "https://devimages.apple.com.edgekey.net/streaming/examples/bipbop_4x3/bipbop_4x3_variant.m3u8";
    ExoPlayer player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vitamio_player);

    }
}
