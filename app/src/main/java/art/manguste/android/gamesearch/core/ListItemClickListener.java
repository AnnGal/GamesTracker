package art.manguste.android.gamesearch.core;

import android.widget.ImageButton;

public interface ListItemClickListener{
    void onListItemClick(Game game);
    void onFavoriteBtnClick(Game game, ImageButton mFavoriteImageButton, int position);
}