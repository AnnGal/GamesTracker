package art.manguste.android.gamesearch.core

import android.widget.ImageButton

interface ListItemClickListener {
    fun onListItemClick(game: Game)
    fun onFavoriteBtnClick(game: Game, mFavoriteImageButton: ImageButton, position: Int)
}