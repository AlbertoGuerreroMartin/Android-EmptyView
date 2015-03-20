package com.welbits.izanrodrigo.emptyview.library;

import android.view.View;

/**
 * Created by IzanRodrigo on 19/03/2015.
 */
public interface EmptyView {
   View getEmptyView();
   View getErrorView();
   View getLoadingView();
   View getContentView();

   void showEmpty();
   void showError();
   void showLoading();
   void showContent();
}
