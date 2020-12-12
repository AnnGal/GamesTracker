package art.manguste.android.gamesearch.gamesList

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import art.manguste.android.gamesearch.GameDetailActivity
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.databinding.FragmentGameSearchBinding
import com.google.android.material.snackbar.Snackbar

/**
 * A simple [Fragment] subclass.
 * Use the [GamesListFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class GamesListFragment : Fragment() {

    private val viewModel: GamesViewModel by lazy {
        ViewModelProvider(this).get(GamesViewModel::class.java)
    }

    private var searchType: SearchType? = null
    private lateinit var binding: FragmentGameSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGameSearchBinding.inflate(inflater)

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

        binding.viewModel = viewModel

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

        // DB actions
        when (game.isFavorite) {
            true -> game.isFavorite = false
            false -> game.isFavorite = true
        }

        Snackbar.make(view!!, snackMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchType = requireArguments().getString(SEARCH_TYPE)?.let { SearchType.valueOf(it) }
        Log.d(TAG, "SEARCH_TYPE = $searchType")

        // hide search panel if it needed
        when (searchType)  {
            SearchType.HOT -> {
                binding.panelSearchGame.visibility = View.GONE
                viewModel.getHotGamesList()
            }
            SearchType.SEARCH -> {
                binding.panelSearchGame.visibility = View.VISIBLE
                // search Game by click on button
                binding.btnStartSearch.setOnClickListener {
                    binding.searchByTitle.text.isNotEmpty().let {
                        viewModel.getSearchGameList(binding.searchByTitle.text.toString())
                    }
                }
            }
            SearchType.FAVORITE -> binding.panelSearchGame.visibility = View.GONE
            else -> Log.d(TAG, "Unexpected search type = $searchType")
        }

        viewModel.gamesList.observe(viewLifecycleOwner, Observer { games ->
            Log.d(TAG, "games = ${games.size}")
            (binding.recyclerGames.adapter as GameAdapter).apply {
                reloadGames(games)
            }
        })

        viewModel.status.observe(viewLifecycleOwner, {status ->
            when (status) {
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
            }
        })
    }

    companion object {
        val TAG = GamesListFragment::class.java.simpleName
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