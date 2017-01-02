package com.example.harshitkhanna.newmusicapp.Services;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.IOException;

/**
 * Created by harshitkhanna on 28/12/16.
 */

public class PlayMP3 extends Service {

    public MediaPlayer mp;
    private final IBinder mbinder = new LocalBinder();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Log.i("hello", "onBind: " + mbinder);
        return mbinder;
    }

    public class LocalBinder extends Binder {

        public PlayMP3 getService(){
            return PlayMP3.this;
        }
    }
    public void PlaySong(String path) {
        Uri songUri=Uri.parse(path);

        mp.reset();
        try {
            mp.setDataSource(getApplicationContext(),songUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        catch (IllegalStateException e){
            e.printStackTrace();
        }

        try {
            mp.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mp.start();
    }
    public int PausePlay(){
        if(mp.isPlaying()){
            Log.i("PK", "PausePlay: ");
            mp.pause();
            return 1;
        }
        else {
            Log.i("PK", "APausePlay: ");
            mp.start();
            if(!mp.isPlaying()){
                return -1;
            }
            return 0;
        }
    }

}




