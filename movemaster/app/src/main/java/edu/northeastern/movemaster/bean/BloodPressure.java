package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class BloodPressure {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public int userId;
    public String time;
    public String highValue;
    public String lowValue;
}
