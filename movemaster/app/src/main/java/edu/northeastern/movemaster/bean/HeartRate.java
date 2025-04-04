package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class HeartRate {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String time;
    public String value;
}
