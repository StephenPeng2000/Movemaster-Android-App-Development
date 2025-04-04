package edu.northeastern.movemaster.bean;

import java.io.Serializable;

public class SportType implements Serializable {
    public SportType(String name, int icon) {
        this.name = name;
        this.icon = icon;
    }

    public String name;
    public int icon;


    @Override
    public String toString() {
        return "SportType{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }
}
