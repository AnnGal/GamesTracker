package art.manguste.android.gamesearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.List;

import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.db.GameDatabase;
import art.manguste.android.gamesearch.threads.GamesApiLoader;
import art.manguste.android.gamesearch.core.SearchType;
import art.manguste.android.gamesearch.db.FavoriteGame;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;
import static art.manguste.android.gamesearch.api.JsonParser.parseGameData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesListFragment extends Fragment {

    private static final String TAG = GamesListFragment.class.getSimpleName()+"CheckLoader";

    private static final String SEARCH_TYPE = "search_type";
    private static final int LOADER_BY_NAME_ID = 1;
    private static final int LOADER_HOT_ID = 2;

    private ProgressBar mProgressBar;
    private EditText mSearchByNameTextView;
    private GameCardAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SearchType searchType;
    LoaderManager mLoaderManager;
    private GameDatabase gameDatabase;

    public GamesListFragment() {
        // Required empty public constructor
    }

    /**
     * New instance via factory method
     * this fragment using the provided parameters.
     *
     * @param searchType - determines what kind of search and API request to use
     * @return new instance of fragment GamesListFragment.
     */
    public static GamesListFragment newInstance(SearchType searchType) {
        GamesListFragment fragment = new GamesListFragment();
        Bundle args = new Bundle();
        args.putString(SEARCH_TYPE, String.valueOf(searchType));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            searchType = SearchType.valueOf(getArguments().getString(SEARCH_TYPE));
        }
        mLoaderManager = LoaderManager.getInstance(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_search, container, false);
        mProgressBar = view.findViewById(R.id.pb_loading);
        mSearchByNameTextView = view.findViewById(R.id.et_search_by_name);

        // Recycler view stuff
        mRecyclerView = view.findViewById(R.id.rv_games_list);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.icon_size) * 2;

        mAdapter = new GameCardAdapter(getContext(), imageSize, container, searchType);
        mRecyclerView.setAdapter(mAdapter);

        // connect data and view
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        if (!SearchType.SEARCH.equals(searchType)){
            (view.findViewById(R.id.ll_search_by_name)).setVisibility(View.GONE);
        }

        // on a click "start search" button
        (view.findViewById(R.id.btn_start_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameSearch(true);
            }
        });

        Log.d(TAG, "onCreateView " + searchType.toString());

        //mLoaderManager.initLoader(LOADER_HOT_ID, null, this);
        gameDatabase = GameDatabase.getInstance(getContext());
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated " + searchType.toString());
        // TODO try to change on ViewModel
        if (!SearchType.SEARCH.equals(searchType)){
            startGameSearch(false);
        }
    }


    /**
     * Go to API and grab data
     * */
    private void startGameSearch(boolean forceNewRequest) {
        if (SearchType.HOT.equals(searchType)) {
            if (mLoaderManager.getLoader(LOADER_HOT_ID) == null){
                mLoaderManager.initLoader(LOADER_HOT_ID, null, mAPILoaderCallbacks);
            }
        } else if (SearchType.SEARCH.equals(searchType)){
            if (mLoaderManager.getLoader(LOADER_BY_NAME_ID) == null){
                mLoaderManager.initLoader(LOADER_BY_NAME_ID, null, mAPILoaderCallbacks);
            } else if (forceNewRequest){
                mLoaderManager.restartLoader(LOADER_BY_NAME_ID, null, mAPILoaderCallbacks);
            }
        } else if (SearchType.FAVORITE.equals(searchType)){
            getFavoriteGames();
        }
    }

    private final LoaderManager.LoaderCallbacks<ArrayList<Game>> mAPILoaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<Game>>() {

                @Override
                @NonNull
                public Loader<ArrayList<Game>> onCreateLoader(int id, @Nullable Bundle args) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    Log.d(TAG, "onCreateLoader " + searchType.toString() +" with Context="+getContext());

                    String searchTxt = String.valueOf(mSearchByNameTextView.getText());
                    String urlString = formURL(searchType, searchTxt);
                    return new GamesApiLoader(getContext(), urlString, searchType);
                }

                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<Game>> loader, ArrayList<Game> data) {
                    mProgressBar.setVisibility(View.GONE);

                    //TODO if none games found - set text about it

                    // change data in view
                    if (data != null && !data.isEmpty()) {
                        mAdapter.setGames(data);
                    }
                    Log.d(TAG, "onLoadFinished " + searchType.toString());
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<Game>> loader) {
                    //Log.d(TAG, "onLoaderReset " + searchType.toString());
                    //mAdapter.setGames(null);
                }
            };

    // for Favorite Games Fragment
    // LiveData + observe
    private void getFavoriteGames() {
        LiveData<List<FavoriteGame>> favGames
                = (LiveData<List<FavoriteGame>>) gameDatabase.favoriteGameDao().selectAll();

        favGames.observe(getActivity(), new Observer<List<FavoriteGame>>() {
            @Override
            public void onChanged(List<FavoriteGame> favoriteGames) {
                ArrayList<Game> games = new ArrayList();
                for (FavoriteGame favGame : favoriteGames) {
                    if (favGame.getJson() != null){
                        games.add(parseGameData(favGame.getJson(), true));
                    }
                }

                // change data in view
                if (games != null && !games.isEmpty()) {
                    mAdapter.setGames(games);
                }

            }

        });
    }
}