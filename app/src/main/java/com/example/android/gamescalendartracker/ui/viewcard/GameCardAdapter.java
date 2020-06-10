package com.example.android.gamescalendartracker.ui.viewcard;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class GameCardAdapter extends ArrayAdapter<GameCard> {

    public GameCardAdapter(@NonNull Context context, ArrayList<GameCard> games) {
        super(context, 0, games);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewItem = convertView;
        return super.getView(position, convertView, parent);
    }
}
