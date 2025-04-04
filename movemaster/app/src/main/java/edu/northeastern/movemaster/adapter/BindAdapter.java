package edu.northeastern.movemaster.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * A generic adapter for a RecyclerView that binds data to a view using ViewBinding.
 * <p>
 * This adapter takes a generic type parameter {@code VB} which extends {@link ViewBinding}
 * and a generic type {@code Data} which represents the data to be displayed in each item of the list.
 * It provides a generic way to bind a list of data to RecyclerView items.
 * </p>
 *
 * @param <VB>   The type of ViewBinding for the item view.
 * @param <Data> The type of data to be bound to the view.
 */
public abstract class BindAdapter<VB extends ViewBinding, Data> extends RecyclerView.Adapter<BindHolder<VB>> {

    /**
     * The list of data items to be displayed in the RecyclerView.
     */
    private List<Data> data = new ArrayList<>();

    /**
     * Returns the list of data items in the adapter.
     *
     * @return The list of data items.
     */
    public List<Data> getData() {
        return data;
    }

    /**
     * Creates and returns a {@link BindHolder} which contains the ViewBinding for a view
     * that will represent each item in the RecyclerView.
     *
     * @param parent   The parent view that the new view will be attached to.
     * @param viewType The type of view to create.
     * @return A new {@link BindHolder} instance containing the ViewBinding for the item view.
     */
    @NonNull
    @Override
    public BindHolder<VB> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BindHolder<>(createHolder(parent));
    }

    /**
     * Creates the ViewBinding for an item view. This method must be implemented
     * by subclasses to provide the specific ViewBinding for the item view.
     *
     * @param parent The parent view that the new view will be attached to.
     * @return The ViewBinding instance for the item view.
     */
    public abstract VB createHolder(ViewGroup parent);

    /**
     * Binds the data to the ViewBinding for the given item in the RecyclerView.
     * This method must be implemented by subclasses to define how the data is
     * bound to the view.
     *
     * @param **vb       The ViewBinding for the item view.
     * @param **data     The data to be bound to the item view.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull BindHolder<VB> holder, int position) {
        Data d = data.get(position);
        bind(holder.getVb(), d, position);
    }

    /**
     * Binds the data to the specified ViewBinding. Subclasses must override this
     * method to define how the data is bound to the ViewBinding for a particular
     * item view.
     *
     * @param vb       The ViewBinding for the item view.
     * @param data     The data to be bound to the item view.
     * @param position The position of the item in the list.
     */
    public abstract void bind(VB vb, Data data, int position);

    /**
     * Returns the number of items in the data set.
     *
     * @return The size of the data list.
     */
    @Override
    public int getItemCount() {
        return data.size();
    }
}
