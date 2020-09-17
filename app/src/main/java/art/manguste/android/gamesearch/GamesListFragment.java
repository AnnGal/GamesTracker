package art.manguste.android.gamesearch;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.util.ArrayList;

import art.manguste.android.gamesearch.get.GamesLoader;
import art.manguste.android.gamesearch.get.SearchType;
import art.manguste.android.gamesearch.ui.viewcard.GameCard;
import art.manguste.android.gamesearch.ui.viewcard.GameCardRVAdapter;

import static art.manguste.android.gamesearch.get.URLMaker.formURL;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GamesListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GamesListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<GameCard>> {

    private static final String SEARCH_TYPE = "search_type";
    private static final int LOADER_BY_NAME_ID = 1;
    private static final int LOADER_HOT_ID = 2;

    private ProgressBar progressBar;
    private GameCardRVAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SearchType searchType;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_game_search, container, false);
        progressBar = view.findViewById(R.id.pb_loading);

        // Recycler view stuff
        mRecyclerView = view.findViewById(R.id.rv_games_list);
        mRecyclerView.setAdapter(new GameCardRVAdapter(new ArrayList<GameCard>()));
        // connect data and view
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        //
        if (SearchType.HOT.equals(searchType)){
            ((LinearLayout) view.findViewById(R.id.ll_search_by_name)).setVisibility(View.GONE);
        }

        // on a click "start search" button
        (view.findViewById(R.id.btn_start_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameSearch();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SearchType.HOT.equals(searchType)){
            startGameSearch();
        }
    }

    /**
     * Go to API and get data
     * */
    private void startGameSearch() {
        LoaderManager loaderManager = LoaderManager.getInstance(this);

        if (SearchType.HOT.equals(searchType)) {
            if (loaderManager.getLoader(LOADER_HOT_ID) == null){
                loaderManager.initLoader(LOADER_HOT_ID, null, this);
            } else {
                loaderManager.restartLoader(LOADER_HOT_ID, null, this);
            }
        } else if (SearchType.SEARCH.equals(searchType)){
            if (loaderManager.getLoader(LOADER_BY_NAME_ID) == null){
                loaderManager.initLoader(LOADER_BY_NAME_ID, null, this);
            } else {
                loaderManager.restartLoader(LOADER_BY_NAME_ID, null, this);
            }
        }
    }

    //********* Loader begin *********//
    /**
     * Start search
     * */
    @NonNull
    @Override
    public Loader<ArrayList<GameCard>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        String searchTxt = String.valueOf(((EditText) getView().findViewById(R.id.et_search_by_name)).getText());
        String urlString = formURL(this.searchType, searchTxt);

        return new GamesLoader(getContext(), urlString);
    }

    /**
     * After search ended
     * */
    @Override
    public void onLoadFinished(@NonNull Loader<ArrayList<GameCard>> loader, ArrayList<GameCard> data) {
        progressBar.setVisibility(View.GONE);

        //TODO if none games found - set text about it

        // change data in view
        if (data != null && !data.isEmpty()) {
            //mAdapter.add(data);
            mRecyclerView.setAdapter(new GameCardRVAdapter(data));
            //Toast.makeText(getContext(), "Got "+(data.size() + " games"), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Reset data
     * */
    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<GameCard>> loader) {
        mRecyclerView.setAdapter(new GameCardRVAdapter(new ArrayList<GameCard>()));
    }
    //********* Loader end *********//
}