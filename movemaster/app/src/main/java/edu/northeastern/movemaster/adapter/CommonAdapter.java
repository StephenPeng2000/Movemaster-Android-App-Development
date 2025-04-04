package edu.northeastern.movemaster.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * A generic adapter for RecyclerView that can handle different data types and layouts.
 * <p>
 * This adapter allows you to create a RecyclerView adapter by providing a layout resource ID
 * and a list of data items. It abstracts the process of binding data to the views
 * and provides a common structure for various types of data.
 * </p>
 *
 * @param <T> The type of data to be displayed in each item.
 */
public abstract class CommonAdapter<T> extends RecyclerView.Adapter<ViewHolder> {

    /**
     * The layout resource ID for the item view.
     */
    private int layoutId;

    /**
     * The list of data items to be displayed in the RecyclerView.
     */
    private List<T> data;

    /**
     * Constructor to create a new adapter with a specified layout and data.
     *
     * @param layoutId The layout resource ID for the item view.
     * @param data     The list of data items to be displayed in the RecyclerView.
     */
    public CommonAdapter(int layoutId, List<T> data) {
        this.layoutId = layoutId;
        this.data = data;
    }

    /**
     * Creates a new {@link ViewHolder} instance for the given view type.
     * The {@link ViewHolder} is used to hold the inflated item view.
     *
     * @param parent   The parent view that the new view will be attached to.
     * @param viewType The type of view to create.
     * @return A new {@link ViewHolder} instance.
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new ViewHolder(inflate);
    }

    /**
     * Binds the data item to the corresponding item view. This method is called for each item
     * in the RecyclerView to populate the view with the relevant data.
     *
     * @param holder   The {@link ViewHolder} containing the item view.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        T t = data.get(position);
        bind(holder, t, position);
    }

    /**
     * Binds the data to the {@link ViewHolder}. This method must be implemented
     * by subclasses to specify how the data is bound to the item view.
     *
     * @param holder   The {@link ViewHolder} containing the item view.
     * @param t        The data item to be displayed.
     * @param position The position of the item in the list.
     */
    public abstract void bind(ViewHolder holder, T t, int position);

    /**
     * Returns the total number of items in the data list.
     *
     * @return The number of items in the data list.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
}
