package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.SportRecord;
import edu.northeastern.movemaster.bean.SportType;
import edu.northeastern.movemaster.databinding.ActivityAddSportRecordBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.SomeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity for adding or editing a sport record.
 * <p>
 * This activity allows the user to select a sport type, set the exercise duration, and input the calories burned.
 * If a sport record is being edited, the existing details are displayed, and the user can modify them.
 * The activity supports navigation from a previous screen and uses a picker view for time selection.
 * </p>
 */
public class AddSportRecordActivity extends BaseBindingActivity<ActivityAddSportRecordBinding> {

    /**
     * List of time options (hours, minutes, seconds) used in the time picker.
     */
    private List<String> timeDate = new ArrayList<>();

    /**
     * The selected time string in "HH:mm:ss" format.
     */
    private String time;

    /**
     * Flag indicating whether the activity is in edit mode.
     */
    private boolean isEdit = false;

    /**
     * The sport record being edited (if any).
     */
    private SportRecord record;

    /**
     * The selected sport type.
     */
    private SportType type;

    /**
     * Initializes listeners for the views in this activity.
     * <p>
     * This method sets up click listeners for selecting sport type, exercise time, and adding the sport record.
     * It also handles saving or updating the record depending on the mode (edit or add).
     * </p>
     */
    @Override
    protected void initListener() {
        // Click listener for selecting sport type
        viewBinder.selectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(AddSportRecordActivity.this, SportTypeActivity.class), 100);
            }
        });

        // Click listener for selecting exercise time
        viewBinder.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionsPickerView<String> pickerView = new OptionsPickerBuilder(AddSportRecordActivity.this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int options2, int options3, View v) {
                        if (options1 == 0 && options2 == 0 && options3 == 0) {
                            return;
                        }
                        // Construct time string from selected options
                        String h = timeDate.get(options1);
                        String m = timeDate.get(options2);
                        String s = timeDate.get(options3);
                        time = h + ":" + m + ":" + s;
                        viewBinder.selectTime.setText(time);
                    }
                }).build();

                // Set the picker options
                pickerView.setNPicker(timeDate, timeDate, timeDate);
                pickerView.show();
            }
        });

        // Click listener for adding or editing the sport record
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String calories = viewBinder.calories.getText().toString();

                // Validation for adding new record
                if (!isEdit) {
                    if (type == null) {
                        Toast.makeText(AddSportRecordActivity.this, "Please select a motion type", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (time == null || time.isEmpty()) {
                        Toast.makeText(AddSportRecordActivity.this, "Please set exercise time", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (calories.isEmpty()) {
                        calories = "0";
                    }
                    // Create a new sport record and save to database
                    SportRecord sportRecord = new SportRecord(type.name, type.icon, App.user.id, time, SomeUtil.getTime());
                    sportRecord.calories = Integer.parseInt(calories);
                    Database.getDao().insertSportRecord(sportRecord);
                    startActivity(new Intent(AddSportRecordActivity.this, SportRecordActivity.class));
                    Toast.makeText(AddSportRecordActivity.this, "Added successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    // Update the existing sport record
                    if (type != null) {
                        record.icon = type.icon;
                        record.name = type.name;
                    }
                    if (time != null && !time.isEmpty()) {
                        record.duration = time;
                    }
                    if (calories.isEmpty()) {
                        calories = "0";
                    }
                    record.calories = Integer.parseInt(calories);
                    Database.getDao().updateSportRecord(record);
                    Toast.makeText(AddSportRecordActivity.this, "Modification succeeded", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }

    /**
     * Initializes data for the activity.
     * <p>
     * This method populates the list of time options (hours, minutes, seconds) for the time picker.
     * It also checks if an existing sport record is being edited and sets the views accordingly.
     * </p>
     */
    @Override
    protected void initData() {
        // Populate time options (hours, minutes, seconds)
        for (int i = 0; i <= 59; i++) {
            if (i <= 9) {
                timeDate.add("0" + i);
            } else {
                timeDate.add(String.valueOf(i));
            }
        }

        // Check if an existing record is being edited
        record = (SportRecord) getIntent().getSerializableExtra("record");
        if (record != null) {
            isEdit = true;
            viewBinder.selectType.setText(record.name);
            viewBinder.selectTime.setText(record.duration);
            viewBinder.calories.setText(String.valueOf(record.calories));
        }
    }

    /**
     * Called when the sport type is selected from the {@link SportTypeActivity}.
     * <p>
     * This method receives the selected sport type and updates the corresponding view with its name.
     * </p>
     *
     * @param requestCode The request code.
     * @param resultCode The result code.
     * @param data The result data containing the selected sport type.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            type = (SportType) data.getSerializableExtra("type");
            viewBinder.selectType.setText(type.name);
        }
    }
}
