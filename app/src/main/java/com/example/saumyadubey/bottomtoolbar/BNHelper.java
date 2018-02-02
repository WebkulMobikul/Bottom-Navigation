package com.example.saumyadubey.bottomtoolbar;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class BNHelper {


	public static Drawable getTintDrawable(Drawable drawable, @ColorInt int color, boolean forceTint) {
		if (forceTint) {
			drawable.clearColorFilter();
			drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
			drawable.invalidateSelf();
			return drawable;
		}
		Drawable wrapDrawable = DrawableCompat.wrap(drawable).mutate();
		DrawableCompat.setTint(wrapDrawable, color);
		return wrapDrawable;
	}


	public static void updateTopMargin(final View view, int fromMargin, int toMargin) {
		ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
		animator.setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float animatedValue = (float) valueAnimator.getAnimatedValue();
				if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
					p.setMargins(p.leftMargin, (int) animatedValue, p.rightMargin, p.bottomMargin);
					view.requestLayout();
				}
			}
		});
		animator.start();
	}

	public static void updateLeftMargin(final View view, int fromMargin, int toMargin) {
		ValueAnimator animator = ValueAnimator.ofFloat(fromMargin, toMargin);
		animator.setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float animatedValue = (float) valueAnimator.getAnimatedValue();
				if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
					ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
					p.setMargins((int) animatedValue, p.topMargin, p.rightMargin, p.bottomMargin);
					view.requestLayout();
				}
			}
		});
		animator.start();
	}

	public static void updateTextSize(final TextView textView, float fromSize, float toSize) {
		ValueAnimator animator = ValueAnimator.ofFloat(fromSize, toSize);
		animator.setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float animatedValue = (float) valueAnimator.getAnimatedValue();
				textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, animatedValue);
			}
		});
		animator.start();
	}


	public static void updateAlpha(final View view, float fromValue, float toValue) {
		ValueAnimator animator = ValueAnimator.ofFloat(fromValue, toValue);
		animator.setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator valueAnimator) {
				float animatedValue = (float) valueAnimator.getAnimatedValue();
				view.setAlpha(animatedValue);
			}
		});
		animator.start();
	}


	public static void updateTextColor(final TextView textView, @ColorInt int fromColor,
                                       @ColorInt int toColor) {
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
		colorAnimation.setDuration(150);
		colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				textView.setTextColor((Integer) animator.getAnimatedValue());
			}
		});
		colorAnimation.start();
	}


	public static void updateViewBackgroundColor(final View view, @ColorInt int fromColor,
                                                 @ColorInt int toColor) {
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
		colorAnimation.setDuration(150);
		colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				view.setBackgroundColor((Integer) animator.getAnimatedValue());
			}
		});
		colorAnimation.start();
	}


	public static void updateDrawableColor(final Context context, final Drawable drawable,
                                           final ImageView imageView, @ColorInt int fromColor,
                                           @ColorInt int toColor, final boolean forceTint) {
		ValueAnimator colorAnimation = ValueAnimator.ofObject(new ArgbEvaluator(), fromColor, toColor);
		colorAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				imageView.setImageDrawable(BNHelper.getTintDrawable(drawable,
						(Integer) animator.getAnimatedValue(), forceTint));
				imageView.requestLayout();
			}
		});
		colorAnimation.start();
	}


	public static void updateWidth(final View view, float fromWidth, float toWidth) {
		ValueAnimator animator = ValueAnimator.ofFloat(fromWidth, toWidth);
		animator.setDuration(150);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animator) {
				ViewGroup.LayoutParams params = view.getLayoutParams();
				params.width = Math.round((float) animator.getAnimatedValue());
				view.setLayoutParams(params);
			}
		});
		animator.start();
	}

}
