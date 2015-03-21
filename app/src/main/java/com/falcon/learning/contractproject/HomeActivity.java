package com.falcon.learning.contractproject;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;


public class HomeActivity extends ActionBarActivity {
    ViewPager pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

    }

    private void init() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(new SwipeAdapter(getSupportFragmentManager()));

        pager.setOnPageChangeListener(onPageChangeListener);

        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.addTab(actionBar.newTab().setText("Home").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Phone").setTabListener(tabListener));
        actionBar.addTab(actionBar.newTab().setText("Others").setTabListener(tabListener));

    }

    private ViewPager.OnPageChangeListener onPageChangeListener= new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int i, float v, int i2) {

        }

        @Override
        public void onPageSelected(int i) {
            getSupportActionBar().setSelectedNavigationItem(i);
        }

        @Override
        public void onPageScrollStateChanged(int i) {

        }
    };



    private ActionBar.TabListener tabListener=new ActionBar.TabListener() {


        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
            pager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

        }
    };

        private class SwipeAdapter extends FragmentPagerAdapter {


        public SwipeAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {

            Fragment fragment=null;

            switch (i){
                case 0 :
                    fragment=new HomeFragment();
                    break;
                case 1:
                    fragment=new PhoneContactFragment();
                    break;
                case 2:
                    fragment =new OtherContactFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}