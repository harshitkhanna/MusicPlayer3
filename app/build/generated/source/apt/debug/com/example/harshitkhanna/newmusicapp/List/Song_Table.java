package com.example.harshitkhanna.newmusicapp.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.converter.DateConverter;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.sql.language.property.TypeConvertedProperty;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.lang.Boolean;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.Date;

/**
 * This is generated code. Please do not modify */
public final class Song_Table extends ModelAdapter<Song> {
  public static final Property<String> name = new Property<String>(Song.class, "name");

  public static final Property<Boolean> isFav = new Property<Boolean>(Song.class, "isFav");

  /**
   * Primary Key */
  public static final Property<String> path = new Property<String>(Song.class, "path");

  public static final Property<String> album = new Property<String>(Song.class, "album");

  public static final Property<String> artist = new Property<String>(Song.class, "artist");

  public static final Property<String> title = new Property<String>(Song.class, "title");

  public static final Property<String> duration = new Property<String>(Song.class, "duration");

  public static final TypeConvertedProperty<Date, Long> date = new TypeConvertedProperty<Date, Long>(Song.class, "date");

  public static final Property<Long> albumid = new Property<Long>(Song.class, "albumid");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{name,isFav,path,album,artist,title,duration,date,albumid};

  private final DateConverter global_typeConverterDateConverter;

  public Song_Table(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
    global_typeConverterDateConverter = (DateConverter) holder.getTypeConverterForClass(Date.class);
  }

  @Override
  public final Class<Song> getModelClass() {
    return Song.class;
  }

  public final String getTableName() {
    return "`Song`";
  }

  @Override
  public final BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`name`":  {
        return name;
      }
      case "`isFav`":  {
        return isFav;
      }
      case "`path`":  {
        return path;
      }
      case "`album`":  {
        return album;
      }
      case "`artist`":  {
        return artist;
      }
      case "`title`":  {
        return title;
      }
      case "`duration`":  {
        return duration;
      }
      case "`date`":  {
        return date;
      }
      case "`albumid`":  {
        return albumid;
      }
      default:  {
        throw new IllegalArgumentException("Invalid column name passed. Ensure you are calling the correct table's column");
      }
    }
  }

  @Override
  public final IProperty[] getAllColumnProperties() {
    return ALL_COLUMN_PROPERTIES;
  }

  @Override
  public final void bindToInsertValues(ContentValues values, Song model) {
    values.put("name", model.name != null ? model.name : null);
    values.put("isFav", model.isFav ? 1 : 0);
    values.put("path", model.path != null ? model.path : null);
    values.put("album", model.album != null ? model.album : null);
    values.put("artist", model.artist != null ? model.artist : null);
    values.put("title", model.title != null ? model.title : null);
    values.put("duration", model.duration != null ? model.duration : null);
    Long refdate = model.date != null ? global_typeConverterDateConverter.getDBValue(model.date) : null;
    values.put("date", refdate != null ? refdate : null);
    values.put("albumid", model.albumid != null ? model.albumid : null);
  }

  @Override
  public final void bindToContentValues(ContentValues values, Song model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, Song model, int start) {
    if (model.name != null)  {
      statement.bindString(1 + start, model.name);
    } else {
      statement.bindNull(1 + start);
    }
    statement.bindLong(2 + start, model.isFav ? 1 : 0);
    if (model.path != null)  {
      statement.bindString(3 + start, model.path);
    } else {
      statement.bindNull(3 + start);
    }
    if (model.album != null)  {
      statement.bindString(4 + start, model.album);
    } else {
      statement.bindNull(4 + start);
    }
    if (model.artist != null)  {
      statement.bindString(5 + start, model.artist);
    } else {
      statement.bindNull(5 + start);
    }
    if (model.title != null)  {
      statement.bindString(6 + start, model.title);
    } else {
      statement.bindNull(6 + start);
    }
    if (model.duration != null)  {
      statement.bindString(7 + start, model.duration);
    } else {
      statement.bindNull(7 + start);
    }
    Long refdate = model.date != null ? global_typeConverterDateConverter.getDBValue(model.date) : null;
    if (refdate != null)  {
      statement.bindLong(8 + start, refdate);
    } else {
      statement.bindNull(8 + start);
    }
    if (model.albumid != null)  {
      statement.bindLong(9 + start, model.albumid);
    } else {
      statement.bindNull(9 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, Song model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `Song`(`name`,`isFav`,`path`,`album`,`artist`,`title`,`duration`,`date`,`albumid`) VALUES (?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `Song`(`name`,`isFav`,`path`,`album`,`artist`,`title`,`duration`,`date`,`albumid`) VALUES (?,?,?,?,?,?,?,?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `Song`(`name` TEXT,`isFav` INTEGER,`path` TEXT,`album` TEXT,`artist` TEXT,`title` TEXT,`duration` TEXT,`date` TEXT,`albumid` INTEGER, PRIMARY KEY(`path`)" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, Song model) {
    int index_name = cursor.getColumnIndex("name");
    if (index_name != -1 && !cursor.isNull(index_name)) {
      model.name = cursor.getString(index_name);
    } else {
      model.name = null;
    }
    int index_isFav = cursor.getColumnIndex("isFav");
    if (index_isFav != -1 && !cursor.isNull(index_isFav)) {
      model.isFav = cursor.getInt(index_isFav) == 1 ? true : false;
    } else {
      model.isFav = false;
    }
    int index_path = cursor.getColumnIndex("path");
    if (index_path != -1 && !cursor.isNull(index_path)) {
      model.path = cursor.getString(index_path);
    } else {
      model.path = null;
    }
    int index_album = cursor.getColumnIndex("album");
    if (index_album != -1 && !cursor.isNull(index_album)) {
      model.album = cursor.getString(index_album);
    } else {
      model.album = null;
    }
    int index_artist = cursor.getColumnIndex("artist");
    if (index_artist != -1 && !cursor.isNull(index_artist)) {
      model.artist = cursor.getString(index_artist);
    } else {
      model.artist = null;
    }
    int index_title = cursor.getColumnIndex("title");
    if (index_title != -1 && !cursor.isNull(index_title)) {
      model.title = cursor.getString(index_title);
    } else {
      model.title = null;
    }
    int index_duration = cursor.getColumnIndex("duration");
    if (index_duration != -1 && !cursor.isNull(index_duration)) {
      model.duration = cursor.getString(index_duration);
    } else {
      model.duration = null;
    }
    int index_date = cursor.getColumnIndex("date");
    if (index_date != -1 && !cursor.isNull(index_date)) {
      model.date = global_typeConverterDateConverter.getModelValue(cursor.getLong(index_date));
    } else {
      model.date = global_typeConverterDateConverter.getModelValue(null);
    }
    int index_albumid = cursor.getColumnIndex("albumid");
    if (index_albumid != -1 && !cursor.isNull(index_albumid)) {
      model.albumid = cursor.getLong(index_albumid);
    } else {
      model.albumid = null;
    }
  }

  @Override
  public final boolean exists(Song model, DatabaseWrapper wrapper) {
    return SQLite.selectCountOf()
    .from(Song.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(Song model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(path.eq(model.path));
    return clause;
  }

  @Override
  public final Song newInstance() {
    return new Song();
  }
}
