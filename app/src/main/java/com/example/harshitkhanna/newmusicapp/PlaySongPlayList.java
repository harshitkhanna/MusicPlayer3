package com.example.harshitkhanna.newmusicapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.harshitkhanna.newmusicapp.List.Playlist;
import com.example.harshitkhanna.newmusicapp.List.Playlist_Table;
import com.example.harshitkhanna.newmusicapp.List.Song;
import com.example.harshitkhanna.newmusicapp.List.SongAdapter;
import com.example.harshitkhanna.newmusicapp.Services.PlayMP3;
import com.raizlabs.android.dbflow.sql.language.Condition;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaySongPlayList extends AppCompatActivity {

    ArrayList<Song> songList = new ArrayList<>();
    String playlist;
    TextView song_tv;
    SongAdapter songAdapter;
    SharedPreferences settings;
    PlayMP3 mservice;
    boolean mbound = false;
    ImageView next, pausePlay, prev;
    ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayMP3.LocalBinder binder = (PlayMP3.LocalBinder) iBinder;
            mbound = true;
            mservice = binder.getService();
            Log.i("hello", "onServiceConnected: " + mservice);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mbound = false;
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mbound) {
            getApplicationContext().unbindService(mconnection);
            mbound = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song_play_list);
        settings = getSharedPreferences(MusicAppConstants.PREF_NAME, 0);
        String songname = settings.getString(MusicAppConstants.SONG_NAME,"");
        Intent intent = new Intent(this, PlayMP3.class);
        getApplicationContext().bindService(intent, mconnection, 0);
        Intent it = getIntent();
        playlist = it.getStringExtra("playlist_name");
        populateSongsInPlaylist();
        ListView lv = (ListView) findViewById(R.id.playlistsong_song_lv);
        song_tv = (TextView) findViewById(R.id.psong_name_bottom);
        song_tv.setText(songname);
        pausePlay = (ImageView) findViewById(R.id.pplayPause);
        prev = (ImageView) findViewById(R.id.pprev);
        next = (ImageView) findViewById(R.id.pnext);
        songAdapter = new SongAdapter(this, songList);
        lv.setAdapter(songAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                settings.edit().putInt(MusicAppConstants.CURR_SONG, i).commit();
                settings.edit().putInt(MusicAppConstants.FRAG_NO, 4).commit();
                settings.edit().putString(MusicAppConstants.PLAYLIST, playlist).commit();
                Song song = songList.get(i);
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                Date date = new Date();
                Log.i("date", "onItemClick: " + date.toString());
                song.date = date;
                song.update();
                updateonSongClick(song.title);
                mservice.PlaySong(song.path);
            }
        });

        pausePlay.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             if (mservice.mp.isPlaying()) {
                                                 mservice.mp.pause();
                                                 pausePlay.setImageResource(android.R.drawable.ic_media_play);
                                             } else {
                                                 int frag = settings.getInt(MusicAppConstants.FRAG_NO, 1);
                                                 String ab = settings.getString(MusicAppConstants.PLAYLIST, "");
                                                 if (frag == 4 && playlist.equals(ab)) {
                                                     mservice.PausePlay();
                                                     int k=settings.getInt(MusicAppConstants.CURR_SONG,0);
                                                     Song s=songList.get(k);
                                                     updateonSongClick(s.title);
                                                 } else {
                                                     //Play initial song saved in shared preference
                                                     settings.edit().putInt(MusicAppConstants.FRAG_NO, 4).commit();
                                                     settings.edit().putInt(MusicAppConstants.CURR_SONG, 0).commit();
                                                     settings.edit().putString(MusicAppConstants.PLAYLIST, playlist).commit();
                                                     Song song;
                                                     song = songList.get(0);
                                                     settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                                                     updateonSongClick(song.title);
                                                     Date date = new Date();
                                                     Log.i("date", "onItemClick: " + date.toString());
                                                     song.date = date;
                                                     song.update();
                                                     mservice.PlaySong(song.path);
                                                 }
                                             }
                                         }
                                     }
        );
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song;
                int currFrag = settings.getInt(MusicAppConstants.FRAG_NO, 1);
                int currSong = settings.getInt(MusicAppConstants.CURR_SONG, 0);
                String a = settings.getString(MusicAppConstants.PLAYLIST, "");
                if (currFrag == 4 && a.equals(playlist)) {
                    currSong = (currSong + 1) % songList.size();
                } else currSong = 0;
                song = songList.get(currSong);
                settings.edit().putInt(MusicAppConstants.CURR_SONG, currSong).commit();
                settings.edit().putInt(MusicAppConstants.FRAG_NO, 4).commit();
                settings.edit().putString(MusicAppConstants.PLAYLIST, playlist).commit();
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                updateonSongClick(song.title);
                Date date = new Date();
                Log.i("date", "onItemClick: " + date.toString());
                song.date = date;
                song.update();
                mservice.PlaySong(song.path);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Song song;
                int currFrag = settings.getInt(MusicAppConstants.FRAG_NO, 1);
                int currSong = settings.getInt(MusicAppConstants.CURR_SONG, 0);
                String a = settings.getString(MusicAppConstants.PLAYLIST, "");
                if (currFrag == 4 && a.equals(playlist)) {
                    currSong = (currSong - 1 + songList.size()) % songList.size();
                } else currSong = 0;
                song = songList.get(currSong);
                settings.edit().putInt(MusicAppConstants.CURR_SONG, currSong).commit();
                settings.edit().putInt(MusicAppConstants.FRAG_NO, 4).commit();
                settings.edit().putString(MusicAppConstants.PLAYLIST, playlist).commit();
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                updateonSongClick(song.title);
                Date date = new Date();
                Log.i("date", "onItemClick: " + date.toString());
                song.date = date;
                song.update();
                mservice.PlaySong(song.path);
            }
        });

    }

    void populateSongsInPlaylist() {
        List<Playlist> plist;
        plist = SQLite.select().from(Playlist.class).where(Playlist_Table.pname.eq(playlist)).queryList();
        for (Playlist p : plist) {
            songList.add(p.psong);
        }
    }

    public void updateonSongClick(String a) {
        song_tv.setText(a);
        pausePlay.setImageResource(android.R.drawable.ic_media_pause);
    }
}
