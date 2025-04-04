package edu.northeastern.movemaster.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;


import java.util.List;

import edu.northeastern.movemaster.bean.BloodPressure;
import edu.northeastern.movemaster.bean.Drink;
import edu.northeastern.movemaster.bean.HeartRate;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.bean.Post;
import edu.northeastern.movemaster.bean.PostComment;
import edu.northeastern.movemaster.bean.PostLike;
import edu.northeastern.movemaster.bean.SportRecord;
import edu.northeastern.movemaster.bean.User;

@Dao
public interface DatabaseDao {

    // User operation
    @Insert
    public void register(User user);

    @Update
    public void updateUserInfo(User user);

    @Query("select * from user where phone=:phone")
    public User queryUserByPhone(String phone);

    @Query("select * from user where id=:id")
    public User queryUserById(int id);

    @Insert
    public void insertSportRecord(SportRecord record);

    @Delete
    public void removeSportRecord(SportRecord record);

    @Update
    public void updateSportRecord(SportRecord record);

    @Query("select * from SPORTRECORD where userId=:userId")
    public List<SportRecord> querySportRecordByUserId(int userId);


    @Insert
    public void insertHeartRate(HeartRate record);

    @Delete
    public void removeHeartRate(HeartRate record);

    @Update
    public void updateHeartRate(HeartRate record);

    @Query("select * from HeartRate where userId=:userId order by id desc")
    public List<HeartRate> queryHeartRateByUserId(int userId);


    @Insert
    public void insertBloodPressure(BloodPressure record);

    @Delete
    public void removeBloodPressure(BloodPressure record);

    @Update
    public void updateBloodPressure(BloodPressure record);

    @Query("select * from BloodPressure where userId=:userId order by id desc")
    public List<BloodPressure> queryBloodPressureByUserId(int userId);


    @Insert
    public void insertNotify(Notify record);

    @Delete
    public void removeNotify(Notify record);

    @Update
    public void updateNotify(Notify record);

    @Query("select * from Notify where userId=:userId order by id desc")
    public List<Notify> queryNotify(int userId);


    @Insert
    public void insertDrink(Drink record);

    @Delete
    public void removeDrink(Drink record);

    @Update
    public void updateDrink(Drink record);

    @Query("select * from Drink where userId=:userId order by id desc")
    public List<Drink> queryDrink(int userId);

    @Query("select * from Drink where userId=:userId and month=:month and day=:day order by id desc")
    public Drink queryDrink(int userId, int month, int day);

    @Insert
    void post(Post post);


    @Delete
    void del(Post post);


    @Insert
    void sendComment(PostComment comment);


    @Query("select * from postcomment where postId=:postId")
    List<PostComment> queryCommentByPost(int postId);


    @Insert
    void doLike(PostLike like);

    @Delete
    void doNotLike(PostLike like);

    @Query("select * from postlike where userid=:userid and postId = :postId")
    PostLike queryLikeByUser(int userid, int postId);

    @Query("select * from postlike where postId=:postId")
    List<PostLike> queryLikeByPost(int postId);

    @Query("select * from post where content like '%' || :key || '%' or title like '%' || :key || '%'")
    List<Post> searchPost(String key);

    @Delete
    void deletePost(Post post);

    @Query("delete from post where userid = :id")
    void deletePost(int id);

    @Query("select * from post order by id desc")
    List<Post> queryPost();

    @Query("select * from post where userid = :userid order by id desc")
    List<Post> queryPostByUser(int userid);


    @Delete
    void deleteUser(User user);


    @Query("delete from postcomment where userid = :id")
    void deletePostComment(int id);
}
