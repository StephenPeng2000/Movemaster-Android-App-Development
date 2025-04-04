package edu.northeastern.movemaster.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import edu.northeastern.movemaster.bean.PostComment;
import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.databinding.ItemCommentImageBinding;
import edu.northeastern.movemaster.databinding.ItemCommentTextBinding;
import edu.northeastern.movemaster.databinding.ItemHeaderBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.GlideUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RecyclerView.Adapter} for displaying a list of comments in different view types.
 * This adapter handles multiple types of comment items, such as text-based comments and image-based comments,
 * and it also includes a header at the top of the list.
 * <p>
 * The adapter binds data to the views using different ViewBinding types for each view type.
 * It also handles user data like avatars and nicknames using {@link GlideUtil} to load images.
 * </p>
 */
public abstract class CommentAdapter extends RecyclerView.Adapter<BindHolder<?>> {

    /**
     * The list of comments to be displayed in the RecyclerView.
     */
    private List<PostComment> comments = new ArrayList<>();

    /**
     * Returns the list of comments in the adapter.
     *
     * @return The list of comments.
     */
    public List<PostComment> getComments() {
        return comments;
    }

    /**
     * Returns the total number of items (including the header) in the adapter.
     *
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return comments.size() + 1;  // +1 for the header
    }

    /**
     * Creates and returns a new {@link BindHolder} based on the view type.
     * Different view types are used for text comments, image comments, and the header.
     *
     * @param parent   The parent view that the new view will be attached to.
     * @param viewType The type of the view to be created (text, image, or header).
     * @return A new {@link BindHolder} instance containing the appropriate ViewBinding.
     */
    @NonNull
    @Override
    public BindHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new BindHolder<>(ItemCommentImageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else if (viewType == 2) {
            return new BindHolder<>(ItemCommentTextBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        } else {
            return new BindHolder<>(ItemHeaderBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }
    }

    /**
     * Binds the data to the appropriate view based on the position of the item.
     * It binds the header data at position 0, and binds either image or text comments for other positions.
     *
     * @param holder   The ViewHolder containing the ViewBinding for the item view.
     * @param position The position of the item in the list.
     */
    @Override
    public void onBindViewHolder(@NonNull BindHolder holder, int position) {
        if (position == 0) {
            bindHeader(((BindHolder<ItemHeaderBinding>) holder).getVb());
        } else {
            int realPos = position - 1;  // Offset by 1 to skip the header
            PostComment comment = comments.get(realPos);
            int itemViewType = getItemViewType(position);
            User user = Database.getDao().queryUserById(comment.userid);
            if (itemViewType == 1) {
                bindImage(((BindHolder<ItemCommentImageBinding>) holder).getVb(), comment, user);
            } else if (itemViewType == 2) {
                bindText(((BindHolder<ItemCommentTextBinding>) holder).getVb(), comment, user);
            }
        }
    }

    /**
     * Binds the header data to the header view.
     *
     * @param vb The ViewBinding instance for the header view.
     */
    protected abstract void bindHeader(ItemHeaderBinding vb);

    /**
     * Binds the text-based comment data to the text comment view.
     *
     * @param holder  The ViewBinding instance for the text comment view.
     * @param comment The comment data to be displayed.
     * @param user    The user data to be displayed (e.g., avatar and nickname).
     */
    private void bindText(ItemCommentTextBinding holder, PostComment comment, User user) {
        GlideUtil.load(holder.ivFace, user.avatar);  // Load user avatar
        holder.tvUpName.setText(user.nickname);      // Set user nickname
        holder.tvUpTime.setText(comment.time);       // Set comment timestamp
        holder.text.setText(comment.content);        // Set comment content
    }

    /**
     * Binds the image-based comment data to the image comment view.
     *
     * @param holder  The ViewBinding instance for the image comment view.
     * @param comment The comment data to be displayed.
     * @param user    The user data to be displayed (e.g., avatar and nickname).
     */
    private void bindImage(ItemCommentImageBinding holder, PostComment comment, User user) {
        GlideUtil.load(holder.ivFace, user.avatar);  // Load user avatar
        holder.tvUpName.setText(user.nickname);      // Set user nickname
        holder.tvUpTime.setText(comment.time);       // Set comment timestamp
        GlideUtil.load(holder.image, comment.image);  // Load the image in the comment
    }

    /**
     * Returns the type of view to be used for a given item position.
     * The header view has a view type of 0, while text and image comments have types 1 and 2, respectively.
     *
     * @param position The position of the item in the list.
     * @return The view type for the item.
     */
    @Override
    public int getItemViewType(int position) {
        if (position == 0) return 0;  // Header view
        return comments.get(position - 1).type;  // Return the type of the comment
    }
}
