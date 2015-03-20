package com.welbits.izanrodrigo.emptyview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by IzanRodrigo on 19/03/2015.
 */
public class CustomEmptyView extends BaseEmptyView {

   private static final int NO_VALUE = -1;

   // Fields
   private View emptyView;
   private View loadingView;
   private View errorView;

   // Constructors
   public CustomEmptyView(Context context) {
      super(context);
   }

   public CustomEmptyView(Context context, AttributeSet attrs) {
      super(context, attrs);
      readAttrs(attrs);
   }

   public CustomEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      readAttrs(attrs);
   }

   // Initialization methods
   private void readAttrs(AttributeSet attrs) {
      // Load attrs
      TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.CustomEmptyView,
            0, 0
      );

      // Read from attrs
      try {
         LayoutInflater layoutInflater = LayoutInflater.from(getContext());

         // Empty layout
         int emptyLayoutRes = typedArray.getResourceId(R.styleable.CustomEmptyView_emptyLayout, NO_VALUE);
         if (emptyLayoutRes != NO_VALUE) {
            setEmptyView(layoutInflater.inflate(emptyLayoutRes, this, false));
         }

         // Error layout
         int errorLayoutRes = typedArray.getResourceId(R.styleable.CustomEmptyView_errorLayout, NO_VALUE);
         if (errorLayoutRes != NO_VALUE) {
            setErrorView(layoutInflater.inflate(errorLayoutRes, this, false));
         }

         // Loading layout
         int loadingLayoutRes = typedArray.getResourceId(R.styleable.CustomEmptyView_loadingLayout, NO_VALUE);
         if (loadingLayoutRes != NO_VALUE) {
            setLoadingView(layoutInflater.inflate(loadingLayoutRes, this, false));
         }
      } finally {
         typedArray.recycle();
      }
   }

   // Views for each state
   @Override
   public View getEmptyView() {
      return emptyView;
   }

   public void setEmptyView(View emptyView) {
      if (this.emptyView != null) {
         removeView(this.emptyView);
      }

      this.emptyView = emptyView;
   }

   @Override
   public View getErrorView() {
      return errorView;
   }

   public void setErrorView(View errorView) {
      if (this.emptyView != null) {
         removeView(this.emptyView);
      }

      this.errorView = errorView;
   }

   @Override
   public View getLoadingView() {
      return loadingView;
   }

   public void setLoadingView(View loadingView) {
      if (this.emptyView != null) {
         removeView(this.emptyView);
      }

      this.loadingView = loadingView;
   }
}
