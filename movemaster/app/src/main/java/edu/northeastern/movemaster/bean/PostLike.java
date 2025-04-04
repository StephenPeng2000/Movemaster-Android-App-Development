package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class PostLike {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int postId;
    public int userid;
}
