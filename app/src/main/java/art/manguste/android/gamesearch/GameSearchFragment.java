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
import android.widget.ProgressBar;

import java.util.ArrayList;

import art.manguste.android.gamesearch.R;
import art.manguste.android.gamesearch.get.GamesLoader;
import art.manguste.android.gamesearch.ui.viewcard.GameCard;
import art.manguste.android.gamesearch.ui.viewcard.GameCardRVAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GameSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GameSearchFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<ArrayList<GameCard>> {

    private static final int GAME_LOADER_ID = 1;

    private ProgressBar progressBar;
    private GameCardRVAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public GameSearchFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GameSearchFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GameSearchFragment newInstance(String param1, String param2) {
        GameSearchFragment fragment = new GameSearchFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
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

        // on click "start search" button
        (view.findViewById(R.id.btn_start_search)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGameSearch();
            }
        });

        return view;
    }

    /**
     * Go to API and get data
     * */
    private void startGameSearch() {
        //LoaderManager loaderManager = getFragmentManager().getSupportLoaderManager();

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).
        //loaderManager.initLoader(GAME_LOADER_ID, null, this);

        // TODO solve getLoaderManager()
        if (getLoaderManager().getLoader(GAME_LOADER_ID) == null){
            getLoaderManager().initLoader(GAME_LOADER_ID, null, this);
        } else {
            getLoaderManager().restartLoader(GAME_LOADER_ID, null, this);
        }
        //ArrayList<GameCard> games_from_web = (new GamesSearch()).makeSearch();

        //ArrayList<GameCard> games_list  = new ArrayList<>();
        //new GamesLoader(getContext(), "https://api.rawg.io/api/games?page_size=5&search=dishonored").execute();
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//
//        Toast.makeText(getContext(), "Got "+(games_list.size() + " games"), Toast.LENGTH_LONG).show();
        // get data
        // place to RV
    }

    //********* Loader begin *********//
    @NonNull
    @Override
    public Loader<ArrayList<GameCard>> onCreateLoader(int id, @Nullable Bundle args) {
        progressBar.setVisibility(View.VISIBLE);

        // TODO Uri.Builder
        String urlString = "https://api.rawg.io/api/games?page_size=5&search="
                +((EditText) getView().findViewById(R.id.et_search_by_name)).getText();

        return new GamesLoader(getContext(), urlString);
    }

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

    @Override
    public void onLoaderReset(@NonNull Loader<ArrayList<GameCard>> loader) {
        mRecyclerView.setAdapter(new GameCardRVAdapter(new ArrayList<GameCard>()));
    }
    //********* Loader end *********//
}