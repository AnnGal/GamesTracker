package art.manguste.android.gamesearch.gamesList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import art.manguste.android.gamesearch.R
import art.manguste.android.gamesearch.core.GameBriefly
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.text.SimpleDateFormat
import java.util.*

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
    private val releaseDate = itemView.findViewById<TextView>(R.id.release_date)
    private val rate = itemView.findViewById<TextView>(R.id.rate)
    private val favorite = itemView.findViewById<ImageButton>(R.id.favorite)
    private val gameIcon = itemView.findViewById<ImageView>(R.id.game_icon)

    fun bind(gameBriefly: GameBriefly) {
        Glide.with(itemView.context)
                .load(gameBriefly.imgHttp)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .placeholder(R.drawable.empty_photo)
                .into(gameIcon)

        title.text = gameBriefly.name

        if (gameBriefly.genres.isNotEmpty())
            description.text = itemView.resources.getString(R.string.card_description_text,
                gameBriefly.genres.joinToString( ", " ) {it.name})
        else description.text = ""

        releaseDate.text = gameBriefly.released.toCardFormat()
        rate.text = gameBriefly.rating

        when (gameBriefly.isFavorite){
            true -> favorite.setImageResource(R.drawable.ic_action_star_filled)
            false -> favorite.setImageResource(R.drawable.ic_action_star_empty)
        }

    }
}

private fun String.toCardFormat(): String {
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
