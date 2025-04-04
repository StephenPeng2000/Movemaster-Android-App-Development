package edu.northeastern.movemaster.adapter;

import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.movemaster.util.GlideUtil;

/**
 * A custom {@link RecyclerView.ViewHolder} that provides utility methods for efficiently accessing
 * views and binding data such as text and images.
 * <p>
 * This class uses a {@link SparseArray} to cache views, which reduces the need for repeated calls to
 * {@link View#findViewById} and improves performance, especially in a RecyclerView.
 * </p>
 */
public class ViewHolder extends RecyclerView.ViewHolder {

    /**
     * A {@link SparseArray} to cache views for faster access.
     */
    private SparseArray<View> views;

    /**
     * Constructor that initializes the ViewHolder with the given item view.
     *
     * @param itemView The root view of the item layout.
     */
    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        views = new SparseArray<>();
    }

    /**
     * Returns the view with the specified ID. The view is cached in a {@link SparseArray}
     * to avoid repeated calls to {@link View#findViewById}.
     *
     * @param id The ID of the view to retrieve.
     * @param <T> The type of the view (e.g., {@link TextView}, {@link ImageView}).
     * @return The view with the specified ID.
     */
    public <T extends View> T getView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    /**
     * Sets the text on a {@link TextView} with the specified ID.
     * This method is a convenient wrapper around {@link TextView#setText(CharSequence)}.
     *
     * @param id   The ID of the {@link TextView}.
     * @param text The text to set on the {@link TextView}.
     * @return The current {@link ViewHolder} instance, allowing for method chaining.
     */
    public ViewHolder setText(int id, String text) {
        TextView view = getView(id);
        view.setText(text);
        return this;
    }

    /**
     * Loads an image from a URL into an {@link ImageView} with the specified ID.
     * This method uses {@link GlideUtil#load(ImageView, String)} to load the image asynchronously.
     *
     * @param id  The ID of the {@link ImageView}.
     * @param url The URL of the image to load.
     * @return The current {@link ViewHolder} instance, allowing for method chaining.
     */
    public ViewHolder loadImg(int id, String url) {
        ImageView view = getView(id);
        GlideUtil.load(view, url);
        return this;
    }

    /**
     * Loads an image from a resource ID into an {@link ImageView} with the specified ID.
     * This method uses {@link GlideUtil#load(ImageView, int)} to load the image asynchronously.
     *
     * @param id  The ID of the {@link ImageView}.
     * @param url The resource ID of the image to load.
     * @return The current {@link ViewHolder} instance, allowing for method chaining.
     */
    public ViewHolder loadImg(int id, int url) {
        ImageView view = getView(id);
        GlideUtil.load(view, url);
        return this;
    }
}
