package com.example.saumyadubey.bottomtoolbar;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.saumyadubey.bottomtoolbar.notification.Notification;
import com.example.saumyadubey.bottomtoolbar.notification.NotificationHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by saumya.dubey on 13/1/18.
 */

public class BottomNavigation  extends FrameLayout{


    public static final int CURRENT_ITEM_NONE = -1;
    public static final int UPDATE_ALL_NOTIFICATIONS = -1;

    public enum TitleState {
        SHOW_WHEN_ACTIVE,
        ALWAYS_SHOW,
        ALWAYS_HIDE
    }

    private static String TAG = "BottomNavigation";
    private static final String EXCEPTION_INDEX_OUT_OF_BOUNDS = "The position (%d) is out of bounds of the items (%d elements)";
    private static final int MIN_ITEMS = 3;
    private static final int MAX_ITEMS = 8;

    private OnTabSelectedListener tabSelectedListener;

    private Context context;
    private Resources resources;
    private ArrayList<BottomNavigationItem> items = new ArrayList<>();
    private ArrayList<View> views = new ArrayList<>();
    private LinearLayout linearLayoutContainer;
    private View backgroundColorView;
    private Animator circleAnim;
    private boolean colored = false;
    private boolean selectedBackgroundVisible = false;
    private List<Notification> notifications = Notification.generateEmptyList(MAX_ITEMS);
    private Boolean[] itemsEnabledStates = {true, true, true, true, true};
    private int currentItem = 0;
    private int currentColor = 0;
    private int defaultBackgroundColor = Color.WHITE;
    private int defaultBackgroundResource = 0;
    private int itemActiveColor;
    private int itemInactiveColor;
    private int titleColorActive;
    private int titleColorInactive;
    private float titleActiveTextSize, titleInactiveTextSize;
    private int bottomNavigationHeight, navigationBarHeight = 0;
    private boolean forceTint = false;
    private TitleState titleState = TitleState.SHOW_WHEN_ACTIVE;
    private int notificationTextColor;
    private int notificationBackgroundColor;
    private Drawable notificationBackgroundDrawable;
    private int notificationActiveMarginLeft, notificationInactiveMarginLeft;


    public BottomNavigation(Context context) {
        super(context);
        init(context, null);
    }

    public BottomNavigation(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BottomNavigation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        createItems();
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("superState", super.onSaveInstanceState());
        bundle.putInt("current_item", currentItem);
//      bundle.putParcelableArrayList("notifications", new ArrayList<> (notifications));
        return bundle;
    }


