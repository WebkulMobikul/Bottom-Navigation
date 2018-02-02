package com.example.saumyadubey.bottomtoolbar;

import android.app.Activity;
import android.support.annotation.ColorInt;
import android.support.annotation.MenuRes;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by saumya.dubey on 13/1/18.
 */
public class BottomNavigationAdapter {

	private Menu mMenu;
	private List<BottomNavigationItem> navigationItems;


	public BottomNavigationAdapter(Activity activity, @MenuRes int menuRes) {
		PopupMenu popupMenu = new PopupMenu(activity, null);
		mMenu = popupMenu.getMenu();
		activity.getMenuInflater().inflate(menuRes, mMenu);
	}


	public void setupWithBottomNavigation(BottomNavigation BottomNavigation) {
		setupWithBottomNavigation(BottomNavigation, null);
	}


	public void setupWithBottomNavigation(BottomNavigation ahBottomNavigation, @ColorInt int[] colors) {
		if (navigationItems == null) {
			navigationItems = new ArrayList<>();
		} else {
			navigationItems.clear();
		}

		if (mMenu != null) {
			for (int i = 0; i < mMenu.size(); i++) {
				MenuItem item = mMenu.getItem(i);
//				if (colors != null && colors.length >= mMenu.size() && colors[i] != 0) {
//					BottomNavigationItem navigationItem = new BottomNavigationItem(String.valueOf(item.getTitle()), item.getIcon(), colors[i]);
//					navigationItems.add(navigationItem);
//				} else {
					BottomNavigationItem navigationItem = new BottomNavigationItem(String.valueOf(item.getTitle()), item.getIcon());
					navigationItems.add(navigationItem);
//				}
			}
			ahBottomNavigation.removeAllItems();
			ahBottomNavigation.addItems(navigationItems);
		}
	}


	public MenuItem getMenuItem(int index) {
		return mMenu.getItem(index);
	}


	public BottomNavigationItem getNavigationItem(int index) {
		return navigationItems.get(index);
	}


	public Integer getPositionByMenuId(int menuId) {
		for (int i = 0; i < mMenu.size(); i++) {
			if (mMenu.getItem(i).getItemId() == menuId)
				return i;
		}
		return null;
	}
}