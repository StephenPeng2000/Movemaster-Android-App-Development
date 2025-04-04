package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;



import java.util.List;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.adapter.BindAdapter;
import edu.northeastern.movemaster.adapter.CommentAdapter;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Post;
import edu.northeastern.movemaster.bean.PostComment;
import edu.northeastern.movemaster.bean.PostLike;
import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.databinding.ActivityPostDeatilBinding;
import edu.northeastern.movemaster.databinding.ItemHeaderBinding;
import edu.northeastern.movemaster.databinding.ItemImageBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.GlideUtil;
import edu.northeastern.movemaster.util.SomeUtil;

/**
 * post detailed info
 */
public class PostDetailActivity extends BaseBindingActivity<ActivityPostDeatilBinding> {
    private CommentAdapter adapter = new CommentAdapter() {
        @Override
        protected void bindHeader(ItemHeaderBinding vb) {
            User user = Database.getDao().queryUserById(post.userid);

            List<PostLike> postLikes = Database.getDao().queryLikeByPost(post.id);
            GlideUtil.load(vb.ivFace, user.avatar);
            vb.tvUpTime.setText(post.time);
            vb.tvUpName.setText(user.nickname);
            vb.title.setText(post.title);
            vb.content.setText(post.content);
            vb.tvLike.setText("likes(" + postLikes.size() + ")");
            vb.tvLike.setOnClickListener(view -> {
                PostLike postLike = Database.getDao().queryLikeByUser(App.user.id, post.id);
                if (postLike == null) {
                    postLike = new PostLike();
                    postLike.postId = post.id;
                    postLike.userid = App.user.id;
                    Database.getDao().doLike(postLike);
                    List<PostLike> size = Database.getDao().queryLikeByPost(post.id);
                    vb.tvLike.setText("likes(" + (size.size()) + ")");
                    Toast.makeText(PostDetailActivity.this, "I like success.", Toast.LENGTH_SHORT).show();
                } else {
                    Database.getDao().doNotLike(postLike);
                    List<PostLike> size = Database.getDao().queryLikeByPost(post.id);
                    vb.tvLike.setText("likes(" + (size.size()) + ")");
                    Toast.makeText(PostDetailActivity.this, "Cancel Like", Toast.LENGTH_SHORT).show();
                }

            });
            if (!post.image.isEmpty()) {
                imageAdapter.getData().addAll(post.image);
                vb.imageRecycler.setAdapter(imageAdapter);
            }
        }
    };
    private BindAdapter<ItemImageBinding, String> imageAdapter = new BindAdapter<ItemImageBinding, String>() {
        @Override
        public ItemImageBinding createHolder(ViewGroup parent) {
            return ItemImageBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemImageBinding itemImageBinding, String s, int position) {
            GlideUtil.load(itemImageBinding.getRoot(), s);
        }
    };

    @Override
    protected void initListener() {
        viewBinder.ivSend.setOnClickListener(view -> {
            startActivityForResult(new Intent(Intent.ACTION_GET_CONTENT).setType("image/*"), 100);
        });
        viewBinder.sendBtn.setOnClickListener(view -> {

            String content = viewBinder.commentEdit.getText().toString().trim();
            PostComment postComment = new PostComment();
            postComment.type = 2;
            postComment.content = content;
            postComment.postId = post.id;
            postComment.userid = App.user.id;
            postComment.time = SomeUtil.getTime();
            adapter.getComments().add(postComment);
            adapter.notifyDataSetChanged();
            Database.getDao().sendComment(postComment);
            viewBinder.commentEdit.setText("");

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String imagePath = SomeUtil.getImagePath(this, uri);
            PostComment postComment = new PostComment();
            postComment.type = 1;
            postComment.image = imagePath;
            postComment.postId = post.id;
            postComment.userid = App.user.id;
            postComment.time = SomeUtil.getTime();
            adapter.getComments().add(postComment);
            adapter.notifyDataSetChanged();
            Database.getDao().sendComment(postComment);


        }
    }

    private Post post;


    @Override
    protected void initData() {
        post = (Post) getIntent().getSerializableExtra("post");
        List<PostComment> postComments = Database.getDao().queryCommentByPost(post.id);
        adapter.getComments().addAll(postComments);
        viewBinder.commentRecycler.addItemDecoration(new DividerItemDecoration(this, LinearLayout.VERTICAL));
        viewBinder.commentRecycler.setAdapter(adapter);
    }
}