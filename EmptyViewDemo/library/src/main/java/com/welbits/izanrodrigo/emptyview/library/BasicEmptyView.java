package com.welbits.izanrodrigo.emptyview.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by IzanRodrigo on 19/03/2015.
 */
public class BasicEmptyView extends BaseEmptyView {

   private static final int NO_VALUE = -1;

   // Fields
   private TextView emptyView;
   private ProgressBar loadingView;
   private LinearLayout errorView;

   // Constructors
   public BasicEmptyView(Context context) {
      super(context);
   }

   public BasicEmptyView(Context context, AttributeSet attrs) {
      super(context, attrs);
      readAttrs(attrs);
   }

   public BasicEmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
      super(context, attrs, defStyleAttr);
      readAttrs(attrs);
   }

   private void readAttrs(AttributeSet attrs) {
      // Load attrs
      TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.BasicEmptyView,
            0, 0
      );

      // Read from attrs
      try {
         // Empty text
         String emptyText = typedArray.getString(R.styleable.BasicEmptyView_emptyText);
         if (!TextUtils.isEmpty(emptyText)) {
            getEmptyView().setText(emptyText);
         }

         // Error text
         String errorText = typedArray.getString(R.styleable.BasicEmptyView_errorText);
         TextView errorTextView = (TextView) getErrorView().findViewById(R.id.emptyView_errorText);
         if (!TextUtils.isEmpty(errorText)) {
            errorTextView.setText(errorText);
         }

         // Text padding
         int textPadding = (int) typedArray.getDimension(R.styleable.BasicEmptyView_textPadding, NO_VALUE);
         if (textPadding != NO_VALUE) {
            getEmptyView().setPadding(textPadding, textPadding, textPadding, textPadding);
            errorTextView.setPadding(textPadding, textPadding, textPadding, textPadding);
         }

         // Text size
         int textSize = typedArray.getInteger(R.styleable.BasicEmptyView_textSizeSP, NO_VALUE);
         if (textSize != NO_VALUE) {
            getEmptyView().setTextSize(textSize);
            errorTextView.setTextSize(textSize);
         }
      } finally {
         typedArray.recycle();
      }
   }

   // Views for each state
   @Override
   public TextView getEmptyView() {
      if (emptyView == null) {
         emptyView = (TextView) LayoutInflater.from(getContext())
               .inflate(R.layout.default_text, this, false);
      }
      return emptyView;
   }

   @Override
   public LinearLayout getErrorView() {
      if (errorView == null) {
         errorView = (LinearLayout) LayoutInflater.from(getContext())
               .inflate(R.layout.default_error, this, false);
      }
      return errorView;
   }

   @Override
   public ProgressBar getLoadingView() {
      if (loadingView == null) {
         loadingView = (ProgressBar) LayoutInflater.from(getContext())
               .inflate(R.layout.default_loading, this, false);
      }
      return loadingView;
   }
}
