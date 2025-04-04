package edu.northeastern.movemaster.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.northeastern.movemaster.App;
import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.bean.User;
import edu.northeastern.movemaster.db.Database;
import edu.northeastern.movemaster.util.ActivityManager;
import edu.northeastern.movemaster.util.SomeUtil;
import com.jaeger.library.StatusBarUtil;

/**
 * LoginActivity allows users to log in to the app by entering a phone number and password.
 */
public class LoginActivity extends AppCompatActivity {

    private EditText phoneEdit;
    private EditText pwdEdit;
    private TextView loginBtn;
    private ImageView backBtn;
    private View qqLogin;
    private View wxLogin;
    private TextView goRegisterBtn;
    private int loginType = 0;  // Represents the login type (0 - standard, 1 - QQ, 2 - WeChat)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Add the current activity to the ActivityManager
        ActivityManager.getInstance().add(this);

        // Initialize UI components
        findViewsById();

        // Set status bar color
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));

        // Get login type from the Intent
        loginType = getIntent().getIntExtra("login_type", 0);

        // Initialize event listeners
        initListener();
    }

    /**
     * Initializes the event listeners for the login buttons and actions.
     */
    private void initListener() {
        // Navigate to the registration screen when the "Go to Register" button is clicked
        goRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        // Handle standard login when the "Login" button is clicked
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginType == 0) {  // Standard login
                    login();
                }
            }
        });

        // Close the login activity when the back button is clicked
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Handle WeChat login
        wxLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("login_type", 2));
            }
        });

        // Handle QQ login
        qqLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, LoginActivity.class).putExtra("login_type", 1));
            }
        });
    }

    /**
     * Handles the standard login process by validating the phone number and password.
     */
    private void login() {
        String phone = phoneEdit.getText().toString().trim();
        String pwd = pwdEdit.getText().toString().trim();

        // Validate phone number input
        if (phone.isEmpty()) {
            phoneEdit.setError("Please enter the account number");
            return;
        }

        // Query the database to find the user by phone number
        User registeredUser = Database.getDao().queryUserByPhone(phone);
        Log.d("TAG", "registeredUser: " + registeredUser);

        // Validate password input
        if (pwd.isEmpty()) {
            pwdEdit.setError("Please enter password");
            return;
        }

        // Check if the password format is valid
        if (!SomeUtil.isTruePwd(pwd)) {
            pwdEdit.setError("Please enter the correct password format");
            Toast.makeText(this, "Password format must be a 6-digit number", Toast.LENGTH_SHORT).show();
            return;
        }

        // If user is not registered, prompt the user to register
        if (registeredUser == null) {
            Toast.makeText(this, "This account is not registered, please register first", Toast.LENGTH_SHORT).show();
        } else {
            // If the password is correct, log in the user
            if (pwd.equals(registeredUser.password)) {
                Toast.makeText(this, "Login succeeded", Toast.LENGTH_SHORT).show();
                App.login(registeredUser);  // Set the logged-in user in the App class
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                finish();  // Close the login activity
            } else {
                Toast.makeText(this, "Incorrect account or password, please re-enter", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Initializes the views by finding them by their ID.
     */
    private void findViewsById() {
        phoneEdit = findViewById(R.id.phoneEdit);
        pwdEdit = findViewById(R.id.pwdEdit);
        loginBtn = findViewById(R.id.loginBtn);
        goRegisterBtn = findViewById(R.id.goRegisterBtn);
        backBtn = findViewById(R.id.backBtn);
        qqLogin = findViewById(R.id.qqLogin);
        wxLogin = findViewById(R.id.wxLogin);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("TAG", "onDestroy: ");
        ActivityManager.getInstance().remove(this);  // Remove the activity from the ActivityManager when it is destroyed
    }
}
