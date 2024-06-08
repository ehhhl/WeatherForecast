package com.example.tqapp.common.database;

import android.app.Application;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tqapp.common.bean.History;
import com.example.tqapp.common.bean.Record;
import com.example.tqapp.common.bean.User;


@androidx.room.Database(entities = {
        User.class,
        History.class,
        Record.class,
}, version = 1, exportSchema = false)
public abstract class Database extends RoomDatabase {
    public abstract DatabaseDao getDatabaseDao();
    private static Database db;
    /**
     * 初始化数据库
     *
     * @param application
     */
    public static void init(Application application) {
        if (db == null) {
            db = Room.databaseBuilder(application, Database.class, "db").allowMainThreadQueries().build();
        }
    }
    /**
     * 获取Dao
     *
     * @return
     */
    public static DatabaseDao getDao() {
        return db.getDatabaseDao();
    }
}
