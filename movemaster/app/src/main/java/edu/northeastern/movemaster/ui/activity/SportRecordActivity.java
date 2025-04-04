package edu.northeastern.movemaster.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.CommonAdapter;
import edu.northeastern.movemaster.adapter.ViewHolder;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.SportRecord;
import edu.northeastern.movemaster.databinding.ActivitySportRecordctivityBinding;
import edu.northeastern.movemaster.db.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * SportRecordActivity handles the display and management of the user's past sport records.
 * It allows the user to view, modify, or delete their past activities.
 */
public class SportRecordActivity extends BaseBindingActivity<ActivitySportRecordctivityBinding> {

    // List to hold sport records
    private List<SportRecord> date = new ArrayList<>();

    // Adapter to display sport records in the RecyclerView
    private CommonAdapter<SportRecord> adapter = new CommonAdapter<SportRecord>(R.layout.item_sport_record, date) {

        @Override
        public void bind(ViewHolder holder, SportRecord sportRecord, int position) {
            // Load sport record icon and set text for each item in the RecyclerView
            holder.loadImg(R.id.icon, sportRecord.icon);
            holder.setText(R.id.name, "Type of exercise：" + sportRecord.name);
            holder.setText(R.id.duration, "Sport duration：" + sportRecord.duration);
            holder.setText(R.id.time, sportRecord.time);

            // Handle item click to show options for modification or deletion
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(SportRecordActivity.this)
                            .setItems(new CharSequence[]{"Modification", "Delete", "Return"},
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // Handle user selection
                                            switch (i) {
                                                case 0:
                                                    // Open AddSportRecordActivity for modification
                                                    startActivityForResult(new Intent(SportRecordActivity.this,
                                                            AddSportRecordActivity.class).putExtra("record", sportRecord), 100);
                                                    break;
                                                case 1:
                                                    // Delete the selected sport record
                                                    Database.getDao().removeSportRecord(sportRecord);
                                                    date.remove(position);
                                                    adapter.notifyItemRemoved(position);
                                                    adapter.notifyItemRangeChanged(position, date.size() - position);
                                                    break;
                                                case 2:
                                                    // Do nothing and return
                                                    break;
                                            }
                                        }
                                    }).show();
                }
            });
        }
    };

    @Override
    protected void initListener() {
        // No listeners to initialize at this moment
    }

    @Override
    protected void initData() {
        // Fetch all sport records for the current user from the database
        date.addAll(Database.getDao().querySportRecordByUserId(App.user.id));

        // Reverse the list so that the most recent records are at the top
        Collections.reverse(date);

        // Set the adapter to the RecyclerView
        viewBinder.recycler.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is OK, meaning the sport record was modified or added
        if (resultCode == RESULT_OK) {
            // Clear the existing list and reload the data from the database
            date.clear();
            date.addAll(Database.getDao().querySportRecordByUserId(App.user.id));

            // Reverse the list to show the most recent records first
            Collections.reverse(date);

            // Notify the adapter to update the RecyclerView
            adapter.notifyDataSetChanged();
        }
    }
}
