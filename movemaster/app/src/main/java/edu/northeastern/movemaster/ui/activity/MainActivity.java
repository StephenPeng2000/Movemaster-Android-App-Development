package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.MainIcon;
import edu.northeastern.movemaster.databinding.ActivityMainBinding;
import edu.northeastern.movemaster.event.OnIconChangeEvent;
import edu.northeastern.movemaster.service.NotifyService;
import edu.northeastern.movemaster.util.GlideUtil;

import com.jaeger.library.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

/**
 * MainActivity serves as the entry point of the application, providing access to various health-related activities.
 */
public class MainActivity extends BaseBindingActivity<ActivityMainBinding> {

    // List to store the main icons representing different activities
    private List<MainIcon> mainIcons = new ArrayList<>();

    // Adapter to bind the main icons to the RecyclerView
    private CommonAdapter<MainIcon> adapter = new CommonAdapter<MainIcon>(R.layout.item_main, mainIcons) {
        @Override
        public void bind(ViewHolder holder, MainIcon mainIcon, int position) {
            ImageView icon = holder.getView(R.id.icon);
            TextView name = holder.getView(R.id.name);
            icon.setImageResource(mainIcon.icon);
            name.setText(mainIcon.name);

            // Set click listener for each item in the RecyclerView
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position <= 3) {
                        // Open CountDownActivity for the first four positions
                        startActivity(new Intent(MainActivity.this, CountDownActivity.class).putExtra("type", position));
                    } else {
                        if (position == 4) {
                            // Open HeartRateActivity for the fifth position
                            startActivity(new Intent(MainActivity.this, HeartRateActivity.class));
                        } else {
                            // Open BloodPressureActivity for the sixth position
                            startActivity(new Intent(MainActivity.this, BloodPressureActivity.class));
                        }
                    }
                }
            });
        }
    };

    /**
     * Initializes click listeners for various UI elements.
     */
    @Override
    protected void initListener() {
        viewBinder.addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddSportRecordActivity.class));
            }
        });
        viewBinder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });
        viewBinder.sportRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SportRecordActivity.class));
            }
        });
        viewBinder.bmi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, BMIActivity.class));
            }
        });
        viewBinder.notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, NotifyListActivity.class));
            }
        });
        viewBinder.face.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
            }
        });
        viewBinder.drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, DrinkActivity.class));
            }
        });
    }

    /**
     * Initializes data for the activity, sets up icons, and manages the status bar.
     */
    @Override
    protected void initData() {
        // Register this activity with EventBus to receive events
        EventBus.getDefault().register(this);

        // Initialize the main icons for the RecyclerView
        initMainIcon();

        // Set up the status bar appearance
        StatusBarUtil.setColorNoTranslucent(this, Color.parseColor("#ffffff"));
        StatusBarUtil.setLightMode(this);
        StatusBarUtil.setTransparentForImageView(this, viewBinder.headBar);

        // Check if the user is logged in; if not, redirect to the LoginActivity
        if (!App.isLogin()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        // Load the user's avatar using Glide
        GlideUtil.loadFace(viewBinder.face, App.user.avatar);

        // Start the NotifyService to handle notifications
        startService(new Intent(this, NotifyService.class));
    }

    /**
     * Event handler to update the user's avatar when the icon changes.
     *
     * @param event The event indicating the icon change
     */
    @Subscribe
    public void onIconChange(OnIconChangeEvent event) {
        GlideUtil.loadFace(viewBinder.face, App.user.avatar);
    }

    /**
     * Initializes the list of main icons representing different activities.
     */
    private void initMainIcon() {
        mainIcons.add(new MainIcon("Running", R.drawable.ic_run));
        mainIcons.add(new MainIcon("Ride", R.drawable.ic_qixing));
        mainIcons.add(new MainIcon("Yoga", R.drawable.yoga));
        mainIcons.add(new MainIcon("Dumbbell", R.drawable.ic_yaling));
        mainIcons.add(new MainIcon("Heart rate", R.drawable.ic_xinbglv));
        mainIcons.add(new MainIcon("Blood Pressure", R.drawable.ic_xueya));
        viewBinder.recycler.setAdapter(adapter);
    }
}
