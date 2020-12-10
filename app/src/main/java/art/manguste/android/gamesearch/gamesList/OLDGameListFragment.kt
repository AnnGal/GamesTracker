package art.manguste.android.gamesearch.gamesList
/*

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.loader.app.LoaderManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import art.manguste.android.gamesearch.R
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.db.GameDatabase
import art.manguste.android.gamesearch.databinding.FragmentGameSearchBinding
*
 * A simple [Fragment] subclass.
 * Use the [GamesListFragment.newInstance] factory method to
 * create an instance of this fragment.


class GamesListFragment1 : Fragment() {

    private lateinit var viewModer: GamesViewModel
    private lateinit var binding: FragmentGameSearchBinding


    //private lateinit var binding: ActivityMainBinding

    private var mProgressBar: ProgressBar? = null
    private var mSearchByNameTextView: EditText? = null
    private var mAdapter: GameCardAdapter? = null
    private var mRecyclerView: RecyclerView? = null
    private var searchType: SearchType? = null
    var mLoaderManager: LoaderManager? = null
    private var gameDatabase: GameDatabase? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            searchType = SearchType.valueOf(arguments!!.getString(SEARCH_TYPE)!!)
        }
        mLoaderManager = LoaderManager.getInstance(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        binding = FragmentGameSearchBinding.inflate(inflater, container, false)


        // set Recycler View
        val imageSize = resources.getDimensionPixelSize(R.dimen.icon_size) * 2
        binding.recyclerGames.adapter = GameCardAdapter(context, imageSize, container, searchType)
        binding.recyclerGames.layoutManager = GridLayoutManager(activity, 1)

        // set fragment appearance
        if (SearchType.SEARCH != searchType) {
            binding.panelSearchGame.visibility = View.GONE
            binding.btnStartSearch.setOnClickListener {
                startGameSearch(true)
            }
        }

        // hook up the view model
        viewModer = ViewModelProvider(this).get(GamesViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated " + searchType.toString())
        // TODO try to change on ViewModel
        if (SearchType.SEARCH != searchType) {
            startGameSearch(false)
        }

    }


    // Inflate the layout for this fragment

  mProgressBar = view.findViewById(R.id.pb_loading)
      mSearchByNameTextView = view.findViewById(R.id.et_search_by_name)

      // Recycler view stuff
      mRecyclerView = view.findViewById(R.id.rv_games_list)
      val imageSize = resources.getDimensionPixelSize(R.dimen.icon_size) * 2
      mAdapter = GameCardAdapter(context, imageSize, container, searchType)
      mRecyclerView?.setAdapter(mAdapter)

      // connect data and view
      mRecyclerView?.setLayoutManager(GridLayoutManager(activity, 1))
      if (SearchType.SEARCH != searchType) {
          view.findViewById<View>(R.id.ll_search_by_name).visibility = View.GONE
      }

      // on a click "start search" button
      view.findViewById<View>(R.id.btn_start_search).setOnClickListener {  }
      Log.d(TAG, "onCreateView " + searchType.toString())

      //mLoaderManager.initLoader(LOADER_HOT_ID, null, this);
      gameDatabase = context?.let { GameDatabase.Companion.getInstance(it) }
      if (SearchType.FAVORITE == searchType) {
          //todo fix favoriteGames
      }


*
     * Go to API and grab data


    private fun startGameSearch(forceNewRequest: Boolean) {
        //todo fix
   if (SearchType.HOT == searchType) {
               if (mLoaderManager!!.getLoader<Any>(LOADER_HOT_ID) == null) {
                   mLoaderManager!!.initLoader(LOADER_HOT_ID, null, mAPILoaderCallbacks)
               }
           } else if (SearchType.SEARCH == searchType) {
               if (mLoaderManager!!.getLoader<Any>(LOADER_BY_NAME_ID) == null) {
                   mLoaderManager!!.initLoader(LOADER_BY_NAME_ID, null, mAPILoaderCallbacks)
               } else if (forceNewRequest) {
                   mLoaderManager!!.restartLoader(LOADER_BY_NAME_ID, null, mAPILoaderCallbacks)
               }
           } else if (SearchType.FAVORITE.equals(searchType)){
               getFavoriteGames();
           }

    }

    //todo fix
 private val mAPILoaderCallbacks: LoaderManager.LoaderCallbacks<ArrayList<Game>> = object : LoaderManager.LoaderCallbacks<ArrayList<Game?>?> {
         override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Game?>?> {
             mProgressBar!!.visibility = View.VISIBLE
             Log.d(TAG, "onCreateLoader " + searchType.toString() + " with Context=" + context)
             val searchTxt = mSearchByNameTextView!!.text.toString()
             val urlString = formURL(searchType!!, searchTxt)
             return GamesApiLoader(context, urlString, searchType)
         }

         override fun onLoadFinished(loader: Loader<ArrayList<Game?>?>, data: ArrayList<Game?>?) {
             mProgressBar!!.visibility = View.GONE

             //TODO if none games found - set text about it

             // change data in view
             if (data != null && !data.isEmpty()) {
                 mAdapter!!.setGames(data)
             }
             Log.d(TAG, "onLoadFinished " + searchType.toString())
         }

         override fun onLoaderReset(loader: Loader<ArrayList<Game?>?>) {
             //Log.d(TAG, "onLoaderReset " + searchType.toString());
             //mAdapter.setGames(null);
         }
     }
// change data in view//View Modesl

    //AppViewModel viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
    //((MainActivity) getActivity()).viewModel.getGamesFavorite().observe(getViewLifecycleOwner(), new Observer<List<FavoriteGame>>() {
// LiveData
    // for Favorite Games Fragment
    // LiveData + observe
    //todo fix
    private val favoriteGames: Unit
        private get() {
            // LiveData
            val favGames = gameDatabase!!.favoriteGameDao().selectAll() as LiveData<List<FavoriteGame?>?>
            favGames.observe(viewLifecycleOwner, Observer<List<FavoriteGame>> { favoriteGames ->

                //View Modesl
                //AppViewModel viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
                //((MainActivity) getActivity()).viewModel.getGamesFavorite().observe(getViewLifecycleOwner(), new Observer<List<FavoriteGame>>() {
                Log.d(TAG, "Load favorite games because of ViewModel and LiveData")
                val games: ArrayList<Game?> = ArrayList<Any?>()
                for (favGame in favoriteGames) {
                    if (favGame.getJson() != null) {
                        games.add(parseGameData(favGame.getJson(), true))
                    }
                }

                // change data in view
                if (games != null && !games.isEmpty()) {
                    mAdapter!!.setGames(games)
                }
            })
        }



    companion object {
        private val TAG = GamesListFragment::class.java.simpleName + "CheckLoader"
        private const val SEARCH_TYPE = "search_type"
        private const val LOADER_BY_NAME_ID = 1
        private const val LOADER_HOT_ID = 2

*
         * New instance via factory method
         * this fragment using the provided parameters.
         *
         * @param searchType - determines what kind of search and API request to use
         * @return new instance of fragment GamesListFragment.


        fun newInstance(searchType: SearchType): GamesListFragment {
            val fragment = GamesListFragment()
            val args = Bundle()
            args.putString(SEARCH_TYPE, searchType.toString())
            fragment.arguments = args
            return fragment
        }
    }
}
*/
