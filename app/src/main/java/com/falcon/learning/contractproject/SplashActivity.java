package com.falcon.learning.contractproject;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import java.io.IOException;


public class SplashActivity extends ActionBarActivity {

    MediaPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        try {
            init();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void playMusic() throws IOException {
        AssetFileDescriptor afd = getAssets().openFd("splash.mp3");
        player = new MediaPlayer();
        player.setDataSource(afd.getFileDescriptor(),afd.getStartOffset(),afd.getLength());
        player.prepare();
        player.start();
    }

    public void stopAudio(){
        if (player!=null){
            player.stop();
            player.release();
            player=null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopAudio();
    }

    private void init() throws IOException {
        try {
            playMusic();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        },5000);
    }

}
