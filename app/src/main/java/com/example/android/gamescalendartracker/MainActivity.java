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

        GamesFolderAdapter gamesFolderAdapter = new GamesFolderAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);

        viewPager.setAdapter(gamesFolderAdapter);
        TabLayout tabs = findViewById(R.id.tabs);



    }
}