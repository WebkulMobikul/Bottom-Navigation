# Bottom Navigation librery

## Features

* Add max 5 items (with title, icon & color)
* Add a OnTabSelectedListener to detect tab selection
* Manage notififcations for each item


## XML
```xml
<com.example.saumyadubey.bottomtoolbar.BottomNavigation

        android:id="@+id/bottom_navigation" 
        android:layout_alignParentBottom="true"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"/>
```

## Activity

  bottomNavigation = (BottomNavigation) findViewById(R.id.bottom_navigation);

#### Add icon 

BottomNavigationItem item1 = new BottomNavigationItem("menu1", R.drawable.ic_bag);

BottomNavigationItem item2 = new BottomNavigationItem("menu2", R.drawable.ic_vector_home);

 bottomNavigationItems.add(item1);
 bottomNavigationItems.add(item2);
 
 bottomNavigation.addItems(bottomNavigationItems);

#### Add Notification Badge

bottomNavigation.setNotification("notification no", int item posiition);

bottomNavigation.setNotificationBackgroundColor(Color.RED);
bottomNavigation.setNotificationTextColor(Color.WHITE);
