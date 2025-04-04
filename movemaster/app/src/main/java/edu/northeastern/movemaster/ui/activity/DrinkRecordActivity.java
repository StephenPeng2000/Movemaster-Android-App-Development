package edu.northeastern.movemaster.ui.activity;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Drink;
import edu.northeastern.movemaster.databinding.ActivitySportRecordctivityBinding;
import edu.northeastern.movemaster.db.Database;
import java.util.ArrayList;
import java.util.List;

/**
 * This activity displays the user's water consumption records.
 */
public class DrinkRecordActivity extends BaseBindingActivity<ActivitySportRecordctivityBinding> {

    // List to store Drink records
    private List<Drink> drinks = new ArrayList<>();

    // Adapter to display Drink records in a RecyclerView
    private CommonAdapter<Drink> adapter = new CommonAdapter<Drink>(R.layout.item_drink, drinks) {
        @Override
        public void bind(ViewHolder holder, Drink drink, int position) {
            // Binding data to the views in the item layout
            holder.setText(R.id.time, drink.time);  // Set time of drink record
            holder.setText(R.id.count, "Drink water " + drink.count + " Times"); // Set drink count
        }
    };

    @Override
    protected void initListener() {
        // No listeners needed in this activity for now
    }

    @Override
    protected void initData() {
        // Retrieve drink records from the database and add them to the list
        drinks.addAll(Database.getDao().queryDrink(App.user.id));
        // Set up RecyclerView with the adapter
        viewBinder.recycler.setAdapter(adapter);
    }
}
