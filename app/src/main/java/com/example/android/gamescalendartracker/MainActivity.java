package com.example.android.gamescalendartracker;

import android.os.Bundle;
import android.widget.ListView;

import com.example.android.gamescalendartracker.ui.viewcard.GameCard;
import com.example.android.gamescalendartracker.ui.viewcard.GameCardAdapter;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*//make an adapter
        GameListsAdapter gameListsAdapter = new GameListsAdapter(this, getSupportFragmentManager());
        // add the adapter to ViewPager
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(gameListsAdapter);

        // connect Tabs to ViewPager
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);*/


        final ArrayList<GameCard> games = new ArrayList<>();
        games.add(new GameCard(0,"RimWorld",  "RimWorld is a sci-fi colony sim driven by an intelligent AI storyteller","developer", "publisher", new Date()));
        games.add(new GameCard(0,"Dishonored",  "It is you who will decide what to do with your enemies. You play as Corvo Attano, Empress&#39; bodyguard, a masterful assassin and a combat specialist. The game reacts to your choices - grim atmosphere by itself can be turned even darker by killing people or slightly lighter by not doing so. It is only a player&#39;s choice what to do with his abilities. Basing on these actions the game will give you with two different endings of the story. Dishonored is the game about stealth. Or action and killing people.","developer2", "publisher2", new Date()));

        GameCardAdapter adapter = new GameCardAdapter(this, games);
        ListView listView = findViewById(R.id.list);
        listView.setAdapter(adapter);

    }
}