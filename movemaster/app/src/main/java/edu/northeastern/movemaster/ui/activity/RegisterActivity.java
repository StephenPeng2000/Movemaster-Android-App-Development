package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.ActivityManager;
import edu.northeastern.movemaster.util.SomeUtil;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * register
 */
public class RegisterActivity extends AppCompatActivity {
    private View backBtn;
    private EditText phoneEdit;
    private EditText pwdEdit;
    private EditText nameEdit;
    private TextView registerBtn;
    private TextView titleTv;
    private CircleImageView headView;

    private boolean isRegister = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ActivityManager.getInstance().add(this);
        findViewsById();
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));
        initListener();
    }

    private void initListener() {
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        headView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 100);
            }
        });

    }

    private String fmFaceImgPath = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 100) {
                Uri uri = data.getData();
                if (uri != null) {
                    try {
                        headView.setImageURI(uri);
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
                        Log.d("TAG", "onActivityResult: " + file.getPath());
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    private void register() {
        String phone = phoneEdit.getText().toString().trim();
        String pwd = pwdEdit.getText().toString().trim();
        String name = nameEdit.getText().toString().trim();

        if (phone.isEmpty()) {
            phoneEdit.setError("Please enter the account number");
            return;
        }
        if (name.isEmpty()) {
            nameEdit.setError("Please enter a nickname");
            return;
        }
        if (fmFaceImgPath.isEmpty()) {
            Toast.makeText(this, "Please select an avatar to upload", Toast.LENGTH_SHORT).show();
            return;
        }
        User registeredUser = null;
        if (isRegister) {
            registeredUser = Database.getDao().queryUserByPhone(phone);
        }
        Log.d("TAG", "register: " + (registeredUser == null));
        if (registeredUser != null) {
            Toast.makeText(this, "The account has been registered.", Toast.LENGTH_SHORT).show();

        } else {
            if (pwd.isEmpty()) {
                pwdEdit.setError("Please enter password");
                return;
            }
            if (!SomeUtil.isTruePwd(pwd)) {
                pwdEdit.setError("Please enter the correct password format");
                Toast.makeText(this, "Password format must be a 6-digit number", Toast.LENGTH_SHORT).show();
                return;
            }
            User user = new User();
            user.phone = phone;
            user.password = pwd;
            user.nickname = name;
            user.avatar = fmFaceImgPath;
            Database.getDao().register(user);
            Toast.makeText(this, "Successful registration, please log in", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void findViewsById() {
        backBtn = findViewById(R.id.backBtn);
        phoneEdit = findViewById(R.id.phoneEdit);
        pwdEdit = findViewById(R.id.pwdEdit);
        nameEdit = findViewById(R.id.nameEdit);
        registerBtn = findViewById(R.id.registerBtn);
        titleTv = findViewById(R.id.titleTv);
        headView = findViewById(R.id.headView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }
}