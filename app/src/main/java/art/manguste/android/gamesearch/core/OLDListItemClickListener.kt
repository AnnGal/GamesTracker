package art.manguste.android.gamesearch.core

import android.widget.ImageButton

interface OLDListItemClickListener {
    fun onListItemClick(OLDGame: OLD_Game)
    fun onFavoriteBtnClick(OLDGame: OLD_Game, mFavoriteImageButton: ImageButton, position: Int)
}