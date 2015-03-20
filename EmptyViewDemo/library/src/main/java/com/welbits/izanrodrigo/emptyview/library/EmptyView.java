package com.welbits.izanrodrigo.emptyview.library;

import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by IzanRodrigo on 19/03/2015.
 */
public interface EmptyView {
   @Nullable
   View getEmptyView();

   @Nullable
   View getErrorView();

   @Nullable
   View getLoadingView();

   View getContentView();

   void showEmpty();

   void showError();

   void showLoading();

   void showContent();
}
