package edu.northeastern.movemaster.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import edu.northeastern.movemaster.bean.BloodPressure;
import edu.northeastern.movemaster.bean.Drink;
import edu.northeastern.movemaster.bean.HeartRate;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.bean.Post;
import edu.northeastern.movemaster.bean.PostComment;
import edu.northeastern.movemaster.bean.PostLike;
import edu.northeastern.movemaster.bean.SportRecord;
import edu.northeastern.movemaster.bean.User;


@Database(entities = {
        User.class,
        SportRecord.class,
        BloodPressure.class,
        HeartRate.class,
        Notify.class,
        Drink.class,
        PostComment.class,
        PostLike.class,
        Post.class,
}, version = 1, exportSchema = false)
public abstract class MyDB extends RoomDatabase {
    public abstract DatabaseDao getDao();
}
