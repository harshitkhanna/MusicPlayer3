package com.example.harshitkhanna.newmusicapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.harshitkhanna.newmusicapp.List.Playlist;
import com.example.harshitkhanna.newmusicapp.List.Playlist_Table;
import com.example.harshitkhanna.newmusicapp.List.Song;
import com.example.harshitkhanna.newmusicapp.List.SongAdapter;
import com.example.harshitkhanna.newmusicapp.Services.PlayMP3;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.R.attr.fragment;
import static android.R.attr.logo;

public class MainActivity extends AppCompatActivity implements SongFragment.FragmentListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    final String PREF_NAME="SharedPref",FIRST_TIME="my_first_time";
    SharedPreferences settings;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    boolean mbound=false;
    HashMap<Integer,Fragment> hashmap=new HashMap<>();
    PlayMP3 mservice;
    ArrayList<Song> fav=new ArrayList<>(),allSong=new ArrayList<>();
    TextView song_tv;
    ImageView pausePlay,prev,next;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    ServiceConnection mconnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            PlayMP3.LocalBinder binder = (PlayMP3.LocalBinder) iBinder;
            mbound=true;
            mservice=binder.getService();
            Log.i("hello", "onServiceConnected: " + mservice);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mbound=false;
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mbound){
            Log.i("helk", "onDestroy: ");
            Intent intent=new Intent(this, PlayMP3.class);
            getApplicationContext().unbindService(mconnection);

            mbound=false;
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent=new Intent(this, PlayMP3.class);
        getApplicationContext().startService(intent);
        getApplicationContext().bindService(intent,mconnection,0);// Context.BIND_AUTO_CREATE);
        settings=getSharedPreferences(PREF_NAME,0);
        if(settings.getBoolean(FIRST_TIME,true)){
            populateDatabase();
            settings.edit().putBoolean(FIRST_TIME,false).commit();
        }

        String songname = settings.getString(MusicAppConstants.SONG_NAME,"");
        song_tv= (TextView) findViewById(R.id.song_name_bottom);
        song_tv.setText(songname);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(1);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        pausePlay= (ImageView) findViewById(R.id.playPause);
        prev= (ImageView) findViewById(R.id.prev);
        next= (ImageView) findViewById(R.id.next);


        pausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int a = mservice.PausePlay();
                if(a==1){  //paused now
                    pausePlay.setImageResource(android.R.drawable.ic_media_play);
                }
                else if(a==0){
                    pausePlay.setImageResource(android.R.drawable.ic_media_pause);
                }
                else{ //Play initial song saved in shared preference
                    ArrayList<Song> nextPrevList;
                    int currFrag=settings.getInt(MusicAppConstants.FRAG_NO,1);
                    int currSong=settings.getInt(MusicAppConstants.CURR_SONG,0);
                    if(currFrag==1 || currFrag==2){
                        nextPrevList=getAllSong();
                    }
                    else if(currFrag==3){
                        nextPrevList=getFav();
                    }
                    else{//currFrag=4 //playlist
                        String ab=settings.getString(MusicAppConstants.PLAYLIST,"");
                        nextPrevList=getPlayList(ab);
                    }
                    Song song;
                    song = nextPrevList.get(currSong);
                    updateonSongClick(song.title);
                    Date date=new Date();
                    Log.i("date", "onItemClick: "+date.toString());
                    song.date=date;
                    song.update();
                    mservice.PlaySong(song.path);
                }
            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Song> nextPrevList;
                Song song;
                int currFrag=settings.getInt(MusicAppConstants.FRAG_NO,1);
                int currSong=settings.getInt(MusicAppConstants.CURR_SONG,0);
                if(currFrag==1 || currFrag==2){
                    nextPrevList=getAllSong();
                }
                else if(currFrag==3){
                    nextPrevList=getFav();
                }
                else{//currFrag=4 //playlist
                        String a=settings.getString(MusicAppConstants.PLAYLIST,"");
                        nextPrevList=getPlayList(a);
                }
                currSong = (currSong + 1) % nextPrevList.size();
                settings.edit().putInt(MusicAppConstants.CURR_SONG,currSong).commit();
                song = nextPrevList.get(currSong);
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                updateonSongClick(song.title);
                Date date=new Date();
                Log.i("date", "onItemClick: "+date.toString());
                song.date=date;
                song.update();
                resetRecentlyPlayed();
                mservice.PlaySong(song.path);
            }
        });
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Song> nextPrevList;
                Song song;
                int currFrag=settings.getInt(MusicAppConstants.FRAG_NO,1);
                int currSong=settings.getInt(MusicAppConstants.CURR_SONG,0);
                if(currFrag==1){
                    nextPrevList=getAllSong();
                }
                else if(currFrag==3){
                    nextPrevList=getFav();
                }
                else{  //currFrag=4 //playlist
                    String a=settings.getString(MusicAppConstants.PLAYLIST,"");
                    nextPrevList=getPlayList(a);
                }
                currSong = (currSong - 1 + nextPrevList.size()) % nextPrevList.size();
                song = nextPrevList.get(currSong);
                settings.edit().putInt(MusicAppConstants.CURR_SONG,currSong).commit();
                settings.edit().putString(MusicAppConstants.SONG_NAME, song.title).commit();
                updateonSongClick(song.title);
                Date date=new Date();
                Log.i("date", "onItemClick: "+date.toString());
                song.date=date;
                song.update();
                resetRecentlyPlayed();
                mservice.PlaySong(song.path);
            }
        });
    }



    private void populateDatabase() {
        Cursor cursor;
        String[] STAR={"*"};
        Uri allsongsuri= MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection=MediaStore.Audio.Media.IS_MUSIC+" !=0";
        boolean isSdPresent=android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        if(isSdPresent){
            cursor= getContentResolver().query(allsongsuri,STAR,selection,null,null);
            if(cursor!=null){
                while(cursor.moveToNext()){
                    Song s=new Song();
                    s.name=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    s.title=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                    s.duration=cursor.getString((cursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                    s.path=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    s.album=cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM));
                    s.albumid=cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    s.artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    Log.i("hk", "populateDatabase: "+ s.albumid +"  "+ s.name);
                    s.isFav=false;
                    s.save();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem searchItem=menu.findItem(R.id.search_icon);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.menuPlayList) {
            Intent intent=new Intent(this,DisplayPlayList.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public PlayMP3 getService() {
        return mservice;
    }

    @Override
    public ArrayList<Song> getFav() {
        populateFavArrayList();
        return fav;
    }

    @Override
    public ArrayList<Song> getAllSong() {
        populateSongArrayList();
        return allSong;
    }

    @Override
    public void updateonSongClick(String a) {
        song_tv.setText(a);
        pausePlay.setImageResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void resetRecentlyPlayed() {
        SongFragment RecentlyPlayedFragment=null;
        if(hashmap.containsKey(1)){
            RecentlyPlayedFragment= (SongFragment) hashmap.get(1);
            RecentlyPlayedFragment.updateRecentlyPlayed();
        }
    }

    @Override
    public void resetFav() {
        SongFragment RecentlyPlayedFragment=null;
        if(hashmap.containsKey(0)){
            RecentlyPlayedFragment= (SongFragment) hashmap.get(0);
            RecentlyPlayedFragment.updateAllSongs();
        }
        if(hashmap.containsKey(1)){
            RecentlyPlayedFragment= (SongFragment) hashmap.get(1);
            RecentlyPlayedFragment.updateRecentlyPlayed();
        }
        if(hashmap.containsKey(2)){
            RecentlyPlayedFragment= (SongFragment) hashmap.get(2);
            RecentlyPlayedFragment.updateFav();
        }
    }


    private void populateFavArrayList() {
        fav.clear();
        List<Song> songs= SQLite.select().from(Song.class).queryList();
        for(Song s:songs){
            if(s.isFav)
                fav.add(s);
        }
    }

    private ArrayList<Song> getPlayList(String a){
        ArrayList<Song> mplaylist=new ArrayList<>();
        List<Playlist> mlist;
        mlist = SQLite.select().from(Playlist.class).where(Playlist_Table.pname.eq(a)).orderBy(Playlist_Table.pname,true).queryList();
        for(Playlist p:mlist){
            mplaylist.add(p.psong);
        }
        return mplaylist;
    }

    private void populateSongArrayList() {
        allSong.clear();
        List<Song> songs = SQLite.select().from(Song.class).queryList();
        for (Song s : songs) {
            Log.i("TAG", "populateSongArrayList: "+ s.name);
            allSong.add(s);
        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            if(hashmap.containsKey(position)==true){
                return hashmap.get(position);
            }
            SongFragment fragment=new SongFragment();
            fragment.setMfragmentListener(MainActivity.this);
            Bundle b=new Bundle();
            b.putInt("section_number",position+1);
//            b.putInt("isRefresh",isRefresh);
            fragment.setArguments(b);
            hashmap.put(position,fragment);
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "All Songs";
                case 1:
                    return "Recently Played";
                case 2:
                    return "Favourites";
                case 3:
                    return "Playlist";
            }
            return null;
        }
    }

}
