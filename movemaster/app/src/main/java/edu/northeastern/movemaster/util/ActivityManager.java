package edu.northeastern.movemaster.util;

import android.app.Activity;
import android.util.Log;

import java.util.Stack;

public class ActivityManager {
    private ActivityManager() {
    }

    private final Stack<Activity> activities = new Stack<>();
    static ActivityManager manager = new ActivityManager();

    public static ActivityManager getInstance() {
        return manager;
    }

    public void add(Activity activity) {
        activities.add(activity);
    }

    public void remove(Activity activity) {
        Log.d("TAG", "remove: ");
        activities.remove(activity);
    }

    public <T extends Activity> void finish(Class<T> activityClass) {
        for (Activity activity : activities) {
            if (activity.getClass().equals(activityClass)) {
                finish(activity);
            }
        }
    }

    public void finish(Activity activity) {
        activity.finish();
    }

}
