package com.example.saumyadubey.bottomtoolbar;

import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    protected MenuItem itemCart;
    private int num_in_cart = 5;
    LinearLayout lp;
    SharedPreferences sp;
    private ArrayList<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private BottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

}
