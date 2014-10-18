package com.welbits.izanrodrigo.emptyview.library;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by IzanRodrigo on 10/09/2014.
 */
public class EmptyView extends RelativeLayout {

   ////////////
   // FIELDS //
   ////////////
   private boolean fadeAnimation;
   private Integer textPadding;
   private String emptyText;
   private float fifteenDP;
   private int textSize;
   private LinearLayout errorLayout;
   private ProgressBar progressBar;
   private TextView emptyTextView;
   private TextView errorTextView;
   private Button retryButton;
   private String errorText;
   private View content;


   //////////////////
   // CONSTRUCTORS //
   //////////////////
   public EmptyView(Context context) {
      super(context);
   }

   public EmptyView(Context context, AttributeSet attrs) {
      super(context, attrs);
      readAttrs(attrs);
   }

   public EmptyView(Context context, AttributeSet attrs, int defStyle) {
      super(context, attrs, defStyle);
      readAttrs(attrs);
   }

   /////////////////////////
   // INITIALIZER METHODS //
   /////////////////////////
   private void readAttrs(AttributeSet attrs) {
      // Load attrs
      TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(
            attrs,
            R.styleable.EmptyView,
            0, 0
      );

      // Read from attrs
      try {
         emptyText = typedArray.getString(R.styleable.EmptyView_emptyText);
         errorText = typedArray.getString(R.styleable.EmptyView_errorText);
         fifteenDP = TypedValue.applyDimension(
               TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
         textPadding = ((Float) typedArray.getDimension(
               R.styleable.EmptyView_textPadding, fifteenDP)).intValue();
         textSize = typedArray.getInteger(R.styleable.EmptyView_textSizeSP, 20);
         fadeAnimation = typedArray.getBoolean(R.styleable.EmptyView_fadeAnimation, true);
      } finally {
         typedArray.recycle();
         checkAttrs();
      }
   }

   private void init() {
      // Initial config
      if (fadeAnimation) {
         setLayoutTransition(new LayoutTransition());
      }

      // Progress bar
      int wC = LayoutParams.WRAP_CONTENT;
      LayoutParams progressBarParams = new LayoutParams(wC, wC);
      progressBarParams.addRule(CENTER_IN_PARENT, TRUE);
      progressBar = new ProgressBar(getContext());
      progressBar.setLayoutParams(progressBarParams);
      addView(progressBar);

      // Empty
      emptyTextView = new TextView(getContext());
      emptyTextView.setId(android.R.id.text1);
      emptyTextView.setGravity(Gravity.CENTER);
      emptyTextView.setPadding(textPadding, 0, textPadding, 0);
      emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
      LayoutParams mPwPlP = new LayoutParams(LayoutParams.MATCH_PARENT, wC);
      mPwPlP.addRule(CENTER_IN_PARENT, TRUE);
      emptyTextView.setLayoutParams(mPwPlP);
      if (!TextUtils.isEmpty(emptyText)) emptyTextView.setText(emptyText);
      addView(emptyTextView);

      // Error layout
      // 0. Container
      errorLayout = new LinearLayout(getContext());
      errorLayout.setOrientation(LinearLayout.VERTICAL);
      errorLayout.setLayoutParams(mPwPlP);
      addView(errorLayout);

      // 1. TextView
      errorTextView = new TextView(getContext());
      errorTextView.setId(android.R.id.text2);
      errorTextView.setGravity(Gravity.CENTER);
      errorTextView.setPadding(textPadding, 0, textPadding, 0);
      errorTextView.setLayoutParams(mPwPlP);
      errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
      if (!TextUtils.isEmpty(errorText)) errorTextView.setText(errorText);
      errorLayout.addView(errorTextView);

      // Display progress bar by default
      startLoading();
   }

   /////////////////////////
   // CHECK ATTRS METHODS //
   /////////////////////////
   private void checkAttrs() {
      if (textSize < 0) {
         throw new IllegalArgumentException("textSizeSP must be a positive number");
      }
   }

   private void checkEmptyText() {
      if (TextUtils.isEmpty(emptyText)) {
         throw new IllegalArgumentException("Empty text cannot be null (use 'empty' method or set value in XML)");
      }
   }

   private void checkErrorText() {
      if (TextUtils.isEmpty(errorText)) {
         throw new IllegalArgumentException("Error text cannot be null (use 'error' method or set value in XML)");
      }
   }

   //////////////////
   // VIEW METHODS //
   //////////////////
   @Override
   public final void addView(View child, ViewGroup.LayoutParams params) {
      if (getChildCount() > 0) {
         throw new IllegalStateException("EmptyView can host only one direct child");
      }

      // Add child
      content = child;
      super.addView(child, -1, params);

      // Initialize widget
      init();
   }

   /////////////////////
   // BUILDER METHODS //
   /////////////////////
   public EmptyView empty(int textResource) {
      try {
         return empty(getContext().getString(textResource));
      } catch (Exception ignored) {
         throw new IllegalArgumentException("Text resource must be a valid ResourceInt");
      }
   }

   public EmptyView empty(String emptyText) {
      if (TextUtils.isEmpty(emptyText)) {
         throw new IllegalArgumentException("Empty text cannot be null");
      }

      this.emptyText = emptyText;
      emptyTextView.setText(emptyText);

      return this;
   }

   public EmptyView error(int textResource) {
      try {
         return error(getContext().getString(textResource));
      } catch (Exception ignored) {
         throw new IllegalArgumentException("Text resource must be a valid ResourceInt");
      }
   }

   public EmptyView error(String errorText) {
      if (TextUtils.isEmpty(errorText)) {
         throw new IllegalArgumentException("Error text cannot be null");
      }

      this.errorText = errorText;
      errorTextView.setText(errorText);

      return this;
   }

   public EmptyView retry(int textResource, OnClickListener onClickListener) {
      try {
         return retry(getContext().getString(textResource), onClickListener);
      } catch (Exception ignored) {
         throw new IllegalArgumentException("Text resource must be a valid ResourceInt");
      }
   }

   public EmptyView retry(String retryText, OnClickListener onClickListener) {
      if (TextUtils.isEmpty(retryText)) {
         throw new IllegalArgumentException("Retry text cannot be null");
      }

      if (onClickListener == null) {
         throw new IllegalArgumentException("Button callback cannot be null");
      }

      // Inflate button
      if (retryButton == null) {
         int wC = LayoutParams.WRAP_CONTENT;
         retryButton = new Button(getContext());
         LinearLayout.LayoutParams retryButtonParams = new LinearLayout.LayoutParams(wC, wC);
         retryButtonParams.gravity = Gravity.CENTER;
         retryButtonParams.topMargin = (int) fifteenDP;
         retryButton.setLayoutParams(retryButtonParams);
         errorLayout.addView(retryButton);
      }

      retryButton.setText(retryText);
      retryButton.setOnClickListener(onClickListener);

      return this;
   }

   public EmptyView retry(int textResource, final Runnable runnable) {
      return retry(textResource, new OnClickListener() {
         @Override
         public void onClick(View v) {
            runnable.run();
         }
      });
   }

   public EmptyView retry(String retryText, final Runnable runnable) {
      return retry(retryText, new OnClickListener() {
         @Override
         public void onClick(View v) {
            runnable.run();
         }
      });
   }

   public EmptyView textConfigResources(int paddingRes, int sizeRes) {
      Resources resources = getContext().getResources();
      int textPadding, textSize;

      try {
         textPadding = (int) resources.getDimension(paddingRes);
      } catch (Exception ignored) {
         throw new IllegalArgumentException("Padding must be a valid dimension (e.g 15dp)");
      }

      try {
         textSize = resources.getInteger(sizeRes);
      } catch (Exception ignored) {
         throw new IllegalArgumentException("Size must be a valid integer");
      }

      return textConfig(textPadding, textSize);
   }

   public EmptyView textConfig(Integer textPadding, Integer textSize) {
      if (textPadding == null || textSize == null) {
         throw new IllegalArgumentException("Neither 'textPadding' nor 'textSize' can be null");
      }

      this.textPadding = textPadding;
      this.textSize = textSize;

      emptyTextView.setPadding(textPadding, 0, textPadding, 0);
      errorTextView.setPadding(textPadding, 0, textPadding, 0);
      emptyTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
      errorTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

      return this;
   }

   public EmptyView fadeAnimation(boolean enabled) {
      fadeAnimation = enabled;

      if (fadeAnimation) {
         if (getLayoutTransition() == null) {
            setLayoutTransition(new LayoutTransition());
         }
      } else {
         setLayoutTransition(null);
      }

      return this;
   }

   /////////////////////
   // VIEWS ACCESSORS //
   /////////////////////
   public View getContent() {
      return content;
   }

   public ProgressBar getProgressBar() {
      return progressBar;
   }

   public Button getRetryButton() {
      return retryButton;
   }

   public TextView getErrorTextView() {
      return errorTextView;
   }

   public TextView getEmptyTextView() {
      return emptyTextView;
   }

   /////////////////////////////
   // VIEW VISIBILITY METHODS //
   /////////////////////////////
   public void startLoading() {
      content.setVisibility(GONE);
      emptyTextView.setVisibility(GONE);
      errorLayout.setVisibility(GONE);
      progressBar.setVisibility(VISIBLE);
   }

   public void successLoading() {
      progressBar.setVisibility(GONE);
      emptyTextView.setVisibility(GONE);
      errorLayout.setVisibility(GONE);
      content.setVisibility(VISIBLE);
   }

   public void errorLoading() {
      checkErrorText();
      progressBar.setVisibility(GONE);
      emptyTextView.setVisibility(GONE);
      errorLayout.setVisibility(VISIBLE);
      content.setVisibility(GONE);
   }

   public void displayEmpty() {
      checkEmptyText();
      progressBar.setVisibility(GONE);
      emptyTextView.setVisibility(VISIBLE);
      errorLayout.setVisibility(GONE);
      content.setVisibility(GONE);
   }

}
