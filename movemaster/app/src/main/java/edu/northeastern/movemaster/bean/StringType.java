package edu.northeastern.movemaster.bean;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class StringType {
    Gson gson = new Gson();

    @TypeConverter
    public List<String> toRubbish(String json) {
        return gson.fromJson(json, new TypeToken<List<String>>() {
        }.getType());
    }

    @TypeConverter

    public String fromRubbish(List<String> rubbishes) {
        return gson.toJson(rubbishes);
    }
}
