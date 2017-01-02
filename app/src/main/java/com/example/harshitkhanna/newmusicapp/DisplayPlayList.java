package com.example.harshitkhanna.newmusicapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.harshitkhanna.newmusicapp.List.Playlist;
import com.example.harshitkhanna.newmusicapp.List.Playlist_Table;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;

public class DisplayPlayList extends AppCompatActivity {

    ArrayList<String> listItems=new ArrayList<>();
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_play_list);
        ListView lv= (ListView) findViewById(R.id.Playlistlv);
        populatePlayList();
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
        lv.setAdapter(arrayAdapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(DisplayPlayList.this,PlaySongPlayList.class);
                intent.putExtra("playlist_name",listItems.get(i));
                startActivity(intent);
            }
        });
        FloatingActionButton fab= (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int n=listItems.size();
                if(n == MusicAppConstants.PLAYLIST_MAX){
                    AlertDialog.Builder builder=new AlertDialog.Builder(DisplayPlayList.this);
                    builder.setTitle("Error");
                    builder.setMessage("Maximum limit crossed!");
                    builder.create().show();
                }
                else{
                    AlertDialog.Builder iibuilder=new AlertDialog.Builder(DisplayPlayList.this);
                    iibuilder.setTitle("New PlayList");
                    LayoutInflater lay=getLayoutInflater();
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
//                            Playlist newpl=new Playlist();
//                            newpl.psong=s;
//                            newpl.pname=editText.getText().toString();
//                            newpl.save();
//                            Log.i("hdialog", "onClick: "+newpl.pname);
                        }
                    });
                    iibuilder.create().show();
                }
            }
        });
    }
    void populatePlayList()
    {
        listItems.clear();
        List<Playlist> plist;
        plist = SQLite.select(Playlist_Table.pname).distinct().from(Playlist.class).orderBy(Playlist_Table.pname,true).queryList();
        for (Playlist playlist:plist){
            listItems.add(playlist.pname);
        }
    }
}
