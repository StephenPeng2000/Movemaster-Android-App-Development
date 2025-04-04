package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class SportRecord implements Serializable {
    public SportRecord(String name, int icon, int userId, String duration, String time) {
        this.name = name;
        this.userId = userId;
        this.icon = icon;
        this.time = time;
        this.duration = duration;
    }

    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String name;
    public int icon;
    public int calories;
    public String duration;
    public String time;
}
