package art.manguste.android.gamesearch.gamesList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.loader.app.LoaderManager
import androidx.loader.content.Loader
import art.manguste.android.gamesearch.R
import art.manguste.android.gamesearch.old_api.URLMaker.formURL
import art.manguste.android.gamesearch.core.Game
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.threads.DBUtils
import com.google.android.material.appbar.CollapsingToolbarLayout
import kotlinx.android.synthetic.main.fragment_game_detail.*
import java.util.*

class OLDGameDetailFragment : Fragment(), LoaderManager.LoaderCallbacks<ArrayList<Game>?> {
    private var gameCode: String? = null
    private var gameName: String? = null
    private var mImageSize = 0

    //UI
    var mProgressBar: ProgressBar? = null
    var mCoverImageView: ImageView? = null
    var mTitle: TextView? = null
    var mRelease: TextView? = null
    var mDescription: TextView? = null
    var mGenre: TextView? = null
    var mDeveloper: TextView? = null
    var mPlatform: TextView? = null
    var mPublisher: TextView? = null
    var mWebsite: TextView? = null
    var mFavoriteButton: ImageButton? = null
    var mShareButton: ImageButton? = null
    var mDisclaimer: TextView? = null
    lateinit var mGame: Game
    var mToolbarLayout: CollapsingToolbarLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            gameCode = arguments!!.getString(EXTRA_GAME_CODE)
            gameName = arguments!!.getString(EXTRA_GAME_NAME)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_game_detail, container, false)
        mImageSize = resources.getDimensionPixelSize(R.dimen.cover_size) * 2

        // UI objects
        mProgressBar = view.findViewById(R.id.progressBar)
        mCoverImageView = view.findViewById(R.id.gameCover)
        mTitle = view.findViewById(R.id.title)
        mRelease = view.findViewById(R.id.release)
        mDescription = view.findViewById(R.id.description)
        mGenre = view.findViewById(R.id.genre)
        mDeveloper = view.findViewById(R.id.developer)
        mPlatform = view.findViewById(R.id.platform)
        mPublisher = view.findViewById(R.id.publisher)
        mWebsite = view.findViewById(R.id.gameWebsite)
        mFavoriteButton = view.findViewById(R.id.favorite)
        mShareButton = view.findViewById(R.id.share)
        mDisclaimer = view.findViewById(R.id.disclaimer)

        //save game as favorite game for tracking
        favorite.setOnClickListener(View.OnClickListener {
            DBUtils.Companion.changeFavoriteStatus(context, mGame)
            var isAddToFavorite = true
            if (!mGame!!.isFavorite) {
                // add to Favorite
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_action_star_filled))
            } else {
                // remove from Favorite
                favorite.setImageDrawable(resources.getDrawable(R.drawable.ic_action_star_empty))
                isAddToFavorite = false
            }
            // todo fix
            //mGame.setFavorite(isAddToFavorite)
        })

        //save game as favorite game for tracking
        // todo share
        share.setOnClickListener(View.OnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, mGame.name + " - " + mGame.apiLink)
            startActivity(intent)
        })

        // toolbar and return button
        val mActivity = activity as AppCompatActivity?
        mActivity!!.setSupportActionBar(view.findViewById<View>(R.id.toolbar) as Toolbar)
        mActivity.supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        mToolbarLayout = view.findViewById(R.id.toolbarCollapsing)
        toolbarCollapsing.setTitle(gameName)
        toolbarCollapsing.setCollapsedTitleTextColor(resources.getColor(R.color.colorTab))
        toolbarCollapsing.setExpandedTitleColor(resources.getColor(android.R.color.transparent))

        // start Loader
        val loaderManager = LoaderManager.getInstance(this)
        loaderManager.initLoader(LOADER_GAME_ID, null, this)
        return view
    }

    // Loader begin
    // todo fix
    override fun onCreateLoader(id: Int, args: Bundle?): Loader<ArrayList<Game>?> {
        TODO("Not yet implemented")

        mProgressBar!!.visibility = View.VISIBLE
        val urlString = formURL(SearchType.GAME, gameCode!!)
        // todo fix return GamesApiLoader(context, urlString, SearchType.GAME)
    }

    override fun onLoadFinished(loader: Loader<ArrayList<Game>?>, data: ArrayList<Game>?) {
        mProgressBar!!.visibility = View.GONE

        //TODO if none games found - set text about it

        // change data in view
        if (data != null && !data.isEmpty()) {
            //todo fix
            //setGameInfo(data)
        }
    }

    override fun onLoaderReset(loader: Loader<ArrayList<Game>?>) {
        //
    }

    // Loader end
    // todo fix
    /*private fun setGameInfo(data: ArrayList<Game>) {
        val game = data[0]
        if (game != null) {
            mGame = game
            Glide.with(context!!)
                    .load(game.imgHttp)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .placeholder(R.drawable.empty_photo)
                    .override(mImageSize, mImageSize)
                    .into(mCoverImageView)
            mTitle.setText(game.name)
            mRelease.setText(game.releaseStr)
            mDescription!!.text = Html.fromHtml(game.description)
            mGenre!!.text = game.genresList
            mDeveloper!!.text = game.developersList
            mPlatform!!.text = game.platformsList
            mPublisher!!.text = game.publishersList
            if (game.isFavorite) {
                mFavoriteButton!!.setImageDrawable(resources.getDrawable(R.drawable.ic_action_star_filled))
            }
            mWebsite!!.text = Html.fromHtml("<u>" + game.website + "</u>")
            mWebsite!!.setTextColor(Color.BLUE)
            mWebsite!!.setOnClickListener {
                val url = mWebsite!!.text.toString()
                if (url.length > 0) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
            mDisclaimer!!.setOnClickListener {
                val url = mGame.getApiLink()
                if (url!!.length > 0) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                }
            }
        }
    }*/

    companion object {
        private const val EXTRA_GAME_CODE = "game_site_code"
        private const val EXTRA_GAME_NAME = "game_name"
        private const val LOADER_GAME_ID = 3

        fun createInstance(gameCode: String?, gameName: String?): OLDGameDetailFragment {
            val fragment = OLDGameDetailFragment()
            val bundle = Bundle()
            bundle.putString(EXTRA_GAME_CODE, gameCode)
            bundle.putString(EXTRA_GAME_NAME, gameName)
            fragment.arguments = bundle
            return fragment
        }
    }
}
