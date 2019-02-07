package com.popup.menu.demo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpToolbar();
    }

    /**
     * set up toolbar
     */
    private void setUpToolbar() {
        Toolbar toolbar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
    }

    /**
     * method to show default popup menu
     *
     * @param view
     */
    public void showDefaultPopupMenu(View view) {
        showPopupMenu(view, false, R.style.MyPopupStyle);
    }

    /**
     * method to show custom popup menu with icons
     *
     * @param view
     */
    public void showCustomPopupMenu(View view) {
        showPopupMenu(view, true, R.style.MyPopupStyle);
    }

    /**
     * method to show styled popup menu with icons
     *
     * @param view
     */
    public void showStyledPopupMenu(View view) {
        showPopupMenu(view, false, R.style.MyPopupOtherStyle);
    }

    /**
     * method responsible to show popup menu
     *
     * @param anchor      is a view where the popup will be shown
     * @param isWithIcons flag to check if icons to be shown or not
     * @param style       styling for popup menu
     */
    private void showPopupMenu(View anchor, boolean isWithIcons, int style) {
        //init the wrapper with style
        Context wrapper = new ContextThemeWrapper(this, style);

        //init the popup
        PopupMenu popup = new PopupMenu(wrapper, anchor);

        /*  The below code in try catch is responsible to display icons*/
        if (isWithIcons) {
            try {
                Field[] fields = popup.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if ("mPopup".equals(field.getName())) {
                        field.setAccessible(true);
                        Object menuPopupHelper = field.get(popup);
                        Class<?> classPopupHelper = Class.forName(menuPopupHelper.getClass().getName());
                        Method setForceIcons = classPopupHelper.getMethod("setForceShowIcon", boolean.class);
                        setForceIcons.invoke(menuPopupHelper, true);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //inflate menu
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //implement click events
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_contact_us:
                        Toast.makeText(MainActivity.this, "Contact us clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_terms_conditions:
                        Toast.makeText(MainActivity.this, "Terms and Conditions clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.action_logout:
                        Toast.makeText(MainActivity.this, "Logout clicked", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        popup.show();

    }
}
