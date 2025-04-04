package edu.northeastern.movemaster;

import android.app.Application;
import android.content.Context;

import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.PreferenceUtil;


public class App extends Application {
    static App context;
    public static User user;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        Database.init(this);
        String logger = PreferenceUtil.getInstance().get("logger", "");
        if (!logger.isEmpty()) {
            user = Database.getDao().queryUserByPhone(logger);
        }
        User zhangSan = Database.getDao().queryUserByPhone("18900000000");
        if (zhangSan == null) {
            zhangSan = new User();
            zhangSan.nickname = "Jack";
            zhangSan.avatar = "";
            zhangSan.password = "123456";
            zhangSan.phone = "18900000000";
            Database.getDao().register(zhangSan);
        }

    }

    public static Context getContext() {
        return context;
    }

    public static boolean isLogin() {
        return user != null;
    }

    public static void login(User user_) {
        user = user_;
        PreferenceUtil.getInstance().save("logger", user_.phone);
    }

    public static void logout() {
        user = null;
        PreferenceUtil.getInstance().remove("logger");
    }
}
