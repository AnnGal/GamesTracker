package com.example.android.gamescalendartracker.ui.viewcard;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.example.android.gamescalendartracker.R;

import java.util.ArrayList;

public class GameCardAdapter extends ArrayAdapter<GameCard> {

    private static final String LOG_TAG = GameCardAdapter.class.getSimpleName();
    private int mBackgroundColorId = 0;

    public GameCardAdapter(@NonNull Context context, ArrayList<GameCard> games) {
        super(context, 0, games);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View viewItem = convertView;
        if (viewItem == null) {
            viewItem = LayoutInflater.from(getContext()).inflate(
                    R.layout.game_card, parent, false);
        }
        GameCard currentGame = getItem(position);

        if (currentGame != null) {
            ((TextView) viewItem.findViewById(R.id.title)).setText(currentGame.getTitle());
            ((TextView) viewItem.findViewById(R.id.description)).setText(currentGame.getDescription());
        }


        //ImageView iconView = (ImageView) viewItem.findViewById(R.id.cover);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        //if (currentGame.getImage() > 0) {
         //   iconView.setImageResource(R.drawable.ic_launcher_foreground);
        //} else iconView.setVisibility(View.GONE);
        //listItemView = game_card
        //return super.getView(position, convertView, parent);
        return viewItem;
        /*
        // Get the {@link AndroidFlavor} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.textMiwoki);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        nameTextView.setText(currentWord.getMiwokTranslation());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView numberTextView = (TextView) listItemView.findViewById(R.id.textDefault);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        numberTextView.setText(currentWord.getDefaultTranslation());

        // Find the ImageView in the list_item.xml layout with the ID list_item_icon
        ImageView iconView = (ImageView) listItemView.findViewById(R.id.image);
        // Get the image resource ID from the current AndroidFlavor object and
        // set the image to iconView
        if (currentWord.getImage() > 0) {
            iconView.setImageResource(currentWord.getImage());
        } else iconView.setVisibility(View.GONE);
        //iconView.setBackground(R.color.category_numbers);

        if (mBackgroundColorId > 0) {
            LinearLayout lineartLayout = listItemView.findViewById(R.id.card_layout_main);
            Drawable color = ContextCompat.getDrawable(getContext(), mBackgroundColorId);
            lineartLayout.setBackground(color);
        }

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
        */
    }







}
