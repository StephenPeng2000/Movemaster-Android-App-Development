package edu.northeastern.movemaster.db;

import android.app.Application;

import androidx.room.Room;



public class Database {
    private static MyDB db;
    public static void init(Application application) {
        if (db == null) {
            db = Room.databaseBuilder(application, MyDB.class, "sport").allowMainThreadQueries().build();
        }
    }
    public static DatabaseDao getDao() {
        return db.getDao();
    }
}
