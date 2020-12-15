package art.manguste.android.gamesearch.gamesList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import art.manguste.android.gamesearch.GameDetailActivity
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.core.LoadStatus
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.databinding.FragmentGameSearchBinding
import art.manguste.android.gamesearch.db.GameDatabase

import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [GamesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamesListFragment : Fragment() {

/*    private val gamesListVM: GamesListVM by lazy {
        ViewModelProvider(this).get(GamesListVM::class.java)
    }*/
    private lateinit var gamesListVM: GamesListVM

    private var searchType: SearchType? = null
    private lateinit var binding: FragmentGameSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGameSearchBinding.inflate(inflater)

        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDao
        gamesListVM = GamesListVM(application, dataSource)

        // data binding will observe LiveData with the lifecycle of the fragment
        binding.lifecycleOwner = this
        // access to View Model from the layout

        binding.recyclerGames.adapter = GameAdapter(OnClickListener(
                // on click by whole card - going to the detail info about game
                { game ->
                    toDetailInfo(game)
                },
                // on click "make favorite" button - make favorite
                { game ->
                    changeGamesFavoriteStatus(game)
                }))

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun toDetailInfo(game: GameBriefly){
        Log.d(TAG, "OnClick ${game.name}")
        val intent = Intent(activity, GameDetailActivity::class.java)
                .putExtra(GameDetailActivity.EXTRA_GAME_CODE, game.alias)
                .putExtra(GameDetailActivity.EXTRA_GAME_NAME, game.name)

        Log.d(TAG, "OnClick start activity ${game.name}")
        startActivity(intent)
    }

    private fun changeGamesFavoriteStatus(game: GameBriefly)  {
        Log.d(TAG, "OnClickFavorite ${game.name}")

        // form message into snakbar
        val snackMessage = if (game.isFavorite) "${game.name} removed from favourites"
        else "${game.name} added to favourites"

        // db actions
        val application = requireNotNull(this.activity).application
        val dataSource = GameDatabase.getInstance(application).gameDao
        // form actions
        when (game.isFavorite) {
            true -> {
                game.isFavorite = false
                gamesListVM.removeGameFavorite(game, dataSource)
            }
            false -> {
                game.isFavorite = true
                gamesListVM.addGameFavorite(game, dataSource)
            }
        }

        Snackbar.make(requireView(), snackMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        gamesListVM.status.observe(viewLifecycleOwner, { status ->
            when (status!!) {
                LoadStatus.LOADING -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.netLostImage.visibility = View.GONE
                }
                LoadStatus.DONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.netLostImage.visibility = View.GONE
                }
                LoadStatus.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    binding.netLostImage.visibility = View.VISIBLE
                }
                LoadStatus.NONE -> {
                    binding.progressBar.visibility = View.GONE
                    binding.netLostImage.visibility = View.GONE
                }
            }
        })

        searchType = requireArguments().getString(SEARCH_TYPE)?.let { SearchType.valueOf(it) }
        Log.d(TAG, "SEARCH_TYPE = $searchType")

        // hide search panel if it needed
        when (searchType)  {
            SearchType.HOT -> {
                binding.panelSearchGame.visibility = View.GONE
                gamesListVM.getHotGamesList()
            }
            SearchType.SEARCH -> {
                binding.panelSearchGame.visibility = View.VISIBLE
                // search Game by click on button
                binding.btnStartSearch.setOnClickListener {
                    binding.searchByTitle.text.isNotEmpty().let {
                        gamesListVM.getSearchGameList(binding.searchByTitle.text.toString())
                    }
                }
            }
            SearchType.FAVORITE -> {
                binding.panelSearchGame.visibility = View.GONE
                val application = requireNotNull(this.activity).application  // todo - del this madness, its just for test idea
                val dataSource = GameDatabase.getInstance(application).gameDao
                gamesListVM.getDBGameList(dataSource)
            }
            else -> Log.d(TAG, "Unexpected search type = $searchType")
        }

        //if (searchType != SearchType.FAVORITE){
            gamesListVM.gamesList.observe(viewLifecycleOwner, { games ->
                Log.d(TAG, "games = ${games.size}")
                (binding.recyclerGames.adapter as GameAdapter).apply {
                    reloadGames(games)
                }
            })
        //}
    }

    companion object {
        private val TAG = GamesListFragment::class.java.simpleName
        private const val SEARCH_TYPE = "search_type"

        /**
         * New instance via factory method
         * this fragment using the provided parameters.
         *
         * @param searchType - determines what kind of search and API request to use
         * @return new instance of fragment GamesListFragment.
         */
        fun newInstance(searchType: SearchType): GamesListFragment {
            val fragment = GamesListFragment()
            val args = Bundle()
            args.putString(SEARCH_TYPE, searchType.toString())
            fragment.arguments = args
            return fragment
        }
    }
}