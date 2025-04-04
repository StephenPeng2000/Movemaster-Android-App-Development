package edu.northeastern.movemaster.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Vibrator;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.SportRecord;
import edu.northeastern.movemaster.bean.SportType;
import edu.northeastern.movemaster.databinding.ActivitySportBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.SomeUtil;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * SportActivity handles the exercise tracking during physical activities such as running, yoga, cycling, etc.
 * It displays the time and calories burned, and allows the user to stop the activity and record the exercise.
 */
public class SportActivity extends BaseBindingActivity<ActivitySportBinding> {

    private boolean isPlaying = true;  // Flag to check whether the exercise is in progress
    private int type = -1;             // The current exercise type (e.g., running, yoga)
    private List<SportType> types = new ArrayList<>();  // List of available exercise types
    private int timesheet = 0;         // Time in seconds that has passed during the exercise
    private int calories = 0;          // Calories burned during the exercise

    @Override
    protected void initListener() {
        // Set up long-click listener to stop the exercise and record the results
        viewBinder.tvStop.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Vibrate the phone for 1 second when the user long-presses the stop button
                Vibrator vibrator = (Vibrator) SportActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);

                // Record the exercise data into the database
                SportRecord sportRecord = new SportRecord(types.get(type).name, types.get(type).icon, App.user.id,
                        formatMiss(timesheet), SomeUtil.getTime());
                sportRecord.calories = calories;
                Database.getDao().insertSportRecord(sportRecord);

                // Show a toast message and navigate to the sport record activity
                Toast.makeText(SportActivity.this, "Exercise Completed", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SportActivity.this, SportRecordActivity.class));
                finish();  // Close the current activity
                return false;
            }
        });

        // Show a message when the stop button is clicked
        viewBinder.tvStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SportActivity.this, "To end the movement, long press", Toast.LENGTH_SHORT).show();
            }
        });

        // Toggle play/pause when the play button is clicked
        viewBinder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isPlaying = !isPlaying;  // Toggle the play state
                viewBinder.ivPlay.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_paly);
                if (isPlaying) {
                    handler.sendEmptyMessageDelayed(100, 1000);  // Start the timer if the exercise is resumed
                }
            }
        });
    }

    @Override
    protected void initData() {
        // Set the status bar color
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#93F3F1"));

        // Start the timer
        handler.sendEmptyMessageDelayed(100, 1000);

        // Initialize the available exercise types
        types.add(new SportType("Running", R.drawable.ic_paobnu_));
        types.add(new SportType("Ride", R.drawable.ic_qixing_));
        types.add(new SportType("Yoga", R.drawable.ic_yoga));
        types.add(new SportType("Dumbbell", R.drawable.ic_yalin));

        // Get the exercise type from the intent and update the UI
        type = getIntent().getIntExtra("type", 0);
        viewBinder.name.setText(types.get(type).name);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // Increment the time elapsed
            timesheet++;

            // Every 2 seconds, randomly increment calories burned
            if (timesheet % 2 == 0) {
                calories += new Random().nextBoolean() ? 0 : 1;
            }

            // Update the UI with the current time and calories
            viewBinder.calories.setText(calories + "");
            viewBinder.timeTv.setText(formatMiss(timesheet));

            // If the exercise is still playing, continue the timer
            if (isPlaying) {
                handler.sendEmptyMessageDelayed(100, 1000);
            }
        }
    };

    /**
     * Formats the given time in seconds into HH:mm:ss format.
     *
     * @param miss The time in seconds.
     * @return The formatted time string.
     */
    public String formatMiss(int miss) {
        String hh = miss / 3600 > 9 ? miss / 3600 + "" : "0" + miss / 3600;
        String mm = (miss % 3600) / 60 > 9 ? (miss % 3600) / 60 + "" : "0" + (miss % 3600) / 60;
        String ss = (miss % 3600) % 60 > 9 ? (miss % 3600) % 60 + "" : "0" + (miss % 3600) % 60;
        return hh + ":" + mm + ":" + ss;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeMessages(100);  // Remove any pending messages to stop the timer
        handler = null;  // Clean up the handler
    }
}
