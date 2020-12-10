package art.manguste.android.gamesearch.gamesList

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import art.manguste.android.gamesearch.OLDGameCardAdapter
import art.manguste.android.gamesearch.R
import art.manguste.android.gamesearch.core.GameBriefly
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class GameAdapter() : RecyclerView.Adapter<GameViewHolder>() {
    var games = listOf<GameBriefly>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(R.layout.card_view_game, parent, false))
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int = games.size

    fun reloadGames(newGamesList: List<GameBriefly>){
        games = newGamesList
        notifyDataSetChanged()
    }
}

class GameViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView) {
    private val title = itemView.findViewById<TextView>(R.id.title)
    private val description = itemView.findViewById<TextView>(R.id.description)
    private val release_date = itemView.findViewById<TextView>(R.id.release_date)
    private val rate = itemView.findViewById<TextView>(R.id.rate)
    private val favorite = itemView.findViewById<ImageButton>(R.id.favorite)
    private val game_icon = itemView.findViewById<ImageView>(R.id.game_icon)

    fun bind(gameBriefly: GameBriefly) {
        Glide.with(itemView.context)
                .load(gameBriefly.imgHttp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.empty_photo)
                .into(game_icon)

        title.text = gameBriefly.name
        description.text = gameBriefly.genres.joinToString( ", " ) {it.name}
        release_date.text = gameBriefly.released
        rate.text = gameBriefly.rating
        favorite.setImageResource(R.drawable.ic_action_star_filled)
    }
}
