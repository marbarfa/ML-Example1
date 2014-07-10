package com.ml.android.melitraining.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.ml.android.melitraining.database.entities.Bookmark;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Helper class which creates/updates our database and provides the DAOs.
 *
 * @author mbarreto
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private String TAG = this.getClass().getSimpleName();
    private static Dao<Bookmark, Long> bookmarkDAO = null;


    public DatabaseHelper(Context context) {
        super(context,
                Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/melitraining/db.sqlite", null, 1
        );
        createDB();
    }

    private void createDB(){
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/melitraining/db.sqlite");
        if (f.exists()){
            f.mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {
        try {
            logger.info("Creating database for class Bookmark");
            TableUtils.createTableIfNotExists(connectionSource, Bookmark.class);
        } catch (SQLException e) {
            Log.e(TAG, "Could not create new table. " + e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        try {
            logger.info("Dropping table Bookmark");
            TableUtils.dropTable(connectionSource, Bookmark.class, true);
            onCreate(sqLiteDatabase, connectionSource);
        } catch (SQLException e) {
            Log.e(TAG, "Error dropping table Bookmark", e);
        }
    }

    //DAOS
    public Dao<Bookmark, Long> getBookmarksDao() {
        if (bookmarkDAO == null) {
            try {
                bookmarkDAO = getDao(Bookmark.class);
            } catch (SQLException e) {
                Log.e(TAG, "Error al crear DAO ", e);
            }
        }
        return bookmarkDAO;
    }

}
