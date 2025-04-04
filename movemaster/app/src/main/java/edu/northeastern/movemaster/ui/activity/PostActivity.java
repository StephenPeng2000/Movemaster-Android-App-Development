package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.List;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.adapter.BindAdapter;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Post;
import edu.northeastern.movemaster.bean.PostComment;
import edu.northeastern.movemaster.bean.PostLike;
import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.databinding.ActivityPostSearchBinding;
import edu.northeastern.movemaster.databinding.ItemImageBinding;
import edu.northeastern.movemaster.databinding.ItemPostBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.GlideUtil;
import edu.northeastern.movemaster.util.SomeUtil;

public class PostActivity extends BaseBindingActivity<ActivityPostSearchBinding> {
    private BindAdapter<ItemPostBinding, Post> adapter = new BindAdapter<ItemPostBinding, Post>() {
        @Override
        public ItemPostBinding createHolder(ViewGroup parent) {
            return ItemPostBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemPostBinding itemPostBinding, Post post, int position) {
            itemPostBinding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(PostActivity.this, PostDetailActivity.class);
                intent.putExtra("post", post);
                startActivity(intent);
            });
            itemPostBinding.llPostAction.setVisibility(View.VISIBLE);
            itemPostBinding.tvDelete.setVisibility(View.VISIBLE);
            itemPostBinding.tvType.setVisibility(View.GONE);
            itemPostBinding.tvDelete.setVisibility(App.user.id == post.userid ? View.VISIBLE : View.GONE);
            itemPostBinding.tvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Database.getDao().deletePost(post);
                    getData().remove(post);
                    notifyDataSetChanged();
                }
            });
            User user = Database.getDao().queryUserById(post.userid);
            List<PostComment> postComments = Database.getDao().queryCommentByPost(post.id);
            List<PostLike> postLikes = Database.getDao().queryLikeByPost(post.id);
            GlideUtil.load(itemPostBinding.ivFace, user.avatar);
            itemPostBinding.tvUpTime.setText(post.time);
            itemPostBinding.tvUpName.setText(user.nickname);
            itemPostBinding.title.setText(post.title);
            itemPostBinding.tvComment.setText("Comments(" + postComments.size() + ")");
            itemPostBinding.tvLike.setText("likes(" + postLikes.size() + ")");
            itemPostBinding.tvLike.setOnClickListener(view -> {
                PostLike postLike = Database.getDao().queryLikeByUser(App.user.id, post.id);
                if (postLike == null) {
                    postLike = new PostLike();
                    postLike.postId = post.id;
                    postLike.userid = App.user.id;
                    Database.getDao().doLike(postLike);
                    Toast.makeText(PostActivity.this, "like success.", Toast.LENGTH_SHORT).show();
                } else {
                    Database.getDao().doNotLike(postLike);
                    Toast.makeText(PostActivity.this, "Cancel Like", Toast.LENGTH_SHORT).show();
                }
                notifyItemChanged(position);
            });
            if (!post.image.isEmpty()) {
                BindAdapter<edu.northeastern.movemaster.databinding.ItemImageBinding, String> imageAdapter = new BindAdapter<edu.northeastern.movemaster.databinding.ItemImageBinding, String>() {
                    @Override
                    public ItemImageBinding createHolder(ViewGroup parent) {
                        return ItemImageBinding.inflate(getLayoutInflater(), parent, false);
                    }

                    @Override
                    public void bind(ItemImageBinding itemImageBinding, String s, int position) {
                        itemImageBinding.getRoot().getLayoutParams().height = (SomeUtil.getDisplayMetrics().widthPixels - SomeUtil.dp2px(20)) / 3;
                        GlideUtil.load(itemImageBinding.getRoot(), s);
                    }
                };
                imageAdapter.getData().addAll(post.image);
                itemPostBinding.imageRecycler.setAdapter(imageAdapter);
            }
        }
    };


    @Override
    protected void initListener() {
        viewBinder.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PostActivity.this, SendPostActivity.class);
                startActivity(intent);
            }
        });

        viewBinder.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                adapter.getData().clear();
                adapter.getData().addAll(Database.getDao().searchPost(s.toString()));
                adapter.notifyDataSetChanged();
            }
        });
        viewBinder.etSearch.setText("");


    }

    @Override
    protected void initData() {
        viewBinder.postRecycler.setAdapter(adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        viewBinder.etSearch.setText("");
    }
}