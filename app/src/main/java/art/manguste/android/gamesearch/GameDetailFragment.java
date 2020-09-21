package art.manguste.android.gamesearch;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;

import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.get.GamesLoader;
import art.manguste.android.gamesearch.get.SearchType;

import static art.manguste.android.gamesearch.get.URLMaker.formURL;

public class GameDetailFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<Game>>{

    private static final String EXTRA_GAME_CODE = "game_site_code";
    private String gameCode = null;
    private static final int LOADER_GAME_ID = 3;

    //UI
    ProgressBar progressBar;

    public GameDetailFragment() {
        // Required empty public constructor
    }

    public static GameDetailFragment createInstance(String gameCode) {
        GameDetailFragment fragment = new GameDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_GAME_CODE, gameCode);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            gameCode = getArguments().getString(EXTRA_GAME_CODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_detail, container, false);

        // UI objects
        progressBar = view.findViewById(R.id.pb_loading);

        // start Loader
        LoaderManager loaderManager = LoaderManager.getInstance(this);
        loaderManager.initLoader(LOADER_GAME_ID, null, this);

        return view;
    }

    // Loader begin
    @NonNull
    @Override
    public Loader<ArrayList<Game>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        String urlString = formURL(SearchType.GAME, gameCode);

        return new GamesLoader(getContext(), urlString, SearchType.GAME);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
        progressBar.setVisibility(View.GONE);

        //TODO if none games found - set text about it

        // change data in view
        if (data != null && !data.isEmpty()) {
            setInfoOnCard(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<Game>> loader) {
        //
    }
    // Loader end


    private void setInfoOnCard(ArrayList<Game> data) {
        Game game = data.get(0);
    }
}