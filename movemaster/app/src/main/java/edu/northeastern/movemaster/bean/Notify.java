package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Notify implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String content;
    public long time;
    public int status;//1. To be completed 2. Completed 3. Not completed 4. Cancel
    public String createTime;
    public boolean isCall;
}
