package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;

import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.SportType;
import edu.northeastern.movemaster.databinding.ActivitySportTypeBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * SportTypeActivity displays a list of sport types to the user.
 * Upon selecting a sport type, the selected type is passed back to the previous activity.
 */
public class SportTypeActivity extends BaseBindingActivity<ActivitySportTypeBinding> {

    // List of sport types to be displayed
    private List<SportType> types = new ArrayList<>();

    // Adapter for binding sport types to the RecyclerView
    private CommonAdapter<SportType> adapter = new CommonAdapter<SportType>(R.layout.item_sport_type, types) {
        @Override
        public void bind(ViewHolder holder, SportType type, int position) {
            // Bind image and name to the RecyclerView items
            holder.loadImg(R.id.icon, type.icon);
            holder.setText(R.id.name, type.name);

            // Set item click listener
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Log the selected sport type for debugging purposes
                    Log.d("SportTypeActivity", "onClick: " + type);

                    // Return the selected sport type to the previous activity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("type", type);  // Pass the selected type back
                    setResult(RESULT_OK, resultIntent);   // Set result as OK
                    finish();  // Finish the current activity and return
                }
            });
        }
    };

    @Override
    protected void initListener() {
        // No listeners to initialize for now
    }

    @Override
    protected void initData() {
        // Populate the list of sport types with sample data
        types.add(new SportType("Basketball", R.drawable.ic_lanqiu));
        types.add(new SportType("Table Tennis", R.drawable.ic_ppq));
        types.add(new SportType("Dumbbell", R.drawable.ic_yalin));

        // Set the adapter to display the sport types in the RecyclerView
        viewBinder.recycler.setAdapter(adapter);
    }
}
