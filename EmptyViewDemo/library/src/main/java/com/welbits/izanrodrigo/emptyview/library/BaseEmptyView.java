package com.welbits.izanrodrigo.emptyview.library;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * Created by IzanRodrigo on 19/03/2015.
 */
public abstract class BaseEmptyView extends RelativeLayout implements EmptyView {
   // Fields
   private View contentView;

   // Constructors
   public BaseEmptyView(Context context) {
      super(context);
   }

   public BaseEmptyView(Context context, AttributeSet attrs) {
      super(context, attrs);
      readAttrs(attrs);
   }

   public BaseEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      readAttrs(attrs);
   }

   // Initialization methods
   @Override
   public final void addView(View child) {
      initializeWidget(child);

      // Add child
      super.addView(child);
   }

   @Override
   public final void addView(View child, ViewGroup.LayoutParams params) {
      initializeWidget(child);

      // Add child
      super.addView(child, -1, params);
   }

   private void initializeWidget(View contentView) {
      if (getChildCount() > 0) {
         throw new IllegalStateException("EmptyView can host only one direct child");
      }

      // Initialize widget
      this.contentView = contentView;
      if (getEmptyView() != null) super.addView(getEmptyView());
      if (getErrorView() != null) super.addView(getErrorView());
      if (getLoadingView() != null) super.addView(getLoadingView());

      // Show content by default
      showContent();
   }

   private void readAttrs(AttributeSet attrs) {
      // Load attrs
      TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.BaseEmptyView,
            0, 0
      );

      // Read from attrs
      try {
         // Fade animation
         if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            setFadeAnimation(typedArray.getBoolean(R.styleable.BaseEmptyView_fadeAnimation, true));
         }
      } finally {
         typedArray.recycle();
      }
   }

   @TargetApi(Build.VERSION_CODES.HONEYCOMB)
   public void setFadeAnimation(boolean enabled) {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
         if (enabled) {
            if (getLayoutTransition() == null) {
               setLayoutTransition(new LayoutTransition());
            }
         } else {
            setLayoutTransition(null);
         }
      }
   }

   @Override
   public View getContentView() {
      return contentView;
   }

   // Methods for each state
   @Override
   public void showEmpty() {
      if (getEmptyView() != null) {
         updateViewsVisibility(EmptyViewState.EMPTY);
      } else {
         throw new IllegalStateException("emptyView must not be null");
      }
   }

   @Override
   public void showError() {
      if (getErrorView() != null) {
         updateViewsVisibility(EmptyViewState.ERROR);
      } else {
         throw new IllegalStateException("errorView must not be null");
      }
   }

   @Override
   public void showLoading() {
      if (getLoadingView() != null) {
         updateViewsVisibility(EmptyViewState.LOADING);
      } else {
         throw new IllegalStateException("loadingView must not be null");
      }
   }

   @Override
   public void showContent() {
      if (getContentView() != null) {
         updateViewsVisibility(EmptyViewState.CONTENT);
      } else {
         throw new IllegalStateException("contentView must not be null");
      }
   }

   private void updateViewsVisibility(EmptyViewState state) {
      if (getEmptyView() != null)
         getEmptyView().setVisibility(state == EmptyViewState.EMPTY ? VISIBLE : GONE);
      if (getErrorView() != null)
         getErrorView().setVisibility(state == EmptyViewState.ERROR ? VISIBLE : GONE);
      if (getLoadingView() != null)
         getLoadingView().setVisibility(state == EmptyViewState.LOADING ? VISIBLE : GONE);
      if (getContentView() != null)
         getContentView().setVisibility(state == EmptyViewState.CONTENT ? VISIBLE : GONE);
   }

   // Internal classes
   private enum EmptyViewState {
      EMPTY, ERROR, LOADING, CONTENT
   }
}
