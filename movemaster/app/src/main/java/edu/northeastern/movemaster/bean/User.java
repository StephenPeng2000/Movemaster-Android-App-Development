package edu.northeastern.movemaster.bean;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class User {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String password;
    public String nickname;
    public String avatar;
    public String phone;
    public String wechat;
    public String qq;

    @Override
    public String toString() {
        return "User{" +
                "userId=" + id +
                ", password='" + password + '\'' +
                ", name='" + nickname + '\'' +
                ", face='" + avatar + '\'' +
                ", phone='" + phone + '\'' +
                ", wechat='" + wechat + '\'' +
                ", qq='" + qq + '\'' +
                '}';
    }
}
