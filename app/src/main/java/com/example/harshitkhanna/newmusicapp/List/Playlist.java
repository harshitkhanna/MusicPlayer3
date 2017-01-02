package com.example.harshitkhanna.newmusicapp.List;

import com.example.harshitkhanna.newmusicapp.Database.MyDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ForeignKey;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by harshitkhanna on 31/12/16.
 */
@Table(database = MyDatabase.class)
public class Playlist extends BaseModel{
    @PrimaryKey
    @Column
    public String pname;
    @PrimaryKey
    @ForeignKey
    @Column
    public Song psong;
}
