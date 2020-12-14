package art.manguste.android.gamesearch.gamesList

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import art.manguste.android.gamesearch.R
import art.manguste.android.gamesearch.core.GameDetail
import art.manguste.android.gamesearch.core.LoadStatus
import art.manguste.android.gamesearch.databinding.FragmentGameDetailBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.text.SimpleDateFormat
import java.util.*

class GameDetailFragment : Fragment() {

    private lateinit var binding: FragmentGameDetailBinding

    private val gameDetailVM: GameDetailVM by lazy {
        ViewModelProvider(this).get(GameDetailVM::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentGameDetailBinding.inflate(inflater)
        binding.lifecycleOwner = this

        return binding.root
    }

    private fun reloadData(game: GameDetail?) {
        Log.d(TAG, "Try to load game = ${game?.name}")
        game?.let { gameCurrent ->
            Glide.with(requireContext())
                    .load(gameCurrent.imgHttp)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.empty_photo)
                    .into(binding.gameCover)

            binding.title.text = gameCurrent.name
            binding.release.text = gameCurrent.released.toDetailFormat()
            binding.description.text = Html.fromHtml(gameCurrent.description)
            binding.genre.text = gameCurrent.genres.joinToString( ", " ) { genre -> genre.name}
            binding.developer.text = gameCurrent.developers.joinToString( ", " ) { dev -> dev.name}
            binding.platform.text = gameCurrent.platforms.joinToString(", ") { pltfrm -> pltfrm.platform.name }
            binding.publisher.text = gameCurrent.publishers.joinToString (", ") {publisher -> publisher.name }

            binding.gameWebsite.apply {
                text = Html.fromHtml("<u>" + game.website + "</u>")
                setTextColor(Color.BLUE)
                setOnClickListener {
                    // click on website
                    if (text.toString().isNotEmpty()) {
                        val intent = Intent(Intent.ACTION_VIEW)
                        intent.data = Uri.parse(text.toString())
                        startActivity(intent)
                    }
                }
            }

            // path on api game page
            binding.disclaimer.setOnClickListener {
                if (gameCurrent.apiLink.isNotEmpty()) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(gameCurrent.apiLink)
                    startActivity(intent)
                }
            }

            // make game favorite
            when (gameCurrent.isFavorite){
                true -> binding.favorite.setImageResource(R.drawable.ic_action_star_filled)
                false -> binding.favorite.setImageResource(R.drawable.ic_action_star_empty)
            }

            binding.favorite.setOnClickListener {
                // form actions
                when (gameCurrent.isFavorite) {
                    true -> {
                        gameCurrent.isFavorite = false
                        binding.favorite.setImageResource(R.drawable.ic_action_star_empty)
                    }
                    false -> {
                        gameCurrent.isFavorite = true
                        binding.favorite.setImageResource(R.drawable.ic_action_star_filled)
                    }
                }
            }


            //share
            binding.share.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, gameCurrent.name + " - " + gameCurrent.apiLink)
                startActivity(intent)
            }

        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gameDetailVM.gameDetail.observe(viewLifecycleOwner, { game ->
            Log.d(TAG, "games = ${game.name}")
            reloadData(game)
        })

        gameDetailVM.status.observe(viewLifecycleOwner, { status ->
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

        // from bundle
        val gameAlias = requireArguments().getString(EXTRA_GAME_CODE)
        val gameName = requireArguments().getString(EXTRA_GAME_NAME)

        (activity as? AppCompatActivity?)?.apply {
            setSupportActionBar(view.findViewById<View>(R.id.toolbar) as Toolbar)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        }

        binding.toolbarCollapsing.title = gameName
        toolbarCollapsing.setCollapsedTitleTextColor(resources.getColor(R.color.colorTab))
        toolbarCollapsing.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        // load data via view model
        gameAlias?.let {
            gameDetailVM.getGameInfo(gameAlias)
        }
    }


    companion object {
        private var TAG = GameDetailFragment::class.java.simpleName
        private const val EXTRA_GAME_CODE = "game_site_code"
        private const val EXTRA_GAME_NAME = "game_name"
    }
}

private fun String.toDetailFormat(): String {
    this.isNotEmpty().let {
        // from string to date
        val date: Date? = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse( this )
        if (date != null) {
            // from date to required format
            return SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(date)
        }
    }
    return ""
}










