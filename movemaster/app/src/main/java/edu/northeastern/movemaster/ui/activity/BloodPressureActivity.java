package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.BloodPressure;
import edu.northeastern.movemaster.databinding.ActivityBloodPressureBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.SomeUtil;

import java.util.Date;

/**
 * Activity for recording blood pressure data.
 * <p>
 * This activity allows the user to input blood pressure readings (high and low values) and select the time of the measurement.
 * The data is then saved to the database, and the user can navigate to their health records.
 * </p>
 */
public class BloodPressureActivity extends BaseBindingActivity<ActivityBloodPressureBinding> {

    /**
     * The selected time for the blood pressure reading.
     */
    private String time;

    /**
     * Initializes listeners for the views in this activity.
     * <p>
     * This method sets up the click listeners for selecting the time of the blood pressure reading, adding the reading to the database,
     * and navigating to the health records screen.
     * </p>
     */
    @Override
    protected void initListener() {

        // Listener for selecting time of the blood pressure measurement
        viewBinder.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open time picker to select time
                TimePickerView pickerView = new TimePickerBuilder(BloodPressureActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time = SomeUtil.getTime(date);  // Convert selected date to string format
                        viewBinder.selectTime.setText(time);  // Set the selected time on the view
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})  // Set time picker type (showing hours, minutes, seconds)
                        .build();
                pickerView.show();  // Display time picker
            }
        });

        // Listener for adding blood pressure record to the database
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Validate that time is selected
                if (time.isEmpty()) {
                    toast("Please select a time");  // Show toast message if time is not selected
                    return;
                }

                // Get high and low blood pressure values from the user input
                String highValue = viewBinder.highEit.getText().toString();
                String lowValue = viewBinder.lowEdit.getText().toString();

                // Create a new BloodPressure object and set its properties
                BloodPressure pressure = new BloodPressure();
                pressure.highValue = highValue;
                pressure.lowValue = lowValue;
                pressure.userId = App.user.id;  // Set the current user's ID
                pressure.time = time;  // Set the selected time

                // Insert the blood pressure record into the database
                Database.getDao().insertBloodPressure(pressure);

                // Start HealthRecordActivity to show the user's health records
                startActivity(new Intent(BloodPressureActivity.this, HealthRecordActivity.class).putExtra("type", 2));
                toast("Blood pressure data inserted successfully");  // Show success message
            }
        });

        // Listener for navigating to health record activity
        viewBinder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start HealthRecordActivity with type 2 (Blood Pressure records)
                startActivity(new Intent(BloodPressureActivity.this, HealthRecordActivity.class).putExtra("type", 2));
            }
        });
    }

    /**
     * Initializes the data for the activity.
     * <p>
     * This method sets the current time as the default time for the blood pressure reading.
     * </p>
     */
    @Override
    protected void initData() {
        // Set the current time as the default time for the blood pressure reading
        time = SomeUtil.getTime(new Date());
        viewBinder.selectTime.setText(time);  // Display the selected time on the view
    }
}
