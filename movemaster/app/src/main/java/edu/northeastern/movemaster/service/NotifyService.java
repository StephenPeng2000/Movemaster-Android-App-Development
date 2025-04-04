package edu.northeastern.movemaster.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.ui.activity.NotifyActivity;

import java.util.ArrayList;
import java.util.List;

public class NotifyService extends Service {
    public static NotifyService service;
    private List<Notify> task = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        service = this;
        initTask();
    }

    /**
     * Clears the task list and queries the database for notification tasks.
     * Updates the status of overdue notifications and adds pending notifications to the task list.
     */
    public void notifyTask() {
        task.clear();
        List<Notify> notifies = Database.getDao().queryNotify(App.user.id);
        Log.d("TAG", "notifyTask: " + notifies);
        for (Notify notify : notifies) {
            if (notify.time < System.currentTimeMillis() && notify.status == 1) {
                notify.status = 3;
                Log.d("TAG", "Removing task: " + notify.content);
                Database.getDao().updateNotify(notify);
            } else if (notify.time > System.currentTimeMillis() && notify.status == 1) {
                task.add(notify);
            }
        }
    }

    /**
     * Initializes the task list by querying the database for notifications.
     * Updates the status of overdue notifications and schedules pending notifications.
     */
    private void initTask() {
        List<Notify> notifies = Database.getDao().queryNotify(App.user.id);
        for (Notify notify : notifies) {
            if (notify.time > System.currentTimeMillis() && notify.status == 1) {
                notify.status = 3;
                Database.getDao().updateNotify(notify);
            } else if (notify.time < System.currentTimeMillis() && notify.status == 1) {
                task.add(notify);
            }
        }
        handler.sendEmptyMessage(100);
    }

    /**
     * Handler to check tasks periodically and trigger notifications.
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            handler.sendEmptyMessageDelayed(100, 1000);
            Log.d("TAG", "handleMessage: ");
            for (Notify notify : task) {
                if (System.currentTimeMillis() > notify.time && !notify.isCall) {
                    notify.isCall = true;
                    Log.d("TAG", "Task completed: " + notify.content);
                    Database.getDao().updateNotify(notify);
                    Intent intent = new Intent(NotifyService.this, NotifyActivity.class).putExtra("notify", notify);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else if (System.currentTimeMillis() > notify.time && notify.isCall) {
                    Log.d("TAG", "Already notified: " + notify.content);
                } else {
                    Log.d("TAG", "Waiting to execute: " + notify.content);
                }
            }
        }
    };
}
