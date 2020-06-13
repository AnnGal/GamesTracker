package com.example.android.gamescalendartracker;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.android.gamescalendartracker.ui.main.GamesFolderAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make an adapter
        GamesFolderAdapter gamesFolderAdapter = new GamesFolderAdapter(this, getSupportFragmentManager());
        // add the adapter to ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(gamesFolderAdapter);

        // connect Tabs to ViewPager
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

    }
}