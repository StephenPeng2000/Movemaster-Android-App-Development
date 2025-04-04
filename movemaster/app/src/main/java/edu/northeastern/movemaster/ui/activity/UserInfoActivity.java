package edu.northeastern.movemaster.ui.activity;

import androidx.appcompat.app.AlertDialog;

import android.content.Intent;
import android.net.Uri;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.base.BaseBindingActivity;
import edu.northeastern.movemaster.databinding.ActivityUserInfoBinding;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.event.OnIconChangeEvent;
import edu.northeastern.movemaster.util.ActivityManager;
import edu.northeastern.movemaster.util.GlideUtil;
import edu.northeastern.movemaster.util.SomeUtil;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * This activity manages the user's information, including updating the avatar, nickname, and password.
 */
public class UserInfoActivity extends BaseBindingActivity<ActivityUserInfoBinding> {

    // Path to the user's selected face image
    private String fmFaceImgPath = "";

    /**
     * Handles the result of the image selection for updating the user's avatar.
     *
     * @param requestCode The request code passed to startActivityForResult
     * @param resultCode  The result code returned by the child activity
     * @param data        The intent data containing the selected image URI
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        // Set the selected image to the ImageView
                        viewBinder.headView.setImageURI(uri);

                        // Save the selected image to the external cache directory
                        InputStream inputStream = getContentResolver().openInputStream(uri);
                        byte[] array = new byte[inputStream.available()];
                        inputStream.read(array);
                        String fileName = System.currentTimeMillis() + "_img_head.jpg";
                        File file = new File(getExternalCacheDir(), fileName);
                        FileOutputStream fos = new FileOutputStream(file);
                        fos.write(array);
                        fos.flush();
                        fos.close();
                        inputStream.close();

                        fmFaceImgPath = file.getPath();
                        Toast.makeText(this, "Update succeeded", Toast.LENGTH_SHORT).show();

                        // Update the user's avatar path
                        App.user.avatar = fmFaceImgPath;
                        Database.getDao().updateUserInfo(App.user);
                        initData();
                        EventBus.getDefault().post(new OnIconChangeEvent());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * Sets up click listeners for various UI elements in the activity.
     */
    @Override
    protected void initListener() {
        // Listener for changing the user's avatar
        viewBinder.headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

        // Listener for changing the user's nickname
        viewBinder.nameEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this).create();
                View dView = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                EditText editText = dView.findViewById(R.id.edit);
                TextView title = dView.findViewById(R.id.titleView);
                TextView sure = dView.findViewById(R.id.sure);
                TextView cancel = dView.findViewById(R.id.cancel);
                title.setText("Modify User Name");
                editText.setHint("Please enter a user name");

                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String name = editText.getText().toString().trim();
                        if (name.isEmpty()) {
                            toast("Username cannot be empty");
                            return;
                        }
                        App.user.nickname = name;
                        Database.getDao().updateUserInfo(App.user);
                        initData();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setView(dView);
                dialog.show();
            }
        });

        // Listener for changing the user's password
        viewBinder.pwdTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog dialog = new AlertDialog.Builder(UserInfoActivity.this).create();
                View dView = getLayoutInflater().inflate(R.layout.dialog_edit, null);
                EditText editText = dView.findViewById(R.id.edit);
                editText.setInputType(InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                editText.setHint("Please enter password");
                TextView title = dView.findViewById(R.id.titleView);
                TextView sure = dView.findViewById(R.id.sure);
                TextView cancel = dView.findViewById(R.id.cancel);
                title.setText("Change Password");

                sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String password = editText.getText().toString().trim();
                        if (!SomeUtil.isTruePwd(password)) {
                            return;
                        }
                        if (password.isEmpty()) {
                            toast("Password cannot be empty");
                            return;
                        }
                        App.user.password = password;
                        Database.getDao().updateUserInfo(App.user);
                        initData();
                        dialog.dismiss();
                    }
                });

                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                dialog.setView(dView);
                dialog.show();
            }
        });

        // Listener for logging out
        viewBinder.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityManager.getInstance().finish(MainActivity.class);
                finish();
                App.logout();
                startActivity(new Intent(UserInfoActivity.this, LoginActivity.class));
            }
        });
    }

    /**
     * Initializes user data by setting the user's current password, nickname, phone number, and avatar.
     */
    @Override
    protected void initData() {
        viewBinder.pwdTv.setText(App.user.password);
        viewBinder.nameEdit.setText(App.user.nickname);
        viewBinder.phoneTv.setText(App.user.phone);
        GlideUtil.loadFace(viewBinder.headView, App.user.avatar);
    }
}
