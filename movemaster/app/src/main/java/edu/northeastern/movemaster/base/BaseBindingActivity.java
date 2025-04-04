package edu.northeastern.movemaster.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewbinding.ViewBinding;

import edu.northeastern.movemaster.R;
import edu.northeastern.movemaster.util.ActivityManager;
import com.jaeger.library.StatusBarUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * A base activity class that uses {@link ViewBinding} for view binding and handles common functionality
 * such as status bar customization, activity management, and event listeners.
 * <p>
 * This activity uses reflection to automatically initialize the appropriate {@link ViewBinding} for the
 * activity's layout and provides a convenient way to interact with UI elements through the binding.
 * </p>
 *
 * @param <T> The type of the {@link ViewBinding} for the activity's layout.
 */
public abstract class BaseBindingActivity<T extends ViewBinding> extends AppCompatActivity {

    /**
     * The {@link ViewBinding} instance for the activity's layout.
     */
    protected T viewBinder;

    /**
     * Called when the activity is created. It initializes the view binding, sets the content view,
     * customizes the status bar, and adds this activity to the {@link ActivityManager}.
     * <p>
     * This method also automatically sets up a back button (if available) to finish the activity when clicked.
     * </p>
     *
     * @param savedInstanceState The saved instance state, if any.
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use reflection to initialize the ViewBinding for the current activity
        Type type = getClass().getGenericSuperclass();
        Type[] actualTypeArguments = ((ParameterizedType) type).getActualTypeArguments();
        Class<T> tClass = (Class<T>) actualTypeArguments[0];
        try {
            Method method = tClass.getMethod("inflate", LayoutInflater.class);
            viewBinder = (T) method.invoke(null, getLayoutInflater());
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Set the root view for the activity
        setContentView(viewBinder.getRoot());

        // Customize the status bar color
        StatusBarUtil.setColorNoTranslucent(this, getResources().getColor(R.color.colorPrimary));

        // Add this activity to the ActivityManager
        ActivityManager.getInstance().add(this);

        // Initialize listeners and data
        initListener();
        initData();

        // Set up back button (if available)
        View backBtn = findViewById(R.id.backBtn);
        if (backBtn != null) {
            backBtn.setOnClickListener(v -> finish());
        }
    }

    /**
     * Called when the activity is destroyed. It removes this activity from the {@link ActivityManager}.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityManager.getInstance().remove(this);
    }

    /**
     * Abstract method for initializing listeners. This should be implemented by subclasses to set up
     * event listeners (e.g., click listeners) for views in the activity.
     */
    protected abstract void initListener();

    /**
     * Abstract method for initializing data. This should be implemented by subclasses to load or
     * initialize any data needed for the activity.
     */
    protected abstract void initData();

    /**
     * Shows a toast message with the specified text.
     *
     * @param msg The message to display in the toast.
     */
    protected void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
