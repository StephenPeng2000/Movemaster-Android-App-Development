package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.view.View;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;


import java.util.Date;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.HeartRate;
import edu.northeastern.movemaster.databinding.ActivityHeartRateBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.SomeUtil;

public class HeartRateActivity extends BaseBindingActivity<ActivityHeartRateBinding> {
    private String time;

    @Override
    protected void initListener() {
        viewBinder.selectTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerView pickerView = new TimePickerBuilder(HeartRateActivity.this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        time = SomeUtil.getTime(date);
                        viewBinder.selectTime.setText(time);
                    }
                }).setType(new boolean[]{true, true, true, true, true, false})
                        .build();
                pickerView.show();
            }
        });
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (time.isEmpty()) {
                    toast("Please select a time");
                    return;
                }
                String value = viewBinder.heartEdit.getText().toString();
                HeartRate heartRate = new HeartRate();
                heartRate.value = value;
                heartRate.userId = App.user.id;
                heartRate.time = time;
                Database.getDao().insertHeartRate(heartRate);
                startActivity(new Intent(HeartRateActivity.this, HealthRecordActivity.class).putExtra("type", 1));
                toast("Heart rate data inserted successfully");
            }
        });

        viewBinder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HeartRateActivity.this, HealthRecordActivity.class).putExtra("type", 1));

            }
        });
    }

    @Override
    protected void initData() {
        time = SomeUtil.getTime(new Date());
        viewBinder.selectTime.setText(time);
    }
}