package com.raizlabs.android.dbflow.config;

import com.example.harshitkhanna.newmusicapp.Database.MyDatabase;
import com.example.harshitkhanna.newmusicapp.List.Playlist;
import com.example.harshitkhanna.newmusicapp.List.Playlist_Table;
import com.example.harshitkhanna.newmusicapp.List.Song;
import com.example.harshitkhanna.newmusicapp.List.Song_Table;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class MyDatabaseMyDatabase_Database extends DatabaseDefinition {
  public MyDatabaseMyDatabase_Database(DatabaseHolder holder) {
    holder.putDatabaseForTable(Song.class, this);
    holder.putDatabaseForTable(Playlist.class, this);
    models.add(Song.class);
    modelTableNames.put("Song", Song.class);
    modelAdapters.put(Song.class, new Song_Table(holder, this));
    models.add(Playlist.class);
    modelTableNames.put("Playlist", Playlist.class);
    modelAdapters.put(Playlist.class, new Playlist_Table(holder, this));
  }

  @Override
  public final Class<?> getAssociatedDatabaseClassFile() {
    return MyDatabase.class;
  }

  @Override
  public final boolean isForeignKeysSupported() {
    return false;
  }

  @Override
  public final boolean isInMemory() {
    return false;
  }

  @Override
  public final boolean backupEnabled() {
    return false;
  }

  @Override
  public final boolean areConsistencyChecksEnabled() {
    return false;
  }

  @Override
  public final int getDatabaseVersion() {
    return 1;
  }

  @Override
  public final String getDatabaseName() {
    return "MyDatabase";
  }
}
