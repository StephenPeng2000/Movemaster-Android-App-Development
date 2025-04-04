package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.adapter.BindAdapter;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.bean.Post;
import edu.northeastern.movemaster.databinding.ActivitySendPostBinding;
import edu.northeastern.movemaster.databinding.ItemImageBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.GlideUtil;
import edu.northeastern.movemaster.util.SomeUtil;


/**
 * create post
 */
public class SendPostActivity extends BaseBindingActivity<ActivitySendPostBinding> {
    private BindAdapter<ItemImageBinding, String> imageAdapter = new BindAdapter<ItemImageBinding, String>() {
        @Override
        public ItemImageBinding createHolder(ViewGroup parent) {
            return ItemImageBinding.inflate(getLayoutInflater(), parent, false);
        }

        @Override
        public void bind(ItemImageBinding itemImageBinding, String s, int position) {
            if (s.equals("Add")) {
                itemImageBinding.getRoot().setImageResource(R.drawable.ic_add);
            } else {
                GlideUtil.load(itemImageBinding.getRoot(), s);
            }
            itemImageBinding.getRoot().setOnClickListener(view -> {
                if (position == getData().size() - 1 && getData().size() < 4) {
                    startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"), 100);
                }
            });

        }
    };

    @Override
    protected void initListener() {
        viewBinder.send.setOnClickListener(view -> {
            String title = viewBinder.etTitle.getText().toString().trim();
            String content = viewBinder.etContent.getText().toString().trim();
            if (title.isEmpty()) {
                toast("Title is empty");
                return;
            }
            if (content.isEmpty()) {
                toast("Content is empty");
                return;
            }
            Post post = new Post();
            post.content = content;
            post.title = title;
            imageAdapter.getData().remove(imageAdapter.getData().size() - 1);
            post.image = imageAdapter.getData();
            post.userid = App.user.id;
            post.time = SomeUtil.getTime();
            Database.getDao().post(post);
            finish();
        });
    }

    @Override
    protected void initData() {
        imageAdapter.getData().add("Add");
        viewBinder.rvIamge.setAdapter(imageAdapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String imagePath = SomeUtil.getImagePath(this, data.getData());
            imageAdapter.getData().add(0, imagePath);
            imageAdapter.notifyDataSetChanged();
        }
    }
}