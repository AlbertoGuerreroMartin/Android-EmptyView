package com.welbits.izanrodrigo.emptyview.app;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.welbits.izanrodrigo.emptyview.library.EmptyView;

import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static org.bitbucket.dollar.Dollar.$;

public class MainActivity extends Activity {
   // Constants
   private static final Random RANDOM = new Random();

   // Fields
   protected @InjectView(android.R.id.empty) EmptyView emptyView;
   protected @InjectView(android.R.id.list) ListView listView;
   private ArrayAdapter<String> adapter;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      ButterKnife.inject(this);

      // Configure list view
      emptyView.retry(R.string.retry, this::loadData);
      adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
      listView.setAdapter(adapter);
      listView.setOnItemClickListener((p, v, position, id) -> {
         Toast.makeText(this, adapter.getItem(position), Toast.LENGTH_SHORT).show();
      });

      // Load data
      loadData();
   }

   private void loadData() {
      // STATE 1: LOADING
      emptyView.startLoading();
      listView.postDelayed(() -> {
         if (RANDOM.nextBoolean()) {
            // STATE 3: ERROR LOADING
            if (RANDOM.nextBoolean()) {
               emptyView.errorLoading();
            }

            // STATE 4: EMPTY
            else {
               emptyView.displayEmpty();
            }
         }

         // STATE 2: SUCCESS LOADING
         else {
            adapter.addAll($(0, 25).map(i -> "Item " + (i + 1)).toList());
            emptyView.successLoading();
         }
      }, 2000);
   }
}
