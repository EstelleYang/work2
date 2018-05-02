package com.lzjs.uappoint.fresco;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.lzjs.uappoint.act.PacsAdviceFragment;
import com.lzjs.uappoint.act.PacsChatMsgFragment;
import com.lzjs.uappoint.fresco.ui.CustomGifViewActivity;
import com.lzjs.uappoint.fresco.ui.GifActivity;
import com.lzjs.uappoint.fresco.ui.ImageListFragment;
import com.lzjs.uappoint.fresco.ui.SubsamplingScaleActivity;
import com.lzjs.uappoint.fresco.ui.ViewPagerActivity;
import com.lzjs.uappoint.R;

import java.util.ArrayList;
import java.util.List;


public class FrescoMainActivity extends AppCompatActivity implements PacsAdviceFragment.OnFragmentInteractionListener{

    private DrawerLayout mDrawerLayout;
    private int tabId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityfresco_main);
        tabId = getIntent().getIntExtra("tabId",0);
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*final ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setHomeAsUpIndicator(R.drawable.ic_menu);
            ab.setDisplayHomeAsUpEnabled(true);
        }*/

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        //NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //if (navigationView != null) {
        //    setupDrawerContent(navigationView);
        //}

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        if (viewPager != null) {
            setupViewPager(viewPager);
            tabLayout.setupWithViewPager(viewPager);

            if (tabId != 0) {
                tabLayout.getTabAt(tabId).select();
            }
        }

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());

        ImageListFragment fragment = new ImageListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("pacsType",1);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "影像");

        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("pacsType",2);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "超声");

        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("pacsType",3);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "检验");

        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 2);
        bundle.putInt("pacsType",4);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "内窥镜");

        fragment = new ImageListFragment();
        bundle = new Bundle();
        bundle.putInt("type", 1);
        bundle.putInt("pacsType",5);
        fragment.setArguments(bundle);
        adapter.addFragment(fragment, "心电图");

        PacsChatMsgFragment pacsChatMsgFragment = PacsChatMsgFragment.newInstance("111");
        adapter.addFragment(pacsChatMsgFragment,"互动消息");

        //PacsAdviceFragment pacsAdviceFragment = PacsAdviceFragment.newInstance("111","222");
        //adapter.addFragment(pacsAdviceFragment, "诊断报告");

        viewPager.setAdapter(adapter);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        mDrawerLayout.closeDrawers();
                        if (menuItem.getItemId() == R.id.nav_viewpager) {
                            Intent intent = new Intent(FrescoMainActivity.this, ViewPagerActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.nav_subsamplingScale) {
                            Intent intent = new Intent(FrescoMainActivity.this, SubsamplingScaleActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.nav_gifview) {
                            Intent intent = new Intent(FrescoMainActivity.this, CustomGifViewActivity.class);
                            startActivity(intent);
                        } else if (menuItem.getItemId() == R.id.nav_home) {
                            Intent intent = new Intent(FrescoMainActivity.this, GifActivity.class);
                            startActivity(intent);
                        }
                        return true;
                    }
                });
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
        //
    }


}
