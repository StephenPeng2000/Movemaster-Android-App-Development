package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PostComment {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int postId;
    public int userid;
    public int type;
    public String time;
    public String image;
    public String content;

}
