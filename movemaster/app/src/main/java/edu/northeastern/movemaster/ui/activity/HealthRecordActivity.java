package edu.northeastern.movemaster.ui.activity;

import android.content.DialogInterface;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.BloodPressure;
import edu.northeastern.movemaster.bean.HeartRate;
import edu.northeastern.movemaster.databinding.ActivityHealthRecordBinding;
import edu.northeastern.movemaster.db.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity to display and manage health records like heart rate and blood pressure.
 */
public class HealthRecordActivity extends BaseBindingActivity<ActivityHealthRecordBinding> {

    // List to store HeartRate records
    private List<HeartRate> heartRates = new ArrayList<>();

    // Adapter for displaying heart rate records
    private CommonAdapter<HeartRate> heartRateAdapter = new CommonAdapter<HeartRate>(R.layout.item_heart, heartRates) {
        @Override
        public void bind(ViewHolder holder, HeartRate heartRate, int position) {
            holder.setText(R.id.timeTv, heartRate.time);  // Display time of heart rate measurement
            holder.setText(R.id.valueTv, heartRate.value + "cpm");  // Display heart rate value
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show options for deleting or returning
                    new AlertDialog.Builder(HealthRecordActivity.this).setItems(new CharSequence[]{"Delete", "Return"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    // Delete selected heart rate record
                                    heartRates.remove(position);
                                    heartRateAdapter.notifyItemRemoved(position);
                                    heartRateAdapter.notifyItemRangeChanged(position, heartRates.size() - position);
                                    Database.getDao().removeHeartRate(heartRate);
                                    break;
                                case 1:
                                    break; // Return, do nothing
                            }
                        }
                    }).show();
                }
            });
        }
    };

    // List to store BloodPressure records
    private List<BloodPressure> pressures = new ArrayList<>();

    // Adapter for displaying blood pressure records
    private CommonAdapter<BloodPressure> pressuresAdapter = new CommonAdapter<BloodPressure>(R.layout.item_pressures, pressures) {
        @Override
        public void bind(ViewHolder holder, BloodPressure pressure, int position) {
            holder.setText(R.id.time, pressure.time);  // Display time of blood pressure measurement
            holder.setText(R.id.valueTv, pressure.highValue + "/" + pressure.lowValue + "mmHg");  // Display blood pressure values
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Show options for deleting or returning
                    new AlertDialog.Builder(HealthRecordActivity.this).setItems(new CharSequence[]{"Delete", "Return"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    // Delete selected blood pressure record
                                    pressures.remove(position);
                                    pressuresAdapter.notifyItemRemoved(position);
                                    pressuresAdapter.notifyItemRangeChanged(position, pressures.size() - position);
                                    Database.getDao().removeBloodPressure(pressure);
                                    break;
                                case 1:
                                    break; // Return, do nothing
                            }
                        }
                    }).show();
                }
            });
        }
    };

    @Override
    protected void initListener() {
        // No listeners are needed in this activity at the moment
    }

    @Override
    protected void initData() {
        // Retrieve the type of record to display (heart rate or blood pressure)
        int type = getIntent().getIntExtra("type", 1);

        // Display heart rate records if type is 1
        if (type == 1) {
            viewBinder.titleView.setText("Heart rate recording");
            viewBinder.recycler.setAdapter(heartRateAdapter);
            heartRates.addAll(Database.getDao().queryHeartRateByUserId(App.user.id));
        }
        // Display blood pressure records if type is 2
        else {
            viewBinder.titleView.setText("Blood pressure recording");
            pressures.addAll(Database.getDao().queryBloodPressureByUserId(App.user.id));
            viewBinder.recycler.setAdapter(pressuresAdapter);
        }
    }
}
