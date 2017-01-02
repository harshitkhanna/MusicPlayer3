package com.example.harshitkhanna.newmusicapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.harshitkhanna.newmusicapp.List.Playlist;
import com.example.harshitkhanna.newmusicapp.List.Playlist_Table;
import com.example.harshitkhanna.newmusicapp.List.Song;
import com.example.harshitkhanna.newmusicapp.List.SongAdapter;
import com.example.harshitkhanna.newmusicapp.List.Song_Table;
import com.example.harshitkhanna.newmusicapp.Services.PlayMP3;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by harshitkhanna on 23/10/16.
 */


public class SongFragment extends Fragment  {


    FragmentListener mfragmentListener;



    public interface FragmentListener{
        public PlayMP3 getService();
        public ArrayList<Song> getFav();
        public ArrayList<Song> getAllSong();
        public void updateonSongClick(String a);
        public void resetRecentlyPlayed();
        public void resetFav();

    }

    public void setMfragmentListener(FragmentListener mfragmentListener) {
        this.mfragmentListener = mfragmentListener;
    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";
    ArrayList<Song> songArrayList;
    SongAdapter songAdapter;
    PlayMP3 mservice;
    int sec;

    SharedPreferences settings;




    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ListView lv= (ListView) rootView.findViewById(R.id.fragment_main_song_lv);
        songArrayList=new ArrayList<>();
        settings=getActivity().getSharedPreferences(MusicAppConstants.PREF_NAME,0);

        //fetching songs from phone
        Bundle b=getArguments();
        sec=b.getInt("section_number");
        if(sec==1) {  //all songs
            songArrayList=mfragmentListener.getAllSong();
        }
        else if(sec==2) //recently played
        {
            songArrayList=populateRecentSongs();
        }
        else if(sec==3){     //favourites
            songArrayList=mfragmentListener.getFav();
        }
        songAdapter=new SongAdapter(getActivity(),songArrayList,mfragmentListener);
        lv.setAdapter(songAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(mservice==null){
                    mservice=mfragmentListener.getService();
                }
                settings.edit().putInt(MusicAppConstants.CURR_SONG,i).commit();
                settings.edit().putInt(MusicAppConstants.FRAG_NO,sec).commit();
                Song song=songArrayList.get(i);
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                Date date=new Date();
                Log.i("date", "onItemClick: "+date.toString());
                song.date=date;
                song.update();
                mfragmentListener.updateonSongClick(song.title);
                mfragmentListener.resetRecentlyPlayed();
                mservice.PlaySong(song.path);
            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());

                final Song s=songArrayList.get(i);
                builder.setTitle(s.title);
                builder.setItems(new String[]{"Add to Playlist"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==0){
                            AlertDialog.Builder ibuilder = new AlertDialog.Builder(getActivity());
                            ibuilder.setTitle("Playlists");
                            List<Playlist> playlist;
                            final String[] pl=new String[10];
                            playlist = SQLite.select(Playlist_Table.pname).distinct().from(Playlist.class).orderBy(Playlist_Table.pname,true).queryList();
                            int k=0;
                            for (Playlist p : playlist) {
                                    pl[k++]=(p.pname);
                            }
                            if(k<MusicAppConstants.PLAYLIST_MAX)
                                pl[k]="Add new PlayList";
                            final int finalK = k;
                            ibuilder.setItems(pl, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if(pl[i].equals(new String("Add new PlayList"))){

                                        AlertDialog.Builder iibuilder=new AlertDialog.Builder(getActivity());
                                        iibuilder.setTitle("New PlayList");
                                        LayoutInflater lay=getActivity().getLayoutInflater();
                                        View m=lay.inflate(R.layout.new_playlist,null);
                                        iibuilder.setView(m);
                                        final EditText editText = (EditText) m.findViewById(R.id.enterplaylistname);
                                        iibuilder.setCancelable(false);
                                        iibuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                            }
                                        });
                                        iibuilder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Playlist newpl=new Playlist();
                                                newpl.psong=s;
                                                newpl.pname=editText.getText().toString();
                                                newpl.save();
                                                Log.i("hdialog", "onClick: "+newpl.pname);
                                            }
                                        });
                                        iibuilder.create().show();
                                    }else{
                                        Playlist newpl=new Playlist();
                                        newpl.pname = pl[i];
                                        newpl.psong = s;
                                        newpl.save();
                                        Log.i("hdialog1", "onClick: "+newpl.pname);
                                    }
                                }
                            });
                            ibuilder.create().show();
                        }
                    }
                });
                builder.create().show();
                return true;
            }
        });

        return rootView;
    }

    public ArrayList<Song> populateRecentSongs() {
        ArrayList<Song> msong=new ArrayList<>();
        List<Song> songs = SQLite.select().from(Song.class).orderBy(Song_Table.date,false).limit(5).queryList();
        for (Song s : songs) {
            Log.i("JK", "populateSongArrayList: "+ s.name +s.date);
            if(s.date!=null)
            msong.add(s);
        }
        return msong;
    }
    public void updateRecentlyPlayed(){
        ArrayList<Song> msongss = populateRecentSongs();
        songArrayList.clear();
        songArrayList.addAll(msongss);
        songAdapter.notifyDataSetChanged();
    }
    public void updateFav(){
        songArrayList = mfragmentListener.getFav();
        songAdapter.notifyDataSetChanged();
    }
    public void updateAllSongs(){
        songArrayList = mfragmentListener.getAllSong();
        songAdapter.notifyDataSetChanged();
    }
}
