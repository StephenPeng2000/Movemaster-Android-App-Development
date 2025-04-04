package edu.northeastern.movemaster.ui.activity;

import android.util.Log;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.databinding.ActivityAddNotifyBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.event.AddNotifyEvent;
import edu.northeastern.movemaster.service.NotifyService;
import edu.northeastern.movemaster.util.SomeUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.Date;

/**
 * This activity allows users to add notification reminders by selecting a time and entering content.
 */
public class AddNotifyActivity extends BaseBindingActivity<ActivityAddNotifyBinding> {
    // Stores the selected time as a formatted string
    private String time = "";

    // Stores the selected time as a timestamp in milliseconds
    private long timestamp = 0;

    /**
     * Initializes click listeners for the time selection and add notification buttons.
     */
    @Override
    protected void initListener() {
        // Listener for selecting a reminder time
        viewBinder.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Initialize the time picker dialog
                TimePickerView pickerView = new TimePickerBuilder(AddNotifyActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        // Format and store the selected time
                        time = SomeUtil.getTime(date);
                        timestamp = date.getTime();
                        Log.d("TAG", "onTimeSelect: " + timestamp);
                        viewBinder.selectTime.setText(time);
                    }
                }).setType(new boolean[]{true, true, true, true, true, false}) // Display year, month, day, hour, minute
                        .build();
                pickerView.show();
            }
        });

        // Listener for adding a new notification reminder
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Check if a time has been selected
                if (timestamp == 0) {
                    toast("Please select a reminder time");
                    return;
                }

                // Get the reminder content from the input field
                String content = viewBinder.contentEdit.getText().toString().trim();
                if (content.isEmpty()) {
                    toast("Please enter a reminder");
                    return;
                }

                // Create a new notification object and set its properties
                Notify notify = new Notify();
                notify.status = 1; // Status 1 indicates the notification is pending
                notify.content = content;
                notify.time = timestamp;
                notify.createTime = SomeUtil.getTime(new Date());
                notify.userId = App.user.id; // Associate the notification with the current user

                // Insert the new notification into the database
                Database.getDao().insertNotify(notify);

                // Post an event to notify other parts of the app about the new notification
                EventBus.getDefault().post(new AddNotifyEvent());

                // Refresh the task list in the notification service if it is running
                if (NotifyService.service != null) {
                    NotifyService.service.notifyTask();
                }

                // Close the current activity
                finish();
            }
        });
    }

    /**
     * Initializes data for the activity. Currently empty, but can be extended for future use.
     */
    @Override
    protected void initData() {
        // No initialization data required at the moment
    }
}
