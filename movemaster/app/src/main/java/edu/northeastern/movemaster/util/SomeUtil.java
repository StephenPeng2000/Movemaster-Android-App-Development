package edu.northeastern.movemaster.util;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SomeUtil {
    public static Boolean isPhone(String phone) {

        String pattern = "^1(3\\d|4[5-9]|5[0-35-9]|6[567]|7[0-8]|8\\d|9[0-35-9])\\d{8}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(phone);
        return m.matches();
    }

    public static Boolean isQQ(String qq) {
        String pattern = "[1-9][0-9]{4,9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(qq);
        return m.matches();
    }

    public static Boolean isTruePwd(String pwd) {
        String pattern = "^\\d{6,}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(pwd);
        return m.matches();
    }

    public static int dp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, Resources.getSystem().getDisplayMetrics());
    }

    public static String getTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }
   public static String getTime(Date date) {

        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        return format.format(date);
    }
    public static DisplayMetrics getDisplayMetrics() {
        return Resources.getSystem().getDisplayMetrics();
    }

    public static String getHMSTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }

    public static String getImagePath(Context context, Uri uri) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            File file = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(bytes);
            fileOutputStream.close();
            inputStream.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
