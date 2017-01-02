package com.example.harshitkhanna.newmusicapp.List;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RemoteViewsService;
import android.widget.TextView;

import com.example.harshitkhanna.newmusicapp.R;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static android.R.attr.bitmap;

/**
 * Created by harshitkhanna on 22/10/16.
 */

public class SongAdapter extends ArrayAdapter {

    Context mcontext;
    ArrayList<Song> msongs;

    public SongAdapter(Context context, ArrayList<Song> objects) {
        super(context, 0, objects);
        msongs=objects;
        mcontext=context;
    }

    public class ViewHolder{
        ImageView imageView;
        TextView tv;
        ImageView isFav;

        public ViewHolder(ImageView imageView, TextView tv, ImageView isFav) {
            this.imageView = imageView;
            this.tv = tv;
            this.isFav = isFav;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView==null){
            convertView=View.inflate(mcontext, R.layout.song_list_view,null);
            ImageView iv= (ImageView) convertView.findViewById(R.id.song_list_icon);
            TextView tv= (TextView) convertView.findViewById(R.id.song_list_songName);
            ImageView fav= (ImageView) convertView.findViewById(R.id.song_list_isFav);
            ViewHolder vh=new ViewHolder(iv,tv,fav);
            convertView.setTag(vh);
        }
        final ViewHolder vh= (ViewHolder) convertView.getTag();
        final Song song=msongs.get(position);
        //Add song image
        vh.tv.setText(song.title);
        Uri sArtworkUri = Uri
                .parse("content://media/external/audio/albumart");
        if(song.albumid!=null) {
            Uri albumArtUri = ContentUris.withAppendedId(sArtworkUri, song.albumid);
//            Picasso.with(mcontext).load(albumArtUri).into(vh.imageView);
            Bitmap bm=null;
            try {
                bm = MediaStore.Images.Media.getBitmap(mcontext.getContentResolver(), albumArtUri);
            } catch (FileNotFoundException exception) {
                exception.printStackTrace();
                bm = BitmapFactory.decodeResource(mcontext.getResources(),
                        android.R.drawable.ic_media_ff);
            } catch (IOException e) {
                e.printStackTrace();
            }
            bm = Bitmap.createScaledBitmap(bm, 100, 100, true);
            vh.imageView.setImageBitmap(bm);
        }
        if(song.isFav==true){
            vh.isFav.setImageResource(android.R.drawable.star_big_on);
        }
        else{
            vh.isFav.setImageResource(android.R.drawable.star_big_off);
        }
        vh.isFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(song.isFav){
                    song.isFav=false;
                    vh.isFav.setImageResource(android.R.drawable.star_big_off);
                    song.update();
                }
                else{
                    song.isFav=true;
                    vh.isFav.setImageResource(android.R.drawable.star_big_on);
                    song.update();
                }
            }
        });
        return convertView;
    }
}
