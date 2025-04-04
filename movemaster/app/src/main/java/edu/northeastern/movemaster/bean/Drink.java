package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Drink {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public int month;
    public int day;
    public int count;
    public String time;
}
