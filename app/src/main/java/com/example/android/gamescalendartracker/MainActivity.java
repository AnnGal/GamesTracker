package com.example.android.gamescalendartracker;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.android.gamescalendartracker.apijson.GamesSearch;
import com.example.android.gamescalendartracker.apijson.HttpHandler;
import com.example.android.gamescalendartracker.ui.main.GameListsAdapter;
import com.example.android.gamescalendartracker.ui.viewcard.GameCard;
import com.example.android.gamescalendartracker.ui.viewcard.GameCardAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private int[] icons = {
            R.drawable.ic_action_search,
            R.drawable.ic_action_fire,
            R.drawable.ic_action_star
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //make an adapter
        GameListsAdapter gameListsAdapter = new GameListsAdapter(this, getSupportFragmentManager());
        // add the adapter to ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(gameListsAdapter);

        // connect Tabs to ViewPager
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

//You tab icons
        for (int i = 0; i < tabs.getTabCount(); i++) {
            tabs.getTabAt(i).setIcon(icons[i]);
        }
/*
        final ArrayList<GameCard> games = new ArrayList<>();
//        games.add(new GameCard(0,"RimWorld",  "RimWorld is a sci-fi colony sim driven by an intelligent AI storyteller","developer", "publisher", new Date()));
//        games.add(new GameCard(0,"Dishonored",  "You play as Corvo Attano, Empress bodyguard, a masterful assassin and a combat specialist. The game reacts to your choices - grim atmosphere by itself can be turned even darker by killing people or slightly lighter by not doing so. It is only a player&#39;s choice what to do with his abilities. Basing on these actions the game will give you with two different endings of the story. Dishonored is the game about stealth. Or action and killing people.","developer2", "publisher2", new Date()));
//        games.add(new GameCard("RimWorld",  "RimWorld is a sci-fi colony sim driven by an intelligent AI storyteller"));
//        games.add(new GameCard("Dishonored",  "You play as Corvo Attano, Empress bodyguard, a masterful assassin and a combat specialist"));

        ArrayList<GameCard> games_from_web = (new GamesSearch()).makeSearch();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (GameCard gameCard : games_from_web) {
            games.add(gameCard);
            Log.e("MainActivity.class", gameCard.getTitle()+" - "+gameCard.getDescription());
        }

        for (GameCard game : games) {
            Log.e("MainActivity.class", game.getTitle()+" - "+game.getDescription());
        }

        *//*final ArrayList<GameCard> *//*
        GameCardAdapter adapter = new GameCardAdapter(this, games);


        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);*/


    }
}