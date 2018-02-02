package com.example.saumyadubey.bottomtoolbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.content.res.AppCompatResources;

/**
 * Created by saumya.dubey on 13/1/18.
 */

public class BottomNavigationItem {

    private String title = "";
    private Drawable drawable;
    private int drawableRes = 0;
    private int titleRes = 0;
    @ColorRes
    private int colorRes = 0;
    private int color = Color.GRAY;



    public BottomNavigationItem(String title, int resource) {
        this.title = title;
        this.drawableRes = resource;
    }


    public BottomNavigationItem(String title, Drawable drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle(Context context) {
        if (titleRes != 0) {
            return context.getString(titleRes);
        }
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        this.titleRes = 0;
    }

    public void setTitle(@StringRes int titleRes) {
        this.titleRes = titleRes;
        this.title = "";
    }

    public Drawable getDrawable(Context context) {
        if (drawableRes != 0) {
            try {
                return AppCompatResources.getDrawable(context, drawableRes);
            } catch (Resources.NotFoundException e) {
                return ContextCompat.getDrawable(context, drawableRes);
            }
        }
        return drawable;
    }

    public void setDrawable(@DrawableRes int drawableRes) {
        this.drawableRes = drawableRes;
        this.drawable = null;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        this.drawableRes = 0;
    }

    public int getColor(Context context) {
        if (colorRes != 0) {
            return ContextCompat.getColor(context, colorRes);
        }
        return color;
    }

    public void setColor(@ColorInt int color) {
        this.color = color;
        this.colorRes = 0;
    }

    public void setColorRes(@ColorRes int colorRes) {
        this.colorRes = colorRes;
        this.color = 0;
    }
}
