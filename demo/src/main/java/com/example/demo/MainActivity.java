package com.example.demo;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

import com.example.saumyadubey.bottomtoolbar.BottomNavigation;
import com.example.saumyadubey.bottomtoolbar.BottomNavigationItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<BottomNavigationItem> bottomNavigationItems = new ArrayList<>();
    private BottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {

        bottomNavigation = (BottomNavigation) findViewById(R.id.bottom_navigation);

        BottomNavigationItem item1 = new BottomNavigationItem("menu1", R.drawable.ic_bag);
        BottomNavigationItem item2 = new BottomNavigationItem("menu2", R.drawable.ic_vector_home);
        BottomNavigationItem item3 = new BottomNavigationItem("menu3", R.drawable.ic_vector_wishlish_grey);
        BottomNavigationItem item4 = new BottomNavigationItem("menu4", R.drawable.ic_grid);
        BottomNavigationItem item5 = new BottomNavigationItem("menu5", R.drawable.ic_search);

        bottomNavigationItems.add(item1);
        bottomNavigationItems.add(item2);
        bottomNavigationItems.add(item3);
        bottomNavigationItems.add(item4);
        bottomNavigationItems.add(item5);
//        bottomNavigationItems.add(item6);

        bottomNavigation.addItems(bottomNavigationItems);
        bottomNavigation.setInactiveColor(Color.WHITE);
//        bottomNavigation.setActiveColor(R.color.colorAccent);
//        bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_SHOW);
//        bottomNavigation.setTitleState(BottomNavigation.TitleState.ALWAYS_HIDE);
        bottomNavigation.setNotification("12", 2);
        bottomNavigation.setNotificationBackgroundColor(Color.RED);
        bottomNavigation.setDefaultBackgroundResource(R.color.colorPrimary);



        bottomNavigation.setOnTabSelectedListener(new BottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(position==0){
                    Intent in = new Intent(getApplication(),MainActivity.class);
                    startActivity(in);
//                    bottomNavigation.setNotification("12", 0);
                }

                if(position==1){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragment2 frag1 = new fragment2();
                    fragmentTransaction.add(R.id.container, frag1, "start").addToBackStack("stack");
                    fragmentTransaction.commit();
                }

                if(position==2){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragment3 frag1 = new fragment3();
                    fragmentTransaction.add(R.id.container, frag1, "start").addToBackStack("stack");
                    fragmentTransaction.commit();

                }

                if(position==3){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragment4 frag1 = new fragment4();
                    fragmentTransaction.add(R.id.container, frag1, "start").addToBackStack("stack");
                    fragmentTransaction.commit();
                }

                if(position==4){
                    FragmentManager fm = getFragmentManager();
                    FragmentTransaction fragmentTransaction = fm.beginTransaction();
                    fragment5 frag1 = new fragment5();
                    fragmentTransaction.add(R.id.container, frag1, "start").addToBackStack("stack");
                    fragmentTransaction.commit();
                }
                return true;
            }
        });
    }

}
