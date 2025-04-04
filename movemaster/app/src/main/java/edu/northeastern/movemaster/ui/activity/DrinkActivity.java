package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.view.View;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Drink;
import edu.northeastern.movemaster.databinding.ActivityDrinkBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.SomeUtil;

import java.util.Calendar;

/**
 * Activity to track the user's daily drink count.
 * <p>
 * This activity allows the user to see their current drink count for the day and update it by adding
 * one more drink. It also provides a button to view the historical drink records.
 * </p>
 */
public class DrinkActivity extends BaseBindingActivity<ActivityDrinkBinding> {

    /**
     * Initializes the listeners for this activity.
     * <p>
     * This method sets up click listeners for the "Record" and "Add" buttons.
     * </p>
     */
    @Override
    protected void initListener() {
        // Listener for the "Record" button to view historical drink records
        viewBinder.record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Start the DrinkRecordActivity to show historical records
                startActivity(new Intent(DrinkActivity.this, DrinkRecordActivity.class));
            }
        });
    }

    /**
     * Initializes the data for this activity.
     * <p>
     * This method fetches the drink count for the current day from the database. If there is no record
     * for the current day, a new record is created with an initial count of 0. The activity also displays
     * the current time and sets up a listener for the "Add" button to increment the drink count.
     * </p>
     */
    @Override
    protected void initData() {
        // Set the current time to the time field
        viewBinder.time.setText(SomeUtil.getTime());

        // Get the current month and day
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

        // Query the drink count for the current day
        Drink drink = Database.getDao().queryDrink(App.user.id, month, day);

        // If there is no drink record for today, create one with an initial count of 0
        if (drink == null) {
            viewBinder.count.setText("0");

            // Create a new Drink object and insert it into the database
            drink = new Drink();
            drink.day = day;
            drink.month = month;
            drink.userId = App.user.id;
            drink.count = 0;
            drink.time = SomeUtil.getTime();
            Database.getDao().insertDrink(drink);
        } else {
            // If a record exists, display the current count
            viewBinder.count.setText(drink.count + "");
        }

        // Listener for the "Add" button to increment the drink count
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Fetch the current drink record, increment the count, and update the database
                Drink drink = Database.getDao().queryDrink(App.user.id, month, day);
                drink.count = drink.count + 1;
                viewBinder.count.setText(drink.count + "");
                Database.getDao().updateDrink(drink);
            }
        });
    }
}
