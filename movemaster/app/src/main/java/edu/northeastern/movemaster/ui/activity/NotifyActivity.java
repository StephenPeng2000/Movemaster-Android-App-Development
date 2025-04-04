package edu.northeastern.movemaster.ui.activity;

import android.content.Context;
import android.os.Vibrator;
import android.view.View;

import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.databinding.ActivityNotifyBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.service.NotifyService;
import edu.northeastern.movemaster.util.SomeUtil;

import java.util.Date;

/**
 * NotifyActivity is used to show reminder notifications to the user.
 * It allows the user to acknowledge or dismiss the reminder,
 * updating the notification status in the database accordingly.
 */
public class NotifyActivity extends BaseBindingActivity<ActivityNotifyBinding> {

    @Override
    protected void initListener() {
        // Listener initialization, handled in initData()
    }

    @Override
    protected void initData() {

        // Get the vibrator service for vibrating the phone
        Vibrator vibrator = (Vibrator) NotifyActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(5000); // Vibrate for 5 seconds

        // Retrieve the notification object passed from the previous activity
        Notify notify = (Notify) getIntent().getSerializableExtra("notify");

        // Set the reminder content and time on the UI
        viewBinder.content.setText("Reminder content：" + notify.content);
        viewBinder.time.setText("Reminder time：" + SomeUtil.getTime(new Date(notify.time)));

        // "Sure" button click listener - Mark the notification as acknowledged
        viewBinder.sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the notification status to acknowledged (2)
                notify.status = 2;
                Database.getDao().updateNotify(notify);  // Update the notification status in the database

                // If NotifyService is running, update its task
                if (NotifyService.service != null) {
                    NotifyService.service.notifyTask();
                }

                // Close the activity
                finish();
            }
        });

        // "No" button click listener - Mark the notification as dismissed
        viewBinder.no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Set the notification status to dismissed (3)
                notify.status = 3;
                Database.getDao().updateNotify(notify);  // Update the notification status in the database

                // If NotifyService is running, update its task
                if (NotifyService.service != null) {
                    NotifyService.service.notifyTask();
                }

                // Close the activity
                finish();
            }
        });
    }
}
