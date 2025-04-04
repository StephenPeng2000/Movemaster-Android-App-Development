package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.io.Serializable;
import java.util.List;

@Entity
public class Post implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userid;
    public String content;
    public String title;
    public String time;
    @TypeConverters(StringType.class)
    public List<String> image;
}
