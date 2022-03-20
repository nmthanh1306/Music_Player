package com.is1423.music_player.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.is1423.music_player.R;
import com.is1423.music_player.adapter.MainViewPagerAdapter;
import com.is1423.music_player.fragment.HomeFragment;
import com.is1423.music_player.fragment.SearchFragment;
import com.is1423.music_player.fragment.UserFragment;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindingView();
        bindingAction();
    }

    private void bindingAction() {
        MainViewPagerAdapter mainViewPagerAdapter = new MainViewPagerAdapter(getSupportFragmentManager());
        mainViewPagerAdapter.addFragment(new HomeFragment(), "Trang chủ");
        mainViewPagerAdapter.addFragment(new SearchFragment(), "Tìm kiếm");
        mainViewPagerAdapter.addFragment(new UserFragment(), "Cá nhân");
        viewPager.setAdapter(mainViewPagerAdapter);
//        viewPager.getAdapter().notifyDataSetChanged();
//        viewPager.setOffscreenPageLimit(2);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.icontrangchu);
        tabLayout.getTabAt(1).setIcon(R.drawable.iconsearch);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_action_user);
    }

    private void bindingView() {
        tabLayout = findViewById(R.id.myTabLayout);
        viewPager = findViewById(R.id.myViewPager);
//        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        prefEditor = sharedPreferences.edit();
    }
}