package com.example.harshitkhanna.newmusicapp.List;

import android.content.ContentValues;
import android.database.Cursor;
import com.raizlabs.android.dbflow.config.DatabaseDefinition;
import com.raizlabs.android.dbflow.config.DatabaseHolder;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.sql.QueryBuilder;
import com.raizlabs.android.dbflow.sql.language.ConditionGroup;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.BaseProperty;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.raizlabs.android.dbflow.sql.language.property.Property;
import com.raizlabs.android.dbflow.structure.ModelAdapter;
import com.raizlabs.android.dbflow.structure.database.DatabaseStatement;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.lang.Override;
import java.lang.String;

/**
 * This is generated code. Please do not modify */
public final class Playlist_Table extends ModelAdapter<Playlist> {
  /**
   * Primary Key */
  public static final Property<String> pname = new Property<String>(Playlist.class, "pname");

  /**
   * Foreign Key / Primary Key */
  public static final Property<String> psong_path = new Property<String>(Playlist.class, "psong_path");

  public static final IProperty[] ALL_COLUMN_PROPERTIES = new IProperty[]{pname,psong_path};

  public Playlist_Table(DatabaseHolder holder, DatabaseDefinition databaseDefinition) {
    super(databaseDefinition);
  }

  @Override
  public final Class<Playlist> getModelClass() {
    return Playlist.class;
  }

  public final String getTableName() {
    return "`Playlist`";
  }

  @Override
  public final BaseProperty getProperty(String columnName) {
    columnName = QueryBuilder.quoteIfNeeded(columnName);
    switch (columnName)  {
      case "`pname`":  {
        return pname;
      }
      case "`psong_path`":  {
        return psong_path;
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
  public final void bindToInsertValues(ContentValues values, Playlist model) {
    values.put("pname", model.pname != null ? model.pname : null);
    if (model.psong != null) {
      values.put("psong_path", model.psong.path);
    } else {
      values.putNull("psong_path");
    }
  }

  @Override
  public final void bindToContentValues(ContentValues values, Playlist model) {
    bindToInsertValues(values, model);
  }

  @Override
  public final void bindToInsertStatement(DatabaseStatement statement, Playlist model, int start) {
    if (model.pname != null)  {
      statement.bindString(1 + start, model.pname);
    } else {
      statement.bindNull(1 + start);
    }
    if (model.psong != null) {
      statement.bindString(2 + start, model.psong.path);
    } else {
      statement.bindNull(2 + start);
    }
  }

  @Override
  public final void bindToStatement(DatabaseStatement statement, Playlist model) {
    bindToInsertStatement(statement, model, 0);
  }

  @Override
  public final String getInsertStatementQuery() {
    return "INSERT INTO `Playlist`(`pname`,`psong_path`) VALUES (?,?)";
  }

  @Override
  public final String getCompiledStatementQuery() {
    return "INSERT INTO `Playlist`(`pname`,`psong_path`) VALUES (?,?)";
  }

  @Override
  public final String getCreationQuery() {
    return "CREATE TABLE IF NOT EXISTS `Playlist`(`pname` TEXT,`psong_path` TEXT, PRIMARY KEY(`pname`,`psong_path`)"+ ", FOREIGN KEY(`psong_path`) REFERENCES " + FlowManager.getTableName(Song.class) + "(`path`) ON UPDATE NO ACTION ON DELETE NO ACTION" + ");";
  }

  @Override
  public final void loadFromCursor(Cursor cursor, Playlist model) {
    int index_pname = cursor.getColumnIndex("pname");
    if (index_pname != -1 && !cursor.isNull(index_pname)) {
      model.pname = cursor.getString(index_pname);
    } else {
      model.pname = null;
    }
    int index_psong_path = cursor.getColumnIndex("psong_path");
    if (index_psong_path != -1 && !cursor.isNull(index_psong_path)) {
      model.psong = SQLite.select().from(Song.class).where()
          .and(Song_Table.path.eq(cursor.getString(index_psong_path)))
          .querySingle();
    } else {
      model.psong = null;
    }
  }

  @Override
  public final boolean exists(Playlist model, DatabaseWrapper wrapper) {
    return SQLite.selectCountOf()
    .from(Playlist.class)
    .where(getPrimaryConditionClause(model))
    .hasData(wrapper);
  }

  @Override
  public final ConditionGroup getPrimaryConditionClause(Playlist model) {
    ConditionGroup clause = ConditionGroup.clause();
    clause.and(pname.eq(model.pname));
    if (model.psong != null) {
      clause.and(psong_path.eq(model.psong.path));
    } else {
      clause.and(psong_path.eq((com.raizlabs.android.dbflow.sql.language.IConditional) null));
    }
    return clause;
  }

  @Override
  public final Playlist newInstance() {
    return new Playlist();
  }
}
