package com.example.harshitkhanna.newmusicapp.List;

import com.example.harshitkhanna.newmusicapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.sql.Time;
import java.util.Date;

/**
 * Created by harshitkhanna on 22/10/16.
 */
@Table(database = MyDatabase.class)
public class Song extends BaseModel{
    @Column
    public String name;
    @Column
    public boolean isFav;
    @PrimaryKey
    @Column
    public String path;
    @Column
    public String album;
    @Column
    public String artist;
    @Column
    public String title;
    @Column
    public String duration;
    @Column
    public Date date;
    @Column
    public Long albumid;
}
