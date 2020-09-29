package art.manguste.android.gamesearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

import art.manguste.android.gamesearch.core.Game;
import art.manguste.android.gamesearch.core.GameCardAdapter;
import art.manguste.android.gamesearch.api.GamesApiLoader;
import art.manguste.android.gamesearch.core.SearchType;
import art.manguste.android.gamesearch.db.FavoriteGame;
import art.manguste.android.gamesearch.db.GamesDBLoader;

import static art.manguste.android.gamesearch.api.URLMaker.formURL;
import static art.manguste.android.gamesearch.core.JsonParser.parseGameData;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesListFragment extends Fragment {

    private static final String TAG = GamesListFragment.class.getSimpleName()+" FragmentLifecycle";

    private static final String SEARCH_TYPE = "search_type";
    private static final int LOADER_BY_NAME_ID = 1;
    private static final int LOADER_HOT_ID = 2;
    private static final int LOADER_FAVORITE_ID = 3;

    private ProgressBar mProgressBar;
    private EditText mSearchByNameTextView;
    private GameCardAdapter mAdapter;
    private SearchType searchType;
    LoaderManager mLoaderManager;

    public GamesListFragment() {
        // Required empty public constructor
    }

    /**
     * New instance via factory method
     * @param searchType - determines what kind of search and API request to use
     * @return new instance of fragment GamesListFragment.
     */
    public static GamesListFragment newInstance(SearchType searchType) {
        GamesListFragment fragment = new GamesListFragment();
        // pack params
        Bundle args = new Bundle();
        args.putString(SEARCH_TYPE, String.valueOf(searchType));
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // unpack params
        if (getArguments() != null) {
            searchType = SearchType.valueOf(getArguments().getString(SEARCH_TYPE));
        }
        // catch loader manager
        mLoaderManager = LoaderManager.getInstance(this);
    }

    @Override
    public void onResume() {
        // temporary, until I not get my hands on LiveData
        if (SearchType.FAVORITE.equals(searchType)){
            startGameSearch(true);
        }
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_search, container, false);
        mProgressBar = view.findViewById(R.id.pb_loading);
        mSearchByNameTextView = view.findViewById(R.id.et_search_by_name);

        // Recycler view stuff
        RecyclerView recyclerView = view.findViewById(R.id.rv_games_list);
        int imageSize = getResources().getDimensionPixelSize(R.dimen.icon_size) * 2;

        // adapter
        mAdapter = new GameCardAdapter(getContext(), imageSize, container, searchType);
        recyclerView.setAdapter(mAdapter);

        // connect data and view
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated " + searchType.toString());
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
            if (mLoaderManager.getLoader(LOADER_FAVORITE_ID) == null){
                mLoaderManager.initLoader(LOADER_FAVORITE_ID, null, mDBLoaderCallbacks);
            } else if (forceNewRequest){
                mLoaderManager.restartLoader(LOADER_FAVORITE_ID, null, mDBLoaderCallbacks);
            }
        }
    }


    /**
     * Loader for API and Http connection
     */
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

    /**
     * Loader for favorite games in database
     */
    private final LoaderManager.LoaderCallbacks<ArrayList<FavoriteGame>> mDBLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<ArrayList<FavoriteGame>>() {

                @Override
                @NonNull
                public Loader<ArrayList<FavoriteGame>> onCreateLoader(int id, @Nullable Bundle args) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    return new GamesDBLoader(getContext());
                }


                @Override
                public void onLoadFinished(@NonNull Loader<ArrayList<FavoriteGame>> loader, ArrayList<FavoriteGame> data) {
                    mProgressBar.setVisibility(View.GONE);

                    //FavoriteGame to Game
                    ArrayList<Game> games = new ArrayList<>();
                    for (FavoriteGame favGame : data) {
                        if (favGame.getJson() != null){
                            games.add(parseGameData(favGame.getJson(), true));
                        }
                    }

                    // change data in view
                    if (!games.isEmpty()) {
                        mAdapter.setGames(games);
                    }
                    Log.d(TAG, "onLoadFinished ");
                }

                @Override
                public void onLoaderReset(@NonNull Loader<ArrayList<FavoriteGame>> loader) {
                    Log.d(TAG, "onLoaderReset ");
                    mAdapter.setGames(null);
                }
            };


}