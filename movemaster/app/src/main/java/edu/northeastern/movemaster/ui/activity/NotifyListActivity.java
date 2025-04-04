package edu.northeastern.movemaster.ui.activity;

import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Notify;
import edu.northeastern.movemaster.databinding.ActivityNotifyListBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.event.AddNotifyEvent;
import edu.northeastern.movemaster.service.NotifyService;
import edu.northeastern.movemaster.util.SomeUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * This activity displays a list of notifications (reminders) and allows the user to add or cancel reminders.
 */
public class NotifyListActivity extends BaseBindingActivity<ActivityNotifyListBinding> {
    // List to store notification data
    private List<Notify> date = new ArrayList<>();

    // Adapter to bind the notification data to the RecyclerView
    private CommonAdapter<Notify> adapter = new CommonAdapter<Notify>(R.layout.item_notify, date) {
        @Override
        public void bind(ViewHolder holder, Notify notify, int position) {
            // Set the reminder content and time
            holder.setText(R.id.notifyContent, "Reminder content：" + notify.content);
            holder.setText(R.id.notifyTime, "Reminder time：" + SomeUtil.getTime(new Date(notify.time)));

            // Set the reminder status based on the notify.status value
            switch (notify.status) {
                case 1:
                    holder.setText(R.id.notifyStatus, "Reminder Status：To be completed");
                    break;
                case 2:
                    holder.setText(R.id.notifyStatus, "Reminder Status：Completed");
                    break;
                case 3:
                    holder.setText(R.id.notifyStatus, "Reminder Status：Incomplete");
                    break;
                case 4:
                    holder.setText(R.id.notifyStatus, "Reminder Status：Cancelled");
                    break;
            }

            // Set click listener for each item in the list
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Only allow cancellation if the status is "To be completed"
                    if (notify.status != 1) {
                        return;
                    }

                    // Show a dialog to confirm cancellation
                    new AlertDialog.Builder(NotifyListActivity.this).setItems(new CharSequence[]{"Cancel it", "Return"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            notify.status = 4; // Set status to "Cancelled"
                            adapter.notifyItemChanged(position); // Update the adapter
                            Database.getDao().updateNotify(notify); // Update the database

                            // Refresh tasks in NotifyService if it is running
                            if (NotifyService.service != null) {
                                NotifyService.service.notifyTask();
                            }
                        }
                    }).show();
                }
            });
        }
    };

    /**
     * Initializes click listeners for the add button.
     */
    @Override
    protected void initListener() {
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start AddNotifyActivity to add a new notification
                startActivity(new Intent(NotifyListActivity.this, AddNotifyActivity.class));
            }
        });
    }

    /**
     * Initializes data by registering the event bus and loading notifications from the database.
     */
    @Override
    protected void initData() {
        // Register this activity with EventBus to receive events
        EventBus.getDefault().register(this);

        // Load notifications from the database
        date.addAll(Database.getDao().queryNotify(App.user.id));

        // Set the adapter for the RecyclerView
        viewBinder.recycler.setAdapter(adapter);
    }

    /**
     * Event handler for adding a new notification.
     *
     * @param event The event indicating a new notification has been added
     */
    @Subscribe
    public void onAddEvent(AddNotifyEvent event) {
        // Clear the current list and reload notifications from the database
        date.clear();
        date.addAll(Database.getDao().queryNotify(App.user.id));
        adapter.notifyDataSetChanged(); // Notify the adapter of the data change
    }
}
