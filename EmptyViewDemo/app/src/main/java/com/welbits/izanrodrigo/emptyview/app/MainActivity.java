package com.welbits.izanrodrigo.emptyview.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;

import com.astuetz.PagerSlidingTabStrip;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by IzanRodrigo on 20/03/2015.
 */
public class MainActivity extends ActionBarActivity {

   @InjectView(R.id.tabStrip) PagerSlidingTabStrip tabStrip;
   @InjectView(R.id.viewPager) ViewPager viewPager;

   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      getSupportActionBar().setElevation(0.0f);
      ButterKnife.inject(this);

      // Configure view pager
      PagerAdapter pagerAdapter = new PagerAdapter();
      viewPager.setOffscreenPageLimit(pagerAdapter.getCount());
      viewPager.setAdapter(pagerAdapter);
      tabStrip.setViewPager(viewPager);
   }

   // Internal classes
   private class PagerAdapter extends FragmentPagerAdapter {

      private final List<TabInfo> tabs;

      public PagerAdapter() {
         super(getSupportFragmentManager());
         tabs = new ArrayList<TabInfo>() {
            {
               add(new TabInfo(new BaseFragment.BasicEmptyViewFragment(), "Basic"));
               add(new TabInfo(new BaseFragment.CustomEmptyViewFragment(), "Custom"));
            }
         };
      }

      @Override
      public Fragment getItem(int position) {
         return tabs.get(position).fragment;
      }

      @Override
      public CharSequence getPageTitle(int position) {
         return tabs.get(position).title;
      }

      @Override
      public int getCount() {
         return tabs.size();
      }

      private final class TabInfo {
         public final Fragment fragment;
         public final String title;

         private TabInfo(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
         }
      }
   }
}
