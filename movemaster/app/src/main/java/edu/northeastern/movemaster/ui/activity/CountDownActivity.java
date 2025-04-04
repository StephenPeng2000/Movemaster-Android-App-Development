package edu.northeastern.movemaster.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.databinding.ActivityCountDownBinding;

/**
 * Activity for countdown animation before starting a sport activity.
 * <p>
 * This activity shows a countdown from 3 to 1 with scaling animation. Once the countdown ends,
 * it transitions to the {@link SportActivity} to start the sport session.
 * </p>
 */
public class CountDownActivity extends BaseBindingActivity<ActivityCountDownBinding> {

    /**
     * Initializes the listeners for this activity.
     * <p>
     * Currently, no specific listeners are needed, but this method is overridden for consistency.
     * </p>
     */
    @Override
    protected void initListener() {
        // No listeners are initialized in this method
    }

    private ValueAnimator animator;
    private int repeatCount = 3;

    /**
     * Initializes the data and sets up the countdown animation.
     * <p>
     * This method sets up a scaling animation for the countdown number. The countdown starts
     * from 3 and decreases to 1. After the animation ends, it starts the {@link SportActivity}.
     * </p>
     */
    @Override
    protected void initData() {
        // Get the type passed through the Intent
        int type = getIntent().getIntExtra("type", 0);

        // Set up the scale animation for the countdown number
        PropertyValuesHolder valuesHolderX = PropertyValuesHolder.ofFloat("scaleX", 2f);
        PropertyValuesHolder valuesHolderY = PropertyValuesHolder.ofFloat("scaleY", 2f);
        animator = ObjectAnimator.ofPropertyValuesHolder(viewBinder.numberTv, valuesHolderX, valuesHolderY);

        // Set the duration and repeat count for the animation
        animator.setDuration(800);
        animator.setRepeatCount(2);

        // Add a listener to handle animation events
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isOnPause) return;

                // Start the SportActivity when the animation ends
                startActivity(new Intent(CountDownActivity.this, SportActivity.class).putExtra("type", type));
                finish();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                // Update the countdown text each time the animation repeats
                repeatCount -= 1;
                viewBinder.numberTv.setText(repeatCount + "");
            }
        });

        // Start the countdown after a slight delay
        handler.sendEmptyMessageDelayed(1, 300);
    }

    /**
     * Handler to start the countdown animation after a short delay.
     */
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            animator.start();
        }
    };

    private boolean isDestroy = false;
    private boolean isOnPause = false;

    /**
     * Called when the activity is paused.
     * <p>
     * This method sets a flag to indicate that the activity is paused. This is used to prevent
     * the animation from continuing if the activity is not in the foreground.
     * </p>
     */
    @Override
    protected void onPause() {
        super.onPause();
        isOnPause = true;
    }

    /**
     * Called when the activity is destroyed.
     * <p>
     * This method cancels the animation and sets a flag to indicate the activity has been destroyed.
     * </p>
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        animator.cancel();
        isDestroy = true;
    }
}
