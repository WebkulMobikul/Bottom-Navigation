package com.example.saumyadubey.bottomtoolbar.notification;

import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;

/**
 * Created by saumya.dubey on 13/1/18.
 */
public final class NotificationHelper {

    private NotificationHelper() {
    }

    public static int getTextColor(@NonNull Notification notification, @ColorInt int defaultTextColor) {
        int textColor = notification.getTextColor();
        return textColor == 0 ? defaultTextColor : textColor;
    }

    public static int getBackgroundColor(@NonNull Notification notification, @ColorInt int defaultBackgroundColor) {
        int backgroundColor = notification.getBackgroundColor();
        return backgroundColor == 0 ? defaultBackgroundColor : backgroundColor;
    }

}
