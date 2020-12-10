package art.manguste.android.gamesearch

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import art.manguste.android.gamesearch.OLDGameCardAdapter.OLDGameViewHolder
import art.manguste.android.gamesearch.core.Game
import art.manguste.android.gamesearch.core.ListItemClickListener
import art.manguste.android.gamesearch.core.SearchType
import art.manguste.android.gamesearch.threads.DBUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import java.util.*

class OLDGameCardAdapter(private val mContext: Context?, private val mImageSize: Int, private val mViewGroup: ViewGroup?, private val mSearchType: SearchType?) : RecyclerView.Adapter<OLDGameViewHolder>(), ListItemClickListener {
    //private var games = ArrayList<Game>()
    private var games = ArrayList<Game>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OLDGameViewHolder {
        val cv = LayoutInflater.from(parent.context).inflate(R.layout.card_view_game, parent, false) as MaterialCardView
        return OLDGameViewHolder(cv, this)
    }

    override fun onBindViewHolder(holderOLD: OLDGameViewHolder, position: Int) {
        holderOLD.bind(games[position])
    }

    override fun getItemCount(): Int {
        return games.size
    }

    fun setGames(games: ArrayList<Game>) {
        this.games = games
        notifyDataSetChanged()
    }

    override fun onListItemClick(game: Game) {
        val intent = Intent(mContext, GameDetailActivity::class.java)
        intent.putExtra(GameDetailActivity.EXTRA_GAME_CODE, game.alias)
        intent.putExtra(GameDetailActivity.EXTRA_GAME_NAME, game.name)
        mContext!!.startActivity(intent)
    }

        override fun onFavoriteBtnClick(game: Game, mFavoriteImageButton: ImageButton, position: Int) {
        // add or remove from DB async
        var isAddToFavorite = true
        if (!game!!.isFavorite) {
            // add to Favorite
            mFavoriteImageButton.setImageDrawable(mContext!!.resources.getDrawable(R.drawable.ic_action_star_filled))
        } else {
            // remove from Favorite
            mFavoriteImageButton.setImageDrawable(mContext!!.resources.getDrawable(R.drawable.ic_action_star_empty))
            isAddToFavorite = false
        }
        game.isFavorite = isAddToFavorite
        showSnackbar(game.name, isAddToFavorite)
        DBUtils.Companion.changeFavoriteStatus(mContext, game)

        // if it favorite list - then remove game from the list
        if (!isAddToFavorite && SearchType.FAVORITE == mSearchType) {
            games.removeAt(position)
            notifyItemRemoved(position)
        }
    }

    private fun showSnackbar(name: String?, added: Boolean) {
        // Snackbar interaction
        var snackMessage = "\"$name\" "
        snackMessage += if (added) "added to favourites" else "removed from favourites"
        val snackbar = Snackbar
                .make(mViewGroup!!, snackMessage, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

    inner class OLDGameViewHolder(itemView: MaterialCardView, private val mListItemClickListener: ListItemClickListener?) : RecyclerView.ViewHolder(itemView) {
        private lateinit var game: Game
        private val mTitleTextView: TextView
        private val mDescriptionTextView: TextView
        private val mReleaseTextView: TextView
        private val mRateTextView: TextView
        private val mFavoriteImageButton: ImageButton
        private val mGameIconImageView: ImageView

        fun bind(game: Game) {
            this.game = game
            if (game != null) {
                mTitleTextView.text = this.game.name
                mDescriptionTextView.text = "Genres: " + game.genresList
                mReleaseTextView.text = game.releaseStr
                mRateTextView.text = game.rating
                if (game.isFavorite) {
                    Log.d(TAG, "Favorite: " + game.alias)
                    mFavoriteImageButton.setImageDrawable(mContext!!.resources.getDrawable(R.drawable.ic_action_star_filled))
                } else mFavoriteImageButton.setImageDrawable(mContext!!.resources.getDrawable(R.drawable.ic_action_star_empty))
                Glide.with(mContext)
                        .load(game.imgHttp)
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .placeholder(R.drawable.empty_photo)
                        .override(mImageSize, mImageSize)
                        .into(mGameIconImageView)
            }
        }

        init {
            mTitleTextView = itemView.findViewById(R.id.title)
            mDescriptionTextView = itemView.findViewById(R.id.description)
            mReleaseTextView = itemView.findViewById(R.id.release_date)
            mRateTextView = itemView.findViewById(R.id.rate)
            mFavoriteImageButton = itemView.findViewById(R.id.favorite)
            mGameIconImageView = itemView.findViewById(R.id.game_icon)
            mFavoriteImageButton.setOnClickListener { mListItemClickListener?.onFavoriteBtnClick(game, mFavoriteImageButton, adapterPosition) }
            itemView.setOnClickListener { mListItemClickListener?.onListItemClick(game) }
        }
    }

    companion object {
        private val TAG = OLDGameCardAdapter::class.java.simpleName
    }
}