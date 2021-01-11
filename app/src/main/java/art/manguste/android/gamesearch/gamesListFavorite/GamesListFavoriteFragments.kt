package art.manguste.android.gamesearch.gamesListFavorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import art.manguste.android.gamesearch.GameDetailActivity
import art.manguste.android.gamesearch.core.GameBriefly
import art.manguste.android.gamesearch.core.LoadStatus
import art.manguste.android.gamesearch.databinding.FragmentGameSearchBinding
import art.manguste.android.gamesearch.GameAdapter
import art.manguste.android.gamesearch.OnClickListener
import com.google.android.material.snackbar.Snackbar

class GamesListFavoriteFragments : Fragment() {

    private val gamesListFavoriteViewModel: GamesListFavoriteViewModel by lazy {
        ViewModelProvider(this).get(GamesListFavoriteViewModel::class.java)
    }

    private lateinit var binding: FragmentGameSearchBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentGameSearchBinding.inflate(inflater)

        // data binding will observe LiveData with the lifecycle of the fragment
        binding.lifecycleOwner = this

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

    private fun toDetailInfo(game: GameBriefly) {
        val intent = Intent(activity, GameDetailActivity::class.java)
                .putExtra(GameDetailActivity.EXTRA_GAME_CODE, game.alias)
                .putExtra(GameDetailActivity.EXTRA_GAME_NAME, game.name)

        startActivity(intent)
    }

    private fun changeGamesFavoriteStatus(game: GameBriefly) {
        // form message into snakbar
        val snackMessage = if (game.isFavorite) "${game.name} removed from favourites"
        else "${game.name} added to favourites"

        // form actions
        when (game.isFavorite) {
            true -> {
                game.isFavorite = false
                gamesListFavoriteViewModel.removeGameFavorite(game)
            }
            false -> {
                game.isFavorite = true
                gamesListFavoriteViewModel.addGameFavorite(game)
            }
        }

        Snackbar.make(requireView(), snackMessage, Snackbar.LENGTH_LONG).show()
    }

    override fun onResume() {
        super.onResume()

        gamesListFavoriteViewModel.getDBGameList()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gamesListFavoriteViewModel.status.observe(viewLifecycleOwner, { status ->
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

        binding.panelSearchGame.visibility = View.GONE
        gamesListFavoriteViewModel.getDBGameList()

        gamesListFavoriteViewModel.gamesList.observe(viewLifecycleOwner, { games ->
            //Log.d(TAG, "games = ${games.size}")
            (binding.recyclerGames.adapter as GameAdapter).apply {
                reloadGames(games)
            }
        })
    }

    companion object {
        //private val TAG = GamesListFavoriteFragments::class.java.simpleName

        /**
         * New instance via factory method
         * this fragment using the provided parameters.
         *
         * @return new instance of fragment GamesListFragment.
         */
        fun newInstance(): GamesListFavoriteFragments {
            return GamesListFavoriteFragments()
        }
    }
}