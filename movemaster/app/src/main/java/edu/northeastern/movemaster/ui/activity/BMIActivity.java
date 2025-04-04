package edu.northeastern.movemaster.ui.activity;

import android.view.View;

import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.databinding.ActivityBmiactivityBinding;

import java.text.DecimalFormat;

/**
 * Activity for calculating and displaying the Body Mass Index (BMI).
 * <p>
 * This activity allows the user to input their weight and height, calculates their BMI,
 * and displays the result along with the corresponding BMI category (underweight, healthy weight, overweight, or obese).
 * </p>
 */
public class BMIActivity extends BaseBindingActivity<ActivityBmiactivityBinding> {

    /**
     * Initializes the listeners for the views in this activity.
     * <p>
     * This method sets up the click listener for the "Add" button. When clicked, it will
     * calculate the BMI based on the inputted weight and height, and display the result on the screen.
     * </p>
     */
    @Override
    protected void initListener() {
        viewBinder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get weight and height from input fields
                String w = viewBinder.wEdit.getText().toString();
                String h = viewBinder.hEdit.getText().toString();

                // If weight or height is empty, do nothing
                if (w.isEmpty() || h.isEmpty()) return;

                // Convert weight and height to float values
                float weight = Float.parseFloat(w);
                float height = Float.parseFloat(h);

                // Calculate BMI (BMI = weight / height^2)
                float bmi = weight / (height / 100 * height / 100);

                // Format the result to one decimal place
                String result = "";
                DecimalFormat format = new DecimalFormat(".0");

                // Determine BMI category based on the calculated BMI value
                if (bmi < 18) {
                    result = "Your BMI is " + format.format(bmi) + "\n Belong to light weight";
                } else if (bmi >= 18.5 && bmi < 24) {
                    result = "Your BMI is " + format.format(bmi) + "\n Belong to health";
                } else if (bmi >= 24 && bmi < 28) {
                    result = "Your BMI is " + format.format(bmi) + "\n Belong to overweight";
                } else if (bmi >= 28) {
                    result = "Your BMI is " + format.format(bmi) + "\n Belong to obesity";
                }

                // Display the result in the result text view
                viewBinder.resultTv.setVisibility(View.VISIBLE);
                viewBinder.resultTv.setText(result);
            }
        });
    }

    /**
     * Initializes the data for the activity.
     * <p>
     * Currently, no data initialization is needed in this activity.
     * </p>
     */
    @Override
    protected void initData() {
        // No data initialization needed for this activity
    }
}
