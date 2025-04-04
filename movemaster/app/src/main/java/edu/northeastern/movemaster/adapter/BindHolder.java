package edu.northeastern.movemaster.adapter;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * A {@link RecyclerView.ViewHolder} that holds a reference to a {@link ViewBinding}
 * instance for an individual item view in the {@link RecyclerView}.
 * <p>
 * This class allows you to easily access and manipulate the views in each item of the RecyclerView
 * through the provided ViewBinding instance.
 * </p>
 *
 * @param <VB> The type of ViewBinding that corresponds to the item view.
 */
public class BindHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {

    /**
     * The ViewBinding instance that binds the item view.
     */
    private VB vb;

    /**
     * Constructor that initializes the ViewHolder with the provided ViewBinding.
     * The root view from the ViewBinding is passed to the superclass constructor.
     *
     * @param vb The ViewBinding instance that binds the item view.
     */
    public BindHolder(VB vb) {
        super(vb.getRoot());
        this.vb = vb;
    }

    /**
     * Returns the ViewBinding instance associated with the item view.
     *
     * @return The ViewBinding instance.
     */
    public VB getVb() {
        return vb;
    }
}
