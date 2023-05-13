package com.efunhub.ticktok.activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.efunhub.ticktok.R;
import com.efunhub.ticktok.application.SessionManager;
import com.efunhub.ticktok.fragments.AllVideoFragment;
import com.efunhub.ticktok.fragments.Following_VideoFragment;
import com.efunhub.ticktok.fragments.NotificationsFragment;
import com.efunhub.ticktok.fragments.ProfileFragment;
import com.efunhub.ticktok.fragments.SearchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    int backCount = 0;
    TabLayout tabLayout;
    Fragment fragment;
    TextView tv_foryou_video, tv_following_video;
    FragmentTransaction fragmentTransaction;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tiktok);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

          fragment = new AllVideoFragment();
         loadFragment();
    }


    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (fragment instanceof AllVideoFragment && fragment.isVisible()) {
            // Handle the back press event of MyFragment
            ((NotificationsFragment) fragment).onBackPressed();
        } else {
            super.onBackPressed();
        }
    }
    /*private void initTabData() {
        tabLayout = findViewById(R.id.tab_layout);
        ViewPager pager2 = findViewById(R.id.view_pager2);
        createViewPager(pager2);
        tabLayout.setupWithViewPager(pager2);

        setupTabIcons();


    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("For You");

        Objects.requireNonNull(tabLayout.getTabAt(0)).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Following");

        Objects.requireNonNull(tabLayout.getTabAt(1)).setCustomView(tabTwo);
    }

    private void createViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new AllVideoFragment(), "For You");
        adapter.addFrag(new Following_VideoFragment(), "Following");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }*/
    public void clearBackStackInclusive(String tag) {
        getSupportFragmentManager().popBackStack(tag, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment = new AllVideoFragment();

                    //fragment = new HomeFragment();
                    //  loadFragment();
                    //title.setText(sessionManager.getNAME());
                    // title.setText(appName);
                    //  icon.setImageResource(Integer.parseInt(sessionManager.getLOGO()));
                    // icon.setImageResource(R.drawable.salon);
                    return true;

                case R.id.navigation_Search:
                    fragment = new SearchFragment();
                    //  loadFragment();
                    //   icon.setImageResource(Integer.parseInt(sessionManager.getLOGO()));
                    //icon.setImageResource(R.drawable.backwhite);
                    return true;

                case R.id.navigation_uploadvideo:
                    //fragment = new AppointmentFragment();
                    //  loadFragment();
                    startActivity(new Intent(MainActivity.this, UploadVideoActivity.class));
                    //  startActivity(new Intent(MainActivity.this,VideoEditorDemoActivity.class));
                    // fragment = new UploadVideoActivity();
                    //loadFragment();
                    //title.setText("Appointments");
                    //   icon.setImageResource(Integer.parseInt(sessionManager.getLOGO()));
                    //  icon.setImageResource(R.drawable.backwhite);
                    return true;

                case R.id.navigation_profile:
                    if (SessionManager.onGetAutoCustomerId().isEmpty()) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    } else {
                        fragment = new ProfileFragment();
                        // loadFragment();
                    }
                    // icon.setImageResource(Integer.parseInt(sessionManager.getLOGO()));
                    // icon.setImageResource(R.drawable.backwhite);
                    return true;
                case R.id.navigation_notifications:
                    fragment = new NotificationsFragment();
                    // loadFragment();
                    // icon.setImageResource(Integer.parseInt(sessionManager.getLOGO()));
                    // icon.setImageResource(R.drawable.backwhite);
                    return true;
            }
            return false;
        }
    };

    @SuppressLint("ResourceAsColor")
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.tv_foryou_video:
//                tv_following_video.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgrey));
//                tv_foryou_video.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//                loadFragment();
//                break;
//            case R.id.tv_following_video:
//                tv_following_video.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.white));
//                tv_foryou_video.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.lightgrey));
//                loadFollowing_video_Fragment();
//                break;

        }
    }

//    private void loadFollowing_video_Fragment() {
//        fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(R.id.nav_host_fragment, new Following_VideoFragment());
//        fragmentTransaction.commit();
//
//
//    }

    private void loadFragment() {
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, new AllVideoFragment());
        fragmentTransaction.commit();

        /*FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, new AllVideoFragment())
                .addToBackStack(String.valueOf(fm))
                .commit();*/
    }
}