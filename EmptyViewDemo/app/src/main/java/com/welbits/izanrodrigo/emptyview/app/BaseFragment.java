package com.welbits.izanrodrigo.emptyview.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;
import com.welbits.izanrodrigo.emptyview.library.CustomEmptyView;
import com.welbits.izanrodrigo.emptyview.library.EmptyView;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static org.bitbucket.dollar.Dollar.$;

public abstract class BaseFragment extends Fragment {
   // Constants
   private static final Random RANDOM = new Random();

   // Fields
   @InjectView(android.R.id.empty) EmptyView emptyView;
   @InjectView(android.R.id.list) ListView listView;
   @InjectView(R.id.fab) FloatingActionButton fab;
   private ArrayAdapter<String> adapter;
   private boolean loading;

   @Override
   public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setHasOptionsMenu(true);
   }

   @Override
   public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View view = inflater.inflate(getLayout(), container, false);
      ButterKnife.inject(this, view);
      return view;
   }

   @Override
   public void onActivityCreated(Bundle savedInstanceState) {
      super.onActivityCreated(savedInstanceState);

      // Configure list view
      adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener((p, v, position, id) -> {
         Toast.makeText(getActivity(), adapter.getItem(position), Toast.LENGTH_SHORT).show();
      });

      // Configure fab
      fab.setOnClickListener(v -> loadData());

      // Load data
      loadData();
   }

   protected abstract int getLayout();

   protected void loadData() {
      // STATE 1: LOADING
      if (loading) return;
      loading = true;
      emptyView.showLoading();
      listView.postDelayed(() -> {
         if (RANDOM.nextBoolean()) {
            // STATE 3: ERROR LOADING
            if (RANDOM.nextBoolean()) {
               emptyView.showError();
            }

            // STATE 4: EMPTY
            else {
               emptyView.showEmpty();
            }
         }

         // STATE 2: SUCCESS LOADING
         else {
            adapter.addAll($(0, 25).map(i -> "Item " + (i + 1)).toList());
            emptyView.showContent();
         }
         loading = false;
      }, 2000);
   }

   public static final class BasicEmptyViewFragment extends BaseFragment {
      @Override
      protected int getLayout() {
         return R.layout.fragment_basic_empty_view;
      }

      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         view.findViewById(R.id.emptyView_retryButton).setOnClickListener(v -> {
            loadData();
         });
      }
   }

   public static final class CustomEmptyViewFragment extends BaseFragment {
      @Override
      public void onViewCreated(View view, Bundle savedInstanceState) {
         super.onViewCreated(view, savedInstanceState);
         CustomEmptyView emptyView = (CustomEmptyView) view.findViewById(android.R.id.empty);
         emptyView.setLoadingView(LayoutInflater.from(view.getContext())
               .inflate(R.layout.loading, emptyView, false));
      }

      @Override
      protected int getLayout() {
         return R.layout.fragment_custom_empty_view;
      }
   }
}