    private void init(Context context, AttributeSet attrs) {
        this.context = getContext();
        resources = this.context.getResources();

        // Item colors
        titleColorActive = ContextCompat.getColor(context, R.color.colorAccent);
        titleColorInactive = ContextCompat.getColor(context, R.color.colorInactive);

        notificationTextColor = ContextCompat.getColor(context, android.R.color.white);
        bottomNavigationHeight = (int) resources.getDimension(R.dimen.height);

        itemActiveColor = titleColorActive;
        itemInactiveColor = titleColorInactive;

        // Notifications
        notificationActiveMarginLeft = (int) resources.getDimension(R.dimen.notification_margin_left_active);
        notificationInactiveMarginLeft = (int) resources.getDimension(R.dimen.notification_margin_left);

        ViewCompat.setElevation(this, resources.getDimension(R.dimen.bottom_navigation_elevation));
        setClipToPadding(false);

        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, bottomNavigationHeight);
        setLayoutParams(params);
    }

    private void createItems() {
        if (items.size() < MIN_ITEMS) {
            Log.d(TAG, "The items list should have at least 3 items");
        } else if (items.size() > MAX_ITEMS) {
            Log.d(TAG, "The items list should not have more than 5 items");
        }

        int layoutHeight = (int) resources.getDimension(R.dimen.height);

        removeAllViews();
        views.clear();
        backgroundColorView = new View(context);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LayoutParams backgroundLayoutParams = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, calculateHeight(layoutHeight));
            addView(backgroundColorView, backgroundLayoutParams);
            bottomNavigationHeight = layoutHeight;
        }

        linearLayoutContainer = new LinearLayout(context);
        linearLayoutContainer.setOrientation(LinearLayout.HORIZONTAL);
        linearLayoutContainer.setGravity(Gravity.CENTER);

        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight);
        addView(linearLayoutContainer, layoutParams);

        if (titleState != TitleState.ALWAYS_HIDE &&
                (items.size() == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW)) {
            createNavigationItems(linearLayoutContainer);
        } else {
//            createSmallItems(linearLayoutContainer);
            createNavigationItems(linearLayoutContainer);
        }

        post(new Runnable() {
            @Override
            public void run() {
                requestLayout();
            }
        });
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private int calculateHeight(int layoutHeight) {

        int resourceId = getResources().getIdentifier("navigation_bar_height", "dimen", "android");
        if (resourceId > 0) {
            navigationBarHeight = resources.getDimensionPixelSize(resourceId);
        }

        int[] attrs = {android.R.attr.fitsSystemWindows, android.R.attr.windowTranslucentNavigation};
        TypedArray typedValue = getContext().getTheme().obtainStyledAttributes(attrs);

        @SuppressWarnings("ResourceType")
        boolean translucentNavigation = typedValue.getBoolean(1, true);

        if(hasImmersive() && translucentNavigation) {
            layoutHeight += navigationBarHeight;
        }

        typedValue.recycle();

        return layoutHeight;
    }

    @SuppressLint("NewApi")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean hasImmersive() {
        Display d = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        DisplayMetrics realDisplayMetrics = new DisplayMetrics();
        d.getRealMetrics(realDisplayMetrics);

        int realHeight = realDisplayMetrics.heightPixels;
        int realWidth = realDisplayMetrics.widthPixels;

        DisplayMetrics displayMetrics = new DisplayMetrics();
        d.getMetrics(displayMetrics);

        int displayHeight = displayMetrics.heightPixels;
        int displayWidth = displayMetrics.widthPixels;

        return (realWidth > displayWidth) || (realHeight > displayHeight);
    }


    private void createNavigationItems(LinearLayout linearLayout) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        float height = resources.getDimension(R.dimen.height);
        float minWidth = resources.getDimension(R.dimen.small_inactive_min_width);
        float maxWidth = resources.getDimension(R.dimen.max_width);

        if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
            minWidth = resources.getDimension(R.dimen.small_inactive_min_width);
            maxWidth = resources.getDimension(R.dimen.small_inactive_max_width);
        }

        int layoutWidth = getWidth();
        if (layoutWidth == 0 || items.size() == 0) {
            return;
        }

        float itemWidth = layoutWidth / items.size();
        if (itemWidth < minWidth) {
            itemWidth = minWidth;
        } else if (itemWidth > maxWidth) {
            itemWidth = maxWidth;
        }

        float activeSize = resources.getDimension(R.dimen.text_size_active);
        float inactiveSize = resources.getDimension(R.dimen.text_size_inactive);
        int activePaddingTop = (int) resources.getDimension(R.dimen.margin_top_active);

        if (titleActiveTextSize != 0 && titleInactiveTextSize != 0) {
            activeSize = titleActiveTextSize;
            inactiveSize = titleInactiveTextSize;
        } else if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
            activeSize = resources.getDimension(R.dimen.text_size_forced_active);
            inactiveSize = resources.getDimension(R.dimen.text_size_forced_inactive);
        }

        for (int i = 0; i < items.size(); i++) {
            final boolean current = currentItem == i;
            final int itemIndex = i;
            BottomNavigationItem item = items.get(itemIndex);

            View view = inflater.inflate(R.layout.bottom_navigation_item, this, false);
            FrameLayout container = (FrameLayout) view.findViewById(R.id.bottom_navigation_container);
            ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
            TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
            TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);
            if(titleState == TitleState.ALWAYS_HIDE) {
                title.setVisibility(INVISIBLE);
            }


            icon.setImageDrawable(item.getDrawable(context));
            title.setText(item.getTitle(context));

            if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
                container.setPadding(0, container.getPaddingTop(), 0, container.getPaddingBottom());
            }

            if (current) {
                if (selectedBackgroundVisible) {
                    view.setSelected(true);
                }
                icon.setSelected(true);
                // Update margins (icon & notification)
                if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
                    ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) icon.getLayoutParams();
                    p.setMargins(p.leftMargin, activePaddingTop, p.rightMargin, p.bottomMargin);

                    ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
                            notification.getLayoutParams();
                    paramsNotification.setMargins(notificationActiveMarginLeft, paramsNotification.topMargin,
                            paramsNotification.rightMargin, paramsNotification.bottomMargin);

                    view.requestLayout();
                }
            } else {
                icon.setSelected(false);
                ViewGroup.MarginLayoutParams paramsNotification = (ViewGroup.MarginLayoutParams)
                        notification.getLayoutParams();
                paramsNotification.setMargins(notificationInactiveMarginLeft, paramsNotification.topMargin,
                        paramsNotification.rightMargin, paramsNotification.bottomMargin);
            }
                if (defaultBackgroundResource != 0) {
                    setBackgroundResource(defaultBackgroundResource);
                } else {
                    setBackgroundColor(defaultBackgroundColor);
                }

            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, current ? activeSize : inactiveSize);

            if (itemsEnabledStates[i]) {
                view.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        updateItems(itemIndex, true);
                    }
                });
                icon.setImageDrawable(BNHelper.getTintDrawable(items.get(i).getDrawable(context),
                        current ? itemActiveColor : itemInactiveColor, forceTint));
                title.setTextColor(current ? itemActiveColor : itemInactiveColor);
            }

            LayoutParams params = new LayoutParams((int) itemWidth, (int) height);
            linearLayout.addView(view, params);
            views.add(view);
        }

        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
    }


    private void updateItems(final int itemIndex, boolean useCallback) {

        if (currentItem == itemIndex) {
            if (tabSelectedListener != null && useCallback) {
                tabSelectedListener.onTabSelected(itemIndex, true);
            }
            return;
        }

        if (tabSelectedListener != null && useCallback) {
            boolean selectionAllowed = tabSelectedListener.onTabSelected(itemIndex, false);
            if (!selectionAllowed) return;
        }

        int activeMarginTop = (int) resources.getDimension(R.dimen.margin_top_active);
        int inactiveMarginTop = (int) resources.getDimension(R.dimen.margin_top_inactive);
        float activeSize = resources.getDimension(R.dimen.text_size_active);
        float inactiveSize = resources.getDimension(R.dimen.text_size_inactive);

        if (titleActiveTextSize != 0 && titleInactiveTextSize != 0) {
            activeSize = titleActiveTextSize;
            inactiveSize = titleInactiveTextSize;
        } else if (titleState == TitleState.ALWAYS_SHOW && items.size() > MIN_ITEMS) {
            activeSize = resources.getDimension(R.dimen.text_size_forced_active);
            inactiveSize = resources.getDimension(R.dimen.text_size_forced_inactive);
        }

        for (int i = 0; i < views.size(); i++) {

            final View view = views.get(i);
            if (selectedBackgroundVisible) {
                view.setSelected(i == itemIndex);
            }

            if (i == itemIndex) {

                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

                icon.setSelected(true);
                BNHelper.updateTopMargin(icon, inactiveMarginTop, activeMarginTop);
                BNHelper.updateLeftMargin(notification, notificationInactiveMarginLeft, notificationActiveMarginLeft);
                BNHelper.updateTextColor(title, itemInactiveColor, itemActiveColor);
                BNHelper.updateTextSize(title, inactiveSize, activeSize);
                BNHelper.updateDrawableColor(context, items.get(itemIndex).getDrawable(context), icon,
                        itemInactiveColor, itemActiveColor, forceTint);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && colored) {

                    int finalRadius = Math.max(getWidth(), getHeight());
                    int cx = (int) view.getX() + view.getWidth() / 2;
                    int cy = view.getHeight() / 2;

                    if (circleAnim != null && circleAnim.isRunning()) {
                        circleAnim.cancel();
                       setBackgroundColor(items.get(itemIndex).getColor(context));
                        backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
                    }

                    circleAnim = ViewAnimationUtils.createCircularReveal(backgroundColorView, cx, cy, 0, finalRadius);
                    circleAnim.setStartDelay(5);
                    circleAnim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            backgroundColorView.setBackgroundColor(items.get(itemIndex).getColor(context));
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            setBackgroundColor(items.get(itemIndex).getColor(context));
                            backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {
                        }
                    });
                    circleAnim.start();
                } else if (colored) {
                    BNHelper.updateViewBackgroundColor(this, currentColor,
                            items.get(itemIndex).getColor(context));
                } else {
                    if (defaultBackgroundResource != 0) {
                        setBackgroundResource(defaultBackgroundResource);
                    } else {
                        setBackgroundColor(defaultBackgroundColor);
                    }
                    backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
                }

            } else if (i == currentItem) {

                final TextView title = (TextView) view.findViewById(R.id.bottom_navigation_item_title);
                final ImageView icon = (ImageView) view.findViewById(R.id.bottom_navigation_item_icon);
                final TextView notification = (TextView) view.findViewById(R.id.bottom_navigation_notification);

                icon.setSelected(false);
                BNHelper.updateTopMargin(icon, activeMarginTop, inactiveMarginTop);
                BNHelper.updateLeftMargin(notification, notificationActiveMarginLeft, notificationInactiveMarginLeft);
                BNHelper.updateTextColor(title, itemActiveColor, itemInactiveColor);
                BNHelper.updateTextSize(title, activeSize, inactiveSize);
                BNHelper.updateDrawableColor(context, items.get(currentItem).getDrawable(context), icon,
                        itemActiveColor, itemInactiveColor, forceTint);
            }
        }

        currentItem = itemIndex;
        if (currentItem > 0 && currentItem < items.size()) {
            currentColor = items.get(currentItem).getColor(context);
        } else if (currentItem == CURRENT_ITEM_NONE) {
            if (defaultBackgroundResource != 0) {
                setBackgroundResource(defaultBackgroundResource);
            } else {
                setBackgroundColor(defaultBackgroundColor);
            }
            backgroundColorView.setBackgroundColor(Color.TRANSPARENT);
        }
    }
    /**
     * Update notifications
     */
    private void updateNotifications(boolean updateStyle, int itemPosition) {
        Log.d(TAG, "updateNotifications: "+"jfkdfj");

        for (int i = 0; i < views.size(); i++) {

            if (i >= notifications.size()) {
                break;
            }

            if (itemPosition != UPDATE_ALL_NOTIFICATIONS && itemPosition != i) {
                continue;
            }

            final Notification notificationItem = notifications.get(i);
            final int currentTextColor = NotificationHelper.getTextColor(notificationItem, notificationTextColor);
            final int currentBackgroundColor = NotificationHelper.getBackgroundColor(notificationItem, notificationBackgroundColor);

            TextView notification = (TextView) views.get(i).findViewById(R.id.bottom_navigation_notification);

            String currentValue = notification.getText().toString();
            boolean animate = !currentValue.equals(String.valueOf(notificationItem.getText()));

            if (updateStyle) {
                notification.setTextColor(currentTextColor);

                if (notificationBackgroundDrawable != null) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        Drawable drawable = notificationBackgroundDrawable.getConstantState().newDrawable();
                        notification.setBackground(drawable);
                    }
                } else if (currentBackgroundColor != 0) {
                    Drawable defautlDrawable = ContextCompat.getDrawable(context, R.drawable.notification_background);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notification.setBackground(BNHelper.getTintDrawable(defautlDrawable,
                                currentBackgroundColor, forceTint));
                    } else {
                        notification.setBackgroundDrawable(BNHelper.getTintDrawable(defautlDrawable,
                                currentBackgroundColor, forceTint));
                    }
                }
            }

            if (notificationItem.isEmpty() && notification.getText().length() > 0) {
                notification.setText("");
                if (animate) {
                    notification.animate()
                            .scaleX(0)
                            .scaleY(0)
                            .alpha(0)
                            .setInterpolator(new AccelerateInterpolator())
                            .start();
                }
            } else if (!notificationItem.isEmpty()) {
                notification.setText(String.valueOf(notificationItem.getText()));
                if (animate) {
                    notification.setScaleX(0);
                    notification.setScaleY(0);
                    notification.animate()
                            .scaleX(1)
                            .scaleY(1)
                            .alpha(1)
                            .setInterpolator(new OvershootInterpolator())
                            .start();
                }
            }
        }
    }

    public void addItem(BottomNavigationItem item) {
        if (this.items.size() > MAX_ITEMS) {
            Log.d(TAG, "The items list should not have more than 5 items");
        }
        items.add(item);
        createItems();
    }


    public void addItems(List<BottomNavigationItem> items) {
        if (items.size() > MAX_ITEMS || (this.items.size() + items.size()) > MAX_ITEMS) {
            Log.d(TAG, "The items list should not have more than 5 items");
        }
        this.items.addAll(items);
        createItems();
    }


    public void removeItemAtIndex(int index) {
        if (index < items.size()) {
            this.items.remove(index);
            createItems();
        }
    }

    public void removeAllItems() {
        this.items.clear();
        createItems();
    }

    public int getDefaultBackgroundColor() {
        return defaultBackgroundColor;
    }

    public void setDefaultBackgroundColor( int defaultBackgroundColor) {
        this.defaultBackgroundColor = defaultBackgroundColor;
        createItems();
    }


    public void setDefaultBackgroundResource( int defaultBackgroundResource) {
        this.defaultBackgroundResource = defaultBackgroundResource;
        createItems();
    }

    public int getAccentColor() {
        return itemActiveColor;
    }


    public void setAccentColor(int accentColor) {
        this.titleColorActive = accentColor;
        this.itemActiveColor = accentColor;
        createItems();
    }

    public int getInactiveColor() {
        return itemInactiveColor;
    }


    public void setInactiveColor(int inactiveColor) {
        this.titleColorInactive = inactiveColor;
        this.itemInactiveColor = inactiveColor;
        createItems();
    }

    public void setTitleTextSize(float activeSize, float inactiveSize) {
        this.titleActiveTextSize = activeSize;
        this.titleInactiveTextSize = inactiveSize;
        createItems();
    }


    public BottomNavigationItem getItem(int position) {
        if (position < 0 || position > items.size() - 1) {
            Log.d(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
            return null;
        }
        return items.get(position);
    }

    public int getCurrentItem() {
        return currentItem;
    }


    public void setCurrentItem(int position) {
        setCurrentItem(position, true);
    }


    public void setCurrentItem(int position, boolean useCallback) {
        if (position >= items.size()) {
            Log.d(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
            return;
        }

        if (titleState != TitleState.ALWAYS_HIDE &&
                (items.size() == MIN_ITEMS || titleState == TitleState.ALWAYS_SHOW)) {
            updateItems(position, useCallback);
        }
    }

    public TitleState getTitleState() {
        return titleState;
    }

    public void setTitleState(TitleState titleState) {
        this.titleState = titleState;
        createItems();
    }

    public void setOnTabSelectedListener(OnTabSelectedListener tabSelectedListener) {
        this.tabSelectedListener = tabSelectedListener;
    }


    public void setNotification(String title, int itemPosition) {
        if (itemPosition < 0 || itemPosition > items.size() - 1) {
            throw new IndexOutOfBoundsException(String.format(Locale.US, EXCEPTION_INDEX_OUT_OF_BOUNDS, itemPosition, items.size()));
        }
        notifications.set(itemPosition, Notification.justText(title));
        updateNotifications(false, itemPosition);
    }


    public void setNotificationTextColor( int textColor) {
        this.notificationTextColor = textColor;
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
    }

    public void setNotificationTextColorResource( int textColor) {
        this.notificationTextColor = ContextCompat.getColor(context, textColor);
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
    }

    public void setNotificationBackground(Drawable drawable) {
        this.notificationBackgroundDrawable = drawable;
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
    }

    public void setNotificationBackgroundColor( int color) {
        this.notificationBackgroundColor = color;
        updateNotifications(true, UPDATE_ALL_NOTIFICATIONS);
    }


    public View getViewAtPosition(int position) {
        if (linearLayoutContainer != null && position >= 0
                && position < linearLayoutContainer.getChildCount()) {
            return linearLayoutContainer.getChildAt(position);
        }
        return null;
    }


    public void enableItemAtPosition(int position) {
        if (position < 0 || position > items.size() - 1) {
            Log.d(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
            return;
        }
        itemsEnabledStates[position] = true;
        createItems();
    }


    public void disableItemAtPosition(int position) {
        if (position < 0 || position > items.size() - 1) {
            Log.d(TAG, "The position is out of bounds of the items (" + items.size() + " elements)");
            return;
        }
        itemsEnabledStates[position] = false;
        createItems();
    }

    public interface OnTabSelectedListener {

        boolean onTabSelected(int position, boolean wasSelected);
    }

}
